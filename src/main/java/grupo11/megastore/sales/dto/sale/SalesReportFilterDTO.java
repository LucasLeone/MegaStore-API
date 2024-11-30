package grupo11.megastore.sales.dto.sale;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class SalesReportFilterDTO {
    private String period; // "daily", "weekly", "monthly"
    private List<Long> brands;
    private List<Long> categories;
    private List<Long> subcategories;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long customerId;
}
