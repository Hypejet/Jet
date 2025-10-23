package net.hypejet.jet.server.registry.codecs.world.particle.block;

import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.server.registry.blockstate.BlockStateProperties;
import net.hypejet.jet.server.registry.codecs.adventure.KeyBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.block.state.BlockStateReferenceBinaryTagCodec;
import net.hypejet.jet.server.registry.codecs.world.particle.ParticleAdditionalCodec;
import net.hypejet.jet.server.world.particle.block.JetBlockParticle;
import net.hypejet.jet.world.block.BlockType;
import net.hypejet.jet.world.block.state.BlockStateReference;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jspecify.annotations.NullMarked;

import static net.hypejet.jet.server.util.nbt.BinaryTagUtil.requiredTag;

/**
 * A {@linkplain ParticleAdditionalCodec particle additional codec} of {@linkplain JetBlockParticle block particles}.
 *
 * @since 1.0
 * @see JetBlockParticle
 * @see ParticleAdditionalCodec
 */
@NullMarked
public final class BlockParticleAdditionalCodec
        implements ParticleAdditionalCodec<JetBlockParticle, JetBlockParticle.Builder> {

    private static final String BLOCK_STATE_FIELD = "block_state";

    /**
     * An instance of the {@linkplain BlockParticleAdditionalCodec block particle additional codec}.
     *
     * @since 1.0
     */
    public static final BlockParticleAdditionalCodec INSTANCE = new BlockParticleAdditionalCodec();

    private BlockParticleAdditionalCodec() {}

    @Override
    public void decode(CompoundBinaryTag compound, JetBlockParticle.Builder particleBuilder) {
        BinaryTag blockStateTag = requiredTag(BLOCK_STATE_FIELD, compound);
        BlockStateReference blockStateReference;

        try {
            blockStateReference = BlockStateReferenceBinaryTagCodec.INSTANCE.decode(blockStateTag);
        } catch (Exception exception) {
            Key blockTypeKey = KeyBinaryTagCodec.INSTANCE.decode(blockStateTag);
            Holder.Reference<BlockType> blockTypeReference = new Holder.Reference<>(blockTypeKey);
            blockStateReference = new BlockStateReference(
                    blockTypeReference,
                    BlockStateProperties.defaultProperties(blockTypeReference)
            );
        }

        particleBuilder.blockState(blockStateReference);
    }

    @Override
    public void encode(JetBlockParticle particle, CompoundBinaryTag.Builder compoundBuilder) {
        compoundBuilder.put(
                BLOCK_STATE_FIELD,
                BlockStateReferenceBinaryTagCodec.INSTANCE.encode(particle.blockState())
        );
    }
}