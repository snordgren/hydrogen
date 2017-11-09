package org.hydrogen;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Pattern {
    private final String[] patternParts;

    public Pattern(String rawPattern) {
        this.patternParts = removeEnclosingSlash(rawPattern).split("/");
    }

    private boolean isVariable(String str) {
        return str.startsWith(":");
    }

    private boolean isWildcard(String str) {
        return str.length() == 1 && str.equals("*");
    }

    public Optional<Map<String, String>> match(String rawUrl) {
        String url = removeEnclosingSlash(rawUrl);
        String[] urlParts = url.split("/");
        if (urlParts.length == patternParts.length) {
            Map<String, String> variables = new HashMap<>();
            for (int i = 0; i < patternParts.length; i++) {
                String patternPart = patternParts[i];
                String urlPart = urlParts[i];
                if (isVariable(patternPart)) {
                    variables.put(patternPart, urlPart);
                } else if (!isWildcard(patternPart)
                        && !patternPart.equalsIgnoreCase(urlPart)) {
                    return Optional.empty();
                }
            }
            return Optional.of(Collections.unmodifiableMap(variables));
        } else return Optional.empty();
    }

    private String removeEnclosingSlash(String str) {
        if (str.isEmpty()) {
            return str;
        } else if (str.equals("/")) {
            return "";
        }

        int startIndex = 0;
        if (str.startsWith("/")) {
            startIndex++;
        }

        int endIndex = str.length();
        if (str.endsWith("/")) {
            endIndex--;
        }

        return str.substring(startIndex, endIndex);
    }
}
