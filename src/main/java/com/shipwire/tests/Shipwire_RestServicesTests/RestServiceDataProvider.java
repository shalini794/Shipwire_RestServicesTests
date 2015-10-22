package com.shipwire.tests.Shipwire_RestServicesTests;

import org.apache.http.HttpStatus;
import org.testng.annotations.DataProvider;

import com.jayway.restassured.RestAssured;

/**
 * Data Provider for Shipwire Rest service tests.
 * @author  Shalini Gupta
 */
public class RestServiceDataProvider {
	
	@DataProvider(name="CreateOrderData")
	public static Object[][] CreateOrderData() {

		return new Object[][] {
                //new Object[]{"Create order API with missing login credentials", "", "", "", HttpStatus.SC_UNAUTHORIZED, "Please include a valid Authorization header (Basic)"},
                new Object[]{"Create order API with correct login credentials", "shalinig123@yahoo.com", "test1234", "held", HttpStatus.SC_OK, ""}
			};
	}
	
	@DataProvider(name="GetOrderData")
	public static Object[][] GetOrderData() {

		return new Object[][] {
                new Object[]{"Get order API with missing login credentials for valid orderId", "", "", "91528779", "", HttpStatus.SC_UNAUTHORIZED, "Please include a valid Authorization header (Basic)"},
                new Object[]{"Get order API for invalid orderId", "shalinig123@yahoo.com", "test1234", "12345678", "", HttpStatus.SC_NOT_FOUND, "Order not found."},
                new Object[]{"Get order API for valid orderId", "shalinig123@yahoo.com", "test1234", "91528779", "held", HttpStatus.SC_OK, ""},
                new Object[]{"Get order API for valid orderId", "shalinig123@yahoo.com", "test1234", "91528037", "cancelled", HttpStatus.SC_OK, ""} // get cancelled order
                
			};
	}
	
	@DataProvider(name="UpdateOrderData")
	public static Object[][] UpdateOrderData() {

		return new Object[][] {
                new Object[]{"Update order API with missing login credentials for valid orderId", "", "", "91528779", "3", HttpStatus.SC_UNAUTHORIZED, "Please include a valid Authorization header (Basic)"},
                new Object[]{"Update order API for invalid orderId", "shalinig123@yahoo.com", "test1234", "12345678", "3", HttpStatus.SC_NOT_FOUND, "Order not found."},
                new Object[]{"Update order API for valid orderId", "shalinig123@yahoo.com", "test1234", "91528779", "3", HttpStatus.SC_OK, ""}
			};
	}
	
	@DataProvider(name="CancelOrderData")
	public static Object[][] CancelOrderData() {

		return new Object[][] {
//                new Object[]{"Cancel order API with missing login credentials for valid orderId", "", "", "91528779", HttpStatus.SC_UNAUTHORIZED, "", "", "Please include a valid Authorization header (Basic)"},
//                new Object[]{"Cancel order API for invalid orderId", "shalinig123@yahoo.com", "test1234", "12345678", HttpStatus.SC_NOT_FOUND,  "", "", "Order not found."},
                new Object[]{"Cancel order API for valid orderId", "shalinig123@yahoo.com", "test1234", "91532535", HttpStatus.SC_OK, "Order cancelled", "cancelled", ""}
			};
	}
}
