package net.hypejet.jet.data.json.test;

import net.hypejet.jet.data.json.util.JsonUnit;
import org.junit.jupiter.api.Test;
/**
 * Represents a GSON conversion test of utility objects.
 *
 * @since 1.0
 */
final class UtilTest {
    @Test
    void testUnit() {
        TestUtil.test(JsonUnit.INSTANCE);
    }
}