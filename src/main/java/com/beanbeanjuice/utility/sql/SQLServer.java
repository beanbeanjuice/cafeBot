package com.beanbeanjuice.utility.sql;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mariadb.jdbc.Driver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Class for creating a {@link SQLServer} object.
 *
 * @author beanbeanjuice
 */
public class SQLServer {

    private Connection connection;
    private final String url;
    private final String port;
    private final boolean encrypt;
    private final String username;
    private final String password;

    /**
     * Creates a {@link SQLServer} object.
     * @param url The url for the SQL server.
     * @param port The port for the SQL server.
     * @param encrypt Whether or not to encrypt the {@link Connection}.
     * @param username The username associated with the SQL database.
     * @param password The password associated with the SQL database.
     */
    public SQLServer(@NotNull String url, @NotNull String port, @NotNull Boolean encrypt, @NotNull String username, @NotNull String password) {
        this.url = url;
        this.port = port;
        this.encrypt = encrypt;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the converted URL for the SQL {@link Connection} to the server.
     * @return The converted SQL url.
     */
    @NotNull
    private String getConvertedURL() {
        String convertedURL = "jdbc:mysql://address=(host=%s)(port=%s)(encrypt=%s)";
        return String.format(convertedURL, url, port, encrypt);
    }

    /**
     * Starts the SQL {@link Connection}.
     * @return Whether or not the SQL {@link Connection} was successful.
     */
    @NotNull
    public Boolean startConnection() {
        Properties properties = new Properties(2);
        properties.setProperty("user", username);
        properties.setProperty("password", password);

        try {
            connection = new Driver().connect(getConvertedURL(), properties);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Tests the SQL {@link Connection}.
     * @return Whether or not the SQL {@link Connection} can be established.
     */
    @NotNull
    public Boolean testConnection() {
        Properties properties = new Properties(2);
        properties.setProperty("user", username);
        properties.setProperty("password", password);

        try {
            new Driver().connect(getConvertedURL(), properties);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the SQL {@link Connection}.
     * @return The {@link Connection}.
     */
    @Nullable
    public Connection getConnection() {
        return connection;
    }

    /**
     * Checks the SQL {@link Connection}.
     * @return Whether or not the current sql {@link Connection} is open.
     */
    @NotNull
    public Boolean checkConnection() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

}