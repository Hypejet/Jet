package net.hypejet.jet.server.util.thread;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a {@linkplain ThreadFactory thread factory}, which creates threads with custom name format.
 *
 * @since 1.0
 * @author Codestech
 * @see Builder
 * @see ThreadType
 */
public final class JetThreadFactory implements ThreadFactory {

    private final ThreadType threadType;
    private final AtomicInteger threadId;

    private final String name;
    private final Thread.UncaughtExceptionHandler exceptionHandler;

    private final boolean inheritInheritableThreadLocals;

    private JetThreadFactory(@NonNull ThreadType threadType, int startingId, @NonNull String name,
                             Thread.@Nullable UncaughtExceptionHandler exceptionHandler,
                             boolean inheritInheritableThreadLocals) {
        this.threadType = threadType;
        this.threadId = new AtomicInteger(startingId);
        this.name = name;
        this.exceptionHandler = exceptionHandler;
        this.inheritInheritableThreadLocals = inheritInheritableThreadLocals;
    }

    @Override
    public Thread newThread(@NotNull Runnable r) {
        Thread.Builder builder = this.threadType.createBuilder()
                .name(this.name.formatted(this.threadId.getAndIncrement()))
                .inheritInheritableThreadLocals(this.inheritInheritableThreadLocals);

        if (this.exceptionHandler != null) {
            builder.uncaughtExceptionHandler(this.exceptionHandler);
        }

        return builder.unstarted(r);
    }

    /**
     * Creates a new {@linkplain Builder thread-factory builder}.
     *
     * @return the builder
     * @since 1.0
     */
    public static @NonNull Builder builder() {
        return new Builder();
    }

    /**
     * Represents a builder creating the {@linkplain JetThreadFactory Jet thread-factory}.
     *
     * @since 1.0
     * @author Codestech
     * @see JetThreadFactory
     */
    public static final class Builder {

        private ThreadType threadType = ThreadType.PLATFORM;
        private int startingId;

        private String name;
        private Thread.UncaughtExceptionHandler exceptionHandler;

        private boolean inheritInheritableThreadLocals = true;

        private Builder() {}

        /**
         * Sets a {@linkplain ThreadType type of threads} that the factory should create.
         *
         * @param threadType the type of threads
         * @return this builder
         * @since 1.0
         * @see Thread#ofPlatform()
         * @see Thread#ofVirtual()
         */
        public @NonNull Builder threadType(@NonNull ThreadType threadType) {
            this.threadType = threadType;
            return this;
        }

        /**
         * Sets a numeric thread identifier that will be assigned to a first thread created by the thread factory.
         *
         * @param startingId the identifier
         * @return this builder
         * @since 1.0
         */
        public @NonNull Builder startingId(int startingId) {
            this.startingId = startingId;
            return this;
        }

        /**
         * Sets a name format of the threads that the thread factory should create.
         *
         * @param name the name format
         * @return this builder
         * @since 1.0
         * @see Thread.Builder#setName(String)
         */
        public @NonNull Builder name(@NonNull String name) {
            this.name = Objects.requireNonNull(name, "The name must not be null");
            return this;
        }

        /**
         * Sets an {@linkplain Thread.UncaughtExceptionHandler uncaught exception handler} that will be used by threads
         * created by the thread factory.
         *
         * @param exceptionHandler the uncaught exception handler
         * @return this builder
         * @since 1.0
         * @see Thread.Builder#setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler)
         */
        public @NonNull Builder exceptionHandler(Thread.@Nullable UncaughtExceptionHandler exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
            return this;
        }

        /**
         * Sets whether the thread inherits the initial values of inheritable-thread-local variables from the
         * constructing thread. The default is to inherit.
         *
         * @param inheritInheritableThreadLocals {@code true} if the thread should inherit the initial values,
         *                                       {@code false} otherwise
         * @return this builder
         * @since 1.0
         * @see Thread.Builder#inheritInheritableThreadLocals(boolean)
         *
         */
        public @NonNull Builder inheritInheritableThreadLocals(boolean inheritInheritableThreadLocals) {
            this.inheritInheritableThreadLocals = inheritInheritableThreadLocals;
            return this;
        }

        /**
         * Builds the {@linkplain ThreadFactory thread factory}.
         *
         * @return the built thread factory
         * @since 1.0
         */
        public @NonNull ThreadFactory build() {
            return new JetThreadFactory(
                    this.threadType, this.startingId,
                    Objects.requireNonNull(this.name, "The name format was not set"),
                    this.exceptionHandler, this.inheritInheritableThreadLocals
            );
        }
    }

    /**
     * Represents a type of {@linkplain Thread thread}.
     *
     * @since 1.0
     * @author Codestech
     */
    public enum ThreadType {
        /**
         * A thread-type that creates {@linkplain Thread#ofPlatform() platform threads}.
         *
         * @since 1.0
         */
        PLATFORM {
            @Override
            public Thread.@NonNull Builder createBuilder() {
                return Thread.ofPlatform();
            }
        },
        /**
         * A thread-type that creates {@linkplain Thread#ofVirtual() virtual threads}.
         *
         * @since 1.0
         */
        VIRTUAL {
            @Override
            public Thread.@NonNull Builder createBuilder() {
                return Thread.ofVirtual();
            }
        };

        /**
         * Creates a {@linkplain Thread.Builder thread builder} with this type.
         *
         * @return the thread builder
         * @since 1.0
         */
        public abstract Thread.@NonNull Builder createBuilder();
    }
}