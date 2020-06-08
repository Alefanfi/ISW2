package deliverable.metrics;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import deliverable.commit.Commit;
import deliverable.commit.Ticket;
import util.Connection;

public final class GetMetrics {
	
	private GetMetrics() {}

	private static final Logger LOGGER = Logger.getLogger(GetMetrics.class.getName());
	
	static List<Commit> commits;
	
	public static List<Ticket> getTickets(String projName) throws JSONException, IOException{
				  
		//Searchs for all the tickets of type 'Tickets' which have been resolved/closed
		      
		List<Ticket> tickets = new ArrayList<>();
		
		Integer j = 0;
		Integer i = 0;
		Integer total = 1;
	
		do {
		
			//Only gets a max of 1000 at a time, so must do this multiple times if >1000
	    
			j = i + 1000;
		    
			//Url for found all Tickets related to task, bug and new features
		       	    
			String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"		    
					+ projName + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
		            + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key," 
		            + "resolutiondate,versions,created&startAt=" + i.toString() + "&maxResults=" + j.toString();        
		         
			JSONObject json = Connection.readJsonFromUrl(url);
		    
			JSONArray issues = json.getJSONArray("issues");
		    
			total = json.getInt("total");
		    
			for (; i < total && i < j; i++) {
	        	
				String key = issues.getJSONObject(i%1000).get("key").toString();
				
				String strdate = issues.getJSONObject(i%1000).getJSONObject("fields").get("resolutiondate").toString();
				
				String formattedDate = strdate.substring(0,10);
						
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				
				LocalDate date = LocalDate.parse(formattedDate, formatter);
				
				LocalDate releaseDate = LocalDate.parse("2011-11-01", formatter);
				
				if(date.isBefore(releaseDate)) {
					
					Ticket t = new Ticket(key);

					//Adds the new ticket to the list
	    
					tickets.add(t);	
					
				}
		               
			}    
		    
		} while (i < total);
		     
		return tickets;
		  
	}

	
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
				   String strdate = commit.getJSONObject("committer").get("date").toString();
				   String sha = comm.getJSONObject(i).get("sha").toString();
				   
				   String formattedDate = strdate.substring(0,10);
					
				   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");	
					
				   LocalDate date = LocalDate.parse(formattedDate, formatter);					
					
				   LocalDate releaseDate = LocalDate.parse("2011-11-01", formatter);
					
					
				   if(date.isBefore(releaseDate)) {
				   
					   Commit c = new Commit(message, formattedDate, author, sha);
					   
					   //Adds the new commit to the list
					   
					   commits.add(c);
				   }

				   i++;
			            
			   }
			 
			 page++;
		
		}
		
		return commits;
		
	}
	
	public static void getFile(List<Commit> commits, String projName, String token) {
		
		JSONObject conn = null;
		String sha;
		List<File> commitFile = new ArrayList<>();
		
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
				   
				   int change = file.getJSONObject(j).getInt("changes");
				   
				   int delete = file.getJSONObject(j).getInt("deletions");
				   
				   int addLine = file.getJSONObject(j).getInt("additions");			   
				   
				   File f = new File(filename, change, delete, addLine);
				  
				   commitFile.add(f);
				   
			   }
		}
		
	}
				
	
}
	

