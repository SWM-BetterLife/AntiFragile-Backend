package swm.betterlife.antifragile.domain.member.dto.response;

public record MemberStatusResponse(
    Status status
) {

    public enum Status {
        EXISTENCE, NOT_EXISTENCE, HUMAN
    }
}
