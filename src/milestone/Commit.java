package milestone;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Commit {
	
	private String message;
	
	private Date date;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	
	public Commit(String message, String date) throws ParseException{
		
		this.message = message;
		
		this.date = formatter.parse(date);
			
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(String date) throws ParseException {
		
		this.date = formatter.parse(date);	
		
	}

}
