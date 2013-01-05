package cn.edu.nju.software.model;

import java.io.Serializable;

public class Contact implements Comparable<Contact>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id; // ��ϵ��ID��������
	private String name;// ��ϵ������
	private String namePinyin;// ��ϵ������ĸ
	private String phone; // ��ϵ�˵绰
	private String mobile;// ��ϵ���ֻ�
	private String email;// ��ϵ���ʼ�
	private String department;// ��ϵ�˲���
	private String nickname;// ��ϵ���ǳ�
	private String address;// ��ϵ�˵�ַ
	private String note;// ��ϵ�˱�ע��Ϣ
	private int groupId;// ��ϵ�˷����

	
	public Contact() {
		this.id = 0;
		this.name = "";
		this.namePinyin = "";
		this.phone = "";
		this.mobile = "";
		this.email = "";
		this.department = "";
		this.nickname = "";
		this.address = "";
		this.note = "";
		this.groupId = 0;
	};

	public Contact(int id, String name, String namePinyin, String phone,
			String mobile, String email, String department, String nickname,
			String address, String note, int groupId) {
		this.id = id;
		this.name = name;
		this.namePinyin = namePinyin;
		this.phone = phone;
		this.mobile = mobile;
		this.email = email;
		this.department = department;
		this.nickname = nickname;
		this.address = address;
		this.note = note;
		this.groupId = groupId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamePinyin() {
		return namePinyin;
	}

	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	@Override
	public int compareTo(Contact another) {
		return this.namePinyin.compareToIgnoreCase(another.getNamePinyin());
	}
}
