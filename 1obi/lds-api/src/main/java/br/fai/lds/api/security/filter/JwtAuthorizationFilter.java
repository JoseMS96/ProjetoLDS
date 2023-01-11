package br.fai.lds.api.security.filter;

import br.fai.lds.api.security.ApiSecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // seleciona o texto
        // ctrl alt t  -> try catch
        try {
            if (!checkJwtToken(request)) {
                SecurityContextHolder.clearContext();
                return;
            }

            Jws<Claims> claims = validateToken(request);

            if (claims == null
                    || claims.getBody().get(ApiSecurityConstants.AUTHORITIES) == null) {

                SecurityContextHolder.clearContext();

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado.");
                return;
            }

            setupSpringAuthentication(claims.getBody());
        } catch (Exception e) {
            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ocorreu um erro inesperado.");
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private void setupSpringAuthentication(Claims claims) {

        List<String> authorities = (List<String>) claims.get(ApiSecurityConstants.AUTHORITIES);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null,
                authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );

        SecurityContextHolder.getContext()
                .setAuthentication(auth);
    }

    private Jws<Claims> validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(ApiSecurityConstants.HEADER)
                .replace(ApiSecurityConstants.PREFIX, "");

        return Jwts.parserBuilder()
                .setSigningKey(ApiSecurityConstants.KEY)
                .build()
                .parseClaimsJws(jwtToken);
    }

    private boolean checkJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(ApiSecurityConstants.HEADER);

        // tem um espaco logo apos a palavra Bearer
        if (authorizationHeader == null
                || !authorizationHeader.startsWith(ApiSecurityConstants.PREFIX)) {
            return false;
        }

        return true;
    }

}
