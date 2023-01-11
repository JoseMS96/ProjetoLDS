package br.fai.lds.api.security;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class ApiSecurityConstants {


    public static final String HEADER = "Authorization";

    public static final String PREFIX = "Bearer ";

    public static final SecretKey KEY = Keys.hmacShaKeyFor("A(*&D6a89s7djhd23jgjgasd324678asajkdHkajhsdhwuidqwdqwdhqwdyasdgd".getBytes(StandardCharsets.UTF_8));

    public static final String AUTHORITIES = "authorities";

    public static final String INVALID_TOKEN = "INVALID_TOKEN";
}
