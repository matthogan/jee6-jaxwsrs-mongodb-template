/**
 * 
 */
package com.softhog.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.softhog.error.KVException;

/**
 * Generic utilities
 * 
 * @author
 * 
 */
public final class Utils
{
    /** */
    private static Logger logger = LoggerFactory.getLogger( Utils.class );

    /**
     * Listifies the params
     * 
     * @param thingslinux
     * @return List<U>
     */
    @SafeVarargs
    public static <U> List<U> listify( U... things )
    {
        if( things == null )
        {
            throw new IllegalArgumentException( "things == null" );
        }

        List<U> thingys = new ArrayList<U>();

        for( U thing : things )
        {
            thingys.add( thing );
        }

        return thingys;
    }

    /**
     * Replaces null with 0l
     * 
     * @param john
     * @return long
     */
    public static long max( Long john, Long... jane )
    {
        if( jane == null )
        {
            throw new IllegalArgumentException( "jane == null" );
        }

        long jill = checkNull( john );

        for( Long jack : jane )
        {
            jill = Math.max( checkNull( jack ), jill );
        }

        return jill;
    }

    /**
     * Replaces null with 0l
     * 
     * @param john
     * @return long
     */
    public static String checkNull( String bean )
    {
        return bean == null ? "" : bean;
    }

    /**
     * Replaces null with "", trims
     * 
     * @param john
     * @return long
     */
    public static String checkNullAndTrim( String bean )
    {
        return bean == null ? "" : bean.trim();
    }

    /**
     * Replaces null with 0l
     * 
     * @param john
     * @return long
     */
    public static long checkNull( Long john )
    {
        return john == null ? 0l : john.longValue();
    }

    /**
     * Returns formatted date, given a string date with a known format.
     * 
     * @return - as string
     */
    public static String formatDate( String date, String fromFormat, String toFormat )
    {
        if( StringUtils.isBlank( date ) )
        {
            return "";
        }

        try
        {
            SimpleDateFormat f = new SimpleDateFormat( fromFormat );

            SimpleDateFormat ff = new SimpleDateFormat( toFormat );

            return ff.format( f.parse( date ) );
        }
        catch( ParseException ex )
        {
            throw new KVException( "FORMAT_ERROR", date, fromFormat, toFormat );
        }
    }

    /**
     * Returns today's date DD/MM/YYYY
     * 
     * @return - as string
     */
    public static String getTodaysDate()
    {
        return getTodaysDate( "dd/MM/yyyy" );
    }

    /**
     * Returns year YYYY
     * 
     * @return - as int
     */
    public static Integer getYear()
    {
        Calendar c = Calendar.getInstance();

        return c.get( Calendar.YEAR );
    }

    /**
     * <li>
     * <ul>
     * -1 = date is before today
     * <ul>
     * 0 = is today
     * <ul>
     * 1 = date is after today</li>
     * 
     * @param date
     *            - yyyyMMdd
     * @param format
     *            - defaults to ddMMyyyy
     * @return
     */
    public static Integer compareToToday( String date, String format )
    {
        if( format == null )
        {
            format = "ddMMyyyy";
        }

        try
        {
            String today = getTodaysDate( format );

            SimpleDateFormat f = new SimpleDateFormat( format );

            Long now = f.parse( today ).getTime();

            Long then = f.parse( date ).getTime();

            return then.compareTo( now );
        }
        catch( Exception ex )
        {
            throw new KVException( "DATE_FORMAT_ERROR", ex, date, format );
        }
    }

    /**
     * Returns today's date with format arg.
     * 
     * @param format
     *            - dd/MM/yyyy, yyyy-MM-dd, etc.
     * @return as a string
     */
    public static String getTodaysDate( String format )
    {
        SimpleDateFormat f = new SimpleDateFormat( format );

        Calendar c = Calendar.getInstance();

        return f.format( c.getTime() );
    }

