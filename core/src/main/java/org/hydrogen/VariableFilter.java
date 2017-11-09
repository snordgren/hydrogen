package org.hydrogen;

@FunctionalInterface
public interface VariableFilter {
    VariableHandler apply(VariableHandler handler);
}
