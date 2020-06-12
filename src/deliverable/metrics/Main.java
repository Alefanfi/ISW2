package deliverable.metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;

import util.PropertiesUtils;
import util.ReadPropertyFile;

public class Main {
	
	static Integer numVersions;
	
	private static Logger logger = Logger.getLogger(Main.class.getName());
	
	public static void main(String[] args) throws JSONException, IOException, ParseException {
		
		String token = PropertiesUtils.getProperty(ReadPropertyFile.TOKEN);
		String project = PropertiesUtils.getProperty(ReadPropertyFile.PROJECT);
		String newToken = PropertiesUtils.getProperty(ReadPropertyFile.NEWTOKEN);
		
		
		GetMetrics.getReleaseInfo(project);
		GetMetrics.getTickets(project);
		GetMetrics.getCommits(project, token);
		GetMetrics.associatingCommitToTickets(GetMetrics.tickets, GetMetrics.commits);
		GetMetrics.getFile(GetMetrics.commits, project, newToken);
		GetMetrics.checkFile(GetMetrics.commitFile, token);
		
		logger.info("Done");
		
		String outname = project + "VersionInfo.csv";
		 
		 try (FileWriter fileWriter = new FileWriter(outname)) {
		        
		     //Name of CSV for output
		     
		     fileWriter.append("Index,Version ID,Version Name,Date,Author");
		     
		     fileWriter.append("\n");
		        
		     numVersions = GetMetrics.release.size();
		     
		     int i;
		        
		     for ( i = 0; i < numVersions/2; i++) {
		        	
		        Integer index = i + 1;
		        
		        fileWriter.append(index.toString());
		        
		        fileWriter.append(",");
		        
		        fileWriter.append(GetMetrics.release.get(i).getId());
		        
		        fileWriter.append(",");
		        
		        fileWriter.append(GetMetrics.release.get(i).getVersion());
		        
		        fileWriter.append(",");
		        
		        fileWriter.append(GetMetrics.release.get(i).getReleaseDate().toString());
		        
		        //fileWriter.append(",");
		        
		        //fileWriter.append(GetMetrics.commits.get(i).getAuth());
		        
		        fileWriter.append("\n");
		      
		     }
		     
		     fileWriter.flush();
		     
		     logger.log(Level.INFO, "File {0} has been created", outname);

		 } catch (Exception e) {
			 
			 logger.log(Level.SEVERE, "Error in csv writer", e);
		     
		 }
	
	}

}
