package milestone;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class Find {
	
	private Find() {}
	
	//List of tickets from the project
	public static List<Ticket> tickets;	
	
	//List of all the commits of the project
	public static List<Commit> commits;	
	
	//Name of the project to analyze
	private static String project ="FALCON";
	
	//AccessCredential
	private static String token;
	
	private static final Logger LOGGER= Logger.getLogger(Find.class.getName());
	
	
   private static String readAll(Reader rd) throws IOException {
	      StringBuilder sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	         sb.append((char) cp);
	      }
	      return sb.toString();
	   }


  public static JSONObject JsonFromUrl(String url) throws IOException, JSONException {
	      
	   java.io.InputStream is = new URL(url).openStream();
	      try {
	         BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	         String jsonText = readAll(rd);
	         JSONObject json = new JSONObject(jsonText);
	         return json;
	       } finally {
	         is.close();
	       }
	}
   
   public static JSONObject readJsonFromUrl(String url, String accept) throws IOException, JSONException {
	   URL url2 = new URL(url);
	   HttpURLConnection urlConnection = (HttpURLConnection)  url2.openConnection();
	   urlConnection.setRequestProperty("Accept", accept);
      try {
         BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), Charset.forName("UTF-8")));
         String jsonText = readAll(rd);
         JSONObject json = new JSONObject(jsonText);
         return json;
       } finally {
         urlConnection.disconnect();
       }
   }
   
   
   public static List<Ticket> getTickets() throws JSONException, IOException{
	 
	   //Searchs for all the tickets of type 'Tickets' which have been resolved/closed
	   
	   tickets = new ArrayList<Ticket>();
	   Integer j = 0, i = 0, total = 1;
	   
	      do {
	         //Only gets a max of 1000 at a time, so must do this multiple times if >1000
	         j = i + 1000;
	         
	         //Url for found all Tickets related to task, bug and new features
	       String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
		             + project + "%22AND(%22status%22=%22closed%22OR"
		             + "%22status%22=%22resolved%22)&fields=key,resolutiondate,versions,created&startAt="
		             + i.toString() + "&maxResults=" + j.toString();
	         
	         
	         JSONObject json = JsonFromUrl(url);
	         JSONArray issues = json.getJSONArray("issues");
	         total = json.getInt("total");
	         
	         for (; i < total && i < j; i++) {
	        	 
	        	 String key = issues.getJSONObject(i%1000).get("key").toString();
	        	 
	        	 Ticket t = new Ticket(key);
	        	
	        	 //Adds the new ticket to the list
	        	 
	        	 tickets.add(t);	
	            
	         }
	         
	      } while (i < total);
	      
	      for(i=0;i<tickets.size();i++) {
	    	  System.out.println(tickets.get(i).getId());
	    	 
	      }	      
	      
	      return tickets;
	   
   }
   
   public static JSONArray readJsonArrayFromUrl(String url, String token) throws IOException, JSONException {
       URL url2 = new URL(url);
       HttpURLConnection urlConnection = (HttpURLConnection)  url2.openConnection();

       //Setting the requirements to access the github api
       
       urlConnection.setRequestProperty("Accept", "application/vnd.github.cloak-preview");
       urlConnection.setRequestProperty("Authorization", "token "+ token);

       BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
       String jsonText = readAll(rd);
       JSONArray json = new JSONArray(jsonText);

       urlConnection.disconnect();

       return json;

   }

   public static List<Commit> getAllCommits() throws JSONException, IOException, InterruptedException, ParseException{
	   
	   //Searchs for all the commits for the specified commit id
	   
	   commits = new ArrayList<Commit>();
	   
	   Integer page=0;
	   JSONArray comm = null;
	   token = PropertiesUtils.getProperty(ReadPropertyFile.TOKEN);

	   //System.out.println(token);
	   
	  while(true) {
		   
		  //Takes all commits
		   
		  String url = "https://api.github.com/repos/apache/falcon/commits?&per_page=100&page="+page.toString();
		  
		  try{
			  
	    	   comm = readJsonArrayFromUrl(url, token);
	       }catch(Exception e) {
	    	   System.out.println(e);
	    	   break;
	       }
	       
		  
	       int total = comm.length();
	       int i;
	       
	       //System.out.println(total);
	       
	       if(total == 0) {
	    	   break;
	       }
	  
		   for (i=0; i < total; i++) {
		        	
			   JSONObject commit = comm.getJSONObject(i).getJSONObject("commit");
			   
			   //System.out.println(commit);
			   
			   String message = commit.get("message").toString();
			   String date = commit.getJSONObject("committer").get("date").toString();
		        	 
			   String formattedDate = date.substring(0,9)+" "+date.substring(11,19);
			   
			   Commit c = new Commit(message,formattedDate);
			   
			 //Adds the new commit to the list
			   
			   commits.add(c);	
		            
		   }

		   page++;	//Going to the next page
		   		 
	   } 

	   return commits;
	   
  }



public static void sortCommits(List<Ticket> tickets2, List<Commit> commits2) throws FileNotFoundException {
	   //Associating commits to tickets
	   
	   String message = null;
	   
	   for(int i=0;i<commits2.size();i++) {
		   
		   message = commits2.get(i).getMessage();
		   //System.out.println(message);
		   
		   for(int j=0;j<tickets2.size();j++) {
			   
			 //If a ticket is found in the message that commit is added to the list
			   
			   if(message.contains(tickets2.get(j).getId()+":")) {	
				   
				   tickets2.get(j).addCommit(commits2.get(i));
				   LOGGER.info(message);
				   break;
			   }
			   
		   }
		     
	   }
	      
	   return;
	
	}

}


