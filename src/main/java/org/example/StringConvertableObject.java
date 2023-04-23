package org.example;

/**
 * Interface to be implemented by classes that should provide conversion to and from string
 * e.g. for data storing and loading purposes.
 */
public interface StringConvertableObject {
    /**
     * Should return String containing attributes of the object implementing ObjectWithStringConversion.
     */
    String toStringWithState();
    /**
     * Should set state of the object implementing ObjectWithStringConversion from objectAsStringWithState.
     */
    void setStateFromString(String objectAsStringWithState);
}
