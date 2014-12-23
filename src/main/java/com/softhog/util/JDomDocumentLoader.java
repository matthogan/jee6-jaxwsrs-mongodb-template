/**
 * 
 */
package com.softhog.util;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Preloads an XML document. 
 * 
 * @author 
 *
 */
public class JDomDocumentLoader
{
    /** */
    private Logger logger = LoggerFactory.getLogger( JDomDocumentLoader.class );
    /** */
    private String documentName;
    /** */
    private Document document = null; 

    /**
     * 
     */
    public JDomDocumentLoader()
    {
    }
    
    /**
     * @return
     */
    public String getDocumentName()
    {
        return documentName;
    }
    
    /**
     * Get the loaded document. Null if 
     * unloaded.
     */
    public Document getDocument()
    {
        return document;
    }
    
    /**
     * Loads a jdom document using documentName as a
     * {@link File} constructor parameter.
     * <p>
     * The document must be stored in the same package directory
     * as this class in the runtime.
     * 
     * @see Utils#readFileToString(Class, String)
     * 
     * @param documentName 
     * @throws Exception - if documentName not found or if the XML cannot be 
     * parsed by Jdom
     */
    public void setDocumentName( String documentName )
        throws Exception
    {
        LogUtils.debug( logger, "Xml document to be loaded ",  documentName );
        
        String xml = null;
        
        try
        {
            xml = Utils.readFileToString( JDomDocumentLoader.class, documentName );
        }
        catch( Exception ex )
        {
            throw new Exception( "Failed to locate an XML file=[" 
                            + documentName + "] " + ex.getMessage(), ex );
        }
        
        setXml( xml );
    }
    
    /**
     * Merges the master to this document where
     * elements that exist in master that are not in
     * this document are added to this document.
     * 
     * @param mergee
     */
    public void outerMerge( Document master, String elementName )
    {                
        LogUtils.debug( logger, "Merging documents ", document, master, elementName );
        
        Element eDocument = document.getRootElement().getChild( elementName );
        
        List<Element> sl = eDocument .getChildren();
        
        List<Element> tl = master.getRootElement().getChild( elementName ) .getChildren();
        
        for( Element t : tl )
        {
            boolean matched = false;
            
            for( Element s : sl )
            {  
                if( s.getName().equals( t.getName() ) )
                {
                    matched = true;
                    
                    break;
                }
            }
            
            if( !matched )
            {                      
                eDocument.addContent( (Element)t.clone() );
            }
        }        
    }
    
    /**
     * Converts a JDom document to an XML string.
     * 
     * @return
     * @throws Exception
     */
    public String getXml()
        throws Exception
    {
        LogUtils.debug( logger, "Parsing XML document ", documentName );
        
        try
        {
            LogUtils.debug( logger, "Outputting XML document ", document );        
            
            XMLOutputter out = new XMLOutputter();
            
            StringWriter w = new StringWriter(); 
            
            out.output( document, w );
            
            return w.toString(); 
        }
        catch( Exception ex )
        {
            throw new Exception( "Failed to output an XML jdom document=[" 
                            + documentName + "] " + ex.getMessage(), ex );
        }       
    }
    
    /**
     * Loads a jdom document using xml as the data.
     * 
     * @param xml - valid XML 
     * @throws Exception - if the XML cannot be 
     * parsed by Jdom
     */
    public void setXml( String xml )
        throws Exception
    {
        LogUtils.debug( logger, "Parsing XML document ", xml );
        
        try
        {
            SAXBuilder builder = new SAXBuilder( XMLReaders.NONVALIDATING );
            // turn off dtd load
            builder.setXMLReaderFactory( XMLReaders.NONVALIDATING );
            builder.setFeature( "http://xml.org/sax/features/validation", false );
            builder.setFeature( "http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false );
            builder.setFeature( "http://apache.org/xml/features/nonvalidating/load-external-dtd", false );
            
            StringReader r = new StringReader( xml );
            
            document = (Document)builder.build( r );
            
            r.close();
        }
        catch( Exception ex )
        {
            throw new Exception( "Failed to load an XML jdom document=[" 
                            + documentName + "] " + ex.getMessage(), ex );
        }
    }
}
