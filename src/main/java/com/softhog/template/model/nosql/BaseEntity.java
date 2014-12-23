package com.softhog.template.model.nosql;

import java.io.Serializable;

import javax.persistence.Version;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * @author matto
 * 
 */
public abstract class BaseEntity implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -56994448364624637L;
    
    /** Optimistic locking is supported. */
    @Version
    protected Long version;
    
    /**
     * 
     */
    public BaseEntity()
    {
    }    

    /**
     * @return the version
     */
    public Long getVersion()
    {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion( Long version )
    {
        this.version = version;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return ReflectionToStringBuilder.toString( this );
    }
}
