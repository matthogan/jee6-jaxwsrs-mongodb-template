/**
 * 
 */
package com.softhog.template.service;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softhog.config.PropertiesPropertyReader;
import com.softhog.template.dao.SimpleMongoDao;
import com.softhog.template.model.domain.DomainEntity;

/**
 * A1 - company formation form.
 * 
 * @author matto
 * 
 */
@Default
@Dependent
public class DataService
{
    /** */
    private Logger logger = LoggerFactory.getLogger( DataService.class );

    /**
     * 
     */
    @Inject
    private PropertiesPropertyReader properties;
    
    /**
     * CRUD for MongoDB
     */
    @Inject
    private SimpleMongoDao simpleMongoDao;

    /**
     * 
     */
    public DataService()
    {
    }
    
    /**
     * @param entity
     */
    public void createDomainEntity( DomainEntity entity )
    {
        simpleMongoDao.create( entity );
    }
    
    /**
     * @param entity
     * @return
     */
    public DomainEntity readDomainEntity( DomainEntity entity )
    {
        return simpleMongoDao.readById( entity, DomainEntity.class );
    }
    
    /**
     * @param entity
     */
    public void updateDomainEntity( DomainEntity entity )
    {
        simpleMongoDao.update( entity );
    }
    
    /**
     * @param entity
     */
    public void deleteDomainEntity( DomainEntity entity )
    {
        simpleMongoDao.delete( entity );
    }
}
