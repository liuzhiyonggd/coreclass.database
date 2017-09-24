package sysu.coreclass.database.bean;

import java.util.Date;
import java.util.List;

public class Revision {

	private String author;
	private Date date;
	private String comment;
	private List<String> files;
	private int id;
	private String project;

	
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

}
