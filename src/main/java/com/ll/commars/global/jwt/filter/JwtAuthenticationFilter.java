package com.ll.commars.global.jwt.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ll.commars.domain.user.entity.User;
import com.ll.commars.domain.user.service.UserService;
import com.ll.commars.global.jwt.component.JwtProvider;
import com.ll.commars.global.jwt.entity.JwtAuthenticationToken;
import com.ll.commars.global.jwt.entity.JwtToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final UserService userService;

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String accessToken = authHeader.substring("Bearer ".length());

		try {
			if (jwtProvider.validateToken(accessToken)) {
				if (SecurityContextHolder.getContext().getAuthentication() == null) {
					JwtToken jwtToken = jwtProvider.extractJwtToken(accessToken);
					Optional<User> userOptional = userService.findByIdAndEmail(jwtToken.getId(), jwtToken.getEmail());

					if (userOptional.isPresent()) {
						User user = userOptional.get();
						UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
							.username(String.valueOf(user.getId()))
							.password("")
							.authorities("USER")
							.build();

						JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userDetails, null,
							userDetails.getAuthorities());
						jwtAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
					}
				}
			}
		} catch (Exception e) {
			logger.warn("JWT token validation failed: " + e.getMessage());
		}

		filterChain.doFilter(request, response);
	}
}
