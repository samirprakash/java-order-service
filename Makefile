verify: 
	mvn clean verify
	
test:
	mvn clean test
	
build:
	mvn clean package

server:
	mvn spring-boot:run