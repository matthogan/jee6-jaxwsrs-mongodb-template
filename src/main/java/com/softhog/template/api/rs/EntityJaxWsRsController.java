/**
 * 
 */
package com.softhog.template.api.rs;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;

import com.softhog.template.model.api.ApiEntity;
import com.softhog.template.model.domain.DomainEntity;
import com.softhog.template.service.DataService;

/**
 * @author matto
 * 
 */
@Path ( "/entity")
@ApplicationScoped
public class EntityJaxWsRsController
{
    /** Data service */
    @Inject
    private DataService dataService;
    
    /**HttpServletResponse respons
	 * 
	 */
    public EntityJaxWsRsController()
    {
    }

    /**
     * Creates the company
     * 
     * @return
     */
    @POST
    @Consumes ( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces ( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path ( "/create")
    public ApiEntity create( @Valid ApiEntity entity )
    {
        DomainEntity domain = new DomainEntity();
        
        domain.setData( entity.getData() );
        
        dataService.createDomainEntity( domain );
        
        return entity;
    }

    /**
     *  
     * @return the details
     */
    @GET
    @Consumes ( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces ( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path ( "/read/{entity_id}")
    public ApiEntity read( @PathParam ( "entity_id") String entityId )
    {
        if( StringUtils.isBlank( entityId ) )
        {
            throw new IllegalArgumentException( "entity_id" );
        }

        DomainEntity domain = new DomainEntity();
        
        domain.setData( entityId );
        
        domain = dataService.readDomainEntity( domain );
                        
        ApiEntity entity = new ApiEntity();
        
        entity.setData( domain.getData() );
        
        return entity;
    }

    /**
     * Creates the company
     * 
     * @return
     */
    @PUT
    @Consumes ( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces ( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path ( "/update")
    public ApiEntity update( @Valid ApiEntity entity )
    {
        DomainEntity domain = new DomainEntity();
        
        domain.setData( entity.getData() );
        
        dataService.updateDomainEntity( domain );
        
        return entity;
    }

    /**
     * Delete 
     * 
     * @return 
     */
    @DELETE
    @Consumes ( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces ( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path ( "/delete")
    public ApiEntity delete( @PathParam ( "entity_id") String entityId )
    {
        if( StringUtils.isBlank( entityId ) )
        {
            throw new IllegalArgumentException( "entityId" );
        }
        
        DomainEntity domain = new DomainEntity();
        
        domain.setData( entityId );
        
        return new ApiEntity();
    }
}
