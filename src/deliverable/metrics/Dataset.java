package deliverable.metrics;

import java.util.List;

import entities.File;

public class Dataset {
	
	private Dataset() {}
	
    //Returns the number of LOC in a file
	
	public static int getLoc(File file) {
		
		String[] lines;
				
		String content = file.getContent();
		
		lines = content.split("\n");
		 
		int loc = lines.length;

		System.out.println("LOC " + loc);
		
    	return loc;
		
	}
	
	//Returns the number of comments in a file
	
	public static int countComment(File file) {
		
		 String[] lines;
		 int count = 0;
		 
		 String content = file.getContent();
		 
		 lines = content.split("\n");
		 
		 for(int i=0; i<lines.length; i++) {
			 
			 if(lines[i].startsWith("//")) {
				 
				 count ++;
				 
			 }else if(lines[i].startsWith("/*") || lines[i].startsWith("/**")) {
				 
				 do {
					 
					 count ++;
				 
				 }while (!lines[i].endsWith("*/"));
		
			 }
			 
		 }
		 
		 System.out.println("count= " + count);
	 
		 return count;
	 
	}	
	
	public static void getSize(List<File> commitFile) {
		
		for(int i=0; i<commitFile.size(); i++) {
			
			int loc = getLoc(commitFile.get(i));
			
			int comment = countComment(commitFile.get(i));
			
			int size = loc - comment;
			
			commitFile.get(i).setSize(size);			
			
			System.out.println(size);
		}
		
		
	}

}