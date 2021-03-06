package deliverable.commit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entities.Commit;
import entities.Ticket;
import util.Connection;
import util.PropertiesUtils;
import util.ReadPropertyFile;

public final class Find {
	
	private Find() {}
	
	//List of tickets from the project
	static List<Ticket> tickets;	
	
	//List of all the commits of the project
	static List<Commit> commits;	
	
	private static final Logger LOGGER = Logger.getLogger(Find.class.getName());
     
	public static List<Ticket> getTickets() throws JSONException, IOException{
	 
	   //Searchs for all the tickets of type 'Tickets' which have been resolved/closed
	      
	   tickets = new ArrayList<>();
	   Integer j = 0;
	   Integer i = 0;
	   int total = 1;
	   String project = PropertiesUtils.getProperty(ReadPropertyFile.PROJECT);
	   
	   LOGGER.info("Searching tickets...");
	   
	      do {
	         //Only gets a max of 1000 at a time, so must do this multiple times if >1000
	        
	    	  j = i + 1000;
	         
	         //Url for found all Tickets related to task, bug and new features
	       
	         String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
		             + project + "%22AND(%22status%22=%22closed%22OR"
		             + "%22status%22=%22resolved%22)&fields=key,resolutiondate,versions,created&startAt="
		             + i.toString() + "&maxResults=" + j.toString();
	         
	         
	         JSONObject json = Connection.readJsonFromUrl(url);
	         JSONArray issues = json.getJSONArray("issues");
	         total = json.getInt("total");
	         
	         //Add the tickets in the list of tickets from the project
	         
	         for (; i < total && i < j; i++) {
	        	 
	        	 String key = issues.getJSONObject(i%1000).get("key").toString();
	        	 
	        	 List<String> version = null;
	        	 
	        	 List<String> fixVersion = null;
	        	 
	        	 LocalDate createdDate = null;
	        	 
	        	 Ticket t = new Ticket(key, version, fixVersion, createdDate);
	        	 
	        	 tickets.add(t);	
	            
	         }
	         
	      } while (i < total);
	      
	      LOGGER.info(tickets.size() + " tickets found!");
	      
	      return tickets;
	   
	}

	public static List<Commit> getAllCommits() throws JSONException, IOException, InterruptedException, ParseException{
	   
	  //Searchs for all the commits for the specified commit id
	   
	  commits = new ArrayList<>();
	   
	  Integer page=0;
	  JSONArray comm;
	  String token = PropertiesUtils.getProperty(ReadPropertyFile.TOKEN);
	  String project = PropertiesUtils.getProperty(ReadPropertyFile.PROJECT);
	  
	  LOGGER.info("Searching all commits...");
	   
	  while(true) {
		   
		  //Takes all commits
		   
		  String url = "https://api.github.com/repos/apache/"+ project +"/commits?&per_page=100&page="+page.toString();
		  
		  try{
			  
	    	   comm = Connection.readJsonArrayFromUrl(url, token);
	    	   
	       
		  }catch(Exception e) {
			  
	    	   LOGGER.log(Level.SEVERE, "[ERROR]", e);
	    	   return commits;
		  }
	       
	      int total = comm.length();
	      int i;
	      
	      if(total == 0) {
	    	   break;
	      }
	  
	      i = 0;
	       
	      while(i<total) {
		        	
			   JSONObject commit = comm.getJSONObject(i).getJSONObject("commit");
			   
			   //Retrieve info from commits
			   
			   String message = commit.get("message").toString();
			   String date = commit.getJSONObject("committer").get("date").toString();
			   String author = commit.getJSONObject("author").get("name").toString();
		        	 
			   String formattedDate = date.substring(0,10);
			   
			   String sha = commit.getJSONObject("tree").get("sha").toString();
			   
			   Commit c = new Commit(message,formattedDate, author, sha);
			   
			   //Adds the new commit to the list
			   
			   commits.add(c);	
			   
			   i++;
		            
		   }
	    
	      //Going to the next page	   
	      
	      page++;	
		   		 
	   } 
	  
	  LOGGER.info(commits.size()+" commits found.");

	   return commits; 
  
	}

	public static void associatingCommitToTickets(List<Ticket> tickets2, List<Commit> commits2) throws FileNotFoundException {
	   
	//Associating commits to tickets
	   
	   String message = null;
	   
	   for(int i=0;i<commits2.size();i++) {
		   
		   message = commits2.get(i).getMessage();
		   
		   for(int j=0;j<tickets2.size();j++) {
			   
			 //If a ticket is found in the message that commit is added to the list
			   
			   if(message.contains(tickets2.get(j).getId()+":")||(message.contains(tickets2.get(j).getId()+"]"))||
					   (message.contains(tickets2.get(j).getId()))){	
				   
				   tickets2.get(j).addCommit(commits2.get(i));
				   
				   break;
			   }
			   
		   }
		     
	   }
	
	}

}