package deliverable;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetReleaseInfo {

	private GetReleaseInfo() {}
	
	static Map<LocalDateTime, String> releaseNames;
	static Map<LocalDateTime, String> releaseID;
	static List<LocalDateTime> releases;
	static Integer numVersions;
	
	private static Logger logger = Logger.getLogger(GetReleaseInfo.class.getName());

	public static List<LocalDateTime> getRelease(String projName) throws JSONException, IOException{
		
		//Fills the arraylist with releases dates and orders them
		   
		//Ignores releases with missing dates
		
		releases = new ArrayList<>();
		         
		Integer i;
		   
		String url = "https://issues.apache.org/jira/rest/api/2/project/" + projName;
		
		JSONObject json = readJsonFromUrl(url);
		
		JSONArray versions = json.getJSONArray("versions");
		
		releaseNames = new HashMap<>();
		
		releaseID = new HashMap<> ();
		   
		for (i = 0; i < versions.length(); i++ ) {
			   
			String name = "";
			String id = "";
			if(versions.getJSONObject(i).has("releaseDate")) {
				   
				if (versions.getJSONObject(i).has("name")) {
					   
					name = versions.getJSONObject(i).get("name").toString();
				   
				 }
				   
				if (versions.getJSONObject(i).has("id")) {
					id = versions.getJSONObject(i).get("id").toString();
				}
				addRelease(versions.getJSONObject(i).get("releaseDate").toString(),name,id);
		        
			}
		    
		}
		   
		// order releases by date
		
		Comparator<LocalDateTime> timeComparator = (o1,o2) -> o1.compareTo(o2);
		
		releases.sort(timeComparator);
		   
		 if (releases.size() < 6) {
		    	
			 return releases;
		    
		 }
		 
		 String outname = projName + "VersionInfo.csv";
		 
		 try (FileWriter fileWriter = new FileWriter(outname)) {
		        
		     //Name of CSV for output
		     
		     fileWriter.append("Index,Version ID,Version Name,Date");
		     
		     fileWriter.append("\n");
		        
		     numVersions = releases.size();
		     
		     int i1;
		        
		     for ( i1 = 0; i1 < releases.size(); i1++) {
		        	
		        Integer index = i1 + 1;
		        
		        fileWriter.append(index.toString());
		        
		        fileWriter.append(",");
		        
		        fileWriter.append(GetReleaseInfo.releaseID.get(GetReleaseInfo.releases.get(i1)));
		        
		        fileWriter.append(",");
		        
		        fileWriter.append(GetReleaseInfo.releaseNames.get(GetReleaseInfo.releases.get(i1)));
		        
		        fileWriter.append(",");
		        
		        fileWriter.append(GetReleaseInfo.releases.get(i1).toString());
		        
		        fileWriter.append("\n");
		      
		     }
		     
		     fileWriter.flush();
		     
		     logger.info(String.format("File [%s] been created", outname));

		 } catch (Exception e) {
			 
			 logger.log(Level.SEVERE, "Error in csv writer", e);
		     
		 }
		 
		 return releases;
	}

	public static void addRelease(String strDate, String name, String id) {
		   
		LocalDate date = LocalDate.parse(strDate);
		LocalDateTime dateTime = date.atStartOfDay();
		   
		if (!releases.contains(dateTime)) {
		   
			releases.add(dateTime);
		
		}
		
		releaseNames.put(dateTime, name);
		releaseID.put(dateTime, id);
	   
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		   
		InputStream is = new URL(url).openStream();
		     
		try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
			
			JSONObject json = new JSONObject(readAll(rd));
			
			is.close();
			
			return json;
	         
		} 
	   
	}
	   
	private static String readAll(Reader rd) throws IOException {
		   
		StringBuilder sb = new StringBuilder();
		int cp;
		   
		while ((cp = rd.read()) != -1) {
			
			sb.append((char) cp);
		   
		}
		  
		return sb.toString();
	      
	}

}
