package deliverable;

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
		
		GetReleaseInfo.getRelease(project);
		GetMetrics.getAuthor(project, token);
		
		String outname = project + "VersionInfo.csv";
		 
		 try (FileWriter fileWriter = new FileWriter(outname)) {
		        
		     //Name of CSV for output
		     
		     fileWriter.append("Index,Version ID,Version Name,Date,Author");
		     
		     fileWriter.append("\n");
		        
		     numVersions = GetReleaseInfo.releases.size();
		     
		     int i;
		        
		     for ( i = 0; i < numVersions; i++) {
		        	
		        Integer index = i + 1;
		        
		        fileWriter.append(index.toString());
		        
		        fileWriter.append(",");
		        
		        fileWriter.append(GetReleaseInfo.releaseID.get(GetReleaseInfo.releases.get(i)));
		        
		        fileWriter.append(",");
		        
		        fileWriter.append(GetReleaseInfo.releaseNames.get(GetReleaseInfo.releases.get(i)));
		        
		        fileWriter.append(",");
		        
		        fileWriter.append(GetReleaseInfo.releases.get(i).toString());
		        
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
