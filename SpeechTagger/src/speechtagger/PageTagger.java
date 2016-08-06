package speechtagger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.apache.commons.validator.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;



/**
 * @class pageTagger that mainatin the functionality of speechtagger
 *
 */
public class PageTagger {

	// initialize instance variables
	public static File outputfile;

	public static FileWriter filewriter;

	public static BufferedWriter bufferedwriter;

/**
 * 
 * @param takes a String as input
 * @return the tagged text as another String
 */
	public String tagText(String input) {
		// pre condition
		assert input!=null;

		// initialize the instance of MaxentTagger
		MaxentTagger maxenttagger = new MaxentTagger("src/tagger/english-left3words-distsim.tagger");

		// tagged String on given input
		String taggedoutput = maxenttagger.tagString(input);

		// post condition
		assert taggedoutput != null;

		// return output
		return taggedoutput;
	}

/**
 * Jsoup: Java HTML Parser to parse HTML web pages
 * 
 * @param take URL as an input
 * @return a String containing all the text in the body of web page excluding html and java scripts tags.
 * @throws IOException
 */

	public String getText(URL inputurl) throws IOException {

		// pre condition validate the url
		UrlValidator validator  = new UrlValidator();
		assert validator.isValid(inputurl.toString());
		
		HttpURLConnection urlConn = (HttpURLConnection) inputurl.openConnection();
		
		
		String line = null;
		StringBuilder tmp = new StringBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		while ((line = in.readLine()) != null) {
		  tmp.append(" "+line);
		}
		 		
		
		Document document = Jsoup.parse(tmp.toString().trim());
		

		// connect the specific url using Jsoup connect method
	//	Document document = Jsoup.connect(inputurl.toString()).get();

		// thats not a problem but sometimes just I put space explicitly just
		// before the anchor tag.
		document.select("a").before("&nbsp");

		// make a String and generate the output String contains text from html
		// web page body tags
		String output = document.body().text();
		
		// post-condition
		assert output != null;

		// return output String
		return output;
	}



	/**
	 * Main method execute getText method and pass the output of getText() as an input to tagText()
	 * store the output off tagText() in the output file.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {

		// generate output file
		outputfile = new File("src/output/taggedoutputlink4.txt");

		// check file is exists or not
		if (!outputfile.exists()) {

			outputfile.createNewFile();
		}

		filewriter = new FileWriter(outputfile);
		bufferedwriter = new BufferedWriter(filewriter);

		// put the page url for one want to extract a text.
		URL url = new URL("http://www.windingroad.com/articles/reviews/quick-drive-2012-bmw-z4-sdrive28i/");

		// create an object of PageTagger class
		PageTagger pagetagger = new PageTagger();

		// get the text as a String input from getText() method
		String input = pagetagger.getText(url);
		
		
		// create temporary file
		File temp = new File("src/speechtagger/temp.txt");

		// check file is exists or not
		if (!temp.exists()) {

			temp.createNewFile();
		}

		FileWriter fw = new FileWriter(temp);
		BufferedWriter bw = new BufferedWriter(fw);

		// write output of getText into temporary file
		bw.write(input);

		// close stream
		bw.close();


		// read from temporary file and make chunks for 4000 words
		Scanner scan = new Scanner(temp);

		String makeinput = "";

		long count = 0;

		// read single word from file and make String 
		while(scan.hasNext())
		{
			makeinput += scan.next() + " ";

			count++;

			// if count is greater than 4000 then pass String with the 4000 words to input for tagText method
			if(count > 4000)
			{				

				// output tagged String from tagText method
				String outputtaggedtext = pagetagger.tagText(makeinput.trim());

				// write that part of chunk into output file
				bufferedwriter.write(outputtaggedtext);

				// make it empty and start count from 0 for next chunk
				makeinput = "";
				count = 0;
			}


		}

		// output tagged String from tagText method 
		// this is for what if web page has below 4000 words or chunk with the less than 4000 words
		String outputtaggedtext = pagetagger.tagText(makeinput.trim());

		// write that part of chunk into output file
		bufferedwriter.write(outputtaggedtext);

		// close the Stream
		bufferedwriter.close();

		// close the scanner
		scan.close();

		// delete temporary file 
		// because its not required now
		temp.delete();

		System.out.println("successfully generated output file");

	}

}
