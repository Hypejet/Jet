package net.hypejet.jet.data.generator.level;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeAccess;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.jspecify.annotations.NullMarked;

/**
 * A {@linkplain RecipeAccess recipe access} containing no recipes.
 *
 * @since 1.0
 * @see RecipeAccess
 */
@NullMarked
final class EmptyRecipeAccess implements RecipeAccess {
    /**
     * An instance of the {@linkplain EmptyRecipeAccess empty recipe access}.
     *
     * @since 1.0
     */
    static final EmptyRecipeAccess INSTANCE = new EmptyRecipeAccess();

    private EmptyRecipeAccess() {}

    @Override
    public RecipePropertySet propertySet(ResourceKey<RecipePropertySet> var1) {
        return RecipePropertySet.EMPTY;
    }

    @Override
    public SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes() {
        return SelectableRecipe.SingleInputSet.empty();
    }
}