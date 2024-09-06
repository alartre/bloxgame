package com.bloxgame.config;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationPropertyLoaderTest {

    @Test
    public void testGetPropertySuccess() throws Exception {
        String mockProperties = "someKey=someValue";
        InputStream mockInputStream = new ByteArrayInputStream(mockProperties.getBytes());

        try (MockedStatic<ApplicationPropertyLoader> mockedStatic = Mockito.mockStatic(ApplicationPropertyLoader.class)) {
            mockedStatic.when(() -> ApplicationPropertyLoader.getProperty("someKey"))
                        .thenReturn("someValue");

            String result = ApplicationPropertyLoader.getProperty("someKey");

            assertEquals("someValue", result);
        }
    }

    @Test
    public void testGetPropertyFileNotFound() {
        try (MockedStatic<ApplicationPropertyLoader> mockedStatic = Mockito.mockStatic(ApplicationPropertyLoader.class)) {
            mockedStatic.when(() -> ApplicationPropertyLoader.getProperty("someKey"))
                        .thenThrow(new IllegalStateException("application.properties file don't found"));

            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                ApplicationPropertyLoader.getProperty("someKey");
            });

            assertEquals("application.properties file don't found", exception.getMessage());
        }
    }

    @Test
    public void testIOExceptionDuringLoading() {
        try (MockedStatic<ApplicationPropertyLoader> mockedStatic = Mockito.mockStatic(ApplicationPropertyLoader.class)) {
            mockedStatic.when(() -> ApplicationPropertyLoader.getProperty("someKey"))
                        .thenThrow(new IllegalStateException("application.properties file don't found"));

            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                ApplicationPropertyLoader.getProperty("someKey");
            });

            assertEquals("application.properties file don't found", exception.getMessage());
        }
    }
}
