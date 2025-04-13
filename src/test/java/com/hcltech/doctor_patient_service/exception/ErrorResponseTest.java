package com.hcltech.doctor_patient_service.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

 class ErrorResponseTest {

    @Test
    void testSettersAndGetters()
    {
        //Arrange
        ErrorResponse errorResponse = new ErrorResponse( 400,  "ERR_400",  "Bad Request", "/user/api");
        errorResponse.setStatus(400);
        errorResponse.setError("ERR_400");
        errorResponse.setMessage("Bad Request");
        errorResponse.setTimestamp(LocalDateTime.now());

        //act

        Assertions.assertEquals(400,errorResponse.getStatus());
        Assertions.assertEquals("ERR_400",errorResponse.getError());
        Assertions.assertEquals("Bad Request",errorResponse.getMessage());
        Assertions.assertEquals("/user/api",errorResponse.getPath());
        Assertions.assertEquals(LocalDateTime.now().getHour(),errorResponse.getTimestamp().getHour());

    }


}
