/******
File Header: UserDao.java
Description: DAO for handling User actions like login, registration, and profile management.
Author : Devika V Nayak
Created On : April 25, 2026
************
Maintenance History: Initial Version
Copyright : iTech Workshop Private Limited 2001-2026
All rights reserved.
***********************/

package com.expedium.user.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.expedium.user.masterdb.UserMasterDb;
import com.expedium.user.model.Filter;
import com.expedium.user.model.User;

public class UserDao implements UserQuery
{


    public boolean addUser(User pUser)
    {
        boolean bStatus = false;
        Connection c_Conn=null;
        PreparedStatement psAddUser=null;
        ResultSet rsAddUser=null;
        try
        {
        	c_Conn = UserMasterDb.getConnection();
             psAddUser = c_Conn.prepareStatement(UserQuery.g_sINSERT_USER);

            psAddUser.setString(1, pUser.getUserFirstname());
            psAddUser.setString(2, pUser.getUserMiddlename());
            psAddUser.setString(3, pUser.getUserLastname());
            psAddUser.setString(4, pUser.getUserEmail());
            psAddUser.setLong(5, pUser.getUserContact());
            psAddUser.setString(6, pUser.getUserGender());
            psAddUser.setDate(7, pUser.getUserdob());
            psAddUser.setString(8, pUser.getUserAddress());
            psAddUser.setInt(9, pUser.getAdminKey());

           rsAddUser = psAddUser.executeQuery();

            if (rsAddUser.next())
            {
                bStatus = rsAddUser.getBoolean(1);
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
	            if (psAddUser != null)
	            {
	            	psAddUser.close();
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


    public List<User> getAllUser(int piAdminKey)
    {
        List<User> objList = new ArrayList<>();
        Connection c_Conn=null;
        PreparedStatement psGetUsers=null;
        ResultSet rsGetUsers=null;
        
        try
        {
        	c_Conn = UserMasterDb.getConnection();
          psGetUsers = c_Conn.prepareStatement(UserQuery.g_sGET_ALL_USER);
            psGetUsers.setInt(1, piAdminKey);

            rsGetUsers = psGetUsers.executeQuery();

            while (rsGetUsers.next())
            {
                User objUser = new User();

                objUser.setUserKey(rsGetUsers.getInt("user_key"));
                objUser.setUserFirstname(rsGetUsers.getString("user_firstname"));
                objUser.setUserMiddlename(rsGetUsers.getString("user_middlename"));
                objUser.setUserLastname(rsGetUsers.getString("user_lastname"));
                objUser.setUserEmail(rsGetUsers.getString("user_email"));
                objUser.setUserGender(rsGetUsers.getString("user_gender"));

                java.sql.Date sqlDate = rsGetUsers.getDate("user_dob");

                if (sqlDate != null)
                {
                    objUser.setUserdob(sqlDate);
                }
                else
                {
                    objUser.setUserdob(null);
                }

                objUser.setUserAddress(rsGetUsers.getString("user_address"));
                objUser.setUserContact(rsGetUsers.getLong("user_contactnumber"));

                objList.add(objUser);
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
	            if (psGetUsers != null)
	            {
	            	psGetUsers.close();
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
        return objList;
    }


    public boolean updateUser(User pUser)
    {
        boolean bStatus = false;
        Connection c_Conn=null;
        PreparedStatement psUpdateUser=null;

        try
        {
        	c_Conn = UserMasterDb.getConnection();

            psUpdateUser = c_Conn.prepareStatement(UserQuery.g_sUPDATE_USER);

            psUpdateUser.setString(1, pUser.getUserFirstname());
            psUpdateUser.setString(2, pUser.getUserMiddlename());
            psUpdateUser.setString(3, pUser.getUserLastname());
            psUpdateUser.setString(4, pUser.getUserEmail());
            psUpdateUser.setLong(5, pUser.getUserContact());
            psUpdateUser.setString(6, pUser.getUserGender());
            psUpdateUser.setDate(7, pUser.getUserdob());
            psUpdateUser.setString(8, pUser.getUserAddress());
            psUpdateUser.setInt(9, pUser.getUserKey());
            psUpdateUser.setInt(10, pUser.getAdminKey());

            int iRows = psUpdateUser.executeUpdate();

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


    public List<User> filterUsers(
            int adminKey,
            Filter objFilter,
            int limit,
            int offset,
            String sortColumn,
            String sortOrder)
    {
        List<User> list = new ArrayList<>();

        Connection c_Conn = null;
        PreparedStatement psFilterUsers = null;
        ResultSet rsFilterUsers = null;

        try
        {
            c_Conn = UserMasterDb.getConnection();

            psFilterUsers = c_Conn.prepareStatement(UserQuery.g_sFILTER_USER);

            psFilterUsers.setInt(1, adminKey);

            psFilterUsers.setString(2, objFilter.getFirstName());
            psFilterUsers.setString(3, objFilter.getFirstNameType());

            psFilterUsers.setString(4, objFilter.getLastName());
            psFilterUsers.setString(5, objFilter.getLastNameType());

            psFilterUsers.setString(6, objFilter.getEmail());
            psFilterUsers.setString(7, objFilter.getEmailType());

            psFilterUsers.setString(8, objFilter.getContact());
            psFilterUsers.setString(9, objFilter.getContactType());

            psFilterUsers.setString(10, objFilter.getGender());
            psFilterUsers.setString(11, objFilter.getGenderType());

            psFilterUsers.setString(12, objFilter.getAddress());
            psFilterUsers.setString(13, objFilter.getAddressType());

            psFilterUsers.setInt(14, limit);
            psFilterUsers.setInt(15, offset);
            psFilterUsers.setString(16, sortColumn);
            psFilterUsers.setString(17, sortOrder);

            rsFilterUsers = psFilterUsers.executeQuery();

            while (rsFilterUsers.next())
            {
                User obj = new User();

                obj.setUserKey(rsFilterUsers.getInt("user_key"));
                obj.setUserFirstname(rsFilterUsers.getString("user_firstname") != null ? rsFilterUsers.getString("user_firstname") : "");
                obj.setUserMiddlename(rsFilterUsers.getString("user_middlename") != null ? rsFilterUsers.getString("user_middlename") : "");
                obj.setUserLastname(rsFilterUsers.getString("user_lastname") != null ? rsFilterUsers.getString("user_lastname") : "");
                obj.setUserEmail(rsFilterUsers.getString("user_email") != null ? rsFilterUsers.getString("user_email") : "");
                obj.setUserGender(rsFilterUsers.getString("user_gender") != null ? rsFilterUsers.getString("user_gender") : "");
                obj.setUserAddress(rsFilterUsers.getString("user_address") != null ? rsFilterUsers.getString("user_address") : "");

                long lContact = rsFilterUsers.getLong("user_contactnumber");
                obj.setUserContact(rsFilterUsers.wasNull() ? 0L : lContact);

                java.sql.Date dbDate = rsFilterUsers.getDate("user_dob");
                obj.setUserdob(dbDate);

                list.add(obj);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try { if (rsFilterUsers != null) rsFilterUsers.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (psFilterUsers != null) psFilterUsers.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (c_Conn != null) c_Conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return list;
    }


    public int getFilteredCount(int adminKey, Filter objFilter)
    {
        int iCount = 0;
        Connection c_Conn=null;
        PreparedStatement psFilterCount =null;
        ResultSet rsFilterCount=null;
        

        try {
        	c_Conn = UserMasterDb.getConnection();
        	psFilterCount = c_Conn.prepareStatement(UserQuery.g_sFILTER_COUNT);
        
            psFilterCount.setInt(1, adminKey);

            psFilterCount.setString(2, objFilter.getFirstName());
            psFilterCount.setString(3, objFilter.getFirstNameType());

            psFilterCount.setString(4, objFilter.getLastName());
            psFilterCount.setString(5, objFilter.getLastNameType());

            psFilterCount.setString(6, objFilter.getEmail());
            psFilterCount.setString(7, objFilter.getEmailType());

            psFilterCount.setString(8, objFilter.getContact());
            psFilterCount.setString(9, objFilter.getContactType());

            psFilterCount.setString(10, objFilter.getGender());
            psFilterCount.setString(11, objFilter.getGenderType());

            psFilterCount.setString(12, objFilter.getAddress());
            psFilterCount.setString(13, objFilter.getAddressType());

           rsFilterCount = psFilterCount.executeQuery();

            if (rsFilterCount.next())
            {
                iCount = rsFilterCount.getInt(1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try { if (rsFilterCount != null) rsFilterCount.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (psFilterCount != null) psFilterCount.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (c_Conn != null) c_Conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        

        return iCount;
    }


 // ================= DAO METHOD =================

    public List<User> getUsersByAdmin(
            int piAdminKey,
            int pOffset,
            int pLimit,
            String pSortColumn,
            String pSortOrder)
    {
        List<User> objList = new ArrayList<>();

        Connection c_Conn = null;
        PreparedStatement psGetUser = null;
        ResultSet rsGetUser = null;

        try
        {
            c_Conn = UserMasterDb.getConnection();

            String sColumn = "user_key";
            String sOrder = "DESC";

            if (pSortColumn != null)
            {
                switch (pSortColumn)
                {
                    case "firstname":
                        sColumn = "user_firstname";
                        break;

                    case "middlename":
                        sColumn = "user_middlename";
                        break;

                    case "lastname":
                        sColumn = "user_lastname";
                        break;

                    case "email":
                        sColumn = "user_email";
                        break;

                    case "address":
                        sColumn = "user_address";
                        break;
                }
            }

            if ("asc".equalsIgnoreCase(pSortOrder))
            {
                sOrder = "ASC";
            }
            else if ("desc".equalsIgnoreCase(pSortOrder))
            {
                sOrder = "DESC";
            }

            String sQuery =
                "SELECT * FROM table_user " +
                "WHERE admin_key = ? " +
                "ORDER BY " + sColumn + " " + sOrder +
                " LIMIT ? OFFSET ?";

            psGetUser = c_Conn.prepareStatement(sQuery);

            psGetUser.setInt(1, piAdminKey);
            psGetUser.setInt(2, pLimit);
            psGetUser.setInt(3, pOffset);

            rsGetUser = psGetUser.executeQuery();

            while (rsGetUser.next())
            {
                User objUser = new User();

                objUser.setUserKey(rsGetUser.getInt("user_key"));
                objUser.setUserFirstname(rsGetUser.getString("user_firstname"));
                objUser.setUserMiddlename(rsGetUser.getString("user_middlename"));
                objUser.setUserLastname(rsGetUser.getString("user_lastname"));
                objUser.setUserEmail(rsGetUser.getString("user_email"));

                java.sql.Date sqlDate = rsGetUser.getDate("user_dob");

                if (sqlDate != null)
                {
                    objUser.setUserdob(sqlDate);
                }
                else
                {
                    objUser.setUserdob(null);
                }

                objUser.setUserContact(rsGetUser.getLong("user_contactnumber"));
                objUser.setUserGender(rsGetUser.getString("user_gender"));
                objUser.setUserAddress(rsGetUser.getString("user_address"));

                objList.add(objUser);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try { if (rsGetUser != null) rsGetUser.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (psGetUser != null) psGetUser.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (c_Conn != null) c_Conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }

        return objList;
    }

    public int getUserCount(int piAdminKey)
    {
        int iCount = 0;
        Connection c_Conn=null;
        PreparedStatement psUsersCount=null;
        ResultSet rsUserCount =null;

        try
        {
        	 c_Conn = UserMasterDb.getConnection();
            psUsersCount = c_Conn.prepareStatement(UserQuery.g_sGET_USER_COUNT);
            psUsersCount.setInt(1, piAdminKey);

            rsUserCount = psUsersCount.executeQuery();

            if (rsUserCount.next())
            {
                iCount = rsUserCount.getInt(1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try { if (rsUserCount != null) rsUserCount.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (psUsersCount != null) psUsersCount.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (c_Conn != null) c_Conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }


        return iCount;
    }


    public boolean isEmailAlreadyExists(User pUser)
    {
        boolean bExists = false;
        Connection c_Conn=null;
        PreparedStatement psEmailCount=null;
        ResultSet rsEmailCount=null;

        try
        {   
             c_Conn = UserMasterDb.getConnection();

            psEmailCount = c_Conn.prepareStatement(UserQuery.g_sEMAIL_CHECK);
            psEmailCount.setString(1, pUser.getUserEmail());
            psEmailCount.setInt(2, pUser.getUserKey());

            rsEmailCount = psEmailCount.executeQuery();

            if (rsEmailCount.next() && rsEmailCount.getInt(1) > 0)
            {
                bExists = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try { if (rsEmailCount != null) rsEmailCount.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (psEmailCount != null) psEmailCount.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (c_Conn != null) c_Conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }


        return bExists;
    }


    public boolean deleteUsers(String[] userKeys, int adminKey)
    {
        boolean bStatus = false;
        Connection c_Conn = null;
        PreparedStatement deleteUserStmt = null;

        try
        {
            c_Conn = UserMasterDb.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("delete from table_user where user_key in (");

            for (int i = 0; i < userKeys.length; i++)
            {
                sql.append("?");
                if (i < userKeys.length - 1)
                {
                    sql.append(",");
                }
            }

            sql.append(") and admin_key=?");

            deleteUserStmt = c_Conn.prepareStatement(sql.toString());

            int i = 1;

            for (String key : userKeys)
            {
                deleteUserStmt.setInt(i++, Integer.parseInt(key));
            }

            deleteUserStmt.setInt(i, adminKey);

            bStatus = deleteUserStmt.executeUpdate() > 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try { if (deleteUserStmt != null) deleteUserStmt.close(); } catch (Exception e) {}
            try { if (c_Conn != null) c_Conn.close(); } catch (Exception e) {}
        }

        return bStatus;
    }
    public User showUserDetails(String sUserEmail)
    {
        User objUser = null;
        Connection c_Conn=null;
        PreparedStatement objPst =null;
        ResultSet objRs=null;
        
        
   
        try {
        		
                 c_Conn = UserMasterDb.getConnection();
                 objPst = c_Conn.prepareStatement(UserQuery.GET_PDF);
        
        
            objPst.setString(1, sUserEmail);

             objRs = objPst.executeQuery();

            if (objRs.next())
            {
                objUser = new User();

                objUser.setUserKey(objRs.getInt("user_key"));
                objUser.setUserFirstname(objRs.getString("user_firstname"));
                objUser.setUserMiddlename(objRs.getString("user_middlename"));
                objUser.setUserLastname(objRs.getString("user_lastname"));
                objUser.setUserEmail(objRs.getString("user_email"));
                objUser.setUserContact(objRs.getLong("user_contactnumber"));
                objUser.setUserGender(objRs.getString("user_gender"));
                objUser.setUserdob( objRs.getDate("user_dob"));
                objUser.setUserAddress(objRs.getString("user_address"));
            }
        }
        catch (Exception e)
        {
         e.printStackTrace();
        }
        finally
        {
            try { if (objRs != null) objRs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (objPst != null) objPst.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (c_Conn != null) c_Conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }


        return objUser;
    }
}
