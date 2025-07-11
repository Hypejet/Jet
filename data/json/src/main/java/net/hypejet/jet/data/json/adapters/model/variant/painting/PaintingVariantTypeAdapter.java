package net.hypejet.jet.data.json.adapters.model.variant.painting;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.hypejet.jet.data.json.model.variant.painting.JsonPaintingVariant;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.util.Objects;

/**
 * Represents a {@linkplain TypeAdapter type adapter} of {@linkplain JsonPaintingVariant painting variants}.
 *
 * @since 1.0
 * @see JsonPaintingVariant
 * @see TypeAdapter
 */
final class PaintingVariantTypeAdapter extends TypeAdapter<JsonPaintingVariant> {

    private static final String WIDTH_FIELD = "width";
    private static final String HEIGHT_FIELD = "height";
    private static final String ASSET_FIELD = "asset";
    private static final String TITLE_FIELD = "title";
    private static final String AUTHOR_FIELD = "author";

    private final Gson gson;

    /**
     * Constructs the {@linkplain PaintingVariantTypeAdapter painting variant type adapter}.
     *
     * @param gson a gson object to convert other objects with
     * @since 1.0
     */
    PaintingVariantTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
    }

    @Override
    public void write(JsonWriter out, JsonPaintingVariant value) throws IOException {
        out.beginObject();

        out.name(WIDTH_FIELD);
        out.value(value.width());

        out.name(HEIGHT_FIELD);
        out.value(value.height());

        out.name(ASSET_FIELD);
        this.gson.toJson(value.asset(), Key.class, out);

        Component title = value.title();
        if (title != null) {
            out.name(TITLE_FIELD);
            this.gson.toJson(title, Component.class, out);
        }

        Component author = value.author();
        if (author != null) {
            out.name(AUTHOR_FIELD);
            this.gson.toJson(author, Component.class, out);
        }

        out.endObject();
    }

    @Override
    public JsonPaintingVariant read(JsonReader in) throws IOException {
        in.beginObject();

        int width = 0;
        int height = 0;
        Key asset = null;
        Component title = null;
        Component author = null;

        boolean widthInitialized = false;
        boolean heightInitialized = false;

        while (in.peek() == JsonToken.NAME) {
            String name = in.nextName();
            switch (name) {
                case WIDTH_FIELD -> {
                    width = in.nextInt();
                    widthInitialized = true;
                }
                case HEIGHT_FIELD -> {
                    height = in.nextInt();
                    heightInitialized = true;
                }
                case ASSET_FIELD -> asset = this.gson.fromJson(in, Key.class);
                case TITLE_FIELD -> title = this.gson.fromJson(in, Component.class);
                case AUTHOR_FIELD -> author = this.gson.fromJson(in, Component.class);
            }
        }

        in.endObject();

        if (!widthInitialized) {
            throw new JsonParseException("The width has not been specified");
        } else if (!heightInitialized) {
            throw new JsonParseException("The height has not been specified");
        } else if (asset == null) {
            throw new JsonParseException("The asset has not been specified");
        }

        return new JsonPaintingVariant(width, height, asset, title, author);
    }
}