package deliverable.metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entities.FileCommitted;

public class Dataset {
	
	private Dataset() {}
	
    //Returns the number of LOC in a file
	
	public static int getLoc(FileCommitted file) {
		
		String[] lines;
				
		String content = file.getContent();
		
		lines = content.split("\n");
		 
		int loc = lines.length;

		System.out.println("LOC " + loc);
		
    	return loc;
		
	}
	
	//Returns the number of comments in a file
	
	public static int countComment(FileCommitted file) {

		 int count = 0;
		 
		 String content = file.getContent();
		 
		 String[] lines = content.split("\n");
		 
		 for(int j=0; j<=lines.length; j++) {
			 
			 if(lines[j].startsWith("//") || (lines[j].startsWith("/*") && lines[j].contains("*/"))) {
				 
				 count ++;
				 	 
			 } else if(lines[j].startsWith("/*") && !lines[j].contains("*/")) {
				 
				 int k = j;
				 
				 do {
					 
					 count ++;
					 
					 k++;
				 
				 }while (!lines[k].contains("*/")); 
		
			 }
			 
		 }
		 
		 System.out.println("count = " + count);
		 
		 System.out.println("\n");
	 
		 return count;
	 
	}	
	
	public static void getSize(List<FileCommitted> fileList) {
		
		for(int z=0; z<=fileList.size(); z++) {
			
			System.out.println("File " + z);
			
			int loc = getLoc(fileList.get(z));
			
			int comment = countComment(fileList.get(z));
			
			int size = loc - comment;
			
			fileList.get(z).setSize(size);			
			
			System.out.println(size);
		}
		
	}
	
	public static void checkFile(List<FileCommitted> commitFile) {
		
		//order File by descending date	
		
		Collections.sort(commitFile, Collections.reverseOrder((FileCommitted o1, FileCommitted o2) -> o1.getDate().compareTo(o2.getDate())));	
		
		//Take only one copy for the file with the latest date, for each release
		
		for(int k=0; k<(GetMetrics.release.size()/2); k++) {
			
			List<String> filenameChecked = new ArrayList<>();
			
			List<FileCommitted> checkedFile = new ArrayList<>();
		
			for(int i=0; i<commitFile.size(); i++) {
				
				LocalDate checkDate = commitFile.get(i).getDate();
				String filename = commitFile.get(i).getFilename();
				
				if(!filenameChecked.contains(filename) && checkDate.isBefore(GetMetrics.release.get(k+1).getReleaseDate()) 
						&& checkDate.isAfter(GetMetrics.release.get(k).getReleaseDate())){
					
					checkedFile.add(commitFile.get(i));
					
					filenameChecked.add(commitFile.get(i).getFilename());
	
				}
			
			}
			
			getSize(checkedFile);
		
		}

	}
	
	//Write dataset
	
	public void writeDataset(String project, List<Result> resultList) throws FileNotFoundException{
		
		String outname = project + "DatasetInfo.csv";
		
		Result result = null;
		
		PrintStream printer = new PrintStream(new File(outname));
		  
		printer.println("Release, File, Size, LocTouched, LocAdded, MaxLocAdded, AvgLocAdded, Nauth, Nfix, Nr, ChgSetSize");
		
		for(int i=0; i<resultList.size(); i++) {
			
			result = resultList.get(i);
			
			printer.println(result.getRelease() + "," + result.getFile() + "," + result.getSize() + "," + result.getLocTouched() 
					+ "," + result.getLocAdded()  + "," + result.getMaxLocAdded() + "," + result.getAvgLocAdded() + "," 
					+ result.getAvgLocAdded() + "," + result.getNauth() + "," + result.getNfix() + "," + result.getLocAdded());
		}
		
		printer.flush();
		
		printer.close();
	
	}
}