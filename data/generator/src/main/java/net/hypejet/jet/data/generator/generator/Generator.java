package net.hypejet.jet.data.generator.generator;

/**
 * Represents something generating a file output.
 *
 * @since 1.0
 */
public sealed interface Generator permits CodeGenerator, ResourceGenerator {}