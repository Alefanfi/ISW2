package entities;

import java.time.LocalDate;

public class FileCommitted {
	
private String filename;
	
	private Integer lineChange;
	
	private Integer lineDelete;
	
	private Integer lineAdded;
	
	private LocalDate date;
	
	private String url;
	
	private String content;
	
	private Integer size;

	public FileCommitted(String filename, Integer lineChange, Integer lineDelete, Integer lineAdded, LocalDate date, String url, String content) {
		
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}


}
