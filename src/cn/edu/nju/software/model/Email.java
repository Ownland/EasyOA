package cn.edu.nju.software.model;

import java.util.Date;

import cn.edu.nju.software.enums.EmailType;

public class Email {

	public final static String ID = "id";
	public final static String TITLE = "title";
	public final static String SENDER = "sender";
	public final static String RECIEVER = "reciever";
	public final static String CONTENT = "content";
	public final static String DATE = "date";
	public final static String TYPE = "type";
	private int id;
	private String title;
	private String sender;
	private String reciever;
	private String content;
	private Date date;
	private EmailType type;
	
	public Email(){
		
	}
	
	public Email(int id,String title,String sender,String reciever,String content,Date date,EmailType type){
		this.id = id;
		this.title = title;
		this.sender = sender;
		this.reciever = reciever;
		this.content = content;
		this.date = date;
		this.type = type;
	}
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getReciever() {
		return reciever;
	}
	public void setReciever(String reciever) {
		this.reciever = reciever;
	}
	public EmailType getType() {
		return type;
	}
	public void setType(EmailType type) {
		this.type = type;
	}
	
	
}
