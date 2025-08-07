package com.ll.commars.auth;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ll.commars.domain.auth.token.JwtProperties;
import com.ll.commars.domain.auth.token.component.JwtProvider;
import com.ll.commars.domain.auth.token.entity.AccessToken;
import com.ll.commars.domain.auth.token.entity.JwtClaims;
import com.ll.commars.domain.auth.token.entity.JwtTokenValue;
import com.ll.commars.domain.auth.token.entity.RefreshToken;
import com.ll.commars.domain.member.entity.Member;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtProvider 테스트")
class JwtProviderTest {

    private JwtProvider jwtProvider;

    @Mock
    private JwtProperties jwtProperties;

    private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
        .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
        .build();

    private Member member;
    private String secretKeyString;

    @BeforeEach
    void setUp() {
        secretKeyString = "secretkey";

        when(jwtProperties.key()).thenReturn(secretKeyString);
        lenient().when(jwtProperties.accessTokenExpiration()).thenReturn(3600000L); // 1시간
        lenient().when(jwtProperties.refreshTokenExpiration()).thenReturn(1209600000L); // 2주

        jwtProvider = new JwtProvider(jwtProperties);

        member = fixtureMonkey.giveMeOne(Member.class);
    }

    @Nested
    class 토큰_생성_및_파싱_성공_테스트 {

        @Test
        void AccessToken을_성공적으로_생성하고_파싱한다() {
            // when
            AccessToken accessToken = jwtProvider.generateAccessToken(member);
            JwtClaims claims = jwtProvider.parseClaim(accessToken.token());

            // then
            assertAll(
                () -> assertThat(accessToken).isNotNull(),
                () -> assertThat(accessToken.subject()).isEqualTo(member.getEmail()),
                () -> assertThat(accessToken.expiration()).isEqualTo(jwtProperties.accessTokenExpiration()),
                () -> assertThat(claims).isNotNull(),
                () -> assertThat(claims.publicClaims().subject()).isEqualTo(member.getEmail()),
                () -> assertThat(claims.privateClaims().userId()).isEqualTo(member.getId()),
                () -> assertThat(claims.privateClaims().roles()).contains("ROLE_USER")
            );
        }

        @Test
        void RefreshToken을_성공적으로_생성하고_파싱한다() {
            // when
            RefreshToken refreshToken = jwtProvider.generateRefreshToken(member);
            JwtClaims claims = jwtProvider.parseClaim(refreshToken.token());

            // then
            assertAll(
                () -> assertThat(refreshToken).isNotNull(),
                () -> assertThat(refreshToken.subject()).isEqualTo(member.getEmail()),
                () -> assertThat(refreshToken.expiration()).isEqualTo(jwtProperties.refreshTokenExpiration()),
                () -> assertThat(claims).isNotNull(),
                () -> assertThat(claims.publicClaims().subject()).isEqualTo(member.getEmail()),
                () -> assertThat(claims.privateClaims().userId()).isEqualTo(member.getId())
            );
        }
    }

    @Nested
    class 토큰_파싱_실패_테스트 {

        @Test
        void 만료된_토큰을_파싱하면_ExpiredJwtException이_발생한다() {
            // given
            long expiredMillis = -1000L;
            JwtTokenValue expiredToken = generateTestToken(member, expiredMillis, Keys.hmacShaKeyFor(secretKeyString.getBytes()));

            // when & then
            assertThatThrownBy(() -> jwtProvider.parseClaim(expiredToken))
                .isInstanceOf(ExpiredJwtException.class);
        }

        @Test
        void 잘못된_형식의_토큰을_파싱하면_MalformedJwtException이_발생한다() {
            // given
            JwtTokenValue malformedToken = JwtTokenValue.of("this.is.malformed.token");

            // when & then
            assertThatThrownBy(() -> jwtProvider.parseClaim(malformedToken))
                .isInstanceOf(MalformedJwtException.class);
        }

        @Test
        void 잘못된_서명을_가진_토큰을_파싱하면_SignatureException이_발생한다() {
            // given
            SecretKey invalidKey = Keys.hmacShaKeyFor("thisIsAnInvalidSecretKeyThatIsDefinitelyNotTheCorrectOne".getBytes());
            JwtTokenValue tokenWithInvalidSignature = generateTestToken(member, jwtProperties.accessTokenExpiration(), invalidKey);

            // when & then
            assertThatThrownBy(() -> jwtProvider.parseClaim(tokenWithInvalidSignature))
                .isInstanceOf(SignatureException.class);
        }
    }

    private JwtTokenValue generateTestToken(Member member, long expirationMillis, SecretKey key) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(expirationMillis, ChronoUnit.MILLIS);

        String token = Jwts.builder()
            .claim("iss", "commars.com")
            .claim("sub", member.getEmail())
            .claim("iat", Date.from(now))
            .claim("exp", Date.from(expiresAt))
            .claim("userId", member.getId())
            .claim("roles", List.of("ROLE_USER"))
            .signWith(key)
            .compact();

        return JwtTokenValue.of(token);
    }
}
