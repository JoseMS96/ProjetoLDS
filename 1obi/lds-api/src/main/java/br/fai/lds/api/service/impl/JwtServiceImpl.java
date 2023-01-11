package br.fai.lds.api.service.impl;

import br.fai.lds.api.security.ApiSecurityConstants;
import br.fai.lds.api.service.JwtService;
import br.fai.lds.model.entities.UserModel;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    @Override
    public String getJwtToken(UserModel user) {

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getType()));

        try {
            String token = Jwts.builder()
                    .setId("FAI_LDS_2022")
                    .setSubject(user.getUsername())
                    .claim(ApiSecurityConstants.AUTHORITIES, grantedAuthorities.stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + convertToMinutes(10)))
                    .signWith(ApiSecurityConstants.KEY)
                    .compact();

            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return ApiSecurityConstants.INVALID_TOKEN;
        }
    }

    private int convertToMinutes(int minutes) {
        return minutes * 60 * 1000;
    }

}
