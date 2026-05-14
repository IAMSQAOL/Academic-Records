package Events.Discounts;

public interface Discount {

    public int getEventId();
    public String getDiscountType();
    public double getDiscountAmount();
    public void insertDiscount();

}

