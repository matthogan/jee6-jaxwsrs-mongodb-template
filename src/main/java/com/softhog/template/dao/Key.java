/**
 * 
 */
package com.softhog.template.dao;

import java.io.Serializable;

/**
 * @author 68893
 *
 */
public class Key<T> implements Serializable
{
    /** */
    private static final long serialVersionUID = 7752199845377727098L;
    /** */
    private T value;
    
    /**
     * 
     */
    public Key()
    {
    }
    
    /**
     * 
     */
    public Key( T value )
    {
        this.value = value;
    }

    /**
     * @return the value
     */
    public T getValue()
    {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue( T value )
    {
        this.value = value;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return value == null ? "" : value.toString();
    }
}
