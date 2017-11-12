package org.hydrogen;

import org.hydrogen.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents a user session.
 */
public final class Session {
    private static final Session EMPTY_SESSION =
            new Session("", true, false, Collections.emptyMap());

    private final Map<String, Object> attributes;
    private final Set<String> attributeNames;
    private final String id;
    private final boolean isNew, valid;

    Session(String id, boolean isNew, boolean valid, Map<String, Object> attributes) {
        this.id = id;
        this.isNew = isNew;
        this.valid = valid;
        this.attributes = CollectionUtils.toImmutable(attributes);
        this.attributeNames = CollectionUtils.toImmutable(attributes.keySet());
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

    /**
     * @return The list of names of attributes associated with this session.
     */
    public Set<String> getAttributeNames() {
        return attributeNames;
    }

    /**
     * @return The unique ID of this session.
     */
    public String getId() {
        return id;
    }

    /**
     * Checks if this session has a specific attribute associated with it.
     *
     * @param name The name of the attribute to check for.
     * @return True if there is an associated attribute with a matching name.
     */
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    /**
     * Invalidates this session, removing all associated values and invalidates
     * the session ID.
     *
     * @return The invalidated session.
     */
    public Session invalidate() {
        return new Session(id, isNew, false, Collections.emptyMap());
    }

    /**
     * @return True if this session was created, but the client does not know
     * of it, or has not joined it.
     */
    public boolean isNew() {
        return isNew;
    }

    /**
     *
     * @return True if this session has not been invalidated.
     */
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
        return EMPTY_SESSION;
    }
}
