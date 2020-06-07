package deliverable;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Utils.Connection;

public class GetReleaseInfo {

	private GetReleaseInfo() {}
	
	static Map<LocalDateTime, String> releaseNames;
	static Map<LocalDateTime, String> releaseID;
	static List<LocalDateTime> releases;

	public static List<LocalDateTime> getRelease(String projName) throws JSONException, IOException{
		
		//Fills the arraylist with releases dates and orders them
		   
		//Ignores releases with missing dates
		
		releases = new ArrayList<>();
		         
		Integer i;
		   
		String url = "https://issues.apache.org/jira/rest/api/2/project/" + projName;
		
		JSONObject json = Connection.readJsonFromUrl(url);
		
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
}