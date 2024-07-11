package swm.betterlife.antifragile.domain.token.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import swm.betterlife.antifragile.domain.token.dto.request.TokenReIssueRequest;
import swm.betterlife.antifragile.domain.token.dto.response.TokenIssueResponse;
import swm.betterlife.antifragile.domain.token.service.TokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/re-issuance")
    public TokenIssueResponse reIssueToken(@RequestBody TokenReIssueRequest request) {
        return tokenService.getNewTokenIssue(request);
    }
}
