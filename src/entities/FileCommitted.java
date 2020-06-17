package entities;

import java.time.LocalDate;

public class FileCommitted {
	
private String filename;
	
	private int lineChange;
	
	private int lineDelete;
	
	private int lineAdded;
	
	private LocalDate date;
	
	private String url;
	
	private String content;
	
	private int size;

	public FileCommitted(String filename, int lineChange, int lineDelete, int lineAdded, LocalDate date, String url, String content) {
		
		this.filename = filename;
		
		this.lineChange = lineChange;
		
		this.lineDelete = lineDelete;
		
		this.lineAdded = lineAdded;
		
		this.date = date;
		
		this.url = url;
		
		this.content = content;
		
		this.size = 0;
		
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public int getLineChange() {
		return lineChange;
	}

	public void setLineChange(int lineChange) {
		this.lineChange = lineChange;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getLineDelete() {
		return lineDelete;
	}

	public void setLineDelete(int lineDelete) {
		this.lineDelete = lineDelete;
	}

	public int getLineAdded() {
		return lineAdded;
	}

	public void setLineAdded(int lineAdded) {
		this.lineAdded = lineAdded;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}


}
