package deliverable.commit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ticket {

	private String id;
	
	private List<Commit> commitsTicket;
	
	private Date resolutionDate = null;

	public Ticket(String id) {

		this.id = id;
		this.commitsTicket = new ArrayList<>();
		
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
	
	public List<Commit> getCommitsTicket() {
		return commitsTicket;
	}

	public void setCommitsTicket(List<Commit> commitsTicket) {
		this.commitsTicket = commitsTicket;
	}
	
	//function to retrive the tickets with the latest date

	public Date findDate() {
		
		if(resolutionDate == null) {

			for(int i=0;i<commitsTicket.size();i++) {
				
				if((resolutionDate == null) || (resolutionDate.compareTo(commitsTicket.get(i).getDate()) < 0)) {
					
					resolutionDate = commitsTicket.get(i).getDate();
				}
				
			}
		}

		return resolutionDate;
		
	}
	
}
