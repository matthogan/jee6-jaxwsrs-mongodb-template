/**
 * 
 */
package com.softhog.error;

import java.io.Serializable;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Key:Value exception. Key mapped to an internal resource - db or properties file.
 *  
 * @author
 * 
 */
public class KVException extends RuntimeException implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -2916650644044072546L;

    /** Typically the lhs of a k:v pair */
    private String code = null;

    /** Integrated into the generic error string */
    private transient String[] messages = null;

    /** Integrated into the generic error string */
    private transient Object[] objects = null;

    /** Store the message */
    private String message = null;

    /** Overriding the exception cause */
    private transient Throwable cause = null;

    /**
     * @param code
     *            - error code identifying the error message
     * @param messages
     *            - the text items to be processed into the string identified by the code
     */
    public KVException( String code, String... messages )
    {
        this.code = code;
        this.messages = messages;
    }

    /**
     * @param code
     *            - error code identifying the error message
     * @param messages
     *            - the text items to be processed into the string identified by the code
     */
    public KVException()
    {
    }

    /**
     * @param code
     *            - error code identifying the error message
     * @param messages
     *            - the text items to be processed into the string identified by the code
     * @param cause
     *            - some other linked error, like a SQLException
     */
    public KVException( String code, Throwable cause, String... messages )
    {
        this.code = code;
        this.cause = cause;
        this.messages = messages;
    }

    /**
     * @param code
     *            - error code identifying the error message
     * @param objects
     *            - preferable objects with useful toString impls
     * @param cause
     *            - some other linked error, like a SQLException
     */
    public KVException( String code, Throwable cause, Object... objects )
    {
        this.code = code;
        this.cause = cause;
        this.objects = objects;
    }

    /**
     * @param message
     */
    public void setMessage( String message )
    {
        this.message = message;
    }

    /**
     * Returns the populated error message based on the error string
     * 
     * @todo - decide on error format
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage()
    {
        if( message != null )
        {
            return message;
        }

        if( messages != null )
        {
            return StringUtils.join( messages );
        }

        if( objects != null )
        {
            return StringUtils.join( objects );
        }

        return "";
    }

    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode( String code )
    {
        this.code = code;
    }

    /**
     * @return the cause
     */
    public Throwable getCause()
    {
        return cause;
    }

    /**
     * @param cause
     *            the cause to set
     */
    public void setCause( Throwable cause )
    {
        this.cause = cause;
    }

    /**
     * @return the messages
     */
    public String[] getMessages()
    {
        return messages;
    }

    /**
     * ??
     * 
     * @see java.lang.Throwable#toString()
     */
    @Override
    public String toString()
    {
        return super.toString() + ", code:" + code + ", " + ArrayUtils.toString( getMessages() );
    }

    /**
     * @return the objects
     */
    public Object[] getObjects()
    {
        return objects;
    }

    /**
     * @param objects the objects to set
     */
    public void setObjects( Object[] objects )
    {
        this.objects = objects;
    }
}
