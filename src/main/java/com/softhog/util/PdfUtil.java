/**
 * 
 */
package com.softhog.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.ImportXFDF;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.fdf.FDFDocument;
import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.softhog.config.PropertiesPropertyReader;
import com.softhog.error.KVException;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.xml.XMLUtil;

/**
 * A1 - company formation form.
 * 
 * @author matto
 * 
 */
@Default
@Dependent
public class PdfUtil
{
    /** */
    private Logger logger = LoggerFactory.getLogger( PdfUtil.class );

    /**
     * 
     */
    @Inject
    private PropertiesPropertyReader properties;

    /**
     * 
     */
    public PdfUtil()
    {
    }

    /**
     * 
     * Remember to close the document...
     *  
     * @param o
     * @param xmlDocumentName
     * @param xfdfDocumentName
     * @return
     */
    public FDFDocument toXfdfDocument( Object o, String xmlDocumentName, String xfdfDocumentName ) 
    {
        try
        {    
            InputStream in = toForm( o, xmlDocumentName, xfdfDocumentName, false );
            
            org.w3c.dom.Document doc = parse( in );
            
            return new FDFDocument( doc );
        }
        catch( IOException ex )
        {
            throw new KVException( "OOPS", ex, "Failed to create the xfdf document", o ); 
        } 
    }

    /**
     * This will parse an XML stream and create a DOM document.
     *
     * @param is The stream to get the XML from.
     * @return The DOM document.
     * @throws IOException It there is an error creating the dom.
     */
    private org.w3c.dom.Document parse( InputStream is ) throws IOException
    {
        try
        {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            
            builderFactory.setXIncludeAware( false );
            builderFactory.setValidating( false );
            builderFactory.setNamespaceAware( false );
            builderFactory.setExpandEntityReferences( false );
            builderFactory.setIgnoringComments( true );
            builderFactory.setIgnoringElementContentWhitespace( true );
            
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            
            return builder.parse( is );
        }
        catch( Exception e )
        {
            IOException thrown = new IOException( e.getMessage() );
            throw thrown;
        }
    }    

    /**
     * 
     * Remember to close the document...
     *  
     * @param o
     * @param xmlDocumentName
     * @param fdfDocumentName
     * @return
     */
    public FDFDocument toFdfDocument( Object o, String xmlDocumentName, String fdfDocumentName ) 
    {
        FDFDocument fdf = null;
        
        try
        {        
            fdf = FDFDocument.load( 
                            toForm( o, xmlDocumentName, fdfDocumentName, true ) );
        }
        catch( IOException ex )
        {
            throw new KVException( "OOPS", ex, "Failed to create the fdf document", o ); 
        }
        
        return fdf;        
    }

    /**
     * Generates the document.
     * 
     * Remember to close the document.
     * 
     * @param fdf
     * @param pdfPath
     * @return
     */
    public PDDocument generate( FDFDocument fdf, URL pdfPath )
    {
        LogUtils.debug( logger, fdf );

        PDDocument pdf = null;

        ImportXFDF importer = new ImportXFDF();

        try
        {
            pdf = PDDocument.load( pdfPath );
            
            importer.importFDF( pdf, fdf );
        }
        catch( IOException ex )
        {
            throw new KVException( "OOPS", ex, "Failed to generate the document", fdf );
        }

        return pdf;
    }
    
    /**
     * @param out
     * @param pdf
     */
    public void flatten( OutputStream out, PDDocument pdf )
    {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        
        PdfReader r = null;
        
        try
        {
            pdf.save( o );
            
            r = new PdfReader( o.toByteArray() );
            
            PdfStamper stamper = new PdfStamper( r, out );
            
            stamper.setFormFlattening( true );
            // unsure about when to close here, some logic in the close method 
            stamper.close();
        }
        catch( Exception ex )
        {
            throw new KVException( "OOPS", ex, "Failed to flatten a PDF stream", pdf );
        }
        finally
        {
            if( r != null )
            {
                r.close();
            }
        }
    }

    /**
     * Get URL using a property key to reference a path
     * 
     * @param resourceKey 
     * @return
     */
    public URL getUrl( String resourceKey )
    {
        LogUtils.debug( logger, resourceKey );

        String vest = properties.getProperty( resourceKey );

        URL url = null;

        try
        {
            url = Thread.currentThread().getContextClassLoader().getResource( vest );
        }
        catch( Exception ex )
        {
            throw new KVException( "OOPS", ex, "ContextClassLoader url load failed", ex.getMessage() );
        }

        return url;
    }

