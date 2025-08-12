package net.hypejet.jet.data.generator.generator.generators;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import net.hypejet.jet.data.generator.adpater.KeyAdapter;
import net.hypejet.jet.data.generator.generator.CodeGenerator;
import net.kyori.adventure.key.Key;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.common.CommonPacketTypes;
import net.minecraft.network.protocol.cookie.CookiePacketTypes;
import net.minecraft.network.protocol.ping.PingPacketTypes;
import org.jspecify.annotations.NonNull;

import javax.lang.model.element.Modifier;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A {@linkplain CodeGenerator code generator} generating fields containing identifiers of {@linkplain Packet packets}.
 *
 * @since 1.0
 * @see Packet
 * @see CodeGenerator
 */
public final class PacketIdentifierGenerator implements CodeGenerator {

    private final String className;
    private final ProtocolInfo.DetailsProvider detailsProvider;
    private final String javadocHeader;
    private final Class<?> keyDefinitionClass;

    /**
     * Constructs the {@linkplain PacketIdentifierGenerator packet identifier generator}.
     *
     * @param className a name that the output class should have
     * @param detailsProvider a details provider providing information about
     *                        packets whose identifiers should be extracted
     * @param javadocHeader a javadoc header that the output class should have
     * @param keyDefinitionClass a class containing constants associated with
     *                           packets whose identifiers should be extracted
     * @since 1.0
     */
    public PacketIdentifierGenerator(@NonNull String className, ProtocolInfo.@NonNull DetailsProvider detailsProvider,
                                     @NonNull String javadocHeader, @NonNull Class<?> keyDefinitionClass) {
        this.className = Objects.requireNonNull(className, "class name");
        this.detailsProvider = Objects.requireNonNull(detailsProvider, "details provider");
        this.javadocHeader = Objects.requireNonNull(javadocHeader, "javadoc header");
        this.keyDefinitionClass = Objects.requireNonNull(keyDefinitionClass, "key definition class");
    }

    @Override
    public @NonNull TypeSpec generate() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(this.className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .build())
                .addJavadoc(this.javadocHeader);

        Map<Key, String> fieldNames = this.createFieldNames();
        this.detailsProvider.details().listPackets((type, identifier) -> {
            Key key = KeyAdapter.convert(type.id());
            String fieldName = fieldNames.get(key);

            if (fieldName == null)
                throw new IllegalStateException(String.format("No field name was specified for %s packet", key));

            builder.addField(
                    FieldSpec.builder(int.class, fieldName)
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer(String.valueOf(identifier))
                            .addJavadoc(CodeBlock.of("An identifier of $S packet", key.asMinimalString()))
                            .build()
            );
        });

        return builder.build();
    }

    @Override
    public @NonNull Destination destination() {
        return Destination.SERVER;
    }

    @Override
    public @NonNull String packageName() {
        return "net.hypejet.jet.server.network.packet.identifiers";
    }

    private @NonNull Map<Key, String> createFieldNames() {
        Map<Key, String> fieldNames = new HashMap<>();
        this.extractPacketFieldNames(CommonPacketTypes.class, fieldNames);
        this.extractPacketFieldNames(CookiePacketTypes.class, fieldNames);
        this.extractPacketFieldNames(PingPacketTypes.class, fieldNames);
        this.extractPacketFieldNames(this.keyDefinitionClass, fieldNames);
        return fieldNames;
    }

    private void extractPacketFieldNames(@NonNull Class<?> clazz, @NonNull Map<Key, String> fieldNameMap) {
        ProtocolInfo.Details details = this.detailsProvider.details();
        for (Field field : clazz.getDeclaredFields()) {
            if (!PacketType.class.isAssignableFrom(field.getType())) continue;
            if (!field.accessFlags().contains(AccessFlag.STATIC)) continue;

            try {
                PacketType<?> packetType = (PacketType<?>) field.get(null);
                if (packetType.flow() != details.flow()) continue;
                fieldNameMap.put(KeyAdapter.convert(packetType.id()), field.getName());
            } catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}