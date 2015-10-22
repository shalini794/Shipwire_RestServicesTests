package com.shipwire.tests.Shipwire_RestServicesTests;

import static com.jayway.restassured.RestAssured.given;

import java.util.ArrayList;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class RestServiceTest {
	
	String baseUri = "https://api.beta.shipwire.com/api";
	String basePath = "/v3/orders";
	private static final Logger logger = LoggerFactory.getLogger(RestServiceTest.class);
	
	@BeforeClass
	public void setup() {
		RestAssured.baseURI = baseUri;
		RestAssured.basePath = basePath;
	}
	
    /**
     * Create request for /v3/orders/{orderId} endpoint returns HTTP 401 and error message for incorrect login credentials.     
     * Create request for /v3/orders/{orderId} endpoint returns HTTP 200 and order info for correct login credentials.
     * 
     * @param description
     * @param username
     * @param password
     * @param httpStatusCode
     * @param expectedMessage
     */
	@Test(dataProvider = "CreateOrderData", dataProviderClass = RestServiceDataProvider.class)
	public void createOrder(String description, String username, String password, String expectedOrderStatus,
			int httpStatusCode, String expectedMessage) {
		
		logger.info(String.format("Testing %s", description));
		String myJson = "{ \"shipTo\": { \"email\": \"test123@bar.com\", \"name\": \"Test123 Bar\", \"address1\": \"201 mathilda Ave\", \"address2\": \"\", \"company\": \"\", \"city\": \"Sunnyvale\", \"state\": \"CA\", \"postalCode\": \"94085\", \"country\": \"US\", \"phone\": \"2223334444\"}, \"items\":[{ \"sku\":\"Santa-Keychain\"," +  "\"quantity\":1}] }";
				
		Response response =
				given().
					auth().preemptive().basic(username, password).
					contentType("application/json").
					body(myJson).
				when().
					post().
				then().
					log().all().
					assertThat().statusCode(httpStatusCode).
				and().
					extract().response();
		
		// Verify order details from JsonPath when response status code is 200 or error message otherwise
		JsonPath jsonPath = response.getBody().jsonPath();
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			String actualOrderId = jsonPath.getString("resource.items.resource.id");
			Assert.assertTrue(actualOrderId != null, "Order Id is null.");
			
			String actualOrderStatus = jsonPath.getString("resource.items.resource.status");
			Assert.assertEquals(actualOrderStatus, expectedOrderStatus, 
					String.format("Order status returned from response is incorrect. Actual: %s -- Expected: %s", actualOrderStatus, expectedOrderStatus));

		} else {
			// Verify the error message 
			String actualMessage = jsonPath.getString("message");
			Assert.assertEquals(actualMessage, expectedMessage, 
					String.format("Message from response is incorrect. Actual: %s -- Expected: %s", actualMessage, expectedMessage));

		}
		
	}
	
    /**
     * GET request for /v3/orders/{orderId} endpoint returns HTTP 401 and error message for incorrect login credentials and valid orderId.
     * GET request for /v3/orders/{orderId} endpoint returns HTTP 404 and error message for correct login credentials and invalid orderId.
     * GET request for /v3/orders/{orderId} endpoint returns HTTP 200 and order info for correct login credentials and valid orderId.
     * 
     * @param description
     * @param username
     * @param password
     * @param expectedOrderId
     * @param httpStatusCode
     * @param expectedMessage
     */
	@Test(dataProvider = "GetOrderData", dataProviderClass = RestServiceDataProvider.class)
	public void getOrder(String description, String username, String password, String expectedOrderId, 
			String expectedOrderStatus, int httpStatusCode, String expectedMessage) {

		logger.info(String.format("Testing %s", description));
		String resourceId = "/" + expectedOrderId;
		
		Response response = 
				given().
					auth().preemptive().basic(username, password).
					queryParam("expand", "items").
				when().
					get(resourceId).
				then().
					log().all().
					assertThat().statusCode(httpStatusCode).
				and().
					extract().response();
		
		// Verify order details from JsonPath when response status code is 200 or error message otherwise
		JsonPath jsonPath = response.getBody().jsonPath();
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			String actualOrderId = jsonPath.getString("resource.id");
			Assert.assertEquals(actualOrderId, expectedOrderId, 
					String.format("OrderId returned from response is incorrect. Actual: %s -- Expected: %s", actualOrderId, expectedOrderId));
			String actualOrderStatus = jsonPath.getString("resource.status");
			Assert.assertEquals(actualOrderStatus, expectedOrderStatus, 
					String.format("Order status returned from response is incorrect. Actual: %s -- Expected: %s", actualOrderStatus, expectedOrderStatus));
			
		} else {
			// Verify the error message
			String actualMessage = jsonPath.getString("message");
			Assert.assertEquals(actualMessage, expectedMessage, 
					String.format("Message from response is incorrect. Actual: %s -- Expected: %s", actualMessage, expectedMessage));

		}
	}
	
    /**
     * Update request for /v3/orders/{orderId} endpoint returns HTTP 401 and error message for incorrect login credentials and valid orderId.
     * Update request for /v3/orders/{orderId} endpoint returns HTTP 404 and error message for correct login credentials and invalid orderId.
     * Update request for /v3/orders/{orderId} endpoint returns HTTP 200 and updated order info for correct login credentials and valid orderId.
     * 
     * @param description
     * @param username
     * @param password
     * @param expectedOrderId
     * @param expectedQuantity
     * @param httpStatusCode
     * @param expectedMessage
     */
	@Test(dataProvider = "UpdateOrderData", dataProviderClass = RestServiceDataProvider.class)
	public void updateOrder(String description, String username, String password, String expectedOrderId, 
			String expectedQuantity, int httpStatusCode, String expectedMessage) {

		logger.info(String.format("Testing %s", description));
		String resourceId = "/" + expectedOrderId;
		String myJson = "{ \"shipTo\": { \"email\": \"test@bar\", \"name\": \"Test Bar\", \"address1\": \"120 main St\", \"address2\": \"\", \"company\": \"\", \"city\": \"Sunnyvale\", \"state\": \"CA\", \"postalCode\": \"94089\", \"country\": \"US\", \"phone\": \"2223334444\"}, \"items\":[{ \"sku\":\"Heart-Pendant\"," +  "\"quantity\":" + expectedQuantity +"}] }";
		
		Response response =
				given().
					auth().preemptive().basic(username, password).
					contentType("application/json").
					queryParam("expand", "items").
			    	body(myJson).
				when().
					put(resourceId).
				then().
					log().all().
					assertThat().statusCode(httpStatusCode).
				and().
					extract().response();
		
		// Verify order details from JsonPath when response status code is 200 or error message otherwise
		JsonPath jsonPath = response.getBody().jsonPath();
		if (response.getStatusCode() == HttpStatus.SC_OK) {
			String actualOrderId = jsonPath.getString("resource.id");
			Assert.assertEquals(actualOrderId, expectedOrderId,
					String.format("OrderId returned from response is incorrect. Actual: %s -- Expected: %s", actualOrderId, expectedOrderId));
			
			ArrayList<Integer> list = jsonPath.get("resource.items.resource.items.resource.quantity");
			String actualQuantity = list.get(0).toString();
			Assert.assertEquals(actualQuantity, expectedQuantity,
					String.format("Quantity from response is incorrect. Actual: %s -- Expected: %s", actualQuantity, expectedQuantity));
		} else {
			// Verify the error message
			String actualMessage = jsonPath.getString("message");
			Assert.assertEquals(actualMessage, expectedMessage, 
					String.format("Message from response is incorrect. Actual: %s -- Expected: %s", actualMessage, expectedMessage));
		}
	}
	
    /**
     * Cancel request for /v3/orders/{orderId}/cancel endpoint returns HTTP 401 and error message for incorrect login credentials and valid orderId.
     * Cancel request for /v3/orders/{orderId}/cancel endpoint returns HTTP 404 and error message for correct login credentials and invalid orderId.
     * Cancel request for /v3/orders/{orderId}/cancel endpoint returns HTTP 200 and cancel the order for correct login credentials and valid orderId.
     * 
     * @param description
     * @param username
     * @param password
     * @param expectedOrderId
     * @param httpStatusCode
     * @param expectedSuccessMessage
     * @param expectedOrderStatus
     * @param expectedErrorMessage
     */
	@Test(dataProvider = "CancelOrderData", dataProviderClass = RestServiceDataProvider.class)
	public void cancelOrder(String description, String username, String password, String expectedOrderId, int httpStatusCode, 
			String expectedSuccessMessage, String expectedOrderStatus, String expectedErrorMessage) {
		
		logger.info(String.format("Testing %s", description));		
		String myJson = "{ \"resource\": { \"id\":" + expectedOrderId + "}}" ;
		String resourceIdPostReq = "/" + expectedOrderId + "/cancel";
		String resourceIdGetReq = "/" + expectedOrderId;
		
		Response postResponse =
				given().
					auth().preemptive().basic(username, password).
					contentType("application/json").
			    	body(myJson).
				when().
					post(resourceIdPostReq).
				then().
					log().all().
					assertThat().statusCode(httpStatusCode).
				and().
					extract().response();
		
		JsonPath postJsonPath = postResponse.getBody().jsonPath();
		if (postResponse.getStatusCode() == HttpStatus.SC_OK) {
			String actualSuccessMessage = postJsonPath.getString("message");
			Assert.assertEquals(actualSuccessMessage, expectedSuccessMessage,
					String.format("Message from response is incorrect. Actual: %s -- Expected: %s", actualSuccessMessage, expectedSuccessMessage));

			// Get the order by id and check if the order status is cancelled
			Response getResponse = 
					given().
						auth().preemptive().basic(username, password).
					when().
						get(resourceIdGetReq).
					then().
						log().all().
						assertThat().statusCode(httpStatusCode).
					and().
						extract().response();
					
			if (getResponse.getStatusCode() == HttpStatus.SC_OK) {
				JsonPath getJsonPath = getResponse.getBody().jsonPath();
				String actualOrderId = getJsonPath.getString("resource.id");
				Assert.assertEquals(actualOrderId, expectedOrderId);
				String actualOrderStatus = getJsonPath.getString("resource.status");
				Assert.assertEquals(actualOrderStatus, expectedOrderStatus, 
					String.format("Order status returned from response is incorrect. Actual: %s -- Expected: %s", actualOrderStatus, expectedOrderStatus));
			}			
		} else {
			// Verify the error message 
			String actualErrorMessage = postJsonPath.getString("message");
			Assert.assertEquals(actualErrorMessage, expectedErrorMessage, 
					String.format("Message from response is incorrect. Actual: %s -- Expected: %s", actualErrorMessage, expectedErrorMessage));
		}
	}
}
