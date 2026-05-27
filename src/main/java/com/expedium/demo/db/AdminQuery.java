package com.expedium.demo.db;

public interface AdminQuery 
{
	 public static final String g_sINSERT_VALUES			= "select insert_admin(?,?,?,?,?,?,?,?,?,?,?,?)";
	 public static final String g_sGET_VALUES				= "select * from table_admin where admin_id=?";
	 public static final String g_sGET_ALL_ADMIN_VALUES		= "select *from table_admin";
	 public static final String UPDATE_VALUES				= "update table_admin set admin_id=?, admin_firstname=?,admin_middlename=?,admin_lastname=?,admin_email=?,admin_contact=?,admin_address=?,admin_gender=?,admin_dob=? where admin_id=?";
	 public static final String DELETE_VALUE				= "delete from  table_admin where admin_id=?";
	 public static final String UPDATE_PROFILE_SQL 			= "UPDATE table_admin SET admin_firstname=?, admin_middlename=?, admin_lastname=?, " +
										                    "admin_email=?, admin_contact=?, admin_address=?, admin_gender=?, admin_dob=? " +
										                    "WHERE admin_id=?";
	 public static final String UPDATE_PASSWORD				= "update table_admin set admin_password=? where admin_id=?";
	

}
