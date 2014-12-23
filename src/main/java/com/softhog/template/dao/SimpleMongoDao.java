/**
 * 
 */
package com.softhog.template.dao;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.softhog.config.PropertiesPropertyReader;
import com.softhog.error.KVException;
import com.softhog.util.LogUtils;
import com.softhog.util.Utils;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

/**
 *  Basic DAO for a MongoDB configuration.
 * 
 * @author matto
 *
 * @see http://docs.oracle.com/cd/E24329_01/web.1211/e24368/cdi.htm#CHDDAEBA
 */
@Default
@Dependent
public class SimpleMongoDao
{
    /** */
    private Logger logger = LoggerFactory.getLogger( SimpleMongoDao.class );
    /** */
    private MongoClient mongoClient; 
    /** */
    private String databaseName; 
    /** */
    private String collectionName; 
    /** */
    @Inject
    private PropertiesPropertyReader properties;
    
    /**
     * 
     */
    public SimpleMongoDao()
    {
    }
    
    /**
     * Get that connection
     */    
    @PostConstruct
    public void postConstruct()
    {
        if( mongoClient == null )
        {
            try
            {
                // simple, some assumptions re complexity, etc
                String dbUrl = properties.getProperty( "mongodb.url.primary" );
                databaseName = properties.getProperty( "mongodb.databaseName" );
                collectionName = properties.getProperty( "mongodb.collectionName" );
                
                Utils.checkEmptyStringEx( dbUrl, "The mongodb.url property was not found in the properties" );
                Utils.checkEmptyStringEx( databaseName, "The mongodb.databaseName property was not found in the properties" );
                Utils.checkEmptyStringEx( collectionName, "The mongodb.collectionName property was not found in the properties" );
                
                MongoClientURI uri = new MongoClientURI( dbUrl );
                
                mongoClient = new MongoClient( uri );
            }
            catch( Throwable t )
            {
                LogUtils.error( logger, t, t.getMessage()  );
            }
        }        
    }
    
    /**
     * Kill that connection
     */
    @PreDestroy
    public void preDestroy()
    {
        databaseName = null;
        
        if( mongoClient != null )
        {
            try
            {
                mongoClient.close();
            }
            catch( Throwable t )
            {
                LogUtils.error( logger, t, t.getMessage()  );
            }
        }
    }
     
    /**
     * @return
     */
    protected DBCollection getDBCollection()
    {
        if( databaseName == null )
        {
            throw new KVException( "OOPS", "databaseName == null" );
        }
        
        if( mongoClient == null )
        {
            throw new KVException( "OOPS", "mongoClient == null" );
        }
        
        DB db = mongoClient.getDB( databaseName );
        
        DBCollection coll = db.getCollection( collectionName );
        
        return coll;
    }

    /**
     * @param key
     */
    public <U extends Key<?>> void delete( U key )
    {        
        DBCollection coll = getDBCollection();
        
        Gson gson = new Gson();
        
        String raw = gson.toJson( key );
        
        DBObject dbo = (DBObject)JSON.parse( raw );
        
        if( !dbo.containsField( "_id" ) )
        {                          
            dbo = new BasicDBObject( "_id", key.getValue() );
        }
        else
        {
            dbo = new BasicDBObject( "_id", dbo.get( "_id" ) );
        }
        
        WriteResult wr = coll.remove( dbo );
        
        LogUtils.debug( logger, wr, wr.getLastConcern() );
    }

    /**
     * @param key
     */
    public <U extends Key<?>> void create( U key )
    {        
        DBCollection coll = getDBCollection();
        
        Gson gson = new Gson();
        
        String raw = gson.toJson( key );
        
        DBObject dbo = (DBObject)JSON.parse( raw );
        
        if( !dbo.containsField( "_id" ) )
        {                          
            dbo.put( "_id", key.getValue() );
        }
        
        WriteResult wr = coll.insert( dbo );
        
        LogUtils.debug( logger, wr, wr.getLastConcern() );
    }

    /**
     * @param key
     */
    public <U extends Key<?>> void update( U key )
    {        
        DBCollection coll = getDBCollection();
        
        Gson gson = new Gson();
        
        String json = gson.toJson( key );
        
        DBObject dbo = (DBObject)JSON.parse( json );
                
        DBObject criteria = new BasicDBObject( "_id", key.getValue() ); 
        
        WriteResult wr = coll.update( criteria, dbo );
        
        LogUtils.debug( logger, wr, wr.getLastConcern() );
    }

    /**
     * @param key
     * @param classOfU
     * @return
     */
    public <U extends Key<?>> U readById( U key, Class<U> classOfU )
    {
        DBCollection coll = getDBCollection();
                
        DBObject criteria = new BasicDBObject( "_id", key.getValue() ); 
        
        DBObject ret = coll.findOne( criteria );
        
        if( ret == null )
        {
            return null;
        }
        
        String json = ret.toString();
        
        Gson gson = new Gson();
        
        U u = gson.fromJson( json, classOfU );
        
        return u;
    }
}
