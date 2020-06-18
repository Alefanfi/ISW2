package entities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Result {
	
	private int release;
	
	private String file;
	
	private int size = 0;
	
	private int locTouched = 0;
	
	private int locAdded = 0;
	
	private int maxLocAdded = 0;
	
	private List<String> auth = new ArrayList<>();

	private int nFix = 0;
	
	private int nR = 0;
	
	private int chgSetSize = 0;
	
	DecimalFormat df = new DecimalFormat("#.####");
	
	private String buggy = "No";

	public Result(int release, String file) {
		
		this.release = release;
		this.file = file;
	}
		
	public void setRelease(int release) {
	
		this.release = release;
		
	}
	
	public int getRelease() {
		return release;
	}

	public String getFile() {
		return file;
	}

	public int getSize() {
		return size;
	}

	public int getLocTouched() {
		return locTouched;
	}

	public int getLocAdded() {
		return locAdded;
	}

	public int getMaxLocAdded() {
		return maxLocAdded;
	}

	public float getAvgLocAdded() {

		return this.locAdded/this.nR;
	}
	

	public int getnFix() {
		return nFix;
	}

	public int getnR() {
		return nR;
	}

	public int getChgSetSize() {
		return (chgSetSize - 1);
	}

	public String getBuggy() {
		return buggy;
	}

	public void setFile(String file) {
	
		this.file = file;
		
	}
	
	public void setSize(int size) {
	
		this.size = size;
		
	}

	public void setLocTouched(int locTouched) {
	
		this.locTouched = locTouched;
		
	}

	public void setLocAdded(int locAdded) {
	
		this.locAdded = locAdded;
		
	}

	public void setMaxLocAdded(int maxLocAdded) {
	
		this.maxLocAdded = maxLocAdded;
		
	}

	public void setnFix(int nFix) {
	
		this.nFix = nFix;
		
	}

	public void setnR(int nR) {
	
		this.nR = nR;
		
	}
	
	public void setChgSetSize(int chgSetSize) {
	
		this.chgSetSize = chgSetSize;
		
	}
	
	public void setBuggy(String buggy) {
	
		this.buggy = buggy;
		
	}
	
	//Adds loc to the number of loc touched
		
	public void addLocTouched(int loc) {
	
		this.locTouched += loc;
		
	}

	//Adds loc to the number of loc added
	
	public void addLocAdded(int loc) {
	
		this.locAdded = this.locAdded + loc;
		
		//Updates the value of the max loc added
		
		if(this.maxLocAdded < loc) {
		
			this.maxLocAdded = loc;
			
		}
		
	}
	
	//Adds to the change set size the new number of files committed together
		
	public void addChgSetSize(int num) {
	
		this.chgSetSize = this.chgSetSize + num -1;
		
		if(this.chgSetSize<0) {
			
			this.chgSetSize = 0;
		}
		
	}
	
	//Adds a revision
		
	public void addRevision() {
	
		this.nR++;
		
	}
	
	//Adds an author to the list
		
	public void addAuth(String author) {

		if(!this.auth.contains(author)){
		
			this.auth.add(author);
			
		}
			
	}
	
	public int getAuth() {
		
		return this.auth.size();
	}

	public void setAuth(List<String> auth) {
		this.auth = auth;
	}
	
	//Adds a fix
		
	public void addFix() {
	
		this.nFix++;
		
	}
	
}
