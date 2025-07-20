package net.hypejet.jet.data.generator.generator.generators;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.TypeSpec;
import net.hypejet.jet.data.generator.generator.CodeGenerator;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Modifier;
import java.util.Objects;

/**
 * Represents a {@linkplain CodeGenerator code generator} generating a definition of {@linkplain Key keys}
 * of default Minecraft registry entries of certain {@linkplain Registry Minecraft registry}.
 *
 * @param <V> a value type of the registry that the keys should be extracted from
 * @since 1.0
 * @see CodeGenerator
 */
public final class KeyDefinitionGenerator<V> implements CodeGenerator {

    private static final char FORBIDDEN_IDENTIFIER_PART_REPLACEMENT = '_';

    private final Registry<V> registry;
    private final String className;

    /**
     * Constructs the {@linkplain KeyDefinitionGenerator key definition generator}.
     *
     * @param registry a Minecraft registry to create the key definition with
     * @param className a name that the output class should have
     * @since 1.0
     */
    public KeyDefinitionGenerator(@NotNull Registry<V> registry, @NotNull String className) {
        this.registry = Objects.requireNonNull(registry, "registry");
        this.className = Objects.requireNonNull(className, "class name");
    }

    @Override
    public @NotNull TypeSpec generate() {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(this.className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc(
                        "A definition of keys of all default $S registry entries",
                        this.registry.key().location().toString()
                );

        for (V value : this.registry) {
            ResourceLocation location = this.registry.getKey(value);
            if (location == null)
                throw new IllegalArgumentException("The value has no bound key in the registry");

            String locationString = location.toString();
            typeSpecBuilder.addField(
                    FieldSpec.builder(Key.class, createConstantName(location))
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer(CodeBlock.of("$T.$L($S)", Key.class, "key", locationString))
                            .addJavadoc("A key of $S registry entry.", locationString)
                            .build()
            );
        }

        return typeSpecBuilder.build();
    }

    @Override
    public @NotNull Destination destination() {
        return Destination.API;
    }

    // TODO: Replace with names specified by Minecraft
    private static @NotNull String createConstantName(@NotNull ResourceLocation location) {
        String locationPathString = location.getPath();
        StringBuilder constantNameBuilder = new StringBuilder();

        for (int index = 0; index < locationPathString.length(); index++) {
            char character = locationPathString.charAt(index);

            if (Character.isJavaIdentifierPart(character)) {
                if (index == 0 && !Character.isJavaIdentifierStart(character))
                    constantNameBuilder.append(FORBIDDEN_IDENTIFIER_PART_REPLACEMENT);
                constantNameBuilder.append(Character.toUpperCase(character));
            } else {
                constantNameBuilder.append(FORBIDDEN_IDENTIFIER_PART_REPLACEMENT);
            }
        }

        return constantNameBuilder.toString();
    }
}