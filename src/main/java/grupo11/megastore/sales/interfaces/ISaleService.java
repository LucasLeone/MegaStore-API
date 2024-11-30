package grupo11.megastore.sales.interfaces;

import grupo11.megastore.sales.dto.sale.CreateSaleDTO;
import grupo11.megastore.sales.dto.sale.SaleDTO;
import grupo11.megastore.sales.dto.sale.SalesReportFilterDTO;

import java.util.List;
import java.util.Map;

public interface ISaleService {
    List<SaleDTO> getAllSales();
    List<SaleDTO> getSalesByUserId(Long userId);
    SaleDTO getSaleById(Long id);
    SaleDTO createSale(CreateSaleDTO body);
    SaleDTO markSaleAsSent(Long id);
    SaleDTO markSaleAsCompleted(Long id);
    SaleDTO markSaleAsCanceled(Long id);
    Map<String, Object> generateSalesReport(SalesReportFilterDTO filterDTO);
    Map<String, Object> getCustomerStatistics();
}
