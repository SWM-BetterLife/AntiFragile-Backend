package swm.betterlife.antifragile.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import swm.betterlife.antifragile.common.exception.IllegalObjectIdException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectIdGenerator {

    public static ObjectId generate(String id) {
        try {
            return new ObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalObjectIdException();
        }
    }
}
