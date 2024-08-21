package org.qinian.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpRequestSender {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Map<String, String> requestHeaders = new ConcurrentHashMap<>();

    static {
        requestHeaders.put("Content-Type", "application/json");
    }

    private static void setRequestHeaders(HttpURLConnection connection) {
        for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    public static String sendGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        setRequestHeaders(connection);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return readInputStream(connection.getInputStream());
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    public static String sendPostRequest(String urlString, Object requestBody) throws IOException {
        String jsonInputString = convertObjectToJson(requestBody);
        return sendPostRequest(urlString, jsonInputString);
    }

    public static String sendPostRequest(String urlString, String jsonInputString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        setRequestHeaders(connection);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return readInputStream(connection.getInputStream());
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }
    }

    private static String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
