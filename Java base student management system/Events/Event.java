package Events;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.DBConnection;

public abstract class Event {
    protected int id;
    protected String name;
    protected String type;
    protected String date;
    protected String time;
    protected String venue;
    protected String organizer_id;
    protected int capacity;
    protected double fee;
    protected String description;

    public Event(int id, String name,String date, String time, String venue, String organizer_id,
                 int capacity, double fee, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.organizer_id = organizer_id;
        this.capacity = capacity;
        this.fee = fee;
        this.description = description;
    }

    // Constructor for creating a new event without an ID
    public Event(String name, String date, String time, String venue, String organizer_id,
                 int capacity, double fee, String description) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.organizer_id = organizer_id;
        this.capacity = capacity;
        this.fee = fee;
        this.description = description;
    }

    public void createEvent() {
        DBConnection.execute("INSERT INTO Events(Event_name,Event_creator_id,Event_type,Event_date,Event_time,Venue,Capacity,Description,Fee) " + 
        "VALUES(?,?,?,?,?,?,?,?,?)", 
        this.name, this.organizer_id, this.type, this.date, this.time, this.venue, this.capacity, this.description,this.fee);
    }

    public void updateEvent() {
        DBConnection.execute("UPDATE Events SET Event_name = ?, Event_type = ?, Event_date = ?, Event_time = ?, Venue = ?, Capacity = ?, Description = ? , Fee = ? WHERE Event_id = ?",
                this.name, this.type, this.date, this.time, this.venue, this.capacity, this.description,this.fee,this.id);
    }

    public static void deleteEvent(int event_id) {
        DBConnection.execute("DELETE FROM Events WHERE Event_id = ?", event_id);
    }

    public static int getNewestId() {
        String tableName = "Events";
        ResultSet rs = DBConnection.getRS("SELECT seq + 1 AS next_id FROM sqlite_sequence WHERE name = ?;", tableName);
        try {
            if(rs.next()) {
                return rs.getInt("next_id");
            } else {
                return -1; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
    }

    public static String getcreatorName(int event_id){
        ResultSet rs = DBConnection.getRS("SELECT name FROM Users u JOIN Events e ON u.userID = e.Event_creator_id WHERE Event_id = ?", event_id);
        try{
            if(rs.next()){
                return rs.getString("name");
            }else{
                return "Unknown creator";
            }
        }catch(SQLException e){
            e.printStackTrace();
            return "Unknown";
        }
    }
    
    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getType(){
        return this.type;
    }

    public String getDate(){
        return this.date;
    }

    public String getTime(){
        return this.time;
    }

    public String getVenue(){
        return this.venue;
    }

    public int getCapacity(){
        return this.capacity;
    }

    public double getFee(){
        return this.fee;
    }

    public String getDescription(){
        return this.description;
    }

    public void setName(String evtname){
        this.name = evtname;
    }

    public void setType(String evtType){
        this.type = evtType;
    }

    public void setDate(String evtDate){
        this.date = evtDate;
    }

    public void setTime(String evtTime){
        this.time = evtTime;
    }

    public void setVenue(String evtVenue){
        this.venue = evtVenue;
    }

    public void setCapacity(int newCapacity){
        this.capacity = newCapacity;
    }

    public void setFee(Double newFee){
        this.fee = newFee;
    }

    public void setDescription(String desc){
        this.description = desc;
    }

}
