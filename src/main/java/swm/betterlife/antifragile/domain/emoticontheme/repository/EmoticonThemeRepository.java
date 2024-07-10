package swm.betterlife.antifragile.domain.emoticontheme.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import swm.betterlife.antifragile.common.exception.EmoticonThemeNotFoundException;
import swm.betterlife.antifragile.domain.emoticontheme.entity.EmoticonTheme;

public interface EmoticonThemeRepository extends MongoRepository<EmoticonTheme, ObjectId> {

    default EmoticonTheme getEmoticonTheme(ObjectId id) {
        return findById(id).orElseThrow(EmoticonThemeNotFoundException::new);
    }

 }
