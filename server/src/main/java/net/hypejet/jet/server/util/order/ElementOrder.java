package net.hypejet.jet.server.util.order;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.hypejet.jet.data.model.api.utils.NullabilityUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Represents an order of elements, which was specified with {@linkplain List a list}. An index of elements is stored
 * in {@linkplain Map a map}, allowing for fast index getting.
 *
 * @param <E> the type of elements
 * @since 1.0
 */
public class ElementOrder<E> {

    private final List<E> sortedElements;
    private final Object2IntMap<E> elementToIdentifierMap;

    /**
     * Constructs the {@linkplain ElementOrder element order}.
     *
     * @param sortedElements a list of elements that the element order should have, the order of elements is preserved
     * @since 1.0
     */
    public ElementOrder(@NonNull List<E> sortedElements) {
        this.sortedElements = List.copyOf(NullabilityUtil.requireNonNull(sortedElements, "sorted elements"));

        Object2IntMap<E> elementToIdentifierMap = new Object2IntOpenHashMap<>();
        for (int index = 0; index < this.sortedElements.size(); index++) {
            E element = this.sortedElements.get(index);
            elementToIdentifierMap.put(element, index);
        }

        this.elementToIdentifierMap = Object2IntMaps.unmodifiable(elementToIdentifierMap);
    }

    /**
     * Gets {@linkplain List a list} of the elements with the order preserved.
     *
     * @return the list
     * @since 1.0
     */
    public final @NonNull List<E> elements() {
        return this.sortedElements;
    }

    /**
     * Gets an element with an identifier specified.
     *
     * @param identifier the identifier
     * @return the element, {@code null} if no element with the identifier specified exists
     * @since 1.0
     */
    public final @Nullable E get(int identifier) {
        return this.sortedElements.get(identifier);
    }

    /**
     * Gets an identifier of an element specified.
     *
     * @param element the element
     * @return the identifier
     * @since 1.0
     */
    public final int identifierOf(@NonNull E element) {
        Integer identifier = this.elementToIdentifierMap.get(element);
        if (identifier == null)
            throw new IllegalArgumentException("The element specified has not been registered in this element order");
        return identifier;
    }
}