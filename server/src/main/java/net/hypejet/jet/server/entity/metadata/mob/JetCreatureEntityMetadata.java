package net.hypejet.jet.server.entity.metadata.mob;

import org.jspecify.annotations.NullMarked;

import net.hypejet.jet.entity.metadata.mob.CreatureEntityMetadata;
import net.hypejet.jet.server.entity.JetEntity;

/**
 * An implementation of the {@linkplain JetCreatureEntityMetadata creature entity metadata}.
 *
 * @since 1.0
 * @see MobEntityMetadata
 */
@NullMarked
public class JetCreatureEntityMetadata extends JetMobEntityMetadata implements CreatureEntityMetadata {

    public JetCreatureEntityMetadata(JetEntity entity) {
        super(entity);
    }
}
