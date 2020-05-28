package milestone;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

import org.json.JSONException;

public class Main {

	public static void main(String[] args) throws IOException, JSONException, InterruptedException, ParseException {
		
		Find.getTickets();
		
		Find.getAllCommits();
		
		Find.sortCommits(Find.tickets, Find.commits);
		
		final Logger logger= Logger.getLogger(Main.class.getName());
		
		//Generate the output.csv file 
		
		PrintStream printer = new PrintStream(new File("output.csv"));
		  
		printer.println("Ticket,Date");

	    String tkt = null;
	    Date d = null;
	    LocalDateTime ld = null;
	       
	    for(int i=0;i<Find.tickets.size();i++) {
	    	   
	    	tkt=Find.tickets.get(i).getId(); 
	    	  
	    	d = Find.tickets.get(i).findDate();
	    	   
	    	if(d == null) {
	    		   
	    		continue;
	    	  
	    	}
	    	    	   
	    	ld = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	    	   
	    	printer.println(tkt + "," + ld.getMonthValue() + "/" + ld.getYear());
	       
	    }
	       
	       
	    printer.flush();
	       
	    printer.close();
	         
	    logger.info("File output.csv has been created");       
	
	}

}