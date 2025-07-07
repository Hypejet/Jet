package net.hypejet.jet.data.generator;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Model.OptionSpec;

import java.nio.file.Path;

/**
 * Represents an initial execution point of the generator application.
 *
 * @since 1.0
 */
public final class GeneratorMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorMain.class);

    private GeneratorMain() {}

    /**
     * Starts execution of the generator application.
     *
     * @param args arguments that the application should be executed with
     * @since 1.0
     */
    public static void main(String[] args) {
        CommandSpec commandSpec = CommandSpec.create()
                .addOption(OptionSpec.builder("-s", "--server")
                        .type(Path.class)
                        .required(true)
                        .description("An output directory of generated Java server source files")
                        .build())
                .addOption(OptionSpec.builder("-a", "--api")
                        .type(Path.class)
                        .required(true)
                        .description("An output directory of generated Java API source files")
                        .build())
                .addOption(OptionSpec.builder("-r", "--resources")
                        .type(Path.class)
                        .required(true)
                        .description("An output directory of generated resource files")
                        .build());

        CommandLine commandLine = new CommandLine(commandSpec);
        commandLine.setExecutionStrategy(GeneratorMain::run);
        System.exit(commandLine.execute(args));
    }

    /**
     * Executes the generator with a {@linkplain CommandLine.ParseResult parse result} of application arguments.
     *
     * @param parseResult the parse result
     * @return an exit code of the execution
     * @since 1.0
     */
    private static int run(CommandLine.@NotNull ParseResult parseResult) {
        Integer helpExitCode = CommandLine.executeHelpRequest(parseResult);
        if (helpExitCode != null) return helpExitCode;

        Path serverPath = parseResult.matchedOptionValue("server", null);
        Path apiPath = parseResult.matchedOptionValue("api", null);
        Path resourcesPath = parseResult.matchedOptionValue("resources", null);

        try {
            Generators.run(serverPath, apiPath, resourcesPath);
        } catch (Exception exception) {
            LOGGER.error("An error occurred while executing the generation", exception);
            return 1;
        }

        return 0;
    }
}