/**
 * 
 */
package com.softhog.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

/**
 * @author 
 *
 */
public class LogUtils
{
    /**
     * Is this more efficient than concatenating in the log statements?
     * And gets rid of the if ... pattern...
     * <p>
     * One of my personal gripes..
     * <p>
     * There are other debug methods in the {@link Logger} that
     * take format params as well.
     * 
     * @param logger - the class level logger most likely
     * @param items - various objects with sensible toString methods...
     */
    public static void debug( Logger logger, Object... items )
    {
        if( logger.isDebugEnabled() )
        {
            logger.debug( StringUtils.join( items ) );
        }
    }
    
    /**
     * Is this more efficient than concatenating in the log statements?
     * And gets rid of the if ... pattern...
     * <p>
     * One of my personal gripes..
     * <p>
     * There are other debug methods in the {@link Logger} that
     * take format params as well.
     * 
     * @param logger - the class level logger most likely
     * @param items - various objects with sensible toString methods...
     */
    public static void warn( Logger logger, Object... items )
    {
        if( logger.isWarnEnabled() )
        {
            logger.warn( StringUtils.join( items ) );
        }
    }
    
    /**
     * Is this more efficient than concatenating in the log statements?
     * And gets rid of the if ... pattern...
     * <p>
     * One of my personal gripes..
     * <p>
     * There are other debug methods in the {@link Logger} that
     * take format params as well.
     * 
     * @param logger - the class level logger most likely
     * @param items - various objects with sensible toString methods...
     */
    public static void warn( Logger logger, Exception ex, Object... items )
    {
        if( logger.isWarnEnabled() )
        {
            logger.warn( StringUtils.join( items ), ex );
        }
    }
    
    /**
     * Is this more efficient than concatenating in the log statements?
     * And gets rid of the if ... pattern...
     * <p>
     * One of my personal gripes..
     * <p>
     * There are other debug methods in the {@link Logger} that
     * take format params as well.
     * 
     * @param logger - the class level logger most likely
     * @param items - various objects with sensible toString methods...
     */
    public static void error( Logger logger, Object... items )
    {
        if( logger.isErrorEnabled() )
        {
            logger.error( StringUtils.join( items ) );
        }
    }
    
    /**
     * Is this more efficient than concatenating in the log statements?
     * And gets rid of the if ... pattern...
     * <p>
     * One of my personal gripes..
     * <p>
     * There are other debug methods in the {@link Logger} that
     * take format params as well.
     * 
     * @param logger - the class level logger most likely
     * @param items - various objects with sensible toString methods...
     */
    public static void error( Logger logger, Exception ex, Object... items )
    {
        if( logger.isErrorEnabled() )
        {
            logger.error( StringUtils.join( items ), ex );
        }
    }
}
