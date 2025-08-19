package com.ll.commars.global.security.resolver;

import static com.ll.commars.global.exception.ErrorCode.*;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.ll.commars.global.exception.CustomException;
import com.ll.commars.global.security.CustomUserDetails;
import com.ll.commars.global.security.annotation.AuthMemberId;

// 관심사: @AuthMemberId가 붙은 Long 또는 long 타입의 파라미터에 인증된 사용자의 memberId를 주입
@Component
public class AuthMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(AuthMemberId.class)
			&& (parameter.getParameterType().equals(Long.class)
			|| parameter.getParameterType().equals(long.class));
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
			|| !(authentication.getPrincipal() instanceof CustomUserDetails)) {
			throw new CustomException(MEMBER_NOT_AUTHENTICATED);
		}

		return ((CustomUserDetails)authentication.getPrincipal()).getMemberId();
	}
}
