package net.hypejet.jet.server.entity.variant;

import net.hypejet.jet.entity.variant.llama.LlamaVariant;
import net.hypejet.jet.server.util.index.IndexUtil;
import net.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;

import java.util.Map;

/**
 * A registry of {@linkplain LlamaVariant llama variants}.
 *
 * @since 1.0
 * @see LlamaVariant
 */
@NullMarked
public final class LlamaVariantRegistry {

    private static final Index<LlamaVariant, Integer> LLAMA_VARIANTS = IndexUtil.fromMap(Map.of(
            0, LlamaVariant.CREAMY,
            1, LlamaVariant.WHITE,
            2, LlamaVariant.BROWN,
            3, LlamaVariant.GRAY
    ));

    private LlamaVariantRegistry() {}

    /**
     * Gets a registered {@linkplain LlamaVariant llama variant} with the specified numeric identifier.
     *
     * @param id the numeric identifier
     * @return the llama variant
     * @since 1.0
     */
    public static LlamaVariant llamaVariant(int id) {
        return LLAMA_VARIANTS.keyOrThrow(id);
    }

    /**
     * Gets a numeric identifier of the specified registered {@linkplain LlamaVariant llama variant}.
     *
     * @param variant the llama variant whose numeric identifier should be returned
     * @return the numeric identifier
     * @since 1.0
     */
    public static int llamaVariantId(LlamaVariant variant) {
        return LLAMA_VARIANTS.valueOrThrow(variant);
    }
}