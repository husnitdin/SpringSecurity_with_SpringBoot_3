package uz.geeks.springsecurity_with_springboot.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.geeks.springsecurity_with_springboot.exception.CustomResponseDto;
import uz.geeks.springsecurity_with_springboot.exception.Status;
import uz.geeks.springsecurity_with_springboot.token.Token;
import uz.geeks.springsecurity_with_springboot.token.TokenRepository;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//      filterChain.doFilter(request, response);
//      return;
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(200);
            new ObjectMapper().writeValue(response.getOutputStream(), new CustomResponseDto(
                    Status.UNAUTHORIZED.getCode(), "Token field is empty or doesnt contain Bearer"));

        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            Optional<Token> byToken = tokenRepository.findByToken(jwt);

            if (byToken.isPresent() && byToken.get().expired) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(200);
                new ObjectMapper().writeValue(response.getOutputStream(), new CustomResponseDto(
                        Status.BAD_REQUEST.getCode(), "Token is expired"));
            }

            var isTokenValid = byToken.map(t -> !t.isExpired() && !t.isRevoked())
                    .orElseThrow();

            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
