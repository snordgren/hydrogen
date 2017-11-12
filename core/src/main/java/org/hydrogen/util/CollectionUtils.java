package org.hydrogen.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class CollectionUtils {
    private CollectionUtils() {
    }

    public static <T> Set<T> toImmutable(Set<T> set) {
        return Collections.unmodifiableSet(new LinkedHashSet<>(set));
    }

    public static <K, V> Map<K, V> toImmutable(Map<K, V> map) {
        return Collections.unmodifiableMap(new LinkedHashMap<>(map));
    }
}