    /**
     * Make this a 1 time load, i.e. store somewhere and load up if available.
     * <p>
     * Digest or some JAXB impl might be better, but this is quite fast.
     * 
     * @param o
     * @param xmlDocumentName
     * @param xfdfDocumentName
     * @param fdf
     * @return
     */
    private InputStream toForm( Object o, String xmlDocumentName, 
                    String xfdfDocumentName, Boolean fdf )
    {
        LogUtils.debug( logger, o );

        JDomDocumentLoader ldr = new JDomDocumentLoader();

        try
        {
            ldr.setDocumentName( xmlDocumentName );
        }
        catch( Exception ex )
        {
            throw new KVException( "OOPS", ex, "Failed to load a document", 
                            xmlDocumentName, ex.getMessage() );
        }

        Document d = ldr.getDocument();

        StringBuilder s = new StringBuilder();
        
        Element root = d.getRootElement();
        // O.N. path
        o = getChild( o, root.getAttribute( "path" ).getValue() );

        List<Element> es = Utils.ifNull( d.getRootElement().getChildren() );

        for( Element e : es )
        {
            appendElement( o, s, e, fdf );
        }

        String vest = Utils.format( xfdfDocumentName, s.toString() );

        return IOUtils.toInputStream( vest );
    }
    
    /**
     * @param parent
     * @param on
     * @return
     */
    private Object getChild( Object parent, String on )
    {
        Object child = parent;
        
        String[] layers = on.split( "\\." );
        
        for( String layer : layers )
        {
            try
            {
                Method m = child.getClass().getMethod( "get" + StringUtils.capitalize( layer ) );
                
                child = m.invoke( child );
            }
            catch( Exception ex )
            {
                throw new KVException( "OOPS", ex, 
                                "Failed while parsing object hierarchy", parent, child, on );
            }
        }
        
        return child;
    }

    /**
     * @param company
     * @param s
     * @param e
     */
    private void appendElement( Object o, StringBuilder s, Element e, boolean fdf )
    {
        LogUtils.debug( logger, o, s, e );

        String fdfName = e.getChildTextNormalize( "name" );

        fdfName = Utils.urlDecode( fdfName );
        // default value
        String def = Utils.ifNull( e.getChildText( "value" ), "" );

        String ret = null;
        // invoke bean method
        if( StringUtils.isNotBlank( fdfName ) )
        {
            ret = getByReflection( o, fdfName );
        }

        ret = StringUtils.isBlank( ret ) ? def : ret;
        
        if( fdf )
        {
            String type = Utils.ifNull( e.getChildText( "type" ), "S" );
            
            appendFdf( s, fdfName, type, def, ret );
        }
        else
        {
            appendXfdf( s, fdfName, ret );
        }
    }

    /**
     * @param s
     * @param fdfName
     * @param value
     */
    private void appendXfdf( StringBuilder s, String fdfName, String value )
    {
        LogUtils.debug( logger, s, fdfName, value );

        // <field name="Text7"><value>A</value></field>
        
        boolean onlyAscii = (properties.getProperty( "onlyAscii" ).charAt( 0 ) == 't');
        
        value = XMLUtil.getInstance().escapeXML( value.toUpperCase(), onlyAscii );
        
        LogUtils.debug( logger, "Escaped value=", value );
        
        s.append( "<field name=\"" ) ;
        s.append( fdfName );
        s.append( "\"><value>" );
        s.append( value );
        s.append( "</value></field>" );
    }

    /**
     * @param s
     * @param fdfName
     * @param type
     * @param def
     * @param value
     */
    private void appendFdf( StringBuilder s, String fdfName, String type, String def, String value )
    {
        LogUtils.debug( logger, s, fdfName, type, def, value );

        s.append( "<</T(" );
        s.append( fdfName );
        s.append( ")/V" );

        switch( type.charAt( 0 ) )
        {
            case 'C':
                s.append( '/' );
                break;
            case 'S':
            default:
                s.append( '(' );
                break;
        }

        s.append( value.toUpperCase() );

        switch( type.charAt( 0 ) )
        {
            case 'S':
            default:
                s.append( ')' );
                break;
        }

        s.append( ">>" );
    }

    /**
     * @param victim
     * @param member
     */
    @SuppressWarnings ( "unchecked")
    private <U> U getByReflection( Object victim, String member )
    {
        LogUtils.debug( logger, victim, member );

        try
        {
            Method m = victim.getClass().getMethod(
                            "get" + member.substring( 0, 1 ).toUpperCase() + member.substring( 1 ) );

            Object o = m.invoke( victim );

            return (U) o;
        }
        catch( Exception ex )
        {
            /*
             * throw new KVException( "OOPS", ex, "Failed to invoke a method", ex.getMessage(), victim, member );
             */
            return null;
        }
    }
}
