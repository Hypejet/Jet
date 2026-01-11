package net.hypejet.jet.data.json.test;

import com.google.common.primitives.ImmutableIntArray;
import net.hypejet.jet.data.json.model.block.JsonBlock;
import net.hypejet.jet.data.json.model.block.JsonBlockEntityType;
import net.hypejet.jet.data.json.model.block.state.JsonBlockState;
import net.hypejet.jet.data.json.model.block.state.property.JsonEnumStatePropertyValueType;
import net.hypejet.jet.data.json.model.block.state.property.JsonStateProperty;
import net.kyori.adventure.key.Key;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

/**
 * Represents a GSON conversion test of block-related objects.
 *
 * @since 1.0
 */
final class BlockTest {
    @Test
    void testBlockEntityType() {
        TestUtil.test(new JsonBlockEntityType(Set.of(Key.key("glowstone"), Key.key("grass"))));
    }

    @Test
    void testBlockState() {
        TestUtil.test(new JsonBlockState(Map.of("property", "value", "this", "is-a-test"), true, true, false, true));
    }

    @Test
    void testBlock() {
    // TODO: fix this later
    //        TestUtil.test(new JsonBlock(
    //                Set.of(Key.key("vanilla"), Key.key("hypejet", "pack")),
    //                3, ImmutableIntArray.of(3, 4, 5, 6, 7, 8, 9, 10),
    //                Map.of(
    //                        "lit", JsonStateProperty.Boolean.INSTANCE,
    //                        "level", new JsonStateProperty.Integer(0, 20),
    //                        "half", new JsonStateProperty.Enum(
    //                                JsonEnumStatePropertyValueType.VERTICAL_OCCUPANCY_TYPE,
    //                                Set.of("upper", "lower")
    //                        )
    //                )
    //        ));
    }
}
