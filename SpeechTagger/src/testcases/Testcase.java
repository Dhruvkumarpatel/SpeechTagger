package testcases;

import org.junit.Test;

import speechtagger.PageTagger;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Testcase {
	
	
	
	@Test
	public void checktagText()
	{
		
		String inputtext = "Hello";
		
		String output = new PageTagger().tagText(inputtext);
		
		String idealoutput = "Hello_UH"; // or equals with "Hello_UH "
		
		assertTrue(output.trim().equals(idealoutput));
		
		System.out.println("tagText() method checked successfully :");
	}
	
	@Test
	public void checkgetText() throws IOException
	{
		
		URL inputurl = new URL("http://www.example.com/");
				
		String output = new PageTagger().getText(inputurl);
				
		String idealoutput = "Example Domain This domain is established to be used for illustrative examples in documents. You may use this domain in examples without prior coordination or asking for permission.  More information...";
		
		assertTrue(output.equals(idealoutput));
		
		System.out.println("getText() method checked successully");
		
		
	}
	

}
