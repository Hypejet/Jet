package net.hypejet.jet.server.entity.illager;

import net.hypejet.jet.entity.illager.IllagerSpellType;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

/**
 * A registry of {@linkplain IllagerSpellType illager spell types}.
 *
 * @since 1.0
 * @see IllagerSpellType
 */
@NullMarked
public final class IllagerSpellTypeRegistry {

    private static final Index<IllagerSpellType, Byte> SPELL_TYPES = IndexUtil.fromMap(Map.of(
            (byte) 0, IllagerSpellType.NONE,
            (byte) 1, IllagerSpellType.SUMMON_VEX,
            (byte) 2, IllagerSpellType.SUMMON_FANGS,
            (byte) 3, IllagerSpellType.SHEEP_COLOR_SWAP,
            (byte) 4, IllagerSpellType.DISAPPEAR,
            (byte) 5, IllagerSpellType.BLINDNESS
    ));

    private IllagerSpellTypeRegistry() {}

    /**
     * Gets a registered {@linkplain IllagerSpellType illager spell type} by its numeric identifier.
     *
     * @param id the identifier
     * @return the illager spell type
     * @since 1.0
     */
    public static IllagerSpellType spellTypeById(byte id) {
        return SPELL_TYPES.keyOrThrow(id);
    }

    /**
     * Gets a numeric identifier of the specified registered {@linkplain IllagerSpellType illager spell type}.
     *
     * @param type the illager spell type whose identifier should be returned
     * @return the numeric identifier of the specified illager spell type
     * @since 1.0
     */
    public static byte spellTypeId(IllagerSpellType type) {
        return SPELL_TYPES.valueOrThrow(type);
    }
}