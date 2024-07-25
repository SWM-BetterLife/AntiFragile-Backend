package swm.betterlife.antifragile.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.common.response.ResponseBody;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.auth.dto.request.AuthLoginRequest;
import swm.betterlife.antifragile.domain.auth.dto.request.AuthLogoutRequest;
import swm.betterlife.antifragile.domain.auth.dto.request.AuthSignUpRequest;
import swm.betterlife.antifragile.domain.auth.dto.response.AuthLoginResponse;
import swm.betterlife.antifragile.domain.auth.dto.response.AuthSignUpResponse;
import swm.betterlife.antifragile.domain.auth.service.AuthService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseBody<AuthLoginResponse> login(@RequestBody AuthLoginRequest authLoginRequest) {
        return ResponseBody.ok(authService.login(authLoginRequest));
    }

    @PostMapping("/sign-up")
    public ResponseBody<AuthSignUpResponse> signUp(
        @RequestBody AuthSignUpRequest authSignUpRequest
    ) {
        return ResponseBody.ok(authService.signUp(authSignUpRequest));
    }

    @PostMapping("/logout")
    public ResponseBody<Void> logout(
        @RequestBody AuthLogoutRequest authLogoutRequest
    ) {
        authService.logout(authLogoutRequest);
        return ResponseBody.ok();
    }

    @DeleteMapping
    public ResponseBody<Void> delete(
        @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        authService.delete(principalDetails.memberId());
       return ResponseBody.ok();
    }

}
