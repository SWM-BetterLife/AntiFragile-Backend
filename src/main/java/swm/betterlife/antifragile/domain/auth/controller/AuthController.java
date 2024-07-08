package swm.betterlife.antifragile.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.domain.auth.dto.LoginRequest;
import swm.betterlife.antifragile.domain.auth.dto.LoginResponse;
import swm.betterlife.antifragile.domain.auth.dto.LogoutRequest;
import swm.betterlife.antifragile.domain.auth.dto.SignUpRequest;
import swm.betterlife.antifragile.domain.auth.service.AuthService;
import swm.betterlife.antifragile.domain.member.dto.MemberDetailResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseBody<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseBody.ok(authService.login(loginRequest));
    }

    @PostMapping("/sign-up")
    public ResponseBody<MemberDetailResponse> signUp(
        @RequestBody SignUpRequest signUpRequest
    ) {
        return ResponseBody.ok(authService.signUp(signUpRequest));
    }

    @PostMapping("/logout")
    public ResponseBody<Void> logout(
        @RequestBody LogoutRequest logoutRequest
    ) {
        authService.logout(logoutRequest);
        return ResponseBody.ok();
    }
}
