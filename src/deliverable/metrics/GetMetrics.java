package deliverable.metrics;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entities.Commit;
import entities.FileCommitted;
import entities.Release;
import entities.Ticket;
import util.Connection;

public final class GetMetrics {
	
	private GetMetrics() {}

	private static final Logger LOGGER = Logger.getLogger(GetMetrics.class.getName());
	
	//list of commits
	static List<Commit> commits;
	
	//list of tickets
	static List<Ticket> tickets;
	
	//list of committed file
	static List<FileCommitted> commitFile;
	
	//list of release
	static List<Release> release;
	
	static List<FileCommitted> checkedFile;
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	/* function that retrieve information about the release of the project */
		
	public static List<Release> getReleaseInfo(String projName) throws JSONException, IOException, ParseException{
		
		release = new ArrayList<>();
		
		LOGGER.info("Searching release...");
		
		//connection to jira
		
		String url = "https://issues.apache.org/jira/rest/api/2/project/" + projName;
		
		JSONObject json = Connection.readJsonFromUrl(url);
		
		JSONArray versions = json.getJSONArray("versions");
		
		//Find info about the release using the jira api
		
		for (int i = 0; i < versions.length(); i++ ) {
			
			//take information only for the release that have a releaseDate
			
			if(versions.getJSONObject(i).has("releaseDate")) {
				
				String strdate = versions.getJSONObject(i).get("releaseDate").toString();
				
				String formattedDate = strdate.substring(0,10);
				
				LocalDate date = LocalDate.parse(formattedDate, formatter);
				
				String id = versions.getJSONObject(i).get("id").toString();
				
				String versionName = versions.getJSONObject(i).get("name").toString();
							
				Release r = new Release(id, date, versionName);
				
				release.add(r);
			
			}
		         
		}
		
		// order releases by date
		
		Collections.sort(release, (Release o1, Release o2) -> o1.getReleaseDate().compareTo(o2.getReleaseDate()));
				   
		if (release.size() < 6) {
				    	
			return release;
				    	
		}
		
		LOGGER.info(release.size()/2 +" release found!");
						
		return release;
		
	}
	
	/* function that retrieve information about the ticket that have the date previous than 
	 * 
	 * the date of the release date (the first half of the project) */
	
	
	public static List<Ticket> getTickets(String projName) throws JSONException, IOException{
				  
		//Searchs for all the tickets of type 'Bug' which have been resolved/closed
		      
		tickets = new ArrayList<>();
		
		Integer j = 0;
		Integer i = 0;
		Integer total = 1;
	
		do {
		
			//Only gets a max of 1000 at a time, so must do this multiple times if >1000
	    
			j = i + 1000;
		    
			//Url for found all Tickets related to bug
		       	    
			String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"		    
					+ projName + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
		            + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key," 
		            + "resolutiondate,versions,fixVersions,created&startAt=" + i.toString() + "&maxResults=" + j.toString();        
		         
			JSONObject json = Connection.readJsonFromUrl(url);
		    
			JSONArray issues = json.getJSONArray("issues");
		    
			total = json.getInt("total");
			
			LOGGER.info("Searching tickets...");
		    
			for (; i < total && i < j; i++) {
				
				//retrieve information from tickets
				
				JSONObject ticket = issues.getJSONObject(i%1000);
	        	
				String key = issues.getJSONObject(i%1000).get("key").toString();
				
				List<String> versionList = new ArrayList<>();
				
				List<String> fixVersionList = new ArrayList<>();
				
				JSONObject field =ticket.getJSONObject("fields");
				
				String strdate = field.get("resolutiondate").toString();
				
				String formattedDate = strdate.substring(0,10);
				
				LocalDate date = LocalDate.parse(formattedDate, formatter);
				
				LocalDate releaseDate = release.get((release.size()/2)-1).getReleaseDate();
				
				//compare the ticket's date to the release's date
				
				if(date.isBefore(releaseDate)) {
					
					/*if the ticket's date is previous than the release's date (the first half of the project), 
					 * 
					 * the fuction get all information about the ticket and it is added to the list*/
						
					JSONArray ticketVersion = field.getJSONArray("versions");
	
					for(int k=0; k<ticketVersion.length(); k++) {
							
						String versionName = ticketVersion.getJSONObject(k).get("name").toString();
													
						versionList.add(versionName);
		
					}	
	
					JSONArray fixVersion = field.getJSONArray("fixVersions");
						
					for(int z=0; z<fixVersion.length(); z++) {

						String fixVersionName = fixVersion.getJSONObject(z).get("name").toString();
		
						fixVersionList.add(fixVersionName);
						
					}
										
					Ticket t = new Ticket(key, versionList, fixVersionList); 
					
					//Adds the new ticket to the list

					tickets.add(t);	
						
					}
		               
			}    
		    
		} while (i < total);
		
		LOGGER.info(tickets.size()+" tickets found!");
		     
		return tickets;
		  
	}

