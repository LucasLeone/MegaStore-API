package grupo11.megastore.sales.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import grupo11.megastore.sales.dto.sale.SaleDTO;
import grupo11.megastore.sales.model.Sale;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ISaleMapper {
    SaleDTO saleToSaleDTO(Sale sale);
    Sale saleDTOToSale(SaleDTO saleDTO);
}
