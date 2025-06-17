package br.ufpb.dcx.apps4society.meuguiapbapi.config;

import br.ufpb.dcx.apps4society.meuguiapbapi.authentication.service.JwtService;
import br.ufpb.dcx.apps4society.meuguiapbapi.exception.StandardError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import static br.ufpb.dcx.apps4society.meuguiapbapi.config.ApplicationConfig.dateTimeFormat;

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
            writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED.value(), "Unauthorized", "User not found: " + e.getMessage());
        } catch (MalformedJwtException e) {
            writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED.value(), "Malformed token", "Malformed token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED.value(), "Expired token", "Expired token: " + e.getMessage());
        } catch (SignatureException e) {
            writeErrorResponse(request, response, HttpStatus.UNAUTHORIZED.value(), "Invalid signature", "Invalid signature: " + e.getMessage());
        }
    }

    private void writeErrorResponse(HttpServletRequest request,
                                    HttpServletResponse response,
                                    int status,
                                    String error,
                                    String message) throws IOException {

        StandardError standardError = new StandardError(
                LocalDateTime.now(),
                status,
                error,
                message,
                request.getRequestURI()
        );
        log.warn("AuthenticationError: {}", standardError.toString());

        response.setStatus(status);
        response.setContentType("application/json");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat(dateTimeFormat));

        String json = mapper.writeValueAsString(standardError);
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
