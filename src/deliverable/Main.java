package deliverable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import org.json.JSONException;

public class Main {
	
	public static Integer numVersions;
	private static Logger LOGGER;

	public static void main(String[] args) throws JSONException, IOException {
		
		LOGGER = Logger.getLogger(Main.class.getName());
	
		GetReleaseInfo.getRelease();
		
		String outname = GetReleaseInfo.projName + "VersionInfo.csv";
		 
		 try (FileWriter fileWriter = new FileWriter(outname)) {
		        
		     //Name of CSV for output
		     
		     fileWriter.append("Index,Version ID,Version Name,Date");
		     
		     fileWriter.append("\n");
		        
		     numVersions = GetReleaseInfo.releases.size();
		     
		     int i;
		        
		     for ( i = 0; i < GetReleaseInfo.releases.size(); i++) {
		        	
		        Integer index = i + 1;
		        
		        fileWriter.append(index.toString());
		        
		        fileWriter.append(",");
		        
		        fileWriter.append(GetReleaseInfo.releaseID.get(GetReleaseInfo.releases.get(i)));
		        
		        fileWriter.append(",");
		        
		        fileWriter.append(GetReleaseInfo.releaseNames.get(GetReleaseInfo.releases.get(i)));
		        
		        fileWriter.append(",");
		        
		        fileWriter.append(GetReleaseInfo.releases.get(i).toString());
		        
		        fileWriter.append("\n");
		      
		     }
		     
		     fileWriter.flush();
	         fileWriter.close();

		 } catch (Exception e) {
			 
			 LOGGER.info("Error in csv writer");
		     e.printStackTrace();
		     
		 }
	}

}
