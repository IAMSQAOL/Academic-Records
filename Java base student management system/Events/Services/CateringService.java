package Events.Services;

import database.DBConnection;

public class CateringService implements AdditionalService {
    private int eventId;
    private String serviceName;
    private double serviceFee;

    public CateringService(int eventId, String serviceName, double serviceFee) {
        this.eventId = eventId;
        this.serviceName = serviceName;
        this.serviceFee = serviceFee;
    }

    @Override
    public int getEventId() {
        return eventId;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public double getServiceFee() {
        return serviceFee;
    }

    @Override
    public void insertService() {
        DBConnection.execute("INSERT INTO CateringServices(Event_id, Catering_type, Catering_fee) VALUES(?, ?, ?)",
                this.eventId, this.serviceName, this.serviceFee);
    }
}