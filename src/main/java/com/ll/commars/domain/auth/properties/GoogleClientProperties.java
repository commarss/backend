package com.ll.commars.domain.auth.properties;

import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "oauth.google")
@Validated
public record GoogleClientProperties(
	@NotBlank
	@URL
	String tokenUrl,

	@NotBlank
	@URL
	String userInfoUrl,

	@NotBlank
	@URL
	String redirectUrl,

	@NotBlank
	String clientId,

	@NotBlank
	String clientSecret
) {
}
