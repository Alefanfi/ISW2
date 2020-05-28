package deliverable;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;

public class Main {
	
	public static Integer numVersions;

	public static void main(String[] args) throws JSONException, IOException {
	
		GetReleaseInfo.getRelease();
		 
		 try {
		     
			 String outname = GetReleaseInfo.projName + "VersionInfo.csv";
		        
		     //Name of CSV for output
		     
		     FileWriter fileWriter = new FileWriter(outname);
		     
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
			 
			 System.out.println("Error in csv writer");
		     e.printStackTrace();
		     
		 }
	}

}
