package com.expedium.user.db;

public interface UserQuery 
{
	public static final String g_sINSERT_USER 				= "select insert_user(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String g_sGET_ALL_USER 			    = "select *from table_user where admin_key=? ORDER BY user_key ASC";
	public static final String g_sGET_USERS_BY_ADMIN 		= "select *from table_user where admin_key=? order by user_key limit ? offset ?";
	public static final String g_sGET_USER_COUNT 			= "select count(*)from table_user where admin_key=?";
	public static final String g_sEMAIL_CHECK 				= "select count(*)from table_user where user_email=? and user_key!=?";
	public static final String g_sUPDATE_USER 				= "UPDATE table_user SET " + "user_firstname=?, " + "user_middlename=?, "
															+ "user_lastname=?, " + "user_email=?, " + "user_contactnumber=?, " + "user_gender=?, " + "user_dob=?, "
															+ "user_address=? " + "WHERE user_key=? AND admin_key=?";;
	public static final String g_sDELETE_USER 				= "delete from table_user where user_key=? and admin_key=?";
	public static final String g_sFILTER_USER 				= "SELECT * FROM filter_user_details(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String g_sFILTER_COUNT 				= "SELECT filter_user_count(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	 public static final String GET_PDF                 	= "SELECT * FROM table_user WHERE user_email = ?";
}
