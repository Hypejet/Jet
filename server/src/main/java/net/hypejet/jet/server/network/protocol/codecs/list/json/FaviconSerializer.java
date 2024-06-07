package net.hypejet.jet.server.network.protocol.codecs.list.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;

/**
 * Represents a {@linkplain JsonSerializer json serializer} and a {@linkplain JsonDeserializer json deserializer},
 * which serializes and deserializes a {@linkplain BufferedImage buffered image} to a {@linkplain JsonElement json
 * element}.
 *
 * @since 1.0
 * @author Codestech
 * @see BufferedImage
 * @see JsonSerializer
 * @see JsonDeserializer
 */
public final class FaviconSerializer implements JsonSerializer<BufferedImage>, JsonDeserializer<BufferedImage> {

    private static final String IMAGE_TYPE = "png";
    private static final String PREFIX = "data:image/" + IMAGE_TYPE + ";base64,";

    @Override
    public JsonElement serialize(BufferedImage src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(src, IMAGE_TYPE, outputStream);
            return new JsonPrimitive(PREFIX + Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BufferedImage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {
            String base64Image = json.getAsString().replaceFirst(PREFIX, "");
            byte[] encodedImage = Base64.getDecoder().decode(base64Image);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedImage);
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}