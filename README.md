# Shipwire_RestServicesTests
This repository contains RestServicesTests for Shipwire Restful APIs.

Shipwire_RestServicesTests Project is a Maven project. I am using Java, TestNG and RestAssured to automate the REST APIs. SLF4j is used for logging. pom.xml file have all of the dependencies. 

Following files are of interest under the main project directory:
* pom.xml under the main project folder
* RestServiceTest.java under /src/main/java contains the test case implementation for create/get/update/cancel orders.
* RestServiceDataProvider.java under /src/main/java contains the  data providers for create/get/update/cancel order tests. 
* logback.xml under /src/main/resources to control the log level - currently set to "INFO"
* testng.xml under /src/main/resources to run the entire test suite

For each RESTFul API, both positive and negative test cases are implemented. There are a total of 12 test cases.

###Create Order
* Create request for /v3/orders/{orderId} endpoint returns HTTP 401 and error message for incorrect login credentials.  
* Create request for /v3/orders/{orderId} endpoint returns HTTP 200 and order info for correct login credentials.

###Get Order by OrderId
* GET request for /v3/orders/{orderId} endpoint returns HTTP 401 and error message for incorrect login credentials and valid orderId.
* GET request for /v3/orders/{orderId} endpoint returns HTTP 404 and error message for correct login credentials and invalid orderId.
* GET request for /v3/orders/{orderId} endpoint returns HTTP 200 and order info for correct login credentials and valid orderId.

###Update Order (Setting SKU Quantity = 3)
* Update request for /v3/orders/{orderId} endpoint returns HTTP 401 and error message for incorrect login credentials and valid orderId.
* Update request for /v3/orders/{orderId} endpoint returns HTTP 404 and error message for correct login credentials and invalid orderId.
* Update request for /v3/orders/{orderId} endpoint returns HTTP 200 and updated order info for correct login credentials and valid orderId.

###Cancel Order
* Cancel request for /v3/orders/{orderId}/cancel endpoint returns HTTP 401 and error message for incorrect login credentials and valid orderId.
* Cancel request for /v3/orders/{orderId}/cancel endpoint returns HTTP 404 and error message for correct login credentials and invalid orderId.
* Cancel request for /v3/orders/{orderId}/cancel endpoint returns HTTP 200 and cancel the order for correct login credentials and valid orderId.
