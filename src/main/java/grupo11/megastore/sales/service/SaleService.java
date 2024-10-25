package grupo11.megastore.sales.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.model.Variant;
import grupo11.megastore.products.model.repository.VariantRepository;
import grupo11.megastore.sales.dto.ISaleMapper;
import grupo11.megastore.sales.interfaces.ISaleService;
import grupo11.megastore.sales.model.repository.SaleRepository;
import grupo11.megastore.users.model.User;
import grupo11.megastore.users.model.repository.UserRepository;
import grupo11.megastore.sales.dto.sale.CreateSaleDTO;
import grupo11.megastore.sales.dto.sale.SaleDTO;
import grupo11.megastore.sales.dto.saleDetail.CreateSaleDetailDTO;
import grupo11.megastore.sales.model.Sale;
import grupo11.megastore.sales.model.SaleDetail;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
public class SaleService implements ISaleService {
    
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private ISaleMapper saleMapper;

    @Override
    public List<SaleDTO> getAllSales() {
        List<Sale> sales = this.saleRepository.findByStatus(EntityStatus.ACTIVE);

        List<SaleDTO> dtos = new ArrayList<>();
        sales.forEach(sale -> {
            dtos.add(this.saleMapper.saleToSaleDTO(sale));
        });

        return dtos;
    }

    @Override
    public SaleDTO getSaleById(Long id) {
        Sale sale = this.saleRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "id", id));

        return this.saleMapper.saleToSaleDTO(sale);
    }

    @Override
    public SaleDTO createSale(CreateSaleDTO body) {
        User user = this.userRepository.findById(body.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", body.getUserId()));

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setSaleDate(LocalDateTime.now());
        sale.setPaymentMethod(body.getPaymentMethod());

        for (CreateSaleDetailDTO detailDTO : body.getSaleDetails()) {
            Variant variant = this.variantRepository.findById(detailDTO.getVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", detailDTO.getVariantId()));

            SaleDetail saleDetail = new SaleDetail();
            saleDetail.setVariant(variant);

            saleDetail.setQuantity(detailDTO.getQuantity());

            BigDecimal unitPrice = BigDecimal.valueOf(variant.getProduct().getPrice());
            saleDetail.setUnitPrice(unitPrice);

            sale.addSaleDetail(saleDetail);
        }

        sale.calculateTotalAmount();

        Sale savedSale = this.saleRepository.save(sale);

        return this.saleMapper.saleToSaleDTO(savedSale);
    }

    @Override
    public void deleteSale(Long id) {
        Sale entity = this.saleRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "id", id));

        entity.delete();

        this.saleRepository.save(entity);
    }

    @Override
    public void restoreSale(Long id) {
        Sale entity = this.saleRepository.findByIdAndStatus(id, EntityStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "id", id));

        entity.restore();

        this.saleRepository.save(entity);
    }
}
