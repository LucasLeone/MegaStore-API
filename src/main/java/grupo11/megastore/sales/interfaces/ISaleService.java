package grupo11.megastore.sales.interfaces;

import grupo11.megastore.sales.dto.sale.CreateSaleDTO;
import grupo11.megastore.sales.dto.sale.SaleDTO;

import java.util.List;

public interface ISaleService {
    List<SaleDTO> getAllSales();
    SaleDTO getSaleById(Long id);
    SaleDTO createSale(CreateSaleDTO body);
    void deleteSale(Long id);
    void restoreSale(Long id);
}
