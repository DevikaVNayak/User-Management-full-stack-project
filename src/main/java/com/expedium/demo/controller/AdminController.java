/******
File Header: LoginController.java
Description: Controller for handling Admin actions like login, registration, and profile management.
Author : Devika V Nayak
Created On : April 25, 2026
************
Maintenance History: Initial Version
Copyright : iTech Workshop Private Limited 2001-2026
All rights reserved.
***********************/
package com.expedium.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.expedium.demo.db.AdminDao;
import com.expedium.demo.model.Admin;
import com.expedium.demo.service.AdminService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Date;

@WebServlet("/loginController")
public class AdminController extends HttpServlet
{
    private static final Logger g_objlogger = LogManager.getLogger(AdminController.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String sAction = request.getParameter("action");

        if ("register".equals(sAction))
        {
            registerAdmin(request, response);
        }
        else if ("login".equals(sAction))
        {
            loginAdmin(request, response);
        }
        else if ("updateProfile".equals(sAction))
        {
            updateProfile(request, response);
        }
        else if ("getProfile".equals(sAction))
        {
            getProfile(request, response);
        }
        else if ("changePassword".equals(sAction))
        {
            changePassword(request, response);
        }
        else if ("logout".equals(sAction))
        {
            logoutAdmin(request, response);
        }
        else if ("verifySecurity".equals(sAction))
        {
            verifySecurity(request, response);
        }
        else if ("resetForgotPassword".equals(sAction))
        {
            resetForgotPassword(request, response);
        }
    }