    /**
     * Returns customer's age with format arg.
     * 
     * @param format
     *            - dd/MM/yyyy, yyyy-MM-dd, etc.
     * @param dob
     * 
     * @return as an Integer
     */
    public static Integer getCustomerAge( String format, String dob )
    {
        SimpleDateFormat f = new SimpleDateFormat( format );
        Date birthday;
        try
        {
            birthday = f.parse( dob );
        }
        catch( ParseException e )
        {
            throw new KVException( "FORMAT_ERROR", format, dob );
        }
        Date current = new Date();

        Calendar a = Calendar.getInstance();
        Calendar b = Calendar.getInstance();
        a.setTime( birthday );
        b.setTime( current );
        Integer diff = b.get( Calendar.YEAR ) - a.get( Calendar.YEAR );
        if( a.get( Calendar.MONTH ) > b.get( Calendar.MONTH )
                        || (a.get( Calendar.MONTH ) == b.get( Calendar.MONTH ) && a.get( Calendar.DATE ) > b
                                        .get( Calendar.DATE )) )
        {
            diff--;
        }
        return diff;

    }

    /**
     * Rounds the d to n decimal places. Uses pow and round methods - do not bother to use in a loop, if required,
     * unfold the pow method.
     * 
     * @param d
     *            - to be rounded
     * @param n
     *            - decimal places
     * @return - the rounded value
     */
    public static double roundToN( double d, int n )
    {
        // may lose some precision due to fp errors

        double places = StrictMath.pow( 10, n );

        double p = StrictMath.round( d * places ) / places;

        return p;
    }

    /**
     * Returns nulls if value equals test
     * 
     * @param <U>
     * @param value
     * @param test
     * @return
     */
    public static <U> U nullIf( U value, U test )
    {
        if( value == null || value.equals( test ) )
        {
            return null;
        }

        return value;
    }

    /**
     * Returns 0 if null
     * 
     * @param number
     * @return the number
     */
    public static Integer ifNull( Integer number )
    {
        return number == null ? 0 : number;
    }

    /**
     * Returns 0 if null
     * 
     * @param number
     * @return the number
     */
    public static Double ifNull( Double number )
    {
        return number == null ? 0d : number;
    }

    /**
     * Returns an empty list
     * 
     * @param accs
     * @return
     */
    public static <U> List<U> ifNull( List<U> accs )
    {
        return accs == null ? new ArrayList<U>() : accs;
    }

    /**
     * Returns defaultValue when value is null, else value is returned.
     * 
     * @param accs
     * @return
     */
    public static <U> U ifNull( U value, U defaultValue )
    {
        return value == null ? defaultValue : value;
    }

    /**
     * Returns defaultValue when value is null or blank or empty, else value is returned.
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public static String ifNullOrBlank( String value, String defaultValue )
    {
        return StringUtils.isBlank( value ) ? defaultValue : value;
    }

    /**
     * Returns a new instance of defaultValue when value is null, else value is returned. Pretty ugly.
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    public static <U> U ifNull( U value, Class<U> defaultValue )
    {
        try
        {
            return value == null ? defaultValue.newInstance() : value;
        }
        catch( Exception ex )
        {
            throw new IllegalArgumentException( "Failed to load a new instance of Class<U>." + ex.getMessage(), ex );
        }
    }

    /**
     * True if not empty or null
     * 
     * @param items
     * @return
     */
    public static <U> Boolean isNotEmptyOrNull( List<U> items )
    {
        return !(items == null || items.size() == 0);
    }

    /**
     * Throws an IllegalArgumentException if vest is blank, empty, or null.
     * 
     * @param vests
     */
    public static void checkConditionEx( boolean condition, String errorCode, String... messages )
    {
        if( !condition )
        {
            throw new KVException( errorCode, messages );
        }
    }

    public static void checkConditionNullEx( Object o, String errorCode, String... messages )
    {
        if( o == null )
        {
            throw new KVException( errorCode, messages );
        }
    }

    public static void checkConditionBlankEx( String str, String errorCode, String... messages )
    {
        if( StringUtils.isBlank( str ) )
        {
            throw new KVException( errorCode, messages );
        }
    }

    /**
     * Throws an IllegalArgumentException if any of the arguments are empty or null.
     * 
     * @param vests
     */
    public static void checkEmptyStringEx( String... vests )
    {
        for( String vest : vests )
        {
            if( StringUtils.isEmpty( vest ) )
            {
                throw new IllegalArgumentException( "A string value is empty or null" );
            }
        }
    }

