package Users;

import database.DBConnection;
import java.sql.*;

public class User {
    
    protected  String user_id;
    protected String password;
    protected String name;
    protected String role;


    public User(String user_id,String password){
        this.user_id = user_id;
        this.password = password;
        this.name = setUserName();
        this.role = setUserRole();
    }

    public boolean login() {
        try {
            return DBConnection.getRS("SELECT * FROM Users WHERE userID = ? AND password = ?", this.user_id,this.password).next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected String setUserName(){
        ResultSet rs = DBConnection.getRS("SELECT name FROM Users WHERE userID = ?", this.user_id);
        try {
            if (rs.next()) {
                String username = rs.getString("name");
                return username;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "User not found";
    }

    protected String setUserRole(){
        ResultSet rs = DBConnection.getRS("SELECT role FROM Users WHERE userID = ?", this.user_id);
        try {
            if (rs.next()) {
                String userRole = rs.getString("role");
                return userRole;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "User Role not found";
    }
    
    public String getUserId(){
        return this.user_id;
    }
    
    public String getUserRole(){
        return this.role;
    }
    
    public String getUsername(){
        return this.name;
    }

    public boolean isStudent(){
        if(this.role == "Student"){
            return true;
        }else{
            return false;
        }
    }

    public boolean isStaff(){
        if(this.role == "Staff"){
            return true;
        }else{
            return false;
        }
    }
}
