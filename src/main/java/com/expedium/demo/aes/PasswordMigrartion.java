package com.expedium.demo.aes;

import java.util.List;

import com.expedium.demo.aes.AESUtil;
import com.expedium.demo.db.AdminDao;
import com.expedium.demo.model.Admin;

public class PasswordMigrartion {

    private static final String SECRET_KEY = "mN4$eK9#sD6@wP1q"
    		+ "";

    public static void main(String[] args) {

        AdminDao objDao = new AdminDao();
        List<Admin> objList = objDao.getAllAdminDetails(); //it'll fetch all admin from db

        for (Admin objAdmin : objList) {

            String sPassword = objAdmin.getAdminPassword();

            if (isEncrypted(sPassword)) {
                System.out.println("Skipped (already encrypted): " + objAdmin.getAdminId());
                continue;
            }

            String sEncrypted = AESUtil.encrypt(sPassword, SECRET_KEY);//method in AESUtil class which accepts arguments data,key

            objDao.updatePassword(objAdmin.getAdminId(), sEncrypted);

            System.out.println("Updated: " + objAdmin.getAdminId());
        }

        System.out.println("Migration Completed!");
    }

    private static boolean isEncrypted(String password) {
        return password != null && password.matches("^[A-Za-z0-9+/=]+$") && password.length() > 20;
    }
}