	/* fuction that retrieve the information about commits that have the date previous than 
	 * 
	 * the release's date (the first half of the project)*/
	
	public static List<Commit> getCommits(String projName, String token) throws IOException, ParseException {
		
		LOGGER.info("Searching commit...");
		
		commits = new ArrayList<>();
	   
		Integer page=0;
		JSONArray comm;
		   
		while(true) {
			   
			//Takes all commits
			     
			String url = "https://api.github.com/repos/apache/"+ projName +"/commits?&per_page=100&page="+page.toString();
			  
			try{
				
				comm = Connection.readJsonArrayFromUrl(url, token);
		    	   	         
			}catch(Exception e) {
				
				LOGGER.severe(e.toString());
				  
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
					
				   LocalDate date = LocalDate.parse(formattedDate, formatter);					
					
				   LocalDate releaseDate = release.get((release.size()/2)-1).getReleaseDate();
				   
				   //Take the commit from the first half of the project
										
				   if(date.isBefore(releaseDate)) {
				   
					   Commit c = new Commit(message, formattedDate, author, sha);
					   
					   //Adds the new commit to the list
					   
					   commits.add(c);

				   }

				   i++;
			            
			   }
			 
			 page++;
		
		}
		
		LOGGER.info(commits.size()+" commits found!");
		
		//order commit by descending date
		
		Collections.sort(commits, Collections.reverseOrder((Commit o1, Commit o2) -> o1.getDate().compareTo(o2.getDate())));
		
		return commits;
		
	}
	
	/* if a ticket is found in the commit, the function add this commit in the list commitsTicket */
	
	public static void associatingCommitToTickets(List<Ticket> tickets, List<Commit> commits) {
	
		String message = null;
		   
		   for(int i1=0;i1<commits.size();i1++) {
			   
			   message = commits.get(i1).getMessage();
			   
			   for(int j=0;j<tickets.size();j++) {
				   
				   //If a ticket is found in the message that commit is added to the list
				 
				   if(message.contains(tickets.get(j).getId()+":")||(message.contains(tickets.get(j).getId()+"]"))||
						   (message.contains(tickets.get(j).getId()))){	
					   
					   tickets.get(j).addCommit(commits.get(i1));
					   
					   break;
				   }
				   
			   }
	
		   }
		   
	}
	
	public static List<FileCommitted> getFile(List<Commit> commits, String projName, String token) throws UnsupportedEncodingException {
		
		JSONObject conn = null;
		String sha;
		commitFile = new ArrayList<>();
		JSONObject conn2 = null;
		
		LOGGER.info("Searching committed file...");
		
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
				   
				   //Take only java file
				   
				   if(filename.contains(".java")) {
					   
					   LOGGER.info("File: " + filename);
					   
					   int change = file.getJSONObject(j).getInt("changes");
					   
					   int delete = file.getJSONObject(j).getInt("deletions");
					   
					   int addLine = file.getJSONObject(j).getInt("additions");	
					   
					   LocalDate date = commits.get(i).getDate();
					   
					   String url = file.getJSONObject(j).get("contents_url").toString();
						
					   try {
												   
							conn2 = Connection.jsonFromUrl(url, token);
			   
						}catch(Exception e) {
							 LOGGER.log(Level.SEVERE, "[ERROR]", e);
							   break;
									  
						}
					
					   String content = conn2.get("content").toString();
					   
					   //endecode content in Base64
					   
					   byte[] contentByteArray = Base64.getMimeDecoder().decode(content);
					   
					   String contentString = new String(contentByteArray);
					   
					   //add file to commitFile

					   FileCommitted f = new FileCommitted(filename, change, delete, addLine, date, url, contentString);
					  
					   commitFile.add(f);
				   
				   }
				      
			   }
		}
		
		LOGGER.info(commitFile.size() + " committed files found!");
		
		return commitFile;
		
	}
	
}