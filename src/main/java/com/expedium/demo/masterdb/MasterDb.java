package com.expedium.demo.masterdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MasterDb {

    public static final String DB_URL =
            "jdbc:postgresql://localhost:5432/DemoProject";

    public static final String DB_USERNAME = "postgres";
    public static final String DB_PASSWORD = "postgres";
	private static Connection c_Con;


    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection()  
	{
		try
		{
			String sJndiName = "java:jboss/PostgresXADS";
			Context initContext = new InitialContext();
			DataSource dseCP = (DataSource) initContext.lookup(sJndiName);
			c_Con = dseCP.getConnection();
 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return c_Con; 
	}

    public static Connection getConnection1() {
        try {
            return DriverManager.getConnection(
                    DB_URL,
                    DB_USERNAME,
                    DB_PASSWORD
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}