public class PrescribedMedication {
    public String medicationName;
    public int units;
    public String status;
    public int lowStockAlertLevel;


    public PrescribedMedication(String medicationName,int units) {
        this.medicationName = medicationName;
        this.units = units;
        this.status = "pending";
    }

    public PrescribedMedication(String name, int units, int lowStockAlertLevel) {
        this.medicationName = name;
        this.units = units;
        this.lowStockAlertLevel = lowStockAlertLevel;
    }

    public String getMedicationName() {
        return this.medicationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        if (units < 0) {
            throw new IllegalArgumentException("Units cannot be negative");
        }
        this.units = units;
    }

    public boolean isLowStock() {
        return units <= lowStockAlertLevel;
    }

    public int getLowStockAlert() {
        return lowStockAlertLevel;
    }

    @Override
    public String toString() {
        return "PrescribedMedication [name=" + medicationName + ", units=" + units + "]";
    }
}
