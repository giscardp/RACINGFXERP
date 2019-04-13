package model;

import db.conexion;

public class userModel {
    
    private String id, username, password, nama, email;
    conexion kon = new conexion();
    private boolean statusUpdate=false;
    
    public void updateUser(String id, String username, String password, String nama, String email){
        try {
            kon.db();
            kon.stat.executeUpdate("update login set"
                    + " password='"+password+"',"
                    + " nama='"+nama+"',"
                    + " email='"+email+"'"
                    + " where id='"+id+"'");
            statusUpdate=true;
        } catch (Exception e) {
            System.out.println(e);
            statusUpdate=false;
        }
    }
    
    public boolean getStatus(){
        return statusUpdate;
    }
    
    
}
