package com.chadgolden.sleeptrack.global;

import java.util.HashMap;
import java.util.Map;

/**
 * Global access point in this application for accessing values stored in key value pairs where
 * the keys and values are the String type. Type casting and conversion is the responsibility
 * of the implementing modules.
 */
public class GlobalValues {

    /** Singleton instance of this class. */
    private static GlobalValues instance;

    /** Keys and values are stored as String pairs. */
    private Map<String, String> keyValuePairs;

    /**
     * Singleton no-arg constructor of this class.
     */
    private GlobalValues() {
        keyValuePairs = new HashMap<>();
    }

    /**
     * Instantiates the instance if it doesn't currently exist then returns the instance.
     * @return The single instance of this class.
     */
    public static synchronized GlobalValues getInstance() {
        if (instance == null) {
            instance = new GlobalValues();
        }
        return instance;
    }

    /**
     * Retrieve the string value of the specified key.
     * @param key The specified key to retrieve a value for.
     * @return The value at the specified key.
     */
    public String getValue(String key) {
        return keyValuePairs.get(key);
    }

    /**
     * Add the key value pair into the map.
     * @param key The index in which to store the data within the map.
     * @param value The value to store for the specified key.
     * @return The string of the previous entry for the key, if it exists, otherwise null.
     */
    public String addKeyValuePair(String key, String value) {
        return keyValuePairs.put(key, value);
    }


}