    // ================= REGISTER =================this is used to register the admin
    private void registerAdmin(HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException
    {
        g_objlogger.debug("Register request received");

        pResponse.setContentType("text/plain");

        String sAdminId         = safeStringValidation(pRequest.getParameter("adminId"));
        String sAdminFirstName  = safeStringValidation(pRequest.getParameter("firstName"));
        String sAdminMiddleName = safeStringValidation(pRequest.getParameter("middleName"));
        String sAdminLastName   = safeStringValidation(pRequest.getParameter("lastName"));
        String sAdminEmail      = safeStringValidation(pRequest.getParameter("email"));
        String sAdminPassword   = safeStringValidation(pRequest.getParameter("password"));
        String sAdminGender     = safeStringValidation(pRequest.getParameter("gender"));
        String sAdminAddress    = safeStringValidation(pRequest.getParameter("address"));
        String sAdminQuestion   = safeStringValidation(pRequest.getParameter("question"));
        String sAdminAnswer     = safeStringValidation(pRequest.getParameter("answer"));
        String sContactStr 		= pRequest.getParameter("contact");

		

        Long lAdminContact = null;
        try
        {
            
            if (sContactStr != null && !sContactStr.trim().isEmpty())
            {
                lAdminContact = Long.parseLong(sContactStr.trim());
            }
        }
        catch (NumberFormatException e)
        {
            g_objlogger.warn("Invalid contact number | adminId: {}", sAdminId);
        }

        Date ldAdminDob = null;
        try
        {
            String sDobStr = pRequest.getParameter("dob");
            if (sDobStr != null && !sDobStr.trim().isEmpty())
            {
                ldAdminDob = java.sql.Date.valueOf(sDobStr.trim());
            }
        }
        catch (DateTimeParseException e)
        {
            g_objlogger.warn("Invalid DOB format | adminId: {}", sAdminId);
        }

        if (!sAdminId.matches("^[a-zA-Z0-9]{12}$"))
        {
            g_objlogger.warn("Invalid adminId format: {}", sAdminId);
            pResponse.getWriter().print("invalid admin id");
            return;
        }
        
        if (sAdminId.isEmpty() || sAdminFirstName.isEmpty() || sAdminLastName.isEmpty()
                || sAdminEmail.isEmpty() || sAdminPassword.isEmpty()
                || sAdminGender.isEmpty() || sAdminAddress.isEmpty()
                || sAdminQuestion.isEmpty() || sAdminAnswer.isEmpty()
                || ldAdminDob == null || lAdminContact == null)
        {
            g_objlogger.warn("Registration failed - empty/invalid fields | adminId: {}", sAdminId);
            pResponse.getWriter().print("empty fields");
            return;
        }

        Admin objAdmin = new Admin();
        objAdmin.setAdminId(sAdminId);
        objAdmin.setAdminFirstName(sAdminFirstName);
        objAdmin.setAdminMiddleName(sAdminMiddleName);
        objAdmin.setAdminLastName(sAdminLastName);
        objAdmin.setAdminEmail(sAdminEmail);
        objAdmin.setAdminPassword(sAdminPassword);
        objAdmin.setAdminGender(sAdminGender);
        objAdmin.setAdminDob(ldAdminDob);
        objAdmin.setAdminContact(lAdminContact);
        objAdmin.setAdminAddress(sAdminAddress);
        objAdmin.setAdminQuestion(sAdminQuestion);
        objAdmin.setAdminAnswer(sAdminAnswer);

        AdminService objService = new AdminService();
        String sResult = objService.registerAdmin(objAdmin);

        if ("Registered successfully".equals(sResult))
        {
            g_objlogger.info("Admin registered successfully: {}", sAdminId);
        }
        else
        {
            g_objlogger.warn("Admin registration failed: {} | reason: {}", sAdminId, sResult);
        }

        pResponse.getWriter().print(sResult);
    }

    // ================= LOGIN =================This is used to login with registerd admin id and password
    private void loginAdmin(HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException
    {
    	pResponse.setContentType("text/plain");
        g_objlogger.debug("Login request received");

        String sAdminId  = safeStringValidation(pRequest.getParameter("adminId"));
        String sPassword = safeStringValidation(pRequest.getParameter("adminPassword"));

        if (sAdminId.isEmpty() || sPassword.isEmpty())
        {
            g_objlogger.warn("Login failed due to empty credentials | adminId: {}", sAdminId);
            pResponse.getWriter().print("fail");
            return;
        }

        Admin objAdmin = new Admin();
        objAdmin.setAdminId(sAdminId);
        objAdmin.setAdminPassword(sPassword);

        AdminService objService = new AdminService();

        g_objlogger.debug("Calling service layer for login | adminId: {}", sAdminId);

        String sResult = objService.loginAdmin(objAdmin);

        g_objlogger.debug("Service response received | adminId: {} | result: {}", sAdminId, sResult);
        String isAutoLogin = pRequest.getParameter("autoLogin"); //this one is for webapi

        if ("success".equals(sResult))
        {
            g_objlogger.debug("Creating session for admin | adminId: {}", sAdminId);

            HttpSession adminSession = pRequest.getSession();
            adminSession.setMaxInactiveInterval(10*60);

            adminSession.setAttribute("firstname", objAdmin.getAdminFirstName());
            adminSession.setAttribute("adminid", objAdmin.getAdminId());
            adminSession.setAttribute("adminkey", objAdmin.getAdminKey());
            if ("true".equals(isAutoLogin)) {
                pResponse.sendRedirect("dashboard.jsp");
                return;
            }
            g_objlogger.info("Login successful for adminId: {}", sAdminId);
        }
        else
        {
            g_objlogger.warn("Login failed for adminId: {} | reason: {}", sAdminId, sResult);
        }

        pResponse.getWriter().print(sResult);
    }

    //This is used to update admin profile in dashboard page
    private void updateProfile(HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException
	{
	    g_objlogger.debug("Update admin profile request received");
	
	    HttpSession adminSession = pRequest.getSession(false);
	
	    if (adminSession == null || adminSession.getAttribute("adminid") == null)
	    {
	        g_objlogger.warn("Update profile failed - session expired");
	        pResponse.getWriter().println("Sessionexpired");
	        return;
	    }
	
	    String sAdminId = (String) adminSession.getAttribute("adminid");
	    g_objlogger.info("Update profile request received for adminId: {}", sAdminId);
	
	    try
	    {
	        String sFirstName  = safeStringValidation(pRequest.getParameter("firstName"));
	        String sMiddleName = safeStringValidation(pRequest.getParameter("middleName"));
	        String sLastName   = safeStringValidation(pRequest.getParameter("lastName"));
	        String sEmail      = safeStringValidation(pRequest.getParameter("email"));
	        String sAddress    = safeStringValidation(pRequest.getParameter("address"));
	        String sGender     = safeStringValidation(pRequest.getParameter("gender"));
	
	        Long lContact = null;
	
	        try
	        {
	            String sContact = safeStringValidation(pRequest.getParameter("contact")).replaceAll("[^0-9]", "");
	            System.out.println("1"+sContact);
	            if (!sContact.isEmpty())
	            {
	                lContact = Long.parseLong(sContact);
	            }
	            System.out.println("2"+sContact);
	        }
	        catch (NumberFormatException e)
	        {
	            System.out.println("Invalid contact number");
	        }
	
	        Date ldDob = null;
	
	        try
	        {
	            String dobStr = pRequest.getParameter("dob");
	            if (dobStr != null && !dobStr.trim().isEmpty())
	            {
	                ldDob = java.sql.Date.valueOf(dobStr.trim());
	            }
	        }
	        catch (DateTimeParseException e)
	        {
	            System.out.println("Invalid DOB format");
	        }
	        System.out.println("3"+lContact);
	        Admin objAdmin = new Admin();
	        objAdmin.setAdminId(sAdminId);
	        objAdmin.setAdminFirstName(sFirstName);
	        objAdmin.setAdminMiddleName(sMiddleName);
	        objAdmin.setAdminLastName(sLastName);
	        objAdmin.setAdminEmail(sEmail);
	        objAdmin.setAdminAddress(sAddress);
	        objAdmin.setAdminGender(sGender);
	        objAdmin.setAdminDob(ldDob);
	        objAdmin.setAdminContact(lContact);
	
	        AdminService objService = new AdminService();
	        String sResult = objService.updateAdminProfile(objAdmin);
	
	        if ("success".equalsIgnoreCase(sResult))
	        {
	            g_objlogger.info("Profile updated successfully | adminId: {}", sAdminId);
	        }
	        else
	        {
	            g_objlogger.warn("Profile update failed | adminId: {} | reason: {}", sAdminId, sResult);
	        }
	
	        pResponse.getWriter().print(sResult);
	    }
	    catch (Exception e)
	    {
	        g_objlogger.error("Unexpected error during profile update | adminId: {}", sAdminId, e);
	        pResponse.getWriter().print("error");
	    }
	}

    // ================= GET PROFILE =================This allows to view my profile for editing admin details
    private void getProfile(HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException
    {
    	pResponse.setContentType("text/plain");
        HttpSession adminSession = pRequest.getSession(false);

        if (adminSession == null || adminSession.getAttribute("adminid") == null)
        {
            g_objlogger.warn("Get profile failed - session expired");
            pResponse.getWriter().print("session_expired");
            return;
        }

        String sAdminId = (String) adminSession.getAttribute("adminid");
        g_objlogger.info("Get profile request received | adminId: {}", sAdminId);

        try
        {
            AdminDao objAdminDao = new AdminDao();
            Admin objAdmin = objAdminDao.getAdminById(sAdminId);

            if (objAdmin != null)
            {
                String sData = objAdmin.getAdminFirstName() + "|" +
                               objAdmin.getAdminMiddleName() + "|" +
                               objAdmin.getAdminLastName() + "|" +
                               objAdmin.getAdminEmail() + "|" +
                               objAdmin.getAdminContact() + "|" +
                               objAdmin.getAdminAddress() + "|" +
                               objAdmin.getAdminGender() + "|" +
                               (objAdmin.getAdminDob() != null ? objAdmin.getAdminDob() : "");

                g_objlogger.info("Profile fetched successfully | adminId: {}", sAdminId);
                pResponse.getWriter().print(sData);
            }
            else
            {
                g_objlogger.warn("Profile not found | adminId: {}", sAdminId);
                pResponse.getWriter().print("error");
            }
        }
        catch (Exception e)
        {
            g_objlogger.error("Unexpected error while fetching profile | adminId: {}", sAdminId, e);
            pResponse.getWriter().print("error");
        }
    }


    // ================= CHANGE PASSWORD =================This is used to change Admin password

    private void changePassword(HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException
    {
    	
    	pResponse.setContentType("text/plain");
        g_objlogger.debug("Change Admin password request received");

        HttpSession adminSession = pRequest.getSession(false);

        if (adminSession == null || adminSession.getAttribute("adminid") == null)
        {
            g_objlogger.warn("Password change failed - session expired");
            pResponse.getWriter().print("session_expired");
            return;
        }

        String sAdminId = (String) adminSession.getAttribute("adminid");
        g_objlogger.info("Password change request received | adminId: {}", sAdminId);

        try
        {
            String sCurrentPassword = safeStringValidation(pRequest.getParameter("currentPassword"));
            String sNewPassword     = safeStringValidation(pRequest.getParameter("newPassword"));

            AdminService objService = new AdminService();
            String sResult = objService.changePassword(sAdminId, sCurrentPassword, sNewPassword);

            if ("Password updated successfully".equals(sResult))
            {
                g_objlogger.info("Password changed successfully | adminId: {}", sAdminId);

                adminSession.invalidate();
                pResponse.getWriter().print("Password changed successfully");
                return;
            }

            g_objlogger.warn("Password change failed | adminId: {} | reason: {}", sAdminId, sResult);
            pResponse.getWriter().print(sResult);
        }
        catch (Exception e)
        {
            g_objlogger.error("Unexpected error during password change | adminId: {}", sAdminId, e);
            pResponse.getWriter().print("error");
        }
    }
 // ================= LOGOUT =================This is used to invalidate session and logout from dashboard page without creating new session
    private void logoutAdmin(HttpServletRequest pRequest, HttpServletResponse pResponse)
    {
    	pResponse.setContentType("text/plain");
        g_objlogger.debug("Logout request received");

        HttpSession adminSession = pRequest.getSession(false);

        try
        {
            if (adminSession != null)
            {
                String sAdminId = (String) adminSession.getAttribute("adminid");
                g_objlogger.info("Logout requested | adminId: {}", sAdminId);

                adminSession.invalidate();

                g_objlogger.info("Session invalidated successfully | adminId: {}", sAdminId);
            }

            pResponse.sendRedirect("Login.html");
        }
        catch (IOException e)
        {
            g_objlogger.error("Error during logout redirect", e);
        }
    }


    // ================= VERIFY SECURITY =================This is used to verify security for changing admin password before loging in through one question and answer 
    private void verifySecurity(HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException
    {
    	pResponse.setContentType("text/plain");
        g_objlogger.debug("Password reset request received");

        String sAdminId  = safeStringValidation(pRequest.getParameter("adminId"));
        String sQuestion = safeStringValidation(pRequest.getParameter("question"));
        String sAnswer   = safeStringValidation(pRequest.getParameter("answer"));

        AdminService objService = new AdminService();
        String sResult = objService.verifySecurity(sAdminId, sQuestion, sAnswer);

        if ("success".equals(sResult))
        {
            HttpSession adminSession = pRequest.getSession(true);
            adminSession.setAttribute("verifiedUser", sAdminId);

            g_objlogger.info("Security verified | adminId: {}", sAdminId);
        }
        else
        {
            g_objlogger.warn("Security verification failed | adminId: {}", sAdminId);
        }

        pResponse.getWriter().print(sResult);
    }


    // ================= RESET FORGOT PASSWORD =================This is used to change admin password before loging in after verifying security credentials
    private void resetForgotPassword(HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException
    {
    	pResponse.setContentType("text/plain");
        String sAdminId     = safeStringValidation(pRequest.getParameter("adminId"));
        String sNewPassword = safeStringValidation(pRequest.getParameter("password"));

        HttpSession adminSession = pRequest.getSession(false);

        if (adminSession == null || adminSession.getAttribute("verifiedUser") == null)
        {
            pResponse.getWriter().print("unauthorized");
            return;
        }

        String sVerifiedUser = (String) adminSession.getAttribute("verifiedUser");

        if (!sAdminId.equals(sVerifiedUser))
        {
            pResponse.getWriter().print("unauthorized");
            return;
        }

        AdminService objService = new AdminService();
        String sResult = objService.resetForgotPassword(sAdminId, sNewPassword);

        if ("success".equals(sResult))
        {
            adminSession.removeAttribute("verifiedUser");
            g_objlogger.info("Forgot password reset successful | adminId: {}", sAdminId);
        }
        else if ("same_password".equals(sResult))
        {
            g_objlogger.warn("Same password used in forgot flow | adminId: {}", sAdminId);
        }
        else
        {
            g_objlogger.warn("Forgot password reset failed | adminId: {}", sAdminId);
        }

        pResponse.getWriter().print(sResult);
    }
    // ================= COMMON VALIDATION =================Adding null validation handling for variables to avoid writing individual validations for each field.
    public String safeStringValidation(String pValue) 
    {
        return (pValue != null && !pValue.trim().isEmpty()) ? pValue.trim() : "";
    }
}