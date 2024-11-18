import java.util.*;
import java.text.SimpleDateFormat;

class ReplenishRequest {
    private String requestId;
    private String pharmacistId;
    private String item;
    private int quantity;
    private String status;
    private Date requestDate;

    public ReplenishRequest(String requestId, String pharmacistId, String item, int quantity, Date requestDate) {
        this.requestId = requestId;
        this.pharmacistId = pharmacistId;
        this.item = item;
        this.quantity = quantity;
        this.requestDate = requestDate;
        this.status = "pending";
    }

    public String getRequestId() {
        return requestId;
    }

    public String getPharmacistId() {
        return pharmacistId;
    }

    public String getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public String getFormattedRequestDate() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        return dateFormatter.format(requestDate);
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

