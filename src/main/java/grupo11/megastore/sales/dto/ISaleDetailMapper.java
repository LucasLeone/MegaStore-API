package grupo11.megastore.sales.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import grupo11.megastore.sales.dto.saleDetail.SaleDetailDTO;
import grupo11.megastore.sales.model.SaleDetail;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface ISaleDetailMapper {
    SaleDetailDTO saleDetailToSaleDetailDTO(SaleDetail saleDetail);
    SaleDetail saleDetailDTOToSaleDetail(SaleDetailDTO saleDetailDTO);
}
