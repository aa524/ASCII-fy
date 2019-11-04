package com.a8h;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.File;
import java.io.IOException;
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
	 * Test Case to create output files
	 */
	public void testApp()
	{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("image.jpg").getFile());
		try {
			ImageImporter.toGrayscale(file);
		} catch (IOException e) {
			System.out.println("Unable to find file");
		}
		file = new File(classLoader.getResource("image_gray.jpg").getFile());
		try {
			ImageImporter.toASCII(file);
		} catch (IOException e) {
			System.out.println("Unable to find file");
		}
		assertTrue(new File(classLoader.getResource("image_gray_ASCII.jpg").getFile()).isFile());
	}
}

