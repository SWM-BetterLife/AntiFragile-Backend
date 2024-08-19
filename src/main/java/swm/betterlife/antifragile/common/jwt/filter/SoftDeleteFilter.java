package swm.betterlife.antifragile.common.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import swm.betterlife.antifragile.common.exception.HumanMemberCannotBeAccessedException;
import swm.betterlife.antifragile.common.security.PrincipalDetails;
import swm.betterlife.antifragile.domain.member.entity.Member;
import swm.betterlife.antifragile.domain.member.repository.MemberRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class SoftDeleteFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            var principal = authentication.getPrincipal();
            if (principal instanceof PrincipalDetails) {
                String memberId = ((PrincipalDetails) principal).memberId();

                Member member = memberRepository.getMember(memberId);

                if (member.getDeletedAt() != null) {
                    throw new HumanMemberCannotBeAccessedException();
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

