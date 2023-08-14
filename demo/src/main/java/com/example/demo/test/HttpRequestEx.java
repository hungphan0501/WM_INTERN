package com.example.demo.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestEx {
    public static void main(String[] args) {
        try {
            // URL của API bạn muốn gọi
            String apiUrl = "https://api.example.com/endpoint";

            // Tạo URL object từ URL string
            URL url = new URL(apiUrl);

            // Mở kết nối HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Thiết lập method là GET
            connection.setRequestMethod("GET");

            // Lấy response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Đọc response từ InputStream
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            // In ra nội dung của response
            System.out.println("Response Content: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
