/**
 * 
 */
package com.softhog.template.api.rs;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author matto
 *
 */
@Path( "/hello" )
public class Hello 
{
	/**
	 * 
	 */
	public Hello() 
	{
	}
	
	/**
	 * @return
	 */
	@GET
	@Produces( MediaType.TEXT_PLAIN )
	public String hello()
	{
		return "Hello !";		
	}
	
	/**
	 * @return
	 */
	@GET
	@Consumes( {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML} )
	@Produces( {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML} )
	public String helloJsonXml()
	{
		return "Hello !";		
	}
	
	/**
	 * @return
	 */
	@GET
	@Produces( MediaType.TEXT_HTML ) 
	public String helloHtml()
	{
		return "<div>Hello!</div>";		
	}
	
	/**
	 * @return 
	 */
	@POST
	@Consumes( {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML} )
	@Produces( {MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML} )
	public HashMap<String, String> map( HashMap<String, String> map )
	{
		return map;		
	}
}
