/******
File Header: AdminDao.java
Description: DAO for handling Admin actions like login, registration, and profile management.
Author : Devika V Nayak
Created On : April 25, 2026
************
Maintenance History: Initial Version
Copyright : iTech Workshop Private Limited 2001-2026
All rights reserved.
***********************/
package com.expedium.demo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.expedium.demo.masterdb.MasterDb;
import com.expedium.demo.model.Admin;

public class AdminDao
{
	
//This is used to update profile in dashboard
	public boolean updateProfile(Admin pAdmin)
	{
	    Connection c_Conn = null;
	    PreparedStatement psUpdateProfile = null;

	    try
	    {
	        c_Conn = MasterDb.getConnection();

	        psUpdateProfile = c_Conn.prepareStatement(AdminQuery.UPDATE_PROFILE_SQL);

	        psUpdateProfile.setString(1, pAdmin.getAdminFirstName());
	        psUpdateProfile.setString(2, pAdmin.getAdminMiddleName());
	        psUpdateProfile.setString(3, pAdmin.getAdminLastName());
	        psUpdateProfile.setString(4, pAdmin.getAdminEmail());
	        psUpdateProfile.setLong  (5, pAdmin.getAdminContact());
	        psUpdateProfile.setString(6, pAdmin.getAdminAddress());
	        psUpdateProfile.setString(7, pAdmin.getAdminGender());
	        psUpdateProfile.setDate  (8, pAdmin.getAdminDob());
	        psUpdateProfile.setString(9, pAdmin.getAdminId());

	        int rowsUpdated = psUpdateProfile.executeUpdate();

	        return rowsUpdated > 0;
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	        return false;
	    }
	    finally
	    {
	        try
	        {
	            if (psUpdateProfile != null)
	            {
	                psUpdateProfile.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }

	        try
	        {
	            if (c_Conn != null)
	            {
	                c_Conn.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }
	    }
	}

	//This is used for registering admin details
	public boolean insertAdmin(Admin pAdmin)
	{
	    boolean bStatus = false;

	    Connection c_Conn = null;
	    PreparedStatement psInsertAdmin = null;
	    ResultSet rs = null;

	    try
	    {
	        c_Conn = MasterDb.getConnection();

	        psInsertAdmin = c_Conn.prepareStatement(AdminQuery.g_sINSERT_VALUES);

	        psInsertAdmin.setString(1, pAdmin.getAdminId());
	        psInsertAdmin.setString(2, pAdmin.getAdminFirstName());
	        psInsertAdmin.setString(3, pAdmin.getAdminMiddleName());
	        psInsertAdmin.setString(4, pAdmin.getAdminLastName());
	        psInsertAdmin.setString(5, pAdmin.getAdminEmail());
	        psInsertAdmin.setString(6, pAdmin.getAdminPassword());
	        psInsertAdmin.setLong(7, pAdmin.getAdminContact());
	        psInsertAdmin.setString(8, pAdmin.getAdminAddress());
	        psInsertAdmin.setString(9, pAdmin.getAdminGender());
	        psInsertAdmin.setDate(10, pAdmin.getAdminDob());
	        psInsertAdmin.setString(11, pAdmin.getAdminQuestion());
	        psInsertAdmin.setString(12, pAdmin.getAdminAnswer());

	        rs = psInsertAdmin.executeQuery();

	        if (rs.next())
	        {
	            bStatus = rs.getBoolean(1);
	        }
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	    finally
	    {
	        try
	        {
	            if (rs != null)
	            {
	                rs.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }

	        try
	        {
	            if (psInsertAdmin != null)
	            {
	                psInsertAdmin.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }

	        try
	        {
	            if (c_Conn != null)
	            {
	                c_Conn.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }
	    }

	    return bStatus;
	}
    public Admin getDetails(String pAdminId)
    {
        Admin objAdmin = null;
        Connection  c_Conn= null;
        PreparedStatement psGetAdmin=null;
        ResultSet rs=null;
        try
        {
        	c_Conn= MasterDb.getConnection();
             psGetAdmin = c_Conn.prepareStatement(AdminQuery.g_sGET_VALUES);
            psGetAdmin.setString(1, pAdminId);

           rs = psGetAdmin.executeQuery();

            if (rs.next())
            {
                objAdmin = new Admin();

                objAdmin.setAdminKey(rs.getInt("admin_key"));
                objAdmin.setAdminId(rs.getString("admin_id"));
                objAdmin.setAdminFirstName(rs.getString("admin_firstname"));
                objAdmin.setAdminMiddleName(rs.getString("admin_middlename"));
                objAdmin.setAdminLastName(rs.getString("admin_lastname"));
                objAdmin.setAdminEmail(rs.getString("admin_email"));
                objAdmin.setAdminPassword(rs.getString("admin_password"));
                objAdmin.setAdminContact(rs.getLong("admin_contact"));
                objAdmin.setAdminAddress(rs.getString("admin_address"));
                objAdmin.setAdminGender(rs.getString("admin_gender"));

                java.sql.Date sqlDate = rs.getDate("admin_dob");

                if (sqlDate != null)
                {
                    objAdmin.setAdminDob(sqlDate);
                }
                else
                {
                    objAdmin.setAdminDob(null);
                }

                objAdmin.setAdminQuestion(rs.getString("admin_question"));
                objAdmin.setAdminAnswer(rs.getString("admin_answer"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
	    {
	        try
	        {
	            if (rs != null)
	            {
	                rs.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }

	        try
	        {
	            if (psGetAdmin != null)
	            {
	            	psGetAdmin.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }

	        try
	        {
	            if (c_Conn != null)
	            {
	                c_Conn.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }
	    }

        return objAdmin;
    }


    public List<Admin> getAllAdminDetails()
    {
        List<Admin> lAdminList = new ArrayList<>();
        Connection  c_Conn= null;
        PreparedStatement psGetAllAdmin=null;
        ResultSet rs=null;
        try
        {
        	c_Conn= MasterDb.getConnection();
             psGetAllAdmin = c_Conn.prepareStatement(AdminQuery.g_sGET_ALL_ADMIN_VALUES);
             rs = psGetAllAdmin.executeQuery();

            while (rs.next())
            {
                Admin objAdmin = new Admin();

                objAdmin.setAdminKey(rs.getInt("admin_key"));
                objAdmin.setAdminId(rs.getString("admin_id"));
                objAdmin.setAdminFirstName(rs.getString("admin_firstname"));
                objAdmin.setAdminMiddleName(rs.getString("admin_middlename"));
                objAdmin.setAdminLastName(rs.getString("admin_lastname"));
                objAdmin.setAdminEmail(rs.getString("admin_email"));
                objAdmin.setAdminPassword(rs.getString("admin_password"));
                objAdmin.setAdminContact(rs.getLong("admin_contact"));
                objAdmin.setAdminAddress(rs.getString("admin_address"));
                objAdmin.setAdminGender(rs.getString("admin_gender"));

                java.sql.Date sqlDate = rs.getDate("admin_dob");

                if (sqlDate != null)
                {
                    objAdmin.setAdminDob(sqlDate);
                }
                else
                {
                    objAdmin.setAdminDob(null);
                }

                objAdmin.setAdminQuestion(rs.getString("admin_question"));
                objAdmin.setAdminAnswer(rs.getString("admin_answer"));

                lAdminList.add(objAdmin);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
	    {
	        try
	        {
	            if (rs != null)
	            {
	                rs.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }

	        try
	        {
	            if (psGetAllAdmin != null)
	            {
	            	psGetAllAdmin.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }

	        try
	        {
	            if (c_Conn != null)
	            {
	                c_Conn.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }
	    }

        return lAdminList;
    }


    public boolean updatefirst(Admin pAdmin)
    {
    	Connection  c_Conn= MasterDb.getConnection();
    	
        try (PreparedStatement psUpdateAdmin = c_Conn.prepareStatement(AdminQuery.UPDATE_VALUES))
        {
            psUpdateAdmin.setString(1, pAdmin.getAdminFirstName());
            psUpdateAdmin.setString(2, pAdmin.getAdminMiddleName());
            psUpdateAdmin.setString(3, pAdmin.getAdminLastName());
            psUpdateAdmin.setString(4, pAdmin.getAdminEmail());
            psUpdateAdmin.setLong(5, pAdmin.getAdminContact());
            psUpdateAdmin.setString(6, pAdmin.getAdminAddress());
            psUpdateAdmin.setString(7, pAdmin.getAdminGender());
            psUpdateAdmin.setDate(8,pAdmin.getAdminDob());
            psUpdateAdmin.setString(9, pAdmin.getAdminId());

            return psUpdateAdmin.executeUpdate() > 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
       
    }

//This is not used
    public boolean deleteAdmin(Admin pAdmin)
    {
        boolean bStatus = false;
        Connection  c_Conn= null;
        PreparedStatement psDeleteAdmin=null;
        try
        {
        	c_Conn= MasterDb.getConnection();
            psDeleteAdmin = c_Conn.prepareStatement(AdminQuery.DELETE_VALUE);
            psDeleteAdmin.setString(1, pAdmin.getAdminId());

            int iDeleteCount = psDeleteAdmin.executeUpdate();

            if (iDeleteCount > 0)
            {
                bStatus = true;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
	    {
	        

	        try
	        {
	            if (psDeleteAdmin != null)
	            {
	            	psDeleteAdmin.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }

	        try
	        {
	            if (c_Conn != null)
	            {
	                c_Conn.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }
	    }

        return bStatus;
    }

//This is used to View profile in dashboard called directly from controller
    public Admin getAdminById(String pAdminId)
    {
        Admin objAdmin = null;

        Connection c_Conn = null;
        PreparedStatement psGetAdminById = null;
        ResultSet rsGetAdminById = null;

        try
        {
            c_Conn = MasterDb.getConnection();

            psGetAdminById = c_Conn.prepareStatement(AdminQuery.g_sGET_VALUES);

            psGetAdminById.setString(1, pAdminId);

            rsGetAdminById = psGetAdminById.executeQuery();

            if (rsGetAdminById.next())
            {
                objAdmin = new Admin();

                objAdmin.setAdminKey(rsGetAdminById.getInt("admin_key"));
                objAdmin.setAdminId(rsGetAdminById.getString("admin_id"));
                objAdmin.setAdminFirstName(rsGetAdminById.getString("admin_firstname"));
                objAdmin.setAdminMiddleName(rsGetAdminById.getString("admin_middlename"));
                objAdmin.setAdminLastName(rsGetAdminById.getString("admin_lastname"));
                objAdmin.setAdminEmail(rsGetAdminById.getString("admin_email"));
                objAdmin.setAdminAddress(rsGetAdminById.getString("admin_address"));
                objAdmin.setAdminGender(rsGetAdminById.getString("admin_gender"));
                objAdmin.setAdminPassword(rsGetAdminById.getString("admin_password"));
                objAdmin.setAdminContact(rsGetAdminById.getLong("admin_contact"));

                java.sql.Date sqlDate = rsGetAdminById.getDate("admin_dob");

                if (sqlDate != null)
                {
                    objAdmin.setAdminDob(sqlDate);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (rsGetAdminById != null)
                {
                    rsGetAdminById.close();
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

            try
            {
                if (psGetAdminById != null)
                {
                    psGetAdminById.close();
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

            try
            {
                if (c_Conn != null)
                {
                    c_Conn.close();
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        return objAdmin;
    }
//This is to check duplicate admin id,email all in one method
    public boolean isValueExists(String pColumnName, Object pValue, String pExcludeAdminId)
    {
        boolean bExists = false;
        Connection  c_Conn= null;
        ResultSet rs =null;
        PreparedStatement psIsValueExist=null;
        c_Conn= MasterDb.getConnection();
        String sSql = "SELECT COUNT(*) FROM table_admin WHERE " + pColumnName + " = ?";

        if (pExcludeAdminId != null && !pExcludeAdminId.trim().isEmpty())
        {
            sSql += " AND admin_id <> ?";
        }
        
        try 
        {         
        	c_Conn= MasterDb.getConnection();
        	psIsValueExist = c_Conn.prepareStatement(sSql);

            if ("admin_contact".equals(pColumnName))
            {
                psIsValueExist.setLong(1, Long.parseLong(pValue.toString()));
            }
            else
            {
                psIsValueExist.setString(1, pValue.toString());
            }

            if (pExcludeAdminId != null)
            {
                psIsValueExist.setString(2, pExcludeAdminId);
            }

             rs = psIsValueExist.executeQuery();

            if (rs.next())
            {
                bExists = rs.getInt(1) > 0;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
	    {
	        try
	        {
	            if (rs != null)
	            {
	                rs.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }

	        try
	        {
	            if (psIsValueExist != null)
	            {
	            	psIsValueExist.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }

	        try
	        {
	            if (c_Conn != null)
	            {
	                c_Conn.close();
	            }
	        }
	        catch (SQLException e)
	        {
	            e.printStackTrace();
	        }
	    }
       

        return bExists;
    }

//This is used to verify security by passing question and answer
    public boolean verifySecurity(String adminId, String question, String answer)
    {
        boolean bIsValid = false;
        Connection  c_Conn= null;
        c_Conn= MasterDb.getConnection();
        try
        {
            String sql = "SELECT COUNT(*) FROM table_admin WHERE admin_id = ? AND admin_question = ? AND admin_answer = ?";

            PreparedStatement psVerifySecurity = c_Conn.prepareStatement(sql);
            psVerifySecurity.setString(1, adminId);
            psVerifySecurity.setString(2, question);
            psVerifySecurity.setString(3, answer);

            ResultSet rsVerifySecurity = psVerifySecurity.executeQuery();

            if (rsVerifySecurity.next())
            {
                bIsValid = rsVerifySecurity.getInt(1) > 0;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        

        return bIsValid;
    }

//This is used to change admin password in dashboard and change admin password through security verification
    public boolean updatePassword(String pAdminId, String pNewPassword)
    {
        boolean bStatus = false;
        Connection  c_Conn= null;
        c_Conn= MasterDb.getConnection();
        try
        {
            PreparedStatement psUpdatePassword = c_Conn.prepareStatement(AdminQuery.UPDATE_PASSWORD);
            psUpdatePassword.setString(1, pNewPassword);
            psUpdatePassword.setString(2, pAdminId);

            int iRows = psUpdatePassword.executeUpdate();

            if (iRows > 0)
            {
                bStatus = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       
        return bStatus;
    }
}