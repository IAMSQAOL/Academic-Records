package Events.Discounts;

import database.DBConnection;

public class IndividualDiscount implements Discount {
    private int eventId;
    private double discountAmount;
    private String discountType;

    public IndividualDiscount(int eventId, double discountAmount) {
        this.eventId = eventId;
        this.discountAmount = discountAmount;
        this.discountType = "Individual";
    }

    @Override
    public int getEventId() {
        return eventId;
    }

    @Override
    public String getDiscountType() {
        return discountType;
    }

    @Override
    public double getDiscountAmount() {
        return discountAmount;
    }

    @Override
    public void insertDiscount() {
        DBConnection.execute("INSERT INTO Discounts(Event_id, discount_type, discount_amount) VALUES(?, ?, ?)",
                this.eventId, this.discountType, this.discountAmount);
    }
    
}
