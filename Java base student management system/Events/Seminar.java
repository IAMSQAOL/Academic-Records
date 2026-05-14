package Events;

public class Seminar extends Event {

    public Seminar(int id, String name, String date, String time, String venue, String organizer_id,
                   int capacity, double fee, String description) {
        super(id, name, date, time, venue, organizer_id, capacity, fee, description);
        this.type = "Seminar";
    }

    public Seminar(String name, String date, String time, String venue, String organizer_id,
                   int capacity, double fee, String description) {
        super(name, date, time, venue, organizer_id, capacity, fee, description);
        this.type = "Seminar";
    }
    
}
