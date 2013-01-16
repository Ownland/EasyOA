package cn.edu.nju.software.model;

import java.io.Serializable;
import java.util.Date;

public class Document implements Comparable<Document>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5924932800367599562L;
	public final static String DOCUMENT_ID = "documentId";
	public final static String TITLE = "title";
	public final static String PATH = "path";
	public final static String UPLOADER_ID = "uploaderId";
	public final static String UPLOADER_NAME = "uploaderName";
	public final static String UPLOAD_DATE = "uploadDate";
	public final static String PARENT_ID = "parentId";
	public final static String TYPE = "type";
	public final static String RESOURCE = "resource"; 
	
	private int docId;
	private String title;
	private String path;
	private int uploaderId;
	private String uploaderName;
	private Date uploadDate;
	private int parentId;
	private int type;
	private int resource; // 0:нд╪Ч 1:д©б╪
	
	public Document(){
		
	}
	
	public Document(int docId,String title, String path, int uploaderId, Date uploadDate,
			int parentId, int type, int resource) {
		super();
		this.docId = docId;
		this.title = title;
		this.path = path;
		this.uploaderId = uploaderId;
		this.uploadDate = uploadDate;
		this.parentId = parentId;
		this.type = type;
		this.resource =resource;
	}

	
	
	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getResource() {
		return resource;
	}

	public void setResource(int resource) {
		this.resource = resource;
	}

	public String getUploaderName() {
		return uploaderName;
	}

	public void setUploaderName(String uploaderName) {
		this.uploaderName = uploaderName;
	}

	@Override
	public int compareTo(Document another) {
		// TODO Auto-generated method stub
		return this.title.compareToIgnoreCase(another.title);
	}
	
	
}
