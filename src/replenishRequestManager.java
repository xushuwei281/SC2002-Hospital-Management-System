import java.util.Date;
import java.util.Map;

public class replenishRequestManager {
    private Map<String, ReplenishRequest> replenishRequests;

    public replenishRequestManager() {
        this.replenishRequests = new java.util.HashMap<>();
    }

    public void submitReplenishRequest(String requestId, String pharmacistId, String item, int quantity, Date requestDate) {
        ReplenishRequest request = new ReplenishRequest(requestId, pharmacistId, item, quantity, requestDate);
        replenishRequests.put(requestId, request);
        System.out.println("Replenish request submitted successfully by Pharmacist ID: " + pharmacistId);
    }

    public void acceptReplenishRequest(String requestId) {
        ReplenishRequest request = replenishRequests.get(requestId);
        if (request != null) {
            CommonInventory.addItem(request.getItem(), request.getQuantity());
            request.setStatus("approved");

        }
    }

    public void viewReplenishRequests() {
        if (replenishRequests.isEmpty()) {
            System.out.println("No replenish requests found.");
        } else {
            for (ReplenishRequest request : replenishRequests.values()) {
                System.out.println("Request ID: " + request.getRequestId() +
                        ", Pharmacist ID: " + request.getPharmacistId() +
                        ", Item: " + request.getItem() +
                        ", Quantity: " + request.getQuantity() +
                        ", Date: " + request.getFormattedRequestDate() +
                        ", Status: " + request.getStatus());
            }
        }
    }
}
