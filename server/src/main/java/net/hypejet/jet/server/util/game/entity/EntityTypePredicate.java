package net.hypejet.jet.server.util.game.entity;

import net.hypejet.jet.entity.EntityType;
import net.hypejet.jet.registry.holder.Holder;
import net.hypejet.jet.registry.keys.EntityTypeKeys;
import net.hypejet.jet.registry.reference.RegistryReference;
import net.hypejet.jet.server.JetMinecraftServer;
import net.hypejet.jet.server.entity.JetEntityType;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import java.util.Set;
import java.util.function.BiPredicate;

/**
 * A predicate of an {@linkplain JetEntityType entity type}.
 *
 * @since 1.0
 * @see JetEntityType
 */
@NullMarked
@FunctionalInterface
public interface EntityTypePredicate extends BiPredicate<Holder.Reference<EntityType>, JetEntityType> {
    /**
     * An {@linkplain EntityTypePredicate entity type predicate} returning a constant value of {@code true}.
     *
     * @since 1.0
     */
    EntityTypePredicate TRUE = (holder, value) -> true;

    /**
     * An {@linkplain EntityTypePredicate entity type predicate} that is
     * satisfied only by living {@linkplain JetEntityType entity types}.
     *
     * @since 1.0
     */
    EntityTypePredicate LIVING = (holder, value) -> value.living();

    /**
     * An {@linkplain EntityTypePredicate entity type predicate} that is
     * satisfied only by mob {@linkplain JetEntityType entity types}.
     *
     * @since 1.0
     */
    EntityTypePredicate MOB = (holder, value) -> value.mob();

    /**
     * An {@linkplain EntityTypePredicate entity type predicate} that is
     * satisfied only by zombie-like {@linkplain JetEntityType entity types}.
     *
     * @since 1.0
     */
    EntityTypePredicate ZOMBIE = typed(
            EntityTypeKeys.ZOMBIE, EntityTypeKeys.DROWNED, EntityTypeKeys.ZOMBIE_VILLAGER,
            EntityTypeKeys.ZOMBIFIED_PIGLIN, EntityTypeKeys.HUSK
    );

    /**
     * An {@linkplain EntityTypePredicate entity type predicate} that is
     * satisfied only by raider {@linkplain JetEntityType entity types}.
     *
     * @since 1.0
     */
    EntityTypePredicate RAIDER = typed(
            EntityTypeKeys.WITCH, EntityTypeKeys.RAVAGER, EntityTypeKeys.PILLAGER,
            EntityTypeKeys.ILLUSIONER, EntityTypeKeys.EVOKER, EntityTypeKeys.VINDICATOR
    );

    /**
     * Evaluates this predicate on the specified {@linkplain JetEntityType entity type}.
     *
     * @param holder the holder referencing to the entity type that should be tested
     * @param value the value of entity type that should be tested
     * @return {@code true} if the specified entity type matches the predicate, {@code false} otherwise
     * @since 1.0
     */
    boolean test(Holder.Reference<EntityType> holder, JetEntityType value);

    /**
     * Tests the specified {@linkplain JetEntityType entity type} using
     * the specified {@linkplain EntityTypePredicate entity type predicate}
     * when the entity type value is not known.
     *
     * @param predicate the entity type predicate that the entity type should be tested with
     * @param holder the holder referencing to the entity type that should be tested
     * @param server the server containing an entity type registry that the entity type value can be retrieved from
     * @return the predicate result
     * @since 1.0
     */
    static boolean test(EntityTypePredicate predicate,
                        Holder.Reference<EntityType> holder,
                        JetMinecraftServer server) {
        EntityType entityType = holder.valueOrThrow(server.registryManager().registry(RegistryReference.ENTITY_TYPE));
        return predicate.test(holder, JetEntityType.cast(entityType));
    }

    /**
     * Creates an {@linkplain EntityTypePredicate entity type predicate}
     * which is satisfied only by the specified entity types.
     *
     * @param entityTypeKeys keys of the entity types that should satisfy the predicate
     * @return the created entity type predicate
     * @since 1.0
     */
    static EntityTypePredicate typed(Key... entityTypeKeys) {
        Set<Key> keySet = Set.of(entityTypeKeys);
        return (holder, value) -> keySet.contains(holder.key());
    }

    /**
     * Creates an {@linkplain EntityTypePredicate entity type predicate} which is satisfied only if
     * at least one of the specified {@linkplain EntityTypePredicate entity type predicates} is satisfied.
     *
     * <p>If no predicates are specified, the created predicate always returns {@code true}.</p>
     *
     * @param predicates the predicates that the "alternative" predicate should be created with
     * @return the created entity type predicate
     * @since 1.0
     */
    static EntityTypePredicate or(EntityTypePredicate... predicates) {
        if (predicates.length == 0) return TRUE;
        return (holder, value) -> {
            for (EntityTypePredicate predicate : predicates)
                if (predicate.test(holder, value)) return true;
            return false;
        };
    }
}