package deliverable.metrics;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
	
	static List<Ticket> tickets;
	
	static List<File> commitFile;
	
	static List<File> checkedFile;
	
	static List<Release> release;
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	
	public static List<Release> getReleaseInfo(String projName) throws JSONException, IOException, ParseException{
		
		release = new ArrayList<>();
		
		LOGGER.info("Searching for release...");
		
		String url = "https://issues.apache.org/jira/rest/api/2/project/" + projName;
		
		JSONObject json = Connection.readJsonFromUrl(url);
		
		JSONArray versions = json.getJSONArray("versions");
		
		//Find info about the release
		
		for (int i = 0; i < versions.length(); i++ ) {
			
			if(versions.getJSONObject(i).has("releaseDate")) {
				
				String strdate = versions.getJSONObject(i).get("releaseDate").toString();
				
				String formattedDate = strdate.substring(0,10);
				
				Date date = (Date) formatter.parse(formattedDate);
				
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
		
		LOGGER.info(release.size()+" release found!");
						
		return release;
		
	}
	
	
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
		            + "resolutiondate,versions,created&startAt=" + i.toString() + "&maxResults=" + j.toString();        
		         
			JSONObject json = Connection.readJsonFromUrl(url);
		    
			JSONArray issues = json.getJSONArray("issues");
		    
			total = json.getInt("total");
			
			LOGGER.info("Searching for tickets...");
		    
			for (; i < total && i < j; i++) {
				
				//retrieve information from tickets
	        	
				String key = issues.getJSONObject(i%1000).get("key").toString();
				
				String strdate = issues.getJSONObject(i%1000).getJSONObject("fields").get("resolutiondate").toString();
				
				String formattedDate = strdate.substring(0,10);
						
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				
				LocalDate date = LocalDate.parse(formattedDate, formatter);
				
				LocalDate releaseDate = LocalDate.parse("2011-11-01", formatter);
				
				//compare the ticket's date to the release's date
				
				if(date.isBefore(releaseDate)) {
					
					//if the ticket's date is previous than the release's date, the ticket is added to the list
					
					Ticket t = new Ticket(key);

					//Adds the new ticket to the list
	    
					tickets.add(t);	
					
				}
		               
			}    
		    
		} while (i < total);
		
		LOGGER.info(tickets.size()+" tickets found!");
		     
		return tickets;
		  
	}

	
	public static List<Commit> getCommits(String projName, String token) throws IOException, ParseException {
		
		LOGGER.info("Searching for commit...");
		
		commits = new ArrayList<>();
	   
		Integer page=0;
		JSONArray comm;
		   
		while(true) {
			   
			//Takes all commits
			     
			String url = "https://api.github.com/repos/apache/"+ projName +"/commits?&per_page=100&page="+page.toString();
			  
			try{
				
				comm = Connection.readJsonArrayFromUrl(url, token);
		    	   
		       
			  }catch(Exception e) {
				  
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
		
		LOGGER.info(commits.size()+" commits found!");
		
		//order commit by descending date
		
		Collections.sort(commits, Collections.reverseOrder((Commit o1, Commit o2) -> o1.getDate().compareTo(o2.getDate())));
		
		return commits;
		
	}
	
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
	
	public static List<File> getFile(List<Commit> commits, String projName, String token) {
		
		JSONObject conn = null;
		String sha;
		commitFile = new ArrayList<>();
		
		LOGGER.info("Searching for committed file...");
		
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
				   
				   Date date = commits.get(i).getDate();
				   
				   String url = file.getJSONObject(j).get("contents_url").toString();

				   File f = new File(filename, change, delete, addLine, /*content, size,*/ date, url);
				  
				   commitFile.add(f);
				   
			   }
		}
		
		LOGGER.info(commitFile.size()+" committed files found!");
		
		return commitFile;
		
	}
	
	public static List<File> checkFile(List<File> commitFile) {
		
		checkedFile = new ArrayList<>();
		
		List<String> filenameChecked = new ArrayList<>();
		
		// order File by descending date	
		
		Collections.sort(commitFile, Collections.reverseOrder((File o1, File o2) -> o1.getDate().compareTo(o2.getDate())));	
		
		//Take only one copy for the file with the latest date, so i can do size request only for this.
		
		for(int i=0; i<commitFile.size(); i++) {
			
			if(checkedFile.size() == 0) {
				
				checkedFile.add(commitFile.get(i));
				
				filenameChecked.add(commitFile.get(i).getFilename());
			
			}else {
				
				String filename = commitFile.get(i).getFilename();
						
				if(!filenameChecked.contains(filename)) {
						
					checkedFile.add(commitFile.get(i));
						
					filenameChecked.add(commitFile.get(i).getFilename());
					
				}

			}
			
		}	
		
		for(int j=0; j<checkedFile.size(); j++) {
			
			System.out.println(checkedFile.get(j).getFilename());
		}
		
		System.out.println(checkedFile.size());
		
		return checkedFile;
		
	}
	
	public static void getSize(List<File> checkedFile, String projName, String token) {
		
		JSONObject conn = null;
	
		LOGGER.info("Searching size for committed file...");
		
		for(int i = 0; i<checkedFile.size(); i++) {
				
			String url1 = checkedFile.get(i).getUrl();
			   
			   try {
						
				   conn = Connection.jsonFromUrl(url1, token);
					
			   }catch(Exception e) {
						  					
				   LOGGER.log(Level.SEVERE, "[ERROR]", e);
				   break;
						  
			   }
			   
			   String content = conn.get("content").toString();
			   int size = conn.getInt("size");
			   
			   System.out.println(content);
			   System.out.println(size);
		}
		
	}
					
}