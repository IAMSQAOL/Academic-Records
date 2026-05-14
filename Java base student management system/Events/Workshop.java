package Events;

public class Workshop extends Event {

    public Workshop(int id, String name, String date, String time, String venue, String organizer_id,
                    int capacity, double fee, String description) {
        super(id, name, date, time, venue, organizer_id, capacity, fee, description);
        this.type = "Workshop";
    }

    public Workshop(String name, String date, String time, String venue, String organizer_id,
                    int capacity, double fee, String description) {
        super(name, date, time, venue, organizer_id, capacity, fee, description);
        this.type = "Workshop";
    } 
}
