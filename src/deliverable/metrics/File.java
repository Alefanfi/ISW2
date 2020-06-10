package deliverable.metrics;

import java.util.Date;

public class File {
	
	private String filename;
	
	private Integer lineChange;
	
	private Integer lineDelete;
	
	private Integer lineAdded;
	
	//private String content;

	//private Integer size;
	
	private Date date;

	public File(String filename, Integer lineChange, Integer lineDelete, Integer lineAdded, /*String content, Integer size,*/ Date date) {
		
		this.filename = filename;
		
		this.lineChange = lineChange;
		
		this.lineDelete = lineDelete;
		
		this.lineAdded = lineAdded;
		
		//this.content = content;
		
		//this.size = size;
		
		this.date = date;
		
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	/*
	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String context) {
		this.content = context;
	}*/
	
	public Integer getLineChange() {
		return lineChange;
	}

	public void setLineChange(Integer lineChange) {
		this.lineChange = lineChange;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Integer getLineDelete() {
		return lineDelete;
	}

	public void setLineDelete(Integer lineDelete) {
		this.lineDelete = lineDelete;
	}

	public Integer getLineAdded() {
		return lineAdded;
	}

	public void setLineAdded(Integer lineAdded) {
		this.lineAdded = lineAdded;
	}
	
}
