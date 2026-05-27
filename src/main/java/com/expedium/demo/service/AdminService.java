/******
File Header: LoginService.java
Description: Service layer for handling Admin actions like login, registration, and profile management.
Author : Devika V Nayak
Created On : April 25, 2026
************
Maintenance History: Initial Version
Copyright : iTech Workshop Private Limited 2001-2026
All rights reserved.
***********************/

package com.expedium.demo.service;

import com.expedium.demo.aes.AESUtil;
import java.time.LocalDate;
import java.time.Period;

import com.expedium.demo.db.AdminDao;
import com.expedium.demo.model.Admin;

public class AdminService
{
    private static final String g_sSECRET_KEY = "mN4$eK9#sD6@wP1q";

    private AdminDao objDao = new AdminDao();


    // ================= REGISTER =================//This is for checking admin object,duplicate admin id,duplicate email id
    public String registerAdmin(Admin pAdmin)
    {
        if (pAdmin == null)
        {
            return "admin object is null";
        }

        String sMessage = "";

        if (objDao.isValueExists("admin_id", pAdmin.getAdminId(), null))
        {
            sMessage += "admin id already exists ";
        }

        if (objDao.isValueExists("admin_email", pAdmin.getAdminEmail(), null))
        {
            sMessage += "email already exists ";
        }

       

        if (pAdmin.getAdminDob() == null)
        {
            sMessage += "DOB is required ";
        }
        

        if (!sMessage.isEmpty())
        {
            return sMessage.trim();
        }

        String sEncryptedPassword = AESUtil.encrypt(pAdmin.getAdminPassword(), g_sSECRET_KEY);
        pAdmin.setAdminPassword(sEncryptedPassword);

        boolean bStatus = objDao.insertAdmin(pAdmin);
        return bStatus ? "Registered successfully" : "Registration failed";
    }

//This one is to check Admin object passed from controller, to check admin exists or not,password matching with db password
    public String loginAdmin(Admin pAdmin)
	{
	    if (pAdmin == null)
	    {
	        return "Invalid request";
	    }
	
	    Admin dbAdmin = objDao.getAdminById(pAdmin.getAdminId());
	
	    if (dbAdmin == null)
	    {
	        return "admin not found";
	    }
	
	    String sEncryptedInput = AESUtil.encrypt(pAdmin.getAdminPassword(), g_sSECRET_KEY);
	
	    if (!dbAdmin.getAdminPassword().equals(sEncryptedInput))
	    {
	        return "invalid password";
	    }
	
	    pAdmin.setAdminKey(dbAdmin.getAdminKey());
	    pAdmin.setAdminFirstName(dbAdmin.getAdminFirstName());
			
	    return "success";
	}


    // ================= UPDATE PROFILE =================This one is to check admin, duplicate email
    public String updateAdminProfile(Admin pAdmin)
    {
        if (pAdmin == null)
        {
            return "Admin object is null";
        }

        Admin existingAdmin = objDao.getAdminById(pAdmin.getAdminId());

        if (existingAdmin != null)
        {
            if (!existingAdmin.getAdminEmail().equals(pAdmin.getAdminEmail()) &&
                objDao.isValueExists("admin_email", pAdmin.getAdminEmail(), pAdmin.getAdminId()))
            {
                return "Email already exists";
            }

            
        }

        boolean bUpdated = objDao.updateProfile(pAdmin);
        return bUpdated ? "success" : "Failed to update profile";
    }


    // ================= CHANGE PASSWORD =================this one is to change password for checking that admin,current password and new password not same and returning messages for ajax
    public String changePassword(String pAdminId, String pCurrentPassword, String pNewPassword)
    {
        if (pAdminId == null || pCurrentPassword == null || pNewPassword == null ||
            pCurrentPassword.trim().isEmpty() || pNewPassword.trim().isEmpty())
        {
            return "Invalid input";
        }

        pCurrentPassword = pCurrentPassword.trim();
        pNewPassword = pNewPassword.trim();

        Admin objAdmin = objDao.getAdminById(pAdminId);

        if (objAdmin == null)
        {
            return "Admin not found";
        }

        if (pCurrentPassword.equals(pNewPassword))
        {
            return "new password cannot be same as old password";
        }

        String sEncryptedCurrent = AESUtil.encrypt(pCurrentPassword, g_sSECRET_KEY);

        if (!objAdmin.getAdminPassword().equals(sEncryptedCurrent))
        {
            return "current password is incorrect";
        }

        String sEncryptedNew = AESUtil.encrypt(pNewPassword, g_sSECRET_KEY);

        boolean bUpdated = objDao.updatePassword(pAdminId, sEncryptedNew);
        return bUpdated ? "Password updated successfully" : "failed to update password";
    }


    // ================= VERIFY SECURITY =================this one is to check question mismatch,answer mismatch and send message to ajax
    public String verifySecurity(String pAdminId, String pQuestion, String pAnswer)
    {
        if (pAdminId == null || pQuestion == null || pAnswer == null ||
            pAdminId.trim().isEmpty() || pQuestion.trim().isEmpty() || pAnswer.trim().isEmpty())
        {
            return "empty";
        }

        AdminDao objDao = new AdminDao();
        Admin objAdmin = objDao.getDetails(pAdminId.trim());

        if (objAdmin == null)
        {
            return "invalid_adminid";
        }

        if (!objAdmin.getAdminQuestion().equalsIgnoreCase(pQuestion.trim()))
        {
            return "invalid_question";
        }

        if (!objAdmin.getAdminAnswer().equalsIgnoreCase(pAnswer.trim()))
        {
            return "incorrect_answer";
        }
        boolean bIsValid = objDao.verifySecurity(pAdminId.trim(), pQuestion.trim(), pAnswer.trim());

        return bIsValid ? "success" : "fail";
    }


    // ================= RESET FORGOT PASSWORD =================this one is to check if old and new paswords same
    public String resetForgotPassword(String pAdminId, String pNewPassword)
    {
        if (pAdminId == null || pNewPassword == null ||
            pAdminId.trim().isEmpty() || pNewPassword.trim().isEmpty())
        {
            return "empty";
        }

        pAdminId = pAdminId.trim();
        pNewPassword = pNewPassword.trim();

        AdminDao objDao = new AdminDao();
        Admin objAdmin = objDao.getAdminById(pAdminId);

        if (objAdmin == null)
        {
            return "fail";
        }

        String sEncryptedNew = AESUtil.encrypt(pNewPassword, g_sSECRET_KEY);

        if (sEncryptedNew.equals(objAdmin.getAdminPassword()))
        {
            return "same_password";
        }

        boolean bIsUpdated = objDao.updatePassword(pAdminId, sEncryptedNew);

        return bIsUpdated ? "success" : "fail";
    }
}