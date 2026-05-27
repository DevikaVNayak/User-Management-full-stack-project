/******
File Header: Admin.java
Description: Model class for Generating getters and setters.
Author : Devika V Nayak
Created On : April 25, 2026
************
Maintenance History: Initial Version
Copyright : iTech Workshop Private Limited 2001-2026
All rights reserved.
***********************/

package com.expedium.demo.model;
import java.sql.Date;
import java.time.LocalDate;

public class Admin 
{
	private Integer sAdminKey;
	private String sAdminId;
	private String sAdminFirstName;
	private String sAdminMiddleName;
	private String sAdminLastName;
	private String sAdminEmail;
	private String sAdminPassword;
	private long   lAdminContact;
	private String sAdminAddress;
	private String sAdminGender;
	private Date ldAdminDob;
	private String sAdminQuestion;
	private String sAdminAnswer;
	
	public Integer getAdminKey() 
	{
		return sAdminKey;
	}
	public void setAdminKey(Integer pAdminKey) 
	{
		this.sAdminKey = pAdminKey;
	}
	public String getAdminId() 
	{
		return sAdminId;
	}
	public void setAdminId(String pAdminId) 
	{
		this.sAdminId = pAdminId;
	}
	public String getAdminFirstName() 
	{
		return sAdminFirstName;
	}
	public void setAdminFirstName(String pFirstName) 
	{
		this.sAdminFirstName = pFirstName;
	}
	public String getAdminMiddleName() 
	{
		return sAdminMiddleName;
	}
	public void setAdminMiddleName(String pAdminMiddleName) 
	{
		this.sAdminMiddleName = pAdminMiddleName;
	}
	public String getAdminLastName() 
	{
		return sAdminLastName;
	}
	public void setAdminLastName(String pAdminLastName) {
		this.sAdminLastName = pAdminLastName;
	}
	public String getAdminEmail() {
		return sAdminEmail;
	}
	public void setAdminEmail(String pAdminEmail) {
		this.sAdminEmail = pAdminEmail;
	}
	public String getAdminPassword() {
		return sAdminPassword;
	}
	public void setAdminPassword(String pAdminPassword) {
		this.sAdminPassword = pAdminPassword;
	}
	public long getAdminContact() {
		return lAdminContact;
	}
	public void setAdminContact(long pAdminContact) {
		this.lAdminContact = pAdminContact;
	}
	public String getAdminAddress() {
		return sAdminAddress;
	}
	public void setAdminAddress(String pAdminAddress) {
		this.sAdminAddress = pAdminAddress;
	}
	public String getAdminGender() {
		return sAdminGender;
	}
	public void setAdminGender(String pAdminGender) {
		this.sAdminGender = pAdminGender;
	}
	public Date getAdminDob() {
		return ldAdminDob;
	}
	public void setAdminDob(Date pAdminDob) {
		this.ldAdminDob = pAdminDob;
	}
	public String getAdminQuestion() {
		return sAdminQuestion;
	}
	public void setAdminQuestion(String pAdminQuestion) {
		this.sAdminQuestion = pAdminQuestion;
	}
	public String getAdminAnswer() {
		return sAdminAnswer;
	}
	public void setAdminAnswer(String pAdminAnswer) {
		this.sAdminAnswer = pAdminAnswer;
	}
	
	@Override
	public String toString() {
		return "Admin [s_AdminKey=" + sAdminKey + ", s_AdminId=" + sAdminId + ", s_AdminFirstName=" + sAdminFirstName
				+ ", s_AdminMiddleName=" + sAdminMiddleName + ", s_AdminLastName=" + sAdminLastName
				+ ", s_AdminEmail=" + sAdminEmail + ", s_AdminPassword=" + sAdminPassword + ", l_AdminContact="
				+ lAdminContact + ", s_AdminAddress=" + sAdminAddress + ", s_AdminGender=" + sAdminGender
				+ ", ld_AdminDob=" + ldAdminDob + ", s_AdminQuestion=" + sAdminQuestion + ", s_AdminAnswer="
				+ sAdminAnswer + "]";
	}
	
	

}
