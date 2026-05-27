/******
File Header: UserService.java
Description: Service layer for handling User actions like login, registration, and profile management.
Author : Devika V Nayak
Created On : April 25, 2026
************
Maintenance History: Initial Version
Copyright : iTech Workshop Private Limited 2001-2026
All rights reserved.
***********************/
package com.expedium.user.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import com.expedium.user.db.UserDao;
import com.expedium.user.model.Filter;
import com.expedium.user.model.User;

public class UserService
{
    UserDao objDao = new UserDao();

    public String addUser(User pUser)
    {
        String sMessage = "";

        if (pUser.getUserEmail() == null ||
            !pUser.getUserEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$"))
        {
            sMessage += "invalid_email,";
        }

        if (objDao.isEmailAlreadyExists(pUser))
        {
            sMessage += "email already exists,";
        }

        Long lContact = pUser.getUserContact();

        if (lContact == null || String.valueOf(lContact).length() != 10)
        {
            sMessage += "contact,";
        }

        if (pUser.getUserdob() != null)
        {
    
        }
       

        if (pUser.getUserGender() == null || pUser.getUserGender().isEmpty())
        {
            sMessage += "gender,";
        }

        if (!sMessage.isEmpty())
        {
            return sMessage.substring(0, sMessage.length() - 1);
        }

        boolean bResult = objDao.addUser(pUser);
        return bResult ? "success" : "error";
    }


    public List<User> getAllUser(int piAdminKey)
    {
        return objDao.getAllUser(piAdminKey);
    }


    public List<User> getUsersByAdmin(
            int piAdminKey,
            int iOffset,
            int piPageSize,
            String pSortColumn,
            String pSortOrder)
    {
        return objDao.getUsersByAdmin(
                piAdminKey,
                iOffset,
                piPageSize,
                pSortColumn,
                pSortOrder
        );
    }

    public int getUserCount(int piAdminKey)
    {
        return objDao.getUserCount(piAdminKey);
    }


    public String updateUser(User pUser)
    {
        if (pUser == null)
        {
            return "invalid user";
        }

        String sMessage = "";

        if (!pUser.getUserEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$"))
        {
            sMessage += "invalid_email,";
        }

        if (objDao.isEmailAlreadyExists(pUser))
        {
            sMessage += "email_exists,";
        }

        if (String.valueOf(pUser.getUserContact()).length() < 10)
        {
            sMessage += "contact,";
        }

        if (pUser.getUserdob() != null)
        {
            
        }

        if (!sMessage.isEmpty())
        {
            return sMessage.substring(0, sMessage.length() - 1);
        }

        boolean bResult = objDao.updateUser(pUser);
        return bResult ? "success" : "error";
    }


    public List<User> filterUsers(
            int pAdminKey,
            Filter pObjFilter,
            int pLimit,
            int pOffset,
            String pSortColumn,
            String pSortOrder
    )
    {
        return objDao.filterUsers(
                pAdminKey,
                pObjFilter,
                pLimit,
                pOffset,
                pSortColumn,
                pSortOrder
        );
    }


    public int getFilteredCount(int pAdminKey, Filter pObjFilter)
    {
        return objDao.getFilteredCount(pAdminKey, pObjFilter);
    }


    public boolean deleteUsers(String[] pUserKeys, int pAdminKey)
    {
        return objDao.deleteUsers(pUserKeys, pAdminKey);
    }
    public User showUserDetails(String sUserEmail)
    {
        return objDao.showUserDetails(sUserEmail);
    }
}