package org.hydrogen;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a user session.
 */
public class Session {
    private final Map<String, Object> attributes;
    private final Set<String> attributeNames;
    private final String id;
    private final boolean isNew, valid;

    Session(String id, boolean isNew, boolean valid, Map<String, Object> attributes) {
        this.id = id;
        this.isNew = isNew;
        this.valid = valid;
        this.attributes = attributes;
        this.attributeNames = Collections.unmodifiableSet(attributes.keySet());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof Session) {
            Session session = (Session) obj;
            return session.id.equals(id) &&
                    session.isNew == isNew &&
                    session.valid == valid &&
                    session.attributes.equals(attributes);
        } else {
            return super.equals(obj);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String name) {
        return (T) attributes.get(name);
    }

    public Set<String> getAttributeNames() {
        return attributeNames;
    }

    public String getId() {
        return id;
    }

    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    public Session invalidate() {
        return new Session(id, isNew, false, attributes);
    }

    public boolean isNew() {
        return isNew;
    }

    public boolean isValid() {
        return valid;
    }

    public Session withAttribute(String key, Object value) {
        Map<String, Object> newMap = new HashMap<>(attributes.size() + 1);
        newMap.putAll(attributes);
        newMap.put(key, value);
        return new Session(id, isNew, valid, newMap);
    }

    public Session withoutAttribute(String key) {
        Map<String, Object> newMap = new HashMap<>(attributes.size() + 1);
        newMap.putAll(attributes);
        newMap.remove(key);
        return new Session(id, isNew, valid, newMap);
    }

    public static Session empty() {
        return new Session("", true, false, Collections.emptyMap());
    }
}
