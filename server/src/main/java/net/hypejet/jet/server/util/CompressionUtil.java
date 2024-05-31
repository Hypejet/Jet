package net.hypejet.jet.server.util;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Represents a utility for easier zlib compression.
 *
 * @since 1.0
 * @author Codestech
 * @see Deflater
 * @see Inflater
 */
public final class CompressionUtil {

    private CompressionUtil() {}

    /**
     * Compresses an input with {@link Deflater deflater}.
     *
     * @param input the input
     * @return the compressed data
     * @since 1.0
     */
    public static byte @NonNull [] compress(byte @NonNull [] input) {
        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] output = new byte[1024];

        while (!deflater.finished()) {
            int compressedSize = deflater.deflate(output);
            outputStream.write(output, 0, compressedSize);
        }

        return outputStream.toByteArray();
    }

    /**
     * Decompresses an input with {@link Inflater inflater}.
     *
     * @param input the input
     * @return the uncompressed data
     * @since 1.0
     */
    public static byte @NonNull [] decompress(byte @NonNull [] input) {
        Inflater inflater = new Inflater();
        inflater.setInput(input);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] output = new byte[1024];

        try {
            while (!inflater.finished()) {
                int decompressedSize = inflater.inflate(output);
                outputStream.write(output, 0, decompressedSize);
            }
        } catch (DataFormatException exception) {
            throw new IllegalArgumentException("Data of the input is not a valid compressed data", exception);
        }

        return outputStream.toByteArray();
    }
}