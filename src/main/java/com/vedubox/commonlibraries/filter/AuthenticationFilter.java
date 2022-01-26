// package com.vedubox.common.filter;
//
// import com.vedubox.common.config.properties.AuthNotRequiredURLPath;
// import com.vedubox.common.service.AuthenticationService;
// // import com.vedubox.common.service.VeduboxUserDetailsService;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.stereotype.Component;
// import org.springframework.util.AntPathMatcher;
// import org.springframework.web.filter.OncePerRequestFilter;
//
// import javax.servlet.FilterChain;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
// import java.util.Arrays;
// import java.util.regex.Pattern;
//
//
// @Slf4j
// @RequiredArgsConstructor
// @Component
// public class AuthenticationFilter extends OncePerRequestFilter {
//
//     // private final VeduboxUserDetailsService veduboxUserDetailsService;
//     private final AuthenticationService authenticationService;
//     private final AuthNotRequiredURLPath authNotRequiredURLPath;
//     private final AntPathMatcher antPathMatcher;
//
//     private static final String JWT_NOT_FOUND_ERROR = "No JWT token found in the request headers";
//     private static final String AUTHENTICATION_DETAILS_NOT_FOUND = "Authentication details not found";
//     private static final String INVALID_AUTHORISATION_HEADER = "Authorisation header contains invalid token type";
//     private static final Pattern BEARER_PATTERN = Pattern.compile("^Bearer (.+?)$");
//
//
//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//             throws ServletException, IOException {
//
//         if (isRequestedPathPublicURL(request)) {
//             chain.doFilter(request, response);
//             return;
//         }
//
//         // After this point, endpoints require a valid JWT in the header.
//         String jwt = extractTokenFromAuthorizationHeader(request.getHeader("Authorization"));
//         String username = extractUsernameFromToken(jwt);
//
//         /**
//          * TODO: check the subject (encoded username)
//          */
//         // if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//         //
//         //     UserDetails userDetails = veduboxUserDetailsService.loadUserByUsername(username);
//         //
//         //     if(authenticationService.isValidToken(jwt, userDetails)) {
//         //         var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
//         //         usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//         //
//         //         SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//         //     }
//         // }
//
//         chain.doFilter(request, response);
//     }
//
//     private String extractTokenFromAuthorizationHeader(String authorisationHeader) {
//         if(authorisationHeader == null || authorisationHeader.isEmpty())
//             throw new AuthenticationCredentialsNotFoundException(JWT_NOT_FOUND_ERROR);
//
//         var matcher = BEARER_PATTERN.matcher(authorisationHeader);
//         if (!matcher.find()) {
//             log.error("Authorisation header contains invalid token type.");
//             throw new AuthenticationCredentialsNotFoundException(INVALID_AUTHORISATION_HEADER);
//         }
//         return matcher.group(1);
//     }
//     private String extractUsernameFromToken(String jwt) {
//         try {
//             return authenticationService.extractUsername(jwt);
//         } catch (Exception ex) {
//             log.error("Cannot extract username from the token.");
//             throw new AuthenticationCredentialsNotFoundException(AUTHENTICATION_DETAILS_NOT_FOUND);
//         }
//     }
//     private boolean isRequestedPathPublicURL(HttpServletRequest req) {
//         if(req.getRequestURI().startsWith("/public/"))
//             return true;
//
//         if (authNotRequiredURLPath.getGets() != null && HttpMethod.GET.name().equals(req.getMethod())) {
//             return Arrays.stream(authNotRequiredURLPath.getGets())
//                     .anyMatch(pattern -> antPathMatcher.match(pattern, req.getRequestURI()));
//         } else if (authNotRequiredURLPath.getPosts() != null && HttpMethod.POST.name().equals(req.getMethod())) {
//             return Arrays.stream(authNotRequiredURLPath.getPosts())
//                     .anyMatch(pattern -> antPathMatcher.match(pattern, req.getRequestURI()));
//         }
//         return false;
//     }
// }
