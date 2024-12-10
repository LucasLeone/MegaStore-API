package grupo11.megastore.sales.dto.sale;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SalesReportFilterDTO {
    private String period; // "daily", "weekly", "monthly", "yearly"
    private Long brand;
    private Long category;
    private Long subcategory;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long customerId;
}
