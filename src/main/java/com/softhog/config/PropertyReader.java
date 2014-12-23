/**
 * 
 */
package com.softhog.config;

/**
 * Abstracting away the property read portion.
 * 
 * @author
 * 
 */
public interface PropertyReader
{
    
    /**
     * Return value from a key:value store.
     * 
     * @param key - the key
     * @return - the value
     */
    String getProperty( String key );
    
    /**
     * Load up the application properties.
     */
    void load();
}
