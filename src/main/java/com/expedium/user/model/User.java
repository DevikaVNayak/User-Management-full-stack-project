/******
File Header: User.java
Description: Model class for generating getters and setters.
Author : Devika V Nayak
Created On : April 25, 2026
************
Maintenance History: Initial Version
Copyright : iTech Workshop Private Limited 2001-2026
All rights reserved.
***********************/
package com.expedium.user.model;

import java.sql.Date;
import java.time.LocalDate;

public class User
{
    private int iUserKey;
    private String sUserFirstname;
    private String sUserMiddlename;
    private String sUserLastname;
    private String sUserEmail;
    private long lUserContact;
    private String sUserGender;
    private String sUserAddress;
    private Date LDUserdob;
    private int iAdminKey;


    public User() {//It is required becuase empty objects are created first and values are setted later without this errors may come because java expects a parameter
		super();
	}

	public int getUserKey()
    {
        return iUserKey;
    }

    public void setUserKey(int iUserKey)
    {
        this.iUserKey = iUserKey;
    }


    public String getUserFirstname()
    {
        return sUserFirstname;
    }

    public void setUserFirstname(String pUserFirstname)
    {
        this.sUserFirstname = pUserFirstname;
    }


    public String getUserMiddlename()
    {
        return sUserMiddlename;
    }

    public void setUserMiddlename(String pUserMiddlename)
    {
        this.sUserMiddlename = pUserMiddlename;
    }


    public String getUserLastname()
    {
        return sUserLastname;
    }

    public void setUserLastname(String sUserLastname)
    {
        this.sUserLastname = sUserLastname;
    }


    public String getUserEmail()
    {
        return sUserEmail;
    }

    public void setUserEmail(String pUserEmail)
    {
        this.sUserEmail = pUserEmail;
    }


    public long getUserContact()
    {
        return lUserContact;
    }

    public void setUserContact(long pUserContact)
    {
        this.lUserContact = pUserContact;
    }


    public String getUserGender()
    {
        return sUserGender;
    }

    public void setUserGender(String pUserGender)
    {
        this.sUserGender = pUserGender;
    }


    public String getUserAddress()
    {
        return sUserAddress;
    }

    public void setUserAddress(String pUserAddress)
    {
        this.sUserAddress = pUserAddress;
    }


    public Date getUserdob()
    {
        return LDUserdob;
    }

    public void setUserdob(Date pDUserdob)
    {
        this.LDUserdob = pDUserdob;
    }


    public int getAdminKey()
    {
        return iAdminKey;
    }

    public void setAdminKey(int piAdminKey)
    {
        this.iAdminKey = piAdminKey;
    }


    @Override
    public String toString()
    {
        return "User [iUserKey=" + iUserKey +
               ", sUserFirstname=" + sUserFirstname +
               ", sUserMiddlename=" + sUserMiddlename +
               ", sUserLastname=" + sUserLastname +
               ", sUserEmail=" + sUserEmail +
               ", lUserContact=" + lUserContact +
               ", sUserGender=" + sUserGender +
               ", sUserAddress=" + sUserAddress +
               ", LDUserdob=" + LDUserdob +
               ", iAdminKey=" + iAdminKey + "]";
    }
}