package FeeSystem;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.DBConnection;

public class Bill {
    private int registration_id;
    private String event_name;
    private int applicant_amount;
    private String CateringService;
    private double cateringfee;
    private String TransportService;
    private double transportFee;
    private String discount_type;
    private int discount_amount;
    private double final_fee;
    private double base_fee;
    
    public Bill(int registration_id) {
        this.registration_id = registration_id;
        this.base_fee = getBase_fee();
        this.applicant_amount = getApplicantamount();
        this.CateringService = getCateringService();
        this.cateringfee = getCateringFee();
        this.TransportService = getTransportService();
        this.transportFee = getTransportFee();
        this.discount_type = getDiscount_applied();
        this.discount_amount = getDiscountAmount();
        this.final_fee = 0;
    }


    public String getEventName() {
        ResultSet rs = DBConnection.getRS("SELECT Event_name FROM Events e JOIN Registration r ON r.Event_id = e.Event_id WHERE r.Registration_id = ?", this.registration_id);
        try {
            if(rs.next()) {
                return rs.getString("Event_name");
            } else {
                return "User not registered for the event"; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving event name";
        }
    }

    public int getApplicantamount(){
        ResultSet rs = DBConnection.getRS("SELECT applicant_amount FROM Registration WHERE Registration_id = ?", this.registration_id);
        try {
            if(rs.next()) {
                return rs.getInt("applicant_amount");
            } else {
                return -1; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public double calculateTotalFee() {
        double totalFee = base_fee;
        //System.out.print(base_fee);
        totalFee *= applicant_amount;
        //System.out.print(applicant_amount);
        if (cateringfee > 0) {
            totalFee += applicant_amount*cateringfee;
        }
        if (this.transportFee > 0) {
            totalFee += applicant_amount*transportFee;
        }
        if (this.discount_amount > 0) {
            totalFee -= (totalFee *discount_amount/100.0);
        }
        final_fee = totalFee;
        return final_fee;
    }

    
    public double getCateringFee(){
        ResultSet cater = DBConnection.getRS("Select Catering_fee FROM CateringServices c JOIN Registration r ON r.Event_id = c.Event_id WHERE r.Registration_id = ? AND c.Catering_type = ?", this.registration_id, this.CateringService);
        try {
            if (cater.next()) {
                this.cateringfee = cater.getDouble("Catering_fee");
                return this.cateringfee;
            } else {
                return 0.0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public String getCateringService(){
        ResultSet cs = DBConnection.getRS("SELECT cater_selected FROM Registration WHERE Registration_id = ?", this.registration_id);
        try {
            if(cs.next()){
                this.CateringService = cs.getString("cater_selected");
                return this.CateringService;
            } else{
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public String getTransportService(){
        ResultSet ts = DBConnection.getRS("SELECT transport_selected FROM Registration WHERE Registration_id = ?", this.registration_id);
        try {
            if(ts.next()){
                this.TransportService = ts.getString("transport_selected");
                return this.TransportService;
            } else{
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public String getDiscount_applied(){
        ResultSet ds = DBConnection.getRS("SELECT discount_applied FROM Registration WHERE Registration_id = ?", this.registration_id);
        try {
            if(ds.next()){
                this.discount_type = ds.getString("discount_applied");
                return this.discount_type;
            } else{
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public double getBase_fee(){
        ResultSet bs = DBConnection.getRS("SELECT base_fee FROM Registration WHERE Registration_id = ?", this.registration_id);
        try {
            if(bs.next()){
                this.base_fee = bs.getDouble("base_fee");
                return this.base_fee;
            } else{
                return 0.0;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return 0.0;
        }
    }

    public double getTransportFee() {
        ResultSet transport = DBConnection.getRS("Select Transport_fee FROM TransportServices t JOIN Registration  r ON r.Event_id = t.Event_id WHERE r.Registration_id = ? AND t.Transport_type = ?", this.registration_id, this.TransportService);
        try {
            if (transport.next()) {
                this.transportFee = transport.getDouble("Transport_fee");
                return this.transportFee;
            } else {
                return 0.0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public int getDiscountAmount() {
        ResultSet discount = DBConnection.getRS("SELECT Discount_amount FROM Discounts d JOIN Registration r ON r.Event_id = d.Event_id WHERE r.Registration_id =? AND d.Discount_type = ?", this.registration_id,this.discount_type);
        try {
            if (discount.next()) {
                this.discount_amount = discount.getInt("Discount_amount");
                return this.discount_amount;
            } else {
                return 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0; 
        }
    }
    public double calculateNetTotal(){

        double totalFee = this.base_fee;
        if (cateringfee > 0) {
            totalFee += this.cateringfee;
        }
        if (transportFee > 0) {
            totalFee += transportFee;
        }
        if(applicant_amount > 1){
            totalFee *= applicant_amount;
        }
        final_fee = totalFee;
        return final_fee;
    }

    public void generateBill() {
        double totalFee = calculateTotalFee();
        DBConnection.execute("UPDATE Registration SET final_fee = ? WHERE Registration_id = ?", totalFee, this.registration_id);
    }

    
}
