package cn.edu.nju.software.model;

import java.util.ArrayList;
import java.util.List;


public class EmailList {
	private int pageSize;
	private List<Email> emailsList = new ArrayList<Email>();

	public void setEmailsList(List<Email> emailsList) {
		this.emailsList = emailsList;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageSize() {
		return pageSize;
	}
	public List<Email> getEmailslist() {
		return emailsList;
	}
}
