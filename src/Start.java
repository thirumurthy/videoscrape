import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import com.google.gson.JsonObject;

import Common.DocumentReader;
import Common.FileOperations;
import Common.General;
import Common.JSON;
import Common.RestClient;

  
public class Start {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void main(String[] args) throws ClientProtocolException, IOException {
		//final Logger logger = Logger.getLogger(Start.class);
		DocumentReader.readSettings();
		switch(DocumentReader.AppData.get("type").toString())
		{
		case "1":
			grabtamil.grab1080pVideos();
			break;
		
		}
	}
}
