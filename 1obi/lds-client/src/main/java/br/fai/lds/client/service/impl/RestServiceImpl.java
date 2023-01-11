package br.fai.lds.client.service.impl;

import br.fai.lds.client.service.RestService;
import br.fai.lds.model.entities.UserModel;
import com.google.gson.Gson;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
public class RestServiceImpl<T> implements RestService<T> {

    private static final String BASE_ENDPOINT = "http://localhost:8081/api/";

    private String buildEndpoint(String resource) {
        return BASE_ENDPOINT + resource;
    }

    @Override
    public HttpHeaders getAuthenticationHeaders(String username, String password) {

        String auth = "Username=" + username + ";Password=" + password;

        byte[] encodedBytes;

        try {
            encodedBytes = Base64.getEncoder()
                    .encode(auth.getBytes(StandardCharsets.UTF_8));

            System.out.println("encodedBytes: " + new String(encodedBytes));

            // tem um santo espaco aqui apos a palavra
            // Basic
            String header = "Basic " + new String(encodedBytes);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", header);

            return httpHeaders;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public HttpHeaders getRequestHeaders(HttpSession httpSession) {
        try {
            UserModel user = (UserModel) httpSession.getAttribute("currentUser");

            System.out.println("userToken: " + user.getToken());

            // tem um espaco apos a palavra Bearer
            String authHeader = "Bearer " + user.getToken();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", authHeader);

            return httpHeaders;
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpHeaders();
        }
    }

    @Override
    public List<T> get(String resource, HttpHeaders requestHeaders) {
        List<T> response = null;
        final RestTemplate restTemplate = new RestTemplate();
        try {
            final HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);

            ResponseEntity<List<T>> requestResponse = restTemplate.exchange(buildEndpoint(resource), HttpMethod.GET,
                    requestEntity,
                    new ParameterizedTypeReference<List<T>>() {
                    });

            response = requestResponse.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public T getById(String resource, Class<T> clazz, HttpHeaders requestHeaders) {

        T response = null;

        final RestTemplate restTemplate = new RestTemplate();

        try {
            HttpEntity<String> requestEntity;

            if (requestHeaders != null) {
                requestEntity = new HttpEntity<>(requestHeaders);
            } else {
                requestEntity = new HttpEntity<>("");
            }

            // JSON
            final ResponseEntity<String> requestResponse = restTemplate.exchange(buildEndpoint(resource), HttpMethod.GET,
                    requestEntity,
                    String.class);

            final Gson gson = new Gson();
            response = gson.fromJson(requestResponse.getBody(), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public int post(String resource, T entity) {

        final RestTemplate restTemplate = new RestTemplate();

        try {
            final HttpEntity<T> httpEntity = new HttpEntity<>(entity);

            final ResponseEntity<String> responseEntity = restTemplate.exchange(buildEndpoint(resource),
                    HttpMethod.POST,
                    httpEntity,
                    String.class);

            final String response = responseEntity.getBody();
            return Integer.parseInt(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean put(String resource, T entity, HttpHeaders requestHeaders) {

        boolean response = false;

        final RestTemplate restTemplate = new RestTemplate();

        try {
            HttpEntity<T> httpEntity;

            if (requestHeaders != null) {
                httpEntity = new HttpEntity<>(entity, requestHeaders);
            } else {
                httpEntity = new HttpEntity<>(entity);
            }

            final ResponseEntity<Boolean> responseEntity = restTemplate.exchange(buildEndpoint(resource), HttpMethod.PUT, httpEntity, Boolean.class);

            response = responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public boolean deleteById(String resource, HttpHeaders requestHeaders) {

        boolean response = false;

        final RestTemplate restTemplate = new RestTemplate();
        try {
            HttpEntity<String> httpEntity;

            if (requestHeaders != null) {
                httpEntity = new HttpEntity<>(requestHeaders);
            } else {
                httpEntity = new HttpEntity<>("");
            }

            final ResponseEntity<Boolean> requestResponse = restTemplate.exchange(buildEndpoint(resource),
                    HttpMethod.DELETE,
                    httpEntity,
                    Boolean.class);

            response = requestResponse.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
