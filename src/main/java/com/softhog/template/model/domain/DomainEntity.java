/**
 * 
 */
package com.softhog.template.model.domain;

import com.softhog.template.dao.Key;

/**
 * @author matto
 * 
 */
public class DomainEntity extends Key<String>
{        
    /**
     * 
     */
    private static final long serialVersionUID = 7517958020159873249L;
    /**  */
    protected String data;
    
    /**
     * 
     */
    public DomainEntity()
    {
    }

    /**
     * @return the data
     */
    public String getData()
    {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData( String data )
    {
        this.data = data;
    }
}
