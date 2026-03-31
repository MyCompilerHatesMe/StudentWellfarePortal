package com.mchm.swp.service;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtServiceTest {

    @Test
    public void JwtService_ShouldThrowIllegalStateException_WhenSecretKeyIsTooShort() {
        JwtService jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", "somethingshort");
        assertThrows(IllegalStateException.class,
                () -> ReflectionTestUtils.invokeMethod(jwtService, "verifySecretKey"));
    }
}