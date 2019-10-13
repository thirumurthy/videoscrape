import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Common.FileOperations;
import Common.General;
import Common.JSON;


public class grabtamil {
	
	public static void grab1080pVideos() {
		Document doc1 =null;
		try {
			 
			System.setProperty("file.encoding","UTF-8");
			Field charset = Charset.class.getDeclaredField("defaultCharset");
			charset.setAccessible(true);
			charset.set(null,null);
			
			String domain = "http://1080pvideos.net/1080pHDVids/";
			String mob = "http://1080pvideos.net/1080pHDVids/index.php?dir=A%20to%20Z&p=1&page=##PAGE##&sort=1";
	         
	         for(int i=0;i<80;i++) {
	        	 // loopover page
	        	 FileOperations.WriteData("data.txt", "page :"+i);
	        	 doc1 = Jsoup.connect(mob.replace("##PAGE##", String.valueOf(i)))
		                    .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1")
		                    .timeout(10 * 1000).get();
	        	for( org.jsoup.nodes.Element slgele: doc1.select("div.bg").select("a")) {
	        		String movie = slgele.text();
	        		System.out.println(slgele.text());
	        		
	        		Document doc2 = Jsoup.connect(getFullUrl(slgele.attr("href")))
                    .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1")
                    .timeout(10 * 1000).get();
	        		int j=1;
	        		for( org.jsoup.nodes.Element slgsng: doc2.select("div.bg").select("a")) {
	        			j++;
	        			Document doc3 = Jsoup.connect(getFullUrl(slgsng.attr("href")))
	                            .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1")
	                            .timeout(10 * 1000).get();
	        			String lnk="";
	        			for(org.jsoup.nodes.Element slglnk : doc3.select("div.bg").select("a")) {
	        				if(slglnk.attr("href").contains("click_id=")) {
	        					continue;
	        				}
	        				else {
	        					lnk = slglnk.attr("href");
	        					break;
	        				}
	        			}
	        			Document doc4 = Jsoup.connect(lnk)
	                            .userAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1")
	                            .timeout(10 * 1000).get();
	        			Elements marea = doc4.select("div.bg");
	        			String content = marea.text().split("Screenshot")[0];
	        			String thumbnail = getFullUrl( marea.select("img").attr("src"));
	        			System.out.println(content);
	        			String sngurl = "";
	        			for(org.jsoup.nodes.Element slglnk : doc4.select("div.bg").select("a")) {
	        				if(slglnk.attr("href").contains("click_id=")) {
	        					continue;
	        				}else {
	        					sngurl = slglnk.attr("href");
	        					break;
	        				}
	        			}
	        			Mysql.savedataToDB("http://1080pvideos.net/",i, j,movie,content, sngurl, thumbnail);
			             
	        			
	        			
	        			
	        		}
	        		
	        		
	        	}
	         }
			
			/*File in = new File("vidukathai_dheivegam.html");
			doc1 = Jsoup.parse(in, null);
			Elements elelst = doc1.select("p");
			for(org.jsoup.nodes.Element slg : elelst) {
				String text =slg.outerHtml();
			    String[] textSplitResult = text.split("<br>");
			    if (null != textSplitResult) {
			         for (String t : textSplitResult) {
			        	 String question = t.split("<strong")[0].replace("<p>", "").replace("</p>", "");
			        	 if(question==null || question.trim().length()<=0 || question.equals("<p></p>")) {
			        		 System.err.println("Empty");
			        	 }
			        	 else {
			        		 Matcher matcher = pattern.matcher(t);
			        		 String answer = "";
			        		 while (matcher.find()) {
			        			 answer = matcher.group(1);
			        		 }
			        		 Mysql.savedataToDB("https://dheivegam.com/vidukathai-tamil/", question, answer);
				             System.out.println(question + " ans:"+answer);
			        	 }
			        	 
			         }
			    }
			}*/
			
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	 
	private static String getFullUrl(String url) {
		return url.startsWith("http")?url : "http://1080pvideos.net/1080pHDVids/"+url;
	}
	
	
	public static void grab(String url) {
		// TODO Auto-generated method stub
	
		Document doc1 =null;
		Document doc2 =null;
		JsonObject jobj=new JsonObject();
		long line=1;
		String ourl="",topicshtml="";
		 Connection.Response response = null;
		try{
			
			System.setProperty("file.encoding","UTF-8");
			Field charset = Charset.class.getDeclaredField("defaultCharset");
			charset.setAccessible(true);
			charset.set(null,null);
			
			
			BufferedWriter out1 = new BufferedWriter
				    (new OutputStreamWriter(new FileOutputStream("data1.txt"),"UTF-8"));
			
			 
			line=Long.parseLong(FileOperations.readFile("data.txt"));
			System.out.println("Job started at: "+ line+ " line");
			for(long j=line;j<407000;j++)
			{
				ourl=url.replace("{{LETTER}}", String.valueOf(line));
			    
				try
		    	{
					  response = Jsoup.connect(ourl)
					            .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
					            .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
								.header("Accept-Encoding","gzip, deflate, sdch")
								.header("Accept-Language","en-US,en;q=0.8")
								.header("Connection","keep-alive")
								.ignoreHttpErrors(true)
					            .timeout(10000)
					            .execute();
					  
					  if(response.statusCode()!=200)
					  {
						  line++;
						 // System.out.println("Error Code:"+response.statusCode()+" . so skipping url:"+ourl );
						  continue;
					  }
					  
					String test =response.body();
					doc1=response.parse();
					
				  //topicshtml=RestClient.Get(ourl);
		    	}
		    	catch(Exception exp)
		    	{
		    		//logger.error(exp);
		    	}
				if(General.isNullOrEmpty(doc1))
					{
					System.out.print("Check internet connection");
					System.exit(0);
					}
				
				//Finding and setting rsize
				//doc1 =Jsoup.parse(topicshtml, "", Parser.xmlParser());
				if(doc1.select("div.content_left").isEmpty()||doc1.select("div.content_left").toString().equals("Page Not Found"))
				{
					System.out.print("you reached Last page: "+ line);
				}
				else
				{
					doc1.getElementsByClass("quick_links").remove();
					doc1.getElementsByClass("next_previous-kavitahai").remove();
					String title=  doc1.select("h1.post_title").text();
					String desc = doc1.select("p.post_desc").text();
					
					
					
					Elements li = doc1.select("div.eluthu_sidebox_tags").select("li");
					String tags="";
					
				/*	wrong tag updated
				 * for (int i = 0; i < li.size(); i++) {
						tags+=li.get(i).select("span").get(0). text()+",";
						     
					}PrintWriter stdout =new PrintWriter(
						    new OutputStreamWriter(System.out, StandardCharsets.UTF_8),true);
					 
					stdout.println("line : "+line+" and title :"+title+" \n desc: "+desc+"\n"+"tags:"+tags);*/
					JsonObject jobj1=new JsonObject();
					jobj1.addProperty("title", title);
					jobj1.addProperty("desc", desc);
					jobj1.addProperty("tags", tags);
					out1.append(jobj1.toString()) ;
					 
					Mysql.savedataToDB("", ourl, title, desc, tags);
				}
				line++;
				FileOperations.WriteData("data.txt", String.valueOf(line));
			}
			
			out1.flush();
			out1.close();
		}
		catch(Exception exp)
		{
			
		}
		
	}

	public static void updateTags(int tag) {
		// TODO Auto-generated method stub
		
		Document doc1 =null;
		int loop=1;
		try
		{
			if(General.isNullOrEmpty(FileOperations.readFile("processed.txt")))
			{
				loop=1;
			}
			else
			{
				loop=Integer.parseInt( FileOperations.readFile("processed.txt"));
			}
			String ourl="";
			 Connection.Response response = null;
			
				boolean isexit=false;
				do 
				{
					System.out.println("processing loop:"+loop+" and tag "+tag);
					ourl=getGrabUrl(tag,loop);
					 response = Jsoup.connect(ourl)
					            .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
					            .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
								.header("Accept-Encoding","gzip, deflate, sdch")
								.header("Accept-Language","en-US,en;q=0.8")
								.header("Connection","keep-alive")
								.ignoreHttpErrors(true)
					            .timeout(10000)
					            .execute();
					  
					  if(response.statusCode()!=200)
					  {
						  loop++;
						 System.out.println("Error Code:"+response.statusCode()+" . so skipping url:"+ourl );
						  continue;
					  }
					  
					  doc1=response.parse();
					  if(doc1.html().contains("Page Not Found"))
						  break; // reaches last
					  Elements alink=doc1.select("div.new_kavithai_title").select("a");
					  String tagsurls="";
					  for(org.jsoup.nodes.Element ele:alink)
					  {
						  System.out.println(ele.attr("href"));
						  tagsurls+="\""+ ele.attr("href")+"\",";
					  }
					
					  JsonArray missobj= Mysql.updateCurrentTag(tag,tagsurls);
					  Updatemissobj(missobj,tag);
					 
						FileOperations.WriteData("processed.txt", String.valueOf(loop));
					loop ++;
				}
				while(isexit==false);
				isexit=false;
				 
			
			
			
		}
		catch(Exception exp)
		{
			
		}
		 
	}
	
	
	private static void Updatemissobj(JsonArray missobj, int i) {
		
		Connection.Response response =null;
		Document doc1=null;
		try
		{
			String test="";
			for(JsonElement jele:missobj)
			{
				String url=jele.getAsJsonObject().get("url").getAsString().replaceAll("\"", "");
				 response = Jsoup.connect(url)
				            .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
				            .header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
							.header("Accept-Encoding","gzip, deflate, sdch")
							.header("Accept-Language","en-US,en;q=0.8")
							.header("Connection","keep-alive")
							.ignoreHttpErrors(true)
				            .timeout(10000)
				            .execute();
				doc1=response.parse();
				doc1.getElementsByClass("quick_links").remove();
				doc1.getElementsByClass("next_previous-kavitahai").remove();
				String title=  doc1.select("h1.post_title").text();
				String desc = doc1.select("p.post_desc").text();
				Mysql.savedataToDB("",url,  title,  desc,String.valueOf(i));
			}
			
		}
		catch (Exception e) {
			System.out.println(e.toString());
			
		}
		
	}
 

	private static JsonObject AppendCurrentLoop(JsonObject procesdata, int i, int loop) {
		try
		{
			 
			switch(i)
			{
			case 1:
				procesdata.addProperty("kathal", loop);
				 
				break;
			case 2:
				procesdata.addProperty("valkai", loop);
				 
				break;
			case 3:
				procesdata.addProperty("iyarkai", loop);
				 
				break;
			case 4:
				procesdata.addProperty("natpu", loop); 
				break;
			case 5:
				procesdata.addProperty("pothu", loop); 
				break;
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return procesdata;
	}

	private static int getNextLoopValue(int i, JsonObject procesdata) {
		int loopval=1;
		try
		{
			 
			switch(i)
			{
			case 1:
				loopval=Integer.parseInt( procesdata.get("kathal").getAsString());
				break;
			case 2:
				loopval=Integer.parseInt( procesdata.get("valkai").getAsString());
				break;
			case 3:
				loopval=Integer.parseInt( procesdata.get("iyarkai").getAsString());
				break;
			case 4:
				loopval=Integer.parseInt( procesdata.get("natpu").getAsString());
				break;
			case 5:
				loopval=Integer.parseInt( procesdata.get("pothu").getAsString());
				break;
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return loopval; 
	}

	private static String getGrabUrl(int i,int loop)
	{
		String ourl="";
		try
		{
			
			switch(i)
			{
			case 1:
				ourl="http://eluthu.com/kavithai-list/tag/2/%E0%AE%95%E0%AE%BE%E0%AE%A4%E0%AE%B2%E0%AF%8D/"+String.valueOf(loop);
				break;
			case 2:
				ourl="http://eluthu.com/kavithai-list/tag/10/%E0%AE%B5%E0%AE%BE%E0%AE%B4%E0%AF%8D%E0%AE%95%E0%AF%8D%E0%AE%95%E0%AF%88/"+String.valueOf(loop);
				break;
			case 3:
				ourl="http://eluthu.com/kavithai-list/tag/9/%E0%AE%87%E0%AE%AF%E0%AE%B1%E0%AF%8D%E0%AE%95%E0%AF%88/"+String.valueOf(loop);
				break;
			case 4:
				ourl="http://eluthu.com/kavithai-list/tag/1/%E0%AE%A8%E0%AE%9F%E0%AF%8D%E0%AE%AA%E0%AF%81/"+String.valueOf(loop);
				break;
			case 5:
				ourl="http://eluthu.com/kavithai-list/tag/23/%E0%AE%AA%E0%AF%8A%E0%AE%A4%E0%AF%81/"+String.valueOf(loop);
				break;
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return ourl;
	}


	
}
