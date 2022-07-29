package com.gmail.at.kotamadeo;

import com.gmail.at.kotamadeo.models.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
public class Main {
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String URL = "http://94.198.50.185:7081/api/users";

    public static void main(String[] args) {
        String result = "";
        HttpHeaders headers = getSessionIdHeaders();
        User user = new User(3L, "James", "Brown", (byte) 30);
        result += postUser(user, headers).getBody();
        user.setName("Thomas");
        user.setLastName("Shelby");
        result += putUser(user, headers).getBody();
        result += deleteUser(headers, 3L).getBody();
        System.out.println("Ответ: " + result);
        System.out.println("Количество символов: " + result.length());
    }

    public static HttpHeaders getSessionIdHeaders() {
        ResponseEntity<List> responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        String sessionId = responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionId);
        return headers;
    }

    public static ResponseEntity<String> postUser(User user, HttpHeaders headers) {
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
    }

    public static ResponseEntity<String> putUser(User user, HttpHeaders headers) {
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class);
    }

    public static ResponseEntity<String> deleteUser(HttpHeaders headers, Long id) {
        HttpEntity<User> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, entity, String.class);
    }
}
