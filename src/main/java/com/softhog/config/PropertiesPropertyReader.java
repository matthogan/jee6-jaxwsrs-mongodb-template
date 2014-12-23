/**
 * 
 */
package com.softhog.config;


import java.util.Properties;

import javax.enterprise.inject.Default;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softhog.error.KVException;
import com.softhog.util.LogUtils;
import com.softhog.util.Utils;

/**
 * Assign something to the {@link #appProperties} member or
 * get null pointer exceptions.
 * 
 * @author 
 *
 */
@Default
public class PropertiesPropertyReader implements PropertyReader
{
    /** */
    private Logger logger = LoggerFactory.getLogger( PropertiesPropertyReader.class );

    /**
     * Data layer interface.
     * <p>
     * Purposefully not auto-wired to let
     * the declarative config to reset it.
     */
    /*@Autowired
    @Qualifier("appProperties")*/
    private Properties appProperties;

    /**
     * Default.
     * 
     */
    public PropertiesPropertyReader()
    {
        load();
    }
    
    /**
     * @see com.softhog.config.PropertyReader#load()
     */
    public void load()
    {
        LogUtils.debug( logger, "Loading appProperties" );
        
        appProperties = Utils.loadFromClasspath( "app.properties" );        
    }

    /**
     * @see com.softhog.config.PropertyReader#getProperty(java.lang.String)
     */
    @Override
    public String getProperty( String key )
    {
        LogUtils.debug( logger, "Reading from appProperties ", key );
        
        Utils.checkBlankStringEx( key, "Property key empty" );
        
        try
        {
            String vest = appProperties.getProperty( key );
            
            Utils.checkBlankStringEx( vest, "Property:" + key +" is null" );
            
            return vest;
        }
        catch( Exception ex )
        {
            throw new KVException( "PROP_NOT_FOUND", ex,  "Property:" + key +" not found"  );
        }
    }

    /**
     * @param appProperties the appProperties to set
     */
    public void setAppProperties( Properties appProperties )
    {
        this.appProperties = appProperties;
    }
}
