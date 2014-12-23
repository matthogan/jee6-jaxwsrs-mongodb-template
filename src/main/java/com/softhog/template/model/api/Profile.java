/**
 * 
 */
package com.softhog.template.model.api;

import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author matto
 * 
 */
@XmlRootElement ( name = "profile")
public class Profile
{
    /** Any character (except line breaks) */
    @Pattern ( regexp = "^.{1,100}$")
    private String name;
    /** Any character (except line breaks) */
    @Pattern ( regexp = "^.{1,255}$")
    private String email;

    /**
	 * 
	 */
    public Profile()
    {
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * @return the email
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail( String email )
    {
        this.email = email;
    }
}
