package deliverable.metrics;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import deliverable.commit.Commit;
import util.Connection;

public final class GetMetrics {
	
	private GetMetrics() {}

	private static final Logger LOGGER = Logger.getLogger(GetMetrics.class.getName());
	static List<Commit> commits;
	
	public static List<Commit> getAuthor(String projName, String token) throws IOException, ParseException {
		
		commits = new ArrayList<>();
	   
		Integer page=0;
		JSONArray comm;
		   
		while(true) {
			   
			//Takes all commits
			     
			String url = "https://api.github.com/repos/apache/"+ projName +"/commits?&per_page=100&page="+page.toString();
			  
			try{
				
				comm = Connection.readJsonArrayFromUrl(url, token);
		    	   
		       
			  }catch(Exception e) {
				  
				  LOGGER.log(Level.SEVERE, "[ERROR]", e);
		    	  return commits;
				  
			  }
			
			int total = comm.length();
		    	
			int i = 0;
		      
			if(total == 0) {
		    	  
				break;
		      }
			
			 while(i<total) {
		        	
				   JSONObject commit = comm.getJSONObject(i).getJSONObject("commit");
				   
				   String author = commit.getJSONObject("author").get("name").toString();
				   String message = commit.get("message").toString();
				   String date = commit.getJSONObject("committer").get("date").toString();
				   String sha = comm.getJSONObject(i).get("sha").toString();
				   
				   String formattedDate = date.substring(0,9)+" "+date.substring(11,19);
				   
				   Commit c = new Commit(message, formattedDate, author, sha);
				   
				   //Adds the new commit to the list
				   
				   commits.add(c);	

				   i++;
			            
			   }
			 
			 page++;
		
		}
		
		return commits;
		
	}
	
	public static void getFile(List<Commit> commits, String projName, String token) {
		
		JSONObject conn = null;
		String sha;
		
		for(int i = 0; i<commits.size(); i++) {
			
			sha = commits.get(i).getSha();
				
			String url1 = "https://api.github.com/repos/apache/"+ projName +"/commits/"+ sha;
			   
			   try {
						
				   conn = Connection.jsonFromUrl(url1, token);
					
			   }catch(Exception e) {
						  					
				   LOGGER.log(Level.SEVERE, "[ERROR]", e);
				   break;
						  
			   }
											
			   JSONArray file = conn.getJSONArray("files");
			   
			   for(int j=0; j<file.length(); j++) {
				
				   String filename = file.getJSONObject(j).get("filename").toString();
						 
				   System.out.println(filename);
				   
			   }
		}
		
	}
	
}
