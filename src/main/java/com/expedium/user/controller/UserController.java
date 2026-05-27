/******
File Header: UserController.java
Description: Controller for handling User actions like login, registration, and profile management.
Author : Devika V Nayak
Created On : April 25, 2026
************
Maintenance History: Initial Version
Copyright : iTech Workshop Private Limited 2001-2026
All rights reserved.
***********************/
package com.expedium.user.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.expedium.demo.controller.AdminController;
import com.expedium.user.model.User;
import com.expedium.user.service.UserService;
import com.itextpdf.html2pdf.HtmlConverter;

@WebServlet("/UserController")
public class UserController extends HttpServlet
{
    private static final Logger g_objlogger = LogManager.getLogger(UserController.class);
    private static final long g_lserialVersionUID = 1L;

    UserService objService = new UserService();


    protected void doPost(HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException
    {
        String sAction = pRequest.getParameter("action");

        if ("add".equals(sAction) || "update".equals(sAction))
        {
            addUser(pRequest, pResponse);
        }
        else if ("delete".equals(sAction))
        {
            deleteUser(pRequest, pResponse);
        }
        
    }
    
    protected void doGet(HttpServletRequest pRequest,HttpServletResponse pResponse)throws IOException {

        String sAction = pRequest.getParameter("action");

        if ("downloadPdf".equals(sAction)) {

            downloadUserProfile(pRequest, pResponse);
        }
    }


    private void addUser(HttpServletRequest pRequest, HttpServletResponse pResponse) throws IOException
    {
        pResponse.setContentType("text/plain");
        String sAction = pRequest.getParameter("action");

        try
        {
            HttpSession userSession = pRequest.getSession();
            Integer iAdminKey = (Integer) userSession.getAttribute("adminkey");

            if (iAdminKey == null)
            {
                g_objlogger.warn("Add/Update user failed - session expired");
                pResponse.getWriter().print("Session expired login once again");
                return;
            }

            User objUser = new User();
            String sFirstName=pRequest.getParameter("firstName");
            String sMiddleName=pRequest.getParameter("middleName");
            String sLastName=pRequest.getParameter("lastName");
            String sEmail=pRequest.getParameter("email");
            String sAddress=pRequest.getParameter("address");
            String sGender=pRequest.getParameter("gender");
            String sUserKey=pRequest.getParameter("userKey");
            if ("update".equals(sAction))
            {
                try
                {
                    objUser.setUserKey(Integer.parseInt(sUserKey));
                }
                catch (NumberFormatException e)
                {
                    g_objlogger.warn("Invalid user key during update | adminKey: {}", iAdminKey);
                    pResponse.getWriter().print("Invalid user key");
                    return;
                }
            }
          
            objUser.setUserFirstname(sFirstName);
            objUser.setUserMiddlename(sMiddleName);
            objUser.setUserLastname(sLastName);
            objUser.setUserEmail(sEmail);
        
            String sContactParam = pRequest.getParameter("contact");
            String sContactStr = (sContactParam != null)
                    ? sContactParam.replaceAll("[^0-9]", "")
                    : "";

            Long lContact = null;

            try
            {
                if (!sContactStr.isEmpty())
                {
                    lContact = Long.parseLong(sContactStr);
                }
                else
                {
                    pResponse.getWriter().print("contact");
                    return;
                }
            }
            catch (NumberFormatException e)
            {
                pResponse.getWriter().print("Invalid contact number");
                return;
            }

            objUser.setUserContact(lContact);
            objUser.setUserGender(sGender);

            String sDobStr = pRequest.getParameter("dob");

            try
            {
                objUser.setUserdob(
                        (sDobStr != null && !sDobStr.trim().isEmpty())
                                ?java.sql.Date.valueOf(sDobStr)
                                : null
                );
            }
            catch (DateTimeParseException e)
            {
                pResponse.getWriter().print("Invalid date format");
                return;
            }

            objUser.setUserAddress(sAddress);
            objUser.setAdminKey(iAdminKey);

            String sResult;

            if ("add".equals(sAction))
            {
                g_objlogger.debug("Add user flow started | adminKey: {}", iAdminKey);
                sResult = objService.addUser(objUser);
            }
            else
            {
                g_objlogger.debug("Update user flow started | adminKey: {}", iAdminKey);
                sResult = objService.updateUser(objUser);
            }

            if (sResult.toLowerCase().contains("success"))
            {
                g_objlogger.info("User {} successful | adminKey: {}", sAction, iAdminKey);
            }
            else
            {
                g_objlogger.warn("User {} failed | adminKey: {} | reason: {}", sAction, iAdminKey, sResult);
            }

            pResponse.getWriter().println(sResult);
        }
        catch (Exception e)
        {
            g_objlogger.error("Unexpected error in add/update user", e);
            pResponse.getWriter().print("error");
        }
    }


    private void deleteUser(HttpServletRequest pRequest, HttpServletResponse pResponse)
    {
        g_objlogger.debug("Delete user request received");

        HttpSession userSession = pRequest.getSession(false);

        if (userSession == null || userSession.getAttribute("adminkey") == null)
        {
            g_objlogger.warn("Delete failed - session expired");
            return;
        }

        Integer iAdminKey = (Integer) userSession.getAttribute("adminkey");
        String sUserKeys = pRequest.getParameter("userKeys");

        try
        {
            boolean bIsDeleted = objService.deleteUsers(sUserKeys.split(","), iAdminKey);

            if (bIsDeleted)
            {
                g_objlogger.info("Users deleted | adminKey: {}", iAdminKey);
                pResponse.getWriter().print("success");
            }
            else
            {
                g_objlogger.warn("Delete failed | adminKey: {}", iAdminKey);
                pResponse.getWriter().print("delete failed");
            }
        }
        catch (Exception e)
        {
            g_objlogger.error("Error during delete | adminKey: {}", iAdminKey, e);
        }
    }
    
    
    
    private void downloadUserProfile(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String sUserEmail = request.getParameter("UserEmail");
        g_objlogger.warn("Received email: {}", sUserEmail);

        try {

            User objUser = objService.showUserDetails(sUserEmail);

            if (objUser == null) {

                g_objlogger.warn("User not found for PDF download");

                response.getWriter().print("User not found");
                return;
            }

            response.setContentType("application/pdf");

            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=User_"
                            + objUser.getUserFirstname()
                            + ".pdf"
            );

            StringBuilder sbHtml = new StringBuilder();

            sbHtml.append(
                    "<html>"
                    + "<head>"
                    + "<style>"

                    + "body{font-family:Arial;padding:20px;}"
                    + "h1{color:#0d6efd;text-align:center;}"
                    + "table{width:100%;border-collapse:collapse;margin-top:20px;}"
                    + "th,td{border:1px solid #ccc;padding:10px;text-align:left;}"
                    + "th{background:#f2f2f2;}"

                    + "</style>"
                    + "</head>"

                    + "<body>"

                    + "<h1>User Profile Report</h1>"

                    + "<table>"

                    + "<tr>"
                    + "<th>Field</th>"
                    + "<th>Value</th>"
                    + "</tr>"

                    + "<tr>"
                    + "<td>First Name</td>"
                    + "<td>" + objUser.getUserFirstname() + "</td>"
                    + "</tr>"

                    + "<tr>"
                    + "<td>Middle Name</td>"
                    + "<td>" + objUser.getUserMiddlename() + "</td>"
                    + "</tr>"

                    + "<tr>"
                    + "<td>Last Name</td>"
                    + "<td>" + objUser.getUserLastname() + "</td>"
                    + "</tr>"

                    + "<tr>"
                    + "<td>Email</td>"
                    + "<td>" + objUser.getUserEmail() + "</td>"
                    + "</tr>"

                    + "<tr>"
                    + "<td>Contact</td>"
                    + "<td>" + objUser.getUserContact() + "</td>"
                    + "</tr>"

                    + "<tr>"
                    + "<td>Gender</td>"
                    + "<td>" + objUser.getUserGender() + "</td>"
                    + "</tr>"

                    + "<tr>"
                    + "<td>DOB</td>"
                    + "<td>" + objUser.getUserdob() + "</td>"
                    + "</tr>"

                    + "<tr>"
                    + "<td>Address</td>"
                    + "<td>" + objUser.getUserAddress() + "</td>"
                    + "</tr>"

                    + "</table>"

                    + "</body>"
                    + "</html>"
            );

            OutputStream objOut = response.getOutputStream();

            HtmlConverter.convertToPdf(
                    sbHtml.toString(),
                    objOut
            );

            objOut.flush();
            objOut.close();

            g_objlogger.info("PDF downloaded successfully");

        }
        catch (Exception e) {

            g_objlogger.error("Error while generating PDF", e);

            response.getWriter().print("PDF generation failed");
        }
    }
}