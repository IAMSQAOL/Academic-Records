package Registrations;

import java.sql.*;
import java.util.Random;

import database.DBConnection;

public class Registration {
    private int registration_id;
    private int event_id;
    private String user_id;
    private int register_amount;
    private String fullname;
    private String email;
    private String phone;
    private String cater_selected;
    private String transport_selected;
    private String discount_type;
    private double event_basefee;

    public Registration(int registration_id, int event_id, String user_id, String fullname, String email, String phone,int register_amount) {
        this.registration_id = registration_id;
        this.event_id = event_id;
        this.user_id = user_id;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.register_amount = register_amount;
        this.cater_selected = null;
        this.transport_selected = null;
        this.discount_type = null;
        this.event_basefee = 0.0;
    }

    public void setCaterSelected(String cater_selected) {
        this.cater_selected = cater_selected;
    }
    public void setTransportSelected(String transport_selected) {
        this.transport_selected = transport_selected;
    }
    public void setDiscountType(String discount_type) {
        this.discount_type = discount_type;
    }
    public void setBasefee(double base_fee){
        this.event_basefee = base_fee;
    }
    
    public void registerForEvent() {
        int registrationId = this.registration_id;
        DBConnection.execute("INSERT INTO Registration (Registration_id,Event_id, userID,FullName,Email,Phone,applicant_amount, cater_selected, transport_selected,discount_applied,base_fee)" 
        + "VALUES(?,?,?,?,?,?,?,?,?,?,?)", registrationId, this.event_id, this.user_id,this.fullname,this.email,this.phone,this.register_amount,this.cater_selected,this.transport_selected, this.discount_type,this.event_basefee);
    }

    public void unregisterFromEvent() {
        DBConnection.execute("DELETE FROM Registration WHERE Event_id = ? AND User_id = ?", this.event_id, this.user_id);
    }

    public static ResultSet getuserRegisterEvent(String userid){
        return DBConnection.getRS("SELECT * FROM Events WHERE Event_id IN (SELECT Event_id FROM Registration WHERE userID = ?)", userid);
    }

    public boolean isRegistered() {
        try {
            return DBConnection.getRS("SELECT * FROM Registration WHERE Event_id = ? AND User_id = ?", this.event_id, this.user_id).next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int generateUniqueRegistrationId() {
        Random random = new Random();
        int id;
        while (true) {
            id = 100000 + random.nextInt(900000); 
            if (!isRegistrationIdExists(id)) {
                break;
            }
        }
        return id;
    }

    public static boolean isRegistrationIdExists(int id) {
        try {
            ResultSet rs = DBConnection.getRS("SELECT 1 FROM Registration WHERE Registration_id = ?", id);
            return rs.next(); 
        } catch (Exception e) {
            System.out.println("Regenerate ID again");
            return true; 
        }
    }
    
    public static int getRegisteredID(int event_id,String user_id){
        try{
            ResultSet rs = DBConnection.getRS("SELECT Registration_id FROM Registration WHERE Event_id = ? AND userID =?",event_id,user_id);
            return rs.getInt("Registration_id");
        }catch(SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

}
