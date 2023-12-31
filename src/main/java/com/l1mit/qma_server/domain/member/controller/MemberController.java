package com.l1mit.qma_server.domain.member.controller;

import com.l1mit.qma_server.domain.member.dto.request.SignInRequest;
import com.l1mit.qma_server.domain.member.dto.response.MemberInfoResponse;
import com.l1mit.qma_server.domain.member.service.MemberService;
import com.l1mit.qma_server.global.auth.AuthService;
import com.l1mit.qma_server.global.auth.oauth.dto.IdTokenResponse;
import com.l1mit.qma_server.global.common.MemberId;
import com.l1mit.qma_server.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    public MemberController(final MemberService memberService, final AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @PostMapping("/{provider}/sign-in")
    public ResponseEntity<ApiResponse<IdTokenResponse>> signIn(@PathVariable("provider") String provider, @RequestBody @Valid SignInRequest signInRequest) {
        IdTokenResponse idTokenResponse = authService.signIn(provider, signInRequest);
        return ResponseEntity.ok()
                .body(ApiResponse.createSuccessWithData(idTokenResponse));
    }

    @GetMapping("/my-info")
    public ResponseEntity<ApiResponse<MemberInfoResponse>> myInfo(@MemberId Long memberId) {
        MemberInfoResponse memberInfoResponse = memberService.findMemberInfoResponseById(memberId);
        return ResponseEntity.ok()
                .body(ApiResponse.createSuccessWithData(memberInfoResponse));
    }

}
