package swm.betterlife.antifragile.domain.token.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import swm.betterlife.antifragile.domain.token.entity.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {
}
