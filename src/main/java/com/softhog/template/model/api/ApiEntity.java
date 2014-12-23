/**
 * 
 */
package com.softhog.template.model.api;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

import com.softhog.template.model.nosql.BaseEntity;

/**
 * @author matto
 * 
 */
@XmlRootElement ( name = "entity")
@Embeddable
public class ApiEntity extends BaseEntity
{
    /**
     * 
     */
    private static final long serialVersionUID = -2428590934452357500L;
    
    /**  */
    protected String data;

    /**
	 * 
	 */
    public ApiEntity()
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
