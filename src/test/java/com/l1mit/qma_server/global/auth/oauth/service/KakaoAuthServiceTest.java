package com.l1mit.qma_server.global.auth.oauth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.l1mit.qma_server.global.auth.oauth.dto.IdTokenResponse;
import com.l1mit.qma_server.global.auth.oauth.key.PublicKeyGenerator;
import com.l1mit.qma_server.global.infra.KakaoRequester;
import com.l1mit.qma_server.global.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KakaoAuthServiceTest {


    @InjectMocks
    private KakaoAuthService kakaoAuthService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PublicKeyGenerator publicKeyGenerator;

    @Mock
    private KakaoRequester kakaoRequester;

    private final String code = "code";
    private final String redirectUri = "redirectUri";

    private final IdTokenResponse idTokenResponse = new IdTokenResponse("id_token");

    @Test
    @DisplayName("Kakao Open Id Token 발급에 성공한다.")
    void getOpenIdToken() {
        //given
        given(kakaoRequester.getOpenIdToken(code, redirectUri))
                .willReturn(idTokenResponse);

        //when
        IdTokenResponse idTokenResponse = kakaoAuthService
                .getOpenIdToken(code, redirectUri);

        //then
        assertThat(idTokenResponse).isEqualTo(idTokenResponse);
    }

    @Test
    @DisplayName("Kakao Account Id를 불러오는데 성공한다.")
    void getAccountId() {
        //given
        String idToken = "IdToken";
        Map<String, String> headers = new HashMap<>();
        PublicKey publicKey = mock(PublicKey.class);

        Claims claims = Jwts.claims().setSubject("12345");

        given(jwtProvider.parseHeaders(idToken)).willReturn(headers);
        given(publicKeyGenerator.generatePublicKey(headers,
                kakaoRequester.getPublicKeys())).willReturn(publicKey);
        given(jwtProvider.getTokenClaims(idToken, publicKey)).willReturn(claims);

        //when
        String accountId = kakaoAuthService.getAccountId(idToken);

        //then
        assertThat(accountId).isEqualTo("12345");
    }


}