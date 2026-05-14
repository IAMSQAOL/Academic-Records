package Events.Services;

import database.DBConnection;

public class TransportService implements AdditionalService {
    private int eventId;
    private String serviceName;
    private double serviceFee;

    public TransportService(int eventId, String serviceName, double serviceFee) {
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
        DBConnection.execute("INSERT INTO TransportServices(Event_id, Transport_type, Transport_fee) VALUES(?, ?, ?)",
                this.eventId, this.serviceName, this.serviceFee);
    }
    
}
