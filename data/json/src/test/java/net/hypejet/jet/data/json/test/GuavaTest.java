package net.hypejet.jet.data.json.test;

import com.google.common.primitives.ImmutableIntArray;
import org.junit.jupiter.api.Test;
/**
 * Represents a GSON conversion test of guava library objects.
 *
 * @since 1.0
 */
final class GuavaTest {
    @Test
    void testImmutableIntArray() {
        TestUtil.test(ImmutableIntArray.of(
                2214, 431, 642, -123, 534251, 534, Integer.MAX_VALUE,
                -431, 1, Integer.MAX_VALUE, 2352352, 12, -4, -153
        ));
    }
}