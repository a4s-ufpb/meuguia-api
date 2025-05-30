package br.ufpb.dcx.apps4society.meuguiapbapi.config;

import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.debug("doFilterInternal called");
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("Auth header not present");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt;
            final String userEmail;

            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (userEmail != null && authentication == null) {
                log.debug("Generate new authentication for ContextHolder");
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    log.debug("Valid token for user {}", userEmail);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    log.debug("Successfully authenticated user {}", userEmail);
                }
            }
            filterChain.doFilter(request, response);
        } catch (UsernameNotFoundException e) {
            log.error("Error at {}: {}", this.getClass().getName(), e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Unauthorized");
        } catch (MalformedJwtException e) {
            log.error("Error at {}: {}", this.getClass().getName(), e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Malformed token");
        } catch (ExpiredJwtException e) {
            log.error("Error at {}: {}", this.getClass().getName(), e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Expired token");
        } catch (SignatureException e) {
            log.error("Error at {}: {}", this.getClass().getName(), e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Signature does not match local calculated signature");
        } catch (Exception e) {
            log.error("Error at {}: {}", this.getClass().getName(), e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("server internal error");
        }
    }
}
