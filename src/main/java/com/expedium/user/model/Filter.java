/******
File Header: Filter.java
Description: Model class for generating getters and setters.
Author : Devika V Nayak
Created On : April 25, 2026
************
Maintenance History: Initial Version
Copyright : iTech Workshop Private Limited 2001-2026
All rights reserved.
***********************/
package com.expedium.user.model;

public class Filter
{
    private String sFirstName;
    private String sLastName;
    private String sEmail;
    private String sContact;
    private String sGender;
    private String sAddress;

    private String sFirstNameType;
    private String sLastNameType;
    private String sEmailType;
    private String sContactType;
    private String sGenderType;
    private String sAddressType;


    public Filter()
    {
        super();
    }


    public String getFirstName()
    {
        return sFirstName;
    }

    public void setFirstName(String sFirstName)
    {
        this.sFirstName = sFirstName;
    }


    public String getLastName()
    {
        return sLastName;
    }

    public void setLastName(String sLastName)
    {
        this.sLastName = sLastName;
    }


    public String getEmail()
    {
        return sEmail;
    }

    public void setEmail(String sEmail)
    {
        this.sEmail = sEmail;
    }


    public String getContact()
    {
        return sContact;
    }

    public void setContact(String sContact)
    {
        this.sContact = sContact;
    }


    public String getGender()
    {
        return sGender;
    }

    public void setGender(String sGender)
    {
        this.sGender = sGender;
    }


    public String getAddress()
    {
        return sAddress;
    }

    public void setAddress(String sAddress)
    {
        this.sAddress = sAddress;
    }


    public String getFirstNameType()
    {
        return sFirstNameType;
    }

    public void setFirstNameType(String sFirstNameType)
    {
        this.sFirstNameType = sFirstNameType;
    }


    public String getLastNameType()
    {
        return sLastNameType;
    }

    public void setLastNameType(String sLastNameType)
    {
        this.sLastNameType = sLastNameType;
    }


    public String getEmailType()
    {
        return sEmailType;
    }

    public void setEmailType(String sEmailType)
    {
        this.sEmailType = sEmailType;
    }


    public String getContactType()
    {
        return sContactType;
    }

    public void setContactType(String sContactType)
    {
        this.sContactType = sContactType;
    }


    public String getGenderType()
    {
        return sGenderType;
    }

    public void setGenderType(String sGenderType)
    {
        this.sGenderType = sGenderType;
    }


    public String getAddressType()
    {
        return sAddressType;
    }

    public void setAddressType(String sAddressType)
    {
        this.sAddressType = sAddressType;
    }
}