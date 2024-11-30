package grupo11.megastore.sales.interfaces;

import grupo11.megastore.sales.dto.sale.CreateSaleDTO;
import grupo11.megastore.sales.dto.sale.SaleDTO;

import java.util.List;

public interface ISaleService {
    List<SaleDTO> getAllSales();
    List<SaleDTO> getSalesByUserId(Long userId);
    SaleDTO getSaleById(Long id);
    SaleDTO createSale(CreateSaleDTO body);
    SaleDTO markSaleAsSent(Long id);
    SaleDTO markSaleAsCompleted(Long id);
    SaleDTO markSaleAsCanceled(Long id);
}
