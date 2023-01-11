package br.fai.lds.client.service;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface RestService<T> {

    HttpHeaders getAuthenticationHeaders(String username, String password);

    HttpHeaders getRequestHeaders(HttpSession httpSession);

    List<T> get(final String resource, HttpHeaders requestHeaders);

    T getById(final String resource, Class<T> clazz, HttpHeaders requestHeaders);

    int post(final String resource, T entity);

    boolean put(final String resource, final T entity, HttpHeaders requestHeaders);

    boolean deleteById(final String resource, HttpHeaders requestHeaders);

}
