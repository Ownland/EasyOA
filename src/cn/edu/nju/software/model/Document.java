package cn.edu.nju.software.model;

import java.util.Date;

import cn.edu.nju.software.enums.DocumentType;

public class Document {
	public final static String TITLE = "title";
	public final static String PATH = "path";
	public final static String UPLOADER_ID = "uploaderId";
	public final static String UPLOAD_DATE = "uploadDate";
	public final static String PARENT_ID = "parentId";
	public final static String TYPE = "type";
	
	private String title;
	private String path;
	private int uploaderId;
	private Date uploadDate;
	private int parentId;
	private DocumentType type;
	
	public Document(){
		
	}
	
	public Document(String title, String path, int uploaderId, Date uploadDate,
			int parentId, DocumentType type) {
		super();
		this.title = title;
		this.path = path;
		this.uploaderId = uploaderId;
		this.uploadDate = uploadDate;
		this.parentId = parentId;
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getUploaderId() {
		return uploaderId;
	}

	public void setUploaderId(int uploaderId) {
		this.uploaderId = uploaderId;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public DocumentType getType() {
		return type;
	}

	public void setType(DocumentType type) {
		this.type = type;
	}
	
	
}
