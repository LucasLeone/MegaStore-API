package grupo11.megastore.sales.constant;

public enum SaleStatus {
    IN_PROCESS("En Proceso"),
    SENT("Enviado"),
    COMPLETED("Completado"),
    CANCELED("Cancelado");

    private final String description;

    SaleStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
