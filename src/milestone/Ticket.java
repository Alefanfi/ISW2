package milestone;

import java.util.Date;
import java.util.ArrayList;

public class Ticket {

	private String id;
	
	private ArrayList<Commit> commitsTicket;
	
	private Date resolutionDate = null;

	public Ticket(String id) {

		this.id = id;
		this.commitsTicket = new ArrayList<Commit>();
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void addCommit(Commit commit) {
		
		commitsTicket.add(commit);
		
	}
	
	public ArrayList<Commit> getCommitsTicket() {
		return commitsTicket;
	}

	public void setCommitsTicket(ArrayList<Commit> commitsTicket) {
		this.commitsTicket = commitsTicket;
	}

	public Date findDate() {
		
		if(resolutionDate == null) {

			for(int i=0;i<commitsTicket.size();i++) {
				
				if(resolutionDate == null) {
					resolutionDate = commitsTicket.get(i).getDate();
				}
				
				else if(resolutionDate.compareTo(commitsTicket.get(i).getDate()) < 0) {
					resolutionDate = commitsTicket.get(i).getDate();
				}
				
			}
		}

		return resolutionDate;
		
	}

		
	
}
