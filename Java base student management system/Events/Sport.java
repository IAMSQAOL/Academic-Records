package Events;

public class Sport extends Event {

    public Sport(int id, String name, String date, String time, String venue, String organizer_id,
                 int capacity, double fee, String description) {
        super(id, name, date, time, venue, organizer_id, capacity, fee, description);
        this.type = "Sport";
    }

    public Sport(String name, String date, String time, String venue, String organizer_id,
                 int capacity, double fee, String description) {
        super(name, date, time, venue, organizer_id, capacity, fee, description);
        this.type = "Sport";
    }
    
}
