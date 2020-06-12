package deliverable.commit;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.logging.Logger;

import org.json.JSONException;

public class Main {

	public static void main(String[] args) throws IOException, JSONException, InterruptedException, ParseException {
		
		Find.getTickets();
		
		Find.getAllCommits();
		
		Find.associatingCommitToTickets(Find.tickets, Find.commits);
		
		final Logger logger= Logger.getLogger(Main.class.getName());
		
		//Generate the output.csv file 
		
		PrintStream printer = new PrintStream(new File("output.csv"));
		  
		printer.println("Ticket,Date");

	    String tkt = null;
	    LocalDate d;
	    
	    //Print tickets and date in the file output.csv
	       
	    for(int i=0;i<Find.tickets.size();i++) {
	    	   
	    	tkt=Find.tickets.get(i).getId(); 
	    	
	    	//find tickets with latest date
	    	  
	    	d = Find.tickets.get(i).findDate();
	    	   
	    	if(d == null) {
	    		   
	    		continue;
	    	  
	    	}
	    	   
	    	printer.println(tkt + "," + d.getMonthValue() + "/" + d.getYear());
	       
	    }
	       	       
	    printer.flush();
	       
	    printer.close();
	         
	    logger.info("File output.csv has been created");       
	
	}

}