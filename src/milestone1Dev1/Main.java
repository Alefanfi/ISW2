package milestone1Dev1;

import java.io.File;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.json.JSONException;

public class Main {

	public static void main(String[] args) throws IOException, JSONException, InterruptedException, ParseException {
		
		Find.getTickets();
		
		Find.getAllCommits();
		
		Find.sortCommits(Find.tickets, Find.commits);
		
		//Generate the output.csv file 
		
		PrintStream printer = new PrintStream(new File("output.csv"));
		
		//FileOutputStream printer = new FileOutputStream("output.csv", true);
		
		//PrintWriter pw = new PrintWriter(printer);
		  
	       printer.println("Ticket,Year,Month");

	       String tkt = null;
	       Date d = null;
	       LocalDate ld = null;
	       
	       for(int i=0;i<Find.tickets.size();i++) {
	    	   
	    	   tkt=Find.tickets.get(i).getId(); 
	    	  
	    	   d = Find.tickets.get(i).findDate();
	    	   
	    	   if(d == null) {
	    		   continue;
	    	   }
	    	   
	    	   ld = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    	   printer.println(tkt+","+ld.getYear()+","+ld.getMonthValue());
	       }
	       
	       printer.flush();
	       printer.close();
	       
	       System.out.println("----------------------------------------");
	       System.out.println("File output.csv has been created");
	       System.out.println("----------------------------------------");
	
	}

}