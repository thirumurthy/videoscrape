import java.nio.charset.Charset;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import Common.FileOperations;
import Common.Mysqlconnection;


public class Mysql {
	//final static  Logger logger = Logger.getLogger(Mysql.class);
	
	

public static String deAccent(String str) {
    String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    return pattern.matcher(nfdNormalizedString).replaceAll("");
}
public static String removeBadChars(String s) {
	  if (s == null) return null;
	  StringBuilder sb = new StringBuilder();
	  for(int i=0;i<s.length();i++){ 
	    if (Character.isHighSurrogate(s.charAt(i))) continue;
	    sb.append(s.charAt(i));
	  }
	  return sb.toString();
	}	

public static void savedataToDB(String data,String graburl,String title,String desc,String Tags)
	{
		
		Connection conn =null;
		try {
		/*String test="à®‰à®©à¯� à®ªà®¾à®šà®®à¯� à®µà¯‡à®·à®®à¯� - à®Žà®© à®¤à¯†à®°à®¿à®¨à¯�à®¤à¯�à®®à¯�....ß’Â” à®®à®©à®®à¯� à®µà®¿à®°à¯�à®®à¯�à®ªà®¿à®¯à®¤à¯ˆ à®µà¯†à®±à¯�à®•à¯�à®• à®®à®±à¯�à®•à¯�à®•à®¿à®±à®¤à¯‡....ß˜Â¢ à®�à®©à¯‡à®¾... à®•à®¾à®¤à®²à¯� à®•à®Ÿà®²à®¿à®²à¯� à®µà¯€à®´à¯�à®¨à¯�à®¤à¯� à®µà®¿à®Ÿà¯�à®Ÿà¯‡à®©à®Ÿà®¾ - à®¨à®¾à®©à¯� à®Žà®´ à®Žà®¤à¯�à®¤à®©à®¿à®•à¯�à®•à®µà¯�à®®à®¿à®²à¯�à®²à¯ˆ .....ß˜Â’ à®�à®©à¯‡à®¾ .....â�¤ à®ªà®¾à®šà®®à¯� à®‡à®µà¯�à®µà®³à®µà¯� à®®à¯‡à®¾à®šà®®à®¾à®©à®¤à®¾à®¯à¯�.....â�¤ à®¤à®¿à®©à®®à¯� à®‰à®©à¯ˆà®¯à¯‡ à®¨à®¿à®©à¯ˆà®•à¯�à®• à®¤à¯‚à®£à¯�à®Ÿà¯�à®µà®¤à¯�à®®à¯� à®�à®©à¯‡à®¾ß˜Â� à®‰à®©à¯�à®©à¯�à®³à¯� à®µà®¨à¯�à®¤à¯‡à®±à®¿à®¯ à®µà®´à®¿à®¯à¯‡ à®¤à®¿à®°à¯�à®®à¯�à®ªà®¿ à®µà®¿à®Ÿà¯�à®Ÿà¯‡à®©à¯� ß˜Â’... à®¨à¯€ à®®à®Ÿà¯�à®Ÿà¯�à®®à¯� à®Žà®©à¯�à®©à¯�à®³à¯� à®µà®¾à®´à¯�à®¤à®¿à®Ÿ à®•à¯‡à®Ÿà¯�à®ªà®¤à¯�à®®à¯� à®�à®©à¯‡à®¾ß˜Â’ à®‡à®¤à¯ˆ à®µà®¿à®¤à®¿ à®Žà®©à¯�à®ªà®¤à®¾ à®‡à®²à¯�à®²à¯ˆ à®‰à®©à¯� à®šà®¤à®¿ à®Žà®©à¯�à®ªà®¤à®¾..... à®†à®šà¯ˆà®•à®³à¯� à®•à¯‚à®Ÿ à®ªà®¾à®µà®®à®¾à®•à®¿à®ªà¯� à®ªà¯‡à®¾à®©à®¤à¯‡ à®�à®©à¯‡ ß’Â”ß’Â”"
;
		//test=  StringEscapeUtils.escapeht (desc.replaceAll("[^\\u0000-\\u007F\\u0900-\\u097f]", ""));
		desc=deAccent(desc);
		
		test=removeBadChars(test.replaceAll("[^\\x20-\\x7e]", ""));
		*/
			conn=Mysqlconnection. getMySqlConnection("grabconn");
			 
			CallableStatement cstmt = conn.prepareCall("{ call sp_saveTamilData(?,?,?,?,?) }");
			cstmt.setString("ddata",data);
			cstmt.setString("dgraburl", graburl);
			cstmt.setString("dtitle",  title);
			cstmt.setString("ddesc", desc);
			cstmt.setString("dTags", Tags); 
			cstmt.executeQuery("SET NAMES 'UTF8'");
			cstmt.executeQuery("SET CHARACTER SET 'UTF8'");
			ResultSet rs=cstmt.executeQuery();
			while(rs.next())
			{
				if(rs.getString("ErrorCode").equals("9999"))
				{
					System.out.println("Added.");
					
				}
				else
					System.out.println("Already Added.");
			}
			
			if(rs!=null&&!rs.isClosed())
				rs.close();
			if(cstmt!=null&&!cstmt.isClosed())
				cstmt.close();
		}
		catch(Exception exp)
		{
			//logger.error(exp);
			System.out.print("Unable to save . Log"+exp.toString());
		}
		finally{
			try {
				
				
				if(conn!=null&&!conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		}

	public static JsonArray updateCurrentTag(int i, String tagsurls) {

		JsonArray jarr=new JsonArray();
		Connection conn =null;
		try {
			conn=Mysqlconnection. getMySqlConnection("grabconn");
			 
			CallableStatement cstmt = conn.prepareCall("{ call sp_updatetag(?,?) }");
			 
			cstmt.setInt("dTagId", i); 
			cstmt.setString("dtagsurls", tagsurls); 
			cstmt.executeQuery("SET NAMES 'UTF8'");
			cstmt.executeQuery("SET CHARACTER SET 'UTF8'");
			ResultSet rs=cstmt.executeQuery();
			while(rs.next())
			{
				JsonObject jobj=new JsonObject();
				jobj.addProperty("url", rs.getString("splitvalue"));
				jarr.add(jobj);
			}
			
			if(rs!=null&&!rs.isClosed())
				rs.close();
			if(cstmt!=null&&!cstmt.isClosed())
				cstmt.close();
		}
		catch(Exception exp)
		{
			//logger.error(exp);
			System.out.print("Unable to save . Log"+exp.toString());
		}
		finally{
			try {
				
				
				if(conn!=null&&!conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return jarr;
	}
	public static void savedataToDB(String url, int page, int snglist, String movie, String content, String sngurl, String thumbnail) {
		Connection conn =null;
		try {
		/*String test="à®‰à®©à¯� à®ªà®¾à®šà®®à¯� à®µà¯‡à®·à®®à¯� - à®Žà®© à®¤à¯†à®°à®¿à®¨à¯�à®¤à¯�à®®à¯�....ß’Â” à®®à®©à®®à¯� à®µà®¿à®°à¯�à®®à¯�à®ªà®¿à®¯à®¤à¯ˆ à®µà¯†à®±à¯�à®•à¯�à®• à®®à®±à¯�à®•à¯�à®•à®¿à®±à®¤à¯‡....ß˜Â¢ à®�à®©à¯‡à®¾... à®•à®¾à®¤à®²à¯� à®•à®Ÿà®²à®¿à®²à¯� à®µà¯€à®´à¯�à®¨à¯�à®¤à¯� à®µà®¿à®Ÿà¯�à®Ÿà¯‡à®©à®Ÿà®¾ - à®¨à®¾à®©à¯� à®Žà®´ à®Žà®¤à¯�à®¤à®©à®¿à®•à¯�à®•à®µà¯�à®®à®¿à®²à¯�à®²à¯ˆ .....ß˜Â’ à®�à®©à¯‡à®¾ .....â�¤ à®ªà®¾à®šà®®à¯� à®‡à®µà¯�à®µà®³à®µà¯� à®®à¯‡à®¾à®šà®®à®¾à®©à®¤à®¾à®¯à¯�.....â�¤ à®¤à®¿à®©à®®à¯� à®‰à®©à¯ˆà®¯à¯‡ à®¨à®¿à®©à¯ˆà®•à¯�à®• à®¤à¯‚à®£à¯�à®Ÿà¯�à®µà®¤à¯�à®®à¯� à®�à®©à¯‡à®¾ß˜Â� à®‰à®©à¯�à®©à¯�à®³à¯� à®µà®¨à¯�à®¤à¯‡à®±à®¿à®¯ à®µà®´à®¿à®¯à¯‡ à®¤à®¿à®°à¯�à®®à¯�à®ªà®¿ à®µà®¿à®Ÿà¯�à®Ÿà¯‡à®©à¯� ß˜Â’... à®¨à¯€ à®®à®Ÿà¯�à®Ÿà¯�à®®à¯� à®Žà®©à¯�à®©à¯�à®³à¯� à®µà®¾à®´à¯�à®¤à®¿à®Ÿ à®•à¯‡à®Ÿà¯�à®ªà®¤à¯�à®®à¯� à®�à®©à¯‡à®¾ß˜Â’ à®‡à®¤à¯ˆ à®µà®¿à®¤à®¿ à®Žà®©à¯�à®ªà®¤à®¾ à®‡à®²à¯�à®²à¯ˆ à®‰à®©à¯� à®šà®¤à®¿ à®Žà®©à¯�à®ªà®¤à®¾..... à®†à®šà¯ˆà®•à®³à¯� à®•à¯‚à®Ÿ à®ªà®¾à®µà®®à®¾à®•à®¿à®ªà¯� à®ªà¯‡à®¾à®©à®¤à¯‡ à®�à®©à¯‡ ß’Â”ß’Â”"
;
		//test=  StringEscapeUtils.escapeht (desc.replaceAll("[^\\u0000-\\u007F\\u0900-\\u097f]", ""));
		desc=deAccent(desc);
		
		test=removeBadChars(test.replaceAll("[^\\x20-\\x7e]", ""));
		*/
			conn=Mysqlconnection. getMySqlConnection("grabconn");
			 
			CallableStatement cstmt = conn.prepareCall("{ call sp_saveTamilVideos(?,?,?,?,?,?,?) }");
			cstmt.setString("dgraburl",url);
			cstmt.setInt("dpage", page);
			cstmt.setInt("dsnglist",  snglist);
			cstmt.setString("dmovie",movie);
			cstmt.setString("dcontent",content);
			cstmt.setString("dsngurl",sngurl);
			cstmt.setString("dthumbnail", thumbnail);
			cstmt.executeQuery("SET NAMES 'UTF8'");
			cstmt.executeQuery("SET CHARACTER SET 'UTF8'");
			ResultSet rs=cstmt.executeQuery();
			while(rs.next())
			{
				if(rs.getString("ErrorCode").equals("9999"))
				{
					System.out.println("Added.");
					
				}
				else
					System.out.println("Already Added.");
				FileOperations.WriteData("dbdata.txt", rs.getString("vid"));
			}
			
			if(rs!=null&&!rs.isClosed())
				rs.close();
			if(cstmt!=null&&!cstmt.isClosed())
				cstmt.close();
		}
		catch(Exception exp)
		{
			//logger.error(exp);
			System.out.print("Unable to save . Log"+exp.toString());
		}
		finally{
			try {
				
				
				if(conn!=null&&!conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
 

	
}