    /**
     * Throws an IllegalArgumentException if vest is blank, empty, or null.
     * 
     * @param vests
     */
    public static void checkEmptyStringEx( String vest, String errorMessage )
    {
        if( StringUtils.isEmpty( vest ) )
        {
            throw new KVException( "NO_VAL", errorMessage );
        }
    }

    /**
     * Throws an IllegalArgumentException if vest is blank, empty, or null.
     * 
     * @param vests
     */
    public static void checkBlankStringEx( String vest, String errorMessage )
    {
        if( StringUtils.isBlank( vest ) )
        {
            throw new KVException( "NO_VAL", errorMessage );
        }
    }

    /**
     * Throws an KVException if o is null.
     * 
     * @param o
     * @param errorMessage
     */
    public static void checkNullEx( Object o, String errorMessage )
    {
        if( o == null )
        {
            throw new KVException( "NULL", errorMessage );
        }
    }

    /**
     * Throws an IllegalArgumentException if os is null, empty, contains any null objects.
     * 
     * @param os
     * @param errorMessage
     */
    public static void checkEmptyOrNullEx( Object[] os, String errorMessage )
    {
        if( os == null || os.length == 0 )
        {
            throw new KVException( "NULL", errorMessage );
        }

        for( Object o : os )
        {
            checkNullEx( o, errorMessage );
        }
    }

    /**
     * Throws an IllegalArgumentException if os is null, empty, contains any null objects.
     * 
     * @param os
     * @param errorMessage
     */
    public static <T> void checkEmptyOrNullEx( List<T> os, String errorMessage )
    {
        if( os == null || os.size() == 0 )
        {
            throw new KVException( "NULL", errorMessage );
        }

        for( Object o : os )
        {
            checkNullEx( o, errorMessage );
        }
    }

    /**
     * Throws exception with code and message if the collection is not populated
     * 
     * @param o
     * @param errorCode
     * @param messages
     */
    public static <T> void checkEmptyOrNullEx( List<T> o, String errorCode, String... messages )
    {
        if( o == null || o.size() == 0 )
        {
            throw new KVException( errorCode, messages );
        }
    }

    /**
     * Throws an IllegalArgumentException if vests is empty, or null.
     * 
     * @param vests
     */
    public static void checkEmptyEx( String[] vests, String errorMessage )
    {
        if( ArrayUtils.isEmpty( vests ) )
        {
            throw new IllegalArgumentException( errorMessage );
        }
    }

    /**
     * @param dt
     * @param delim
     * @return
     */
    public static String yyyymmddToddmmyyyy( String dt, String delim )
    {
        String dte = dt;

        if( StringUtils.isNotBlank( dt ) && dt.length() >= 10 )
        {
            dte = dt.substring( 8, 10 ) + delim + dt.substring( 5, 7 ) + delim + dt.substring( 0, 4 );
        }

        return dte;
    }

    /**
     * Utility to return the first item in a list.
     * 
     * @param t
     * @return
     */
    public static <T> T first( List<T> t )
    {
        return (t == null || t.size() == 0 ? null : t.get( 0 ));
    }

    /**
     * Utility to return the nth item in a list.
     * 
     * @param t
     * @param index
     * @return null if out of bounds or null
     */
    public static <T> T any( List<T> t, int index )
    {
        return (t == null || t.size() <= index ? null : t.get( index ));
    }

    /**
     * Utility to return the first item in an array.
     * 
     * @param t
     * @return
     */
    public static <T> T first( T[] t )
    {
        return (t == null || t.length == 0 ? null : t[ 0 ]);
    }

    /**
     * Returns the trimmed string in an array and checks for nulls
     * 
     * @param value
     * @return
     */
    public static String[] parameterise( String value )
    {
        return new String[] { Utils.checkNullAndTrim( value ) };
    }

    /**
     * Returns the string in an array and checks for nulls
     * 
     * @param value
     * @return
     */
    public static String[] parameteriseNoTrim( String value )
    {
        return new String[] { Utils.checkNull( value ) };
    }

    /**
     * Converts true to Y, false or null to N
     * 
     * @param value
     * @return
     */
    public static String toYN( Boolean value )
    {
        if( value == null || !value )
        {
            return "N";
        }

        return "Y";
    }

    /**
     * Converts Y|N to true|false
     * 
     * @param yn
     * @return
     * @see org.apache.commons.lang.StringUtils#equals(String, String)
     */
    public static Boolean ynToBoolean( String yn )
    {
        return StringUtils.equals( yn, "Y" );
    }

