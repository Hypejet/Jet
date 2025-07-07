package net.hypejet.jet.data.generator.generator.generators;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import net.hypejet.jet.data.generator.generator.CodeGenerator;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Modifier;
import java.util.Objects;

/**
 * Represents a {@linkplain CodeGenerator code generator} generating a definition of typed keys
 * of default Minecraft registry entries of certain {@linkplain Registry Minecraft registry}.
 *
 * @since 1.0
 * @see CodeGenerator
 */
public final class TypedKeyDefinitionGenerator implements CodeGenerator {

    private static final char FORBIDDEN_IDENTIFIER_PART_REPLACEMENT = '_';

    private static final String CREATE_METHOD_NAME = "create";
    private static final String CREATE_PARAMETER_NAME = "key";

    private static final ClassName TYPED_KEY_CLASS_NAME = ClassName.get(
            "net.hypejet.jet.registry.key",
            "TypedKey"
    );

    private final Registry<?> registry;
    private final String className;
    private final CodeBlock resourceKeyReference;
    private final ParameterizedTypeName typedKeyTypeName;

    /**
     * Constructs the {@linkplain TypedKeyDefinitionGenerator typed-key definition generator}.
     *
     * @param registry a Minecraft registry to create the typed-key definition with
     * @param className a name that the output class should have
     * @param resourceKeyReference a code block referencing to a resource key that the typed keys
     *                             should be associated with
     * @param valueType a type name of value types that the typed keys should have
     * @see 1.0
     */
    public TypedKeyDefinitionGenerator(@NotNull Registry<?> registry, @NotNull String className,
                                       @NotNull CodeBlock resourceKeyReference, @NotNull TypeName valueType) {
        this.registry = Objects.requireNonNull(registry, "registry");
        this.className = Objects.requireNonNull(className, "class name");
        this.resourceKeyReference = Objects.requireNonNull(resourceKeyReference, "resource key reference");

        Objects.requireNonNull(valueType, "value type");
        this.typedKeyTypeName = ParameterizedTypeName.get(TYPED_KEY_CLASS_NAME, valueType);
    }

    @Override
    public @NotNull TypeSpec generate() {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(this.className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc(
                        "A definition of keys of all default $S registry entries",
                        this.registry.key().location().toString()
                )
                .addMethod(MethodSpec.methodBuilder(CREATE_METHOD_NAME)
                        .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                        .returns(this.typedKeyTypeName)
                        .addParameter(ParameterSpec.builder(String.class, CREATE_PARAMETER_NAME).build())
                        .addCode(
                                "return $L.createTypedKey($T.key($L))",
                                this.resourceKeyReference, Key.class, CREATE_PARAMETER_NAME
                        )
                        .build());

        for (ResourceLocation location : this.registry.keySet()) {
            String locationString = location.toString();
            typeSpecBuilder.addField(
                    FieldSpec.builder(this.typedKeyTypeName, createConstantName(location))
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer(CodeBlock.of("$L($S)", CREATE_METHOD_NAME, locationString))
                            .addJavadoc("A typed key of $S registry entry.", locationString)
                            .build()
            );
        }

        return typeSpecBuilder.build();
    }

    @Override
    public @NotNull Destination destination() {
        return Destination.API;
    }

    private static @NotNull String createConstantName(@NotNull ResourceLocation location) {
        String locationPathString = location.getPath();
        StringBuilder constantNameBuilder = new StringBuilder();

        for (int index = 0; index < locationPathString.length(); index++) {
            char character = locationPathString.charAt(index);
            if (index == 0 ? Character.isJavaIdentifierStart(character) : Character.isJavaIdentifierPart(character)) {
                constantNameBuilder.append(Character.toUpperCase(character));
            } else {
                constantNameBuilder.append(FORBIDDEN_IDENTIFIER_PART_REPLACEMENT);
            }
        }

        return constantNameBuilder.toString();
    }
}