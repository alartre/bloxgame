package com.bloxgame.config;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2ConfigTest {

    private Connection mockConnection;
    private Statement mockStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
    }

    @Test
    public void testGetConnection() throws SQLException {
        try (MockedStatic<ApplicationPropertyLoader> propertyLoaderMock = Mockito.mockStatic(ApplicationPropertyLoader.class);
             MockedStatic<DriverManager> driverManagerMock = Mockito.mockStatic(DriverManager.class)) {

            propertyLoaderMock.when(() -> ApplicationPropertyLoader.getProperty("db.url"))
                    .thenReturn("jdbc:h2:mem:testdb");
            propertyLoaderMock.when(() -> ApplicationPropertyLoader.getProperty("db.user"))
                    .thenReturn("sa");
            propertyLoaderMock.when(() -> ApplicationPropertyLoader.getProperty("db.password"))
                    .thenReturn("");

            driverManagerMock.when(() -> DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", ""))
                    .thenReturn(mockConnection);

            Connection connection = H2Config.getConnection();

            assertNotNull(connection);
            assertEquals(mockConnection, connection);
        }
    }

    @Test
    public void testCreateTables() throws SQLException {
        try (MockedStatic<ApplicationPropertyLoader> propertyLoaderMock = Mockito.mockStatic(ApplicationPropertyLoader.class);
             MockedStatic<DriverManager> driverManagerMock = Mockito.mockStatic(DriverManager.class)) {

            propertyLoaderMock.when(() -> ApplicationPropertyLoader.getProperty("db.url"))
                    .thenReturn("jdbc:h2:mem:testdb");
            propertyLoaderMock.when(() -> ApplicationPropertyLoader.getProperty("db.user"))
                    .thenReturn("sa");
            propertyLoaderMock.when(() -> ApplicationPropertyLoader.getProperty("db.password"))
                    .thenReturn("");

            driverManagerMock.when(() -> DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", ""))
                    .thenReturn(mockConnection);

            when(mockStatement.execute(anyString())).thenReturn(true);

            H2Config.createTables();

            verify(mockStatement, times(2)).execute(anyString());
        }
    }

    @Test
    public void testCreateTablesSQLException() throws SQLException {
        try (MockedStatic<ApplicationPropertyLoader> propertyLoaderMock = Mockito.mockStatic(ApplicationPropertyLoader.class);
             MockedStatic<DriverManager> driverManagerMock = Mockito.mockStatic(DriverManager.class)) {

            propertyLoaderMock.when(() -> ApplicationPropertyLoader.getProperty("db.url"))
                    .thenReturn("jdbc:h2:mem:testdb");
            propertyLoaderMock.when(() -> ApplicationPropertyLoader.getProperty("db.user"))
                    .thenReturn("sa");
            propertyLoaderMock.when(() -> ApplicationPropertyLoader.getProperty("db.password"))
                    .thenReturn("");

            driverManagerMock.when(() -> DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", ""))
                    .thenReturn(mockConnection);

            doThrow(new SQLException("SQL Error")).when(mockStatement).execute(anyString());

            RuntimeException exception = assertThrows(RuntimeException.class, H2Config::createTables);
            assertEquals("java.sql.SQLException: SQL Error", exception.getCause().toString());
        }
    }
}
