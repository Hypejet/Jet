package net.hypejet.jet.data.generator.generator.generators;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import net.hypejet.jet.data.generator.generator.CodeGenerator;
import net.minecraft.SharedConstants;
import net.minecraft.WorldVersion;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.Modifier;

/**
 * Represents a {@linkplain CodeGenerator code generator} generating constant values of a Minecraft version.
 *
 * @since 1.0
 * @see CodeGenerator
 */
public final class VersionInfoGenerator implements CodeGenerator {
    @Override
    public @NotNull TypeSpec generate() {
        WorldVersion version = SharedConstants.getCurrentVersion();
        return TypeSpec.classBuilder("MinecraftVersion")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc("A definition of constant values of a Minecraft version that the server runs on.")
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addField(FieldSpec.builder(String.class, "VERSION_NAME")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer(CodeBlock.of("$S", version.name()))
                        .addJavadoc("Name of a Minecraft version that the server runs on.")
                        .build())
                .addField(FieldSpec.builder(int.class, "PROTOCOL_VERSION")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer(CodeBlock.of("$L", version.protocolVersion()))
                        .addJavadoc("A version of Minecraft protocol that the server supports.")
                        .build())
                .addField(FieldSpec.builder(int.class, "DATA_VERSION")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer(CodeBlock.of("$L", version.dataVersion().version()))
                        .addJavadoc("A version of Minecraft worlds that the server supports.")
                        .build())
                .build();
    }

    @Override
    public @NotNull Destination destination() {
        return Destination.SERVER;
    }
}