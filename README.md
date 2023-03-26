# Order Service

This is a simple order service that allows you to create, read, update and delete orders.

### Prerequisites

- Clone the repository
- Install MySQL
  - Ensure that it is running on port `3306`
  - Create a user called `root` with your preferred password
  - Update `application.properties` with the password you used
  - Create a database called `order_service`
- Install [make](https://www.gnu.org/software/make/)
- Install [docker](https://www.docker.com/products/docker-desktop/)
- Install [TestContainers](https://www.testcontainers.org/)

### Getting Started

- Run `make test` to run the tests
- Run `make build` to build the project
- Run `make run` to run the project
