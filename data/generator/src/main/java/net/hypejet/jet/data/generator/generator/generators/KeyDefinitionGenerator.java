package net.hypejet.jet.data.generator.generator.generators;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import net.hypejet.jet.data.generator.generator.CodeGenerator;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(KeyDefinitionGenerator.class);

    private final Registry<V> registry;
    private final String className;
    private final Class<V> registryValueClass;
    private final Class<?> keyDefinitionClass;

    /**
     * Constructs the {@linkplain KeyDefinitionGenerator key definition generator}.
     *
     * @param registry a Minecraft registry to create the key definition with
     * @param className a name that the output class should have
     * @param registryValueClass a value class of the specified registry
     * @param keyDefinitionClass a class containing constants associated with values of the specified registry
     * @since 1.0
     */
    public KeyDefinitionGenerator(@NotNull Registry<V> registry, @NotNull String className,
                                  @NotNull Class<V> registryValueClass, @NonNull Class<?> keyDefinitionClass) {
        this.registry = Objects.requireNonNull(registry, "registry");
        this.className = Objects.requireNonNull(className, "class name");
        this.registryValueClass = Objects.requireNonNull(registryValueClass, "registry value class");
        this.keyDefinitionClass = Objects.requireNonNull(keyDefinitionClass, "key definition class");
    }

    @Override
    public @NotNull TypeSpec generate() {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(this.className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addJavadoc(
                        "A definition of keys of all default $S registry entries",
                        this.registry.key().location().toString()
                );

        Map<ResourceLocation, String> fieldNames = this.createFieldNames();
        for (V value : this.registry) {
            ResourceLocation location = this.registry.getKey(value);
            if (location == null)
                throw new IllegalArgumentException("The value has no bound key in the registry");

            String locationString = location.toString();
            typeSpecBuilder.addField(
                    FieldSpec.builder(Key.class, fieldNames.get(location))
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

    @Override
    public @NonNull String packageName() {
        return "net.hypejet.jet.registry.keys";
    }

    private @NotNull Map<ResourceLocation, String> createFieldNames() {
        Map<ResourceLocation, String> fieldNames = new HashMap<>();

        for (Field field : this.keyDefinitionClass.getDeclaredFields()) {
            if (!field.accessFlags().contains(AccessFlag.STATIC)) continue;

            ResourceLocation location;
            String fieldName = field.getName();

            // Some classes also contain a field referencing to the default registry value, we should not rely on it
            if (fieldName.equalsIgnoreCase("DEFAULT")) continue;

            try {
                field.setAccessible(true);

                Class<?> type = field.getType();
                Object value = field.get(null);

                if (ResourceKey.class.isAssignableFrom(type)) {
                    location = ((ResourceKey<?>) value).location();
                } else if (Holder.Reference.class.isAssignableFrom(type)) {
                    location = ((Holder.Reference<?>) value).key().location();
                } else if (Holder.class.isAssignableFrom(type)) {
                    Holder<?> holder = (Holder<?>) value;
                    if (holder.kind() != Holder.Kind.REFERENCE) continue;
                    location = holder.unwrapKey().orElseThrow().location();
                } else if (this.registryValueClass.isAssignableFrom(type)) {
                    V registryValue = this.registryValueClass.cast(value);
                    location = this.registry.getKey(registryValue);
                    if (location == null) continue;
                } else {
                    continue;
                }
            } catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }

            fieldNames.put(location, fieldName);
        }

        for (ResourceKey<?> key : this.registry.registryKeySet()) {
            ResourceLocation location = key.location();
            if (fieldNames.containsKey(location)) continue;
            String fieldName = createConstantName(location);
            fieldNames.put(location, createConstantName(location));
            LOGGER.warn("No field name was specified for {} registry entry, using {} instead", key, fieldName);
        }

        return fieldNames;
    }

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