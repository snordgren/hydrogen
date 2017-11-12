package org.hydrogen.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class CollectionUtils {
    public static <K, V> Map<K, V> toImmutable(Map<K, V> map) {
        return Collections.unmodifiableMap(new LinkedHashMap<>(map));
    }
}