    /**
     * @param data
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] b64Decode( String data, String charset ) throws UnsupportedEncodingException
    {
        if( data != null )
        {
            return Base64.decodeBase64( data.getBytes( charset ) );
        }
        else
        {
            return null;
        }
    }

    /**
     * If all are equal to then return true. False otherwise, or if vests is empty or null.
     * 
     * @param comparable
     * @param vests
     * @return
     * @see org.apache.commons.lang.StringUtils#equals(String, String)
     */
    public static boolean ifAllEqualTo( String comparable, String... vests )
    {
        if( vests == null || vests.length == 0 )
        {
            return false;
        }

        for( String vest : vests )
        {
            if( !StringUtils.equals( comparable, vest ) )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * If any are equal to then return true. False otherwise, or if vests is empty or null.
     * 
     * @param comparable
     * @param vests
     * @return
     * @see org.apache.commons.lang.StringUtils#equals(String, String)
     */
    public static boolean equalsAny( String comparable, String... vests )
    {
        if( vests == null || vests.length == 0 )
        {
            return false;
        }

        for( String vest : vests )
        {
            if( StringUtils.equals( comparable, vest ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * If any of the trimmed values are equal to then return true. False otherwise, or if vests is empty or null.
     * <p>
     * Handy for fields populated by db char fields where the trailing blanks are insignificant.
     * 
     * @param comparable
     * @param vests
     * @return
     * @see org.apache.commons.lang.StringUtils#equals(String, String)
     */
    public static boolean treaclesAny( String comparable, String... vests )
    {
        if( vests == null || vests.length == 0 )
        {
            return false;
        }

        comparable = StringUtils.trimToEmpty( comparable );

        for( String vest : vests )
        {
            if( StringUtils.equals( comparable, StringUtils.trimToEmpty( vest ) ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * If the trimmed values are equal to then return true. False otherwise, or if vests is empty or null.
     * <p>
     * Handy for fields populated by db char fields where the trailing blanks are insignificant.
     * 
     * @param comparable
     * @param vest
     * @return
     * @see org.apache.commons.lang.StringUtils#equals(String, String)
     * @see org.apache.commons.lang.StringUtils#trimToEmpty(String)
     */
    public static boolean treacles( String comparable, String vest )
    {
        if( StringUtils.equals( StringUtils.trimToEmpty( comparable ), StringUtils.trimToEmpty( vest ) ) )
        {
            return true;
        }

        return false;
    }

    /**
     * If it starts with any vest then return true. False otherwise, or if vests is empty or null.
     * 
     * @param comparable
     * @param vests
     * @return
     * @see org.apache.commons.lang.StringUtils#equals(String, String)
     */
    public static boolean startsWithAny( String comparable, String... vests )
    {
        if( vests == null || vests.length == 0 )
        {
            return false;
        }

        for( String vest : vests )
        {
            if( StringUtils.startsWith( comparable, vest ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * {@link String#substring(int, int)}, but it returns an empty string where the vest is empty or the base is further
     * out than the vest length.
     * <p>
     * "931012123456", 0, 6 = 931012
     * 
     * @param vest
     *            - the victim
     * @param base
     *            - 0 based start
     * @param offset
     *            - index + 1 of last character
     * @return
     */
    public static String substring( String vest, int base, int offset )
    {
        if( StringUtils.isEmpty( vest ) )
        {
            return "";
        }

        if( vest.length() >= base + offset )
        {
            return vest.substring( base, base + offset );
        }
        else if( vest.length() < base )
        {
            return "";
        }
        else
        {
            return vest.substring( base );
        }
    }

    public static String removeLeadingZeros( String string )
    {
        return string.replaceFirst( "^0+(?!$)", "" );
    }

    /**
     * Read a file off the classpath with reference to the class parameter.
     * 
     * @param c
     *            - filename will be relative to this file
     * @param filename
     * @return - the string'alised file
     */
    public static String readFileToString( Class<?> c, String filename )
    {
        try
        {
            URL url = c.getResource( filename );

            InputStream input = null;

            try
            {
                input = url.openStream();

                StringWriter output = new StringWriter();

                IOUtils.copy( input, output, Charset.forName( "UTF-8" ) );

                return output.toString();
            }
            finally
            {
                IOUtils.closeQuietly( input );
            }
        }
        catch( IOException ex )
        {
            throw new KVException( "APPL_GENERAL", ex, ex.getMessage() );
        }
    }

    /**
     * Utility to bubble unchecked exceptions up the stack.
     * 
     * @param t
     */
    public static void rethrowUnchecked( Throwable t )
    {
        if( t instanceof RuntimeException )
        {
            throw (RuntimeException) t;
        }
    }

    /**
     * Converts a -000003423 numeric string to a -3423 int.
     * 
     * @param numeric
     *            - return 0 if null or empty.
     */
    public static Long hostNumericToLong( String numeric )
    {
        if( StringUtils.isBlank( numeric ) )
        {
            return 0l;
        }

        char sign = '+';

        char[] cs = numeric.toCharArray();

        for( int i = 0; i < cs.length; i++ )
        {
            if( cs[ i ] > '0' && cs[ i ] <= '9' )
            {
                break;
            }
            else if( cs[ i ] == '-' )
            {
                sign = cs[ i ];

                cs[ i ] = '0';

                break;
            }
        }

        Long ret = Long.parseLong( new String( cs ), 10 );

        return ret * (sign == '-' ? -1 : 1);
    }

    /**
     * Null and blank safe. 0 if null or empty or exception.
     * 
     * @param vest
     * @return Double
     */
    public static Double toDouble( String vest )
    {
        vest = StringUtils.isBlank( vest ) ? "0" : vest;

        try
        {
            return Double.valueOf( vest );
        }
        catch( NumberFormatException ex )
        {
            LogUtils.warn( logger, ex, "String->Double conversion failed", ex.getMessage(), vest );

            return 0d;
        }
    }

    /**
     * Null and blank safe. 0 if null or empty.
     * 
     * @param vest
     * @return Integer
     */
    public static Integer toInteger( String vest )
    {
        vest = StringUtils.isBlank( vest ) ? "0" : vest;

        return Integer.valueOf( vest, 10 );
    }

    /**
     * Null safe. 0 if null.
     * 
     * @param vest
     * @return Double
     */
    public static Double toDouble( Double vest )
    {
        vest = vest == null ? 0d : vest;

        return Double.valueOf( vest );
    }

    /**
     * Closes implementations of the {@link Closeable} interface silently.
     * 
     * @param closeable
     */
    public static void close( Closeable closeable )
    {
        try
        {
            if( closeable != null )
            {
                closeable.close();
            }
        }
        catch( IOException ex )
        {
            LogUtils.warn( logger, ex, ex.getMessage() );
        }
    }

    /**
     * @param filename
     * @return
     */
    public static Properties loadFromClasspath( String filename )
    {
        Properties p = new Properties();

        InputStream in = null;

        try
        {
            URL u = Thread.currentThread().getContextClassLoader().getResource( filename );

            in = u.openStream();

            p.load( in );
        }
        catch( Exception ex )
        {
            throw new KVException( "OOPS", ex, filename, "Failed to load" );
        }
        finally
        {
            close( in );
        }

        return p;
    }

    /**
     * @param victim
     * @return
     */
    public static String urlDecode( String victim )
    {
        String[] charsets = { "UTF-8", "ISO-8859-1" };

        for( String charset : charsets )
        {
            try
            {
                return URLDecoder.decode( victim, charset );
            }
            catch( UnsupportedEncodingException ex )
            {
                LogUtils.warn( logger, ex, "Failed to URL decode a string.", victim, charset );
            }
        }

        return victim;
    }

    /**
     * Make this a 1 time load
     * 
     * @param format
     * @return
     */
    public static MessageFormat getMessageFormat( String format )
    {
        LogUtils.debug( logger, format );

        String vest = Utils.readFileToString( Utils.class, format );

        MessageFormat f = new MessageFormat( vest );

        return f;
    }

    /**
     * MessageFormat pattern format
     * 
     * @param pattern
     * @param params
     * @return
     */
    public static String format( String pattern, Object... params )
    {
        LogUtils.debug( logger, pattern, params );

        MessageFormat f = getMessageFormat( pattern );

        String vest = f.format( params );

        return vest;
    }
}
