package com.sp.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.sp.orderservice.dto.InventoryResponse;
import com.sp.orderservice.dto.OrderLineItemsDTO;
import com.sp.orderservice.dto.OrderRequest;
import com.sp.orderservice.model.Order;
import com.sp.orderservice.model.OrderLineItems;
import com.sp.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;
    
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDTOList().stream().map(orderLineItemsDTO -> mapOrderLineItems(orderLineItemsDTO)).toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        // Call inventory service to check if the product is available.
        // This is an example of synchronous call as we are using .block() method.
        InventoryResponse[] inventoryResponseArray = webClient.get()
                            .uri("http://localhost:8082/api/v1/inventory", uribuilder -> uribuilder.queryParam("skuCode", skuCodes).build())
                            .retrieve()
                            .bodyToMono(InventoryResponse[].class)
                            .block();
        
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
        if (!allProductsInStock) {
            throw new IllegalArgumentException("Product is not in stock!");
        } else {
            orderRepository.save(order);
        }
    }

    private OrderLineItems mapOrderLineItems(OrderLineItemsDTO orderLineItemsDTO) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setSkuCode(orderLineItemsDTO.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDTO.getPrice());
        orderLineItems.setQuantity(orderLineItemsDTO.getQuantity());
        return orderLineItems;
    }
}
