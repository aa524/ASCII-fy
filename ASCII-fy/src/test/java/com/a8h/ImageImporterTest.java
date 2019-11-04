package com.a8h;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.a8h.ImageImporter;
import com.a8h.CharacterImporter;


public class ImageImporterTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ImageImporterTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ImageImporterTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}

