package grupo11.megastore.sales.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import grupo11.megastore.carts.model.Cart;
import grupo11.megastore.carts.model.repository.CartRepository;
import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.APIException;
import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.model.Variant;
import grupo11.megastore.products.model.repository.VariantRepository;
import grupo11.megastore.sales.constant.SaleStatus;
import grupo11.megastore.sales.dto.ISaleMapper;
import grupo11.megastore.sales.interfaces.ISaleService;
import grupo11.megastore.sales.model.repository.SaleRepository;
import grupo11.megastore.users.model.User;
import grupo11.megastore.users.model.repository.UserRepository;
import grupo11.megastore.sales.dto.sale.CreateSaleDTO;
import grupo11.megastore.sales.dto.sale.SaleDTO;
import grupo11.megastore.sales.dto.sale.ShippingInfoDTO;
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
    private CartRepository cartRepository;

    @Autowired
    private ISaleMapper saleMapper;

    @Override
    public List<SaleDTO> getAllSales() {
        List<Sale> sales = this.saleRepository.findAll();

        List<SaleDTO> dtos = new ArrayList<>();
        sales.forEach(sale -> {
            dtos.add(this.saleMapper.saleToSaleDTO(sale));
        });

        return dtos;
    }

    @Override
    public List<SaleDTO> getSalesByUserId(Long userId) {
        this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", userId));

        List<Sale> sales = this.saleRepository.findByUserId(userId);

        List<SaleDTO> dtos = new ArrayList<>();
        sales.forEach(sale -> {
            dtos.add(this.saleMapper.saleToSaleDTO(sale));
        });

        return dtos;
    }

    @Override
    public SaleDTO getSaleById(Long id) {
        Sale sale = this.saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "id", id));

        return this.saleMapper.saleToSaleDTO(sale);
    }

    @Override
    @Transactional
    public SaleDTO createSale(CreateSaleDTO body) {
        User user = this.userRepository.findById(body.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", body.getUserId()));

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setSaleDate(LocalDateTime.now());
        sale.setPaymentMethod(body.getPaymentMethod());

        ShippingInfoDTO shippingInfo = body.getShippingInfo();
        sale.setShippingCost(body.getShippingCost());
        sale.setShippingMethod(body.getShippingMethod());
        sale.setFullName(shippingInfo.getFullName());
        sale.setAddress(shippingInfo.getAddress());
        sale.setCity(shippingInfo.getCity());
        sale.setState(shippingInfo.getState());
        sale.setPostalCode(shippingInfo.getPostalCode());
        sale.setCountry(shippingInfo.getCountry());

        for (CreateSaleDetailDTO detailDTO : body.getSaleDetails()) {
            int updatedRows = variantRepository.decrementStock(detailDTO.getVariantId(), detailDTO.getQuantity());
            if (updatedRows == 0) {
                throw new APIException("Stock insuficiente para la variante con id " + detailDTO.getVariantId());
            }

            Variant variant = variantRepository.findById(detailDTO.getVariantId())
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

        Cart cart = user.getCart();
        if (cart != null) {
            cart.getCartItems().clear();
            cartRepository.save(cart);
        }

        return this.saleMapper.saleToSaleDTO(savedSale);
    }

    @Override
    @Transactional
    public SaleDTO markSaleAsSent(Long id) {
        Sale sale = this.saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "id", id));

        if (sale.getStatus() != SaleStatus.IN_PROCESS) {
            throw new APIException("Solo las ventas en proceso pueden ser marcadas como enviadas.");
        }

        sale.markAsSent();
        Sale updatedSale = saleRepository.save(sale);
        return saleMapper.saleToSaleDTO(updatedSale);
    }

    @Override
    @Transactional
    public SaleDTO markSaleAsCompleted(Long id) {
        Sale sale = this.saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "id", id));

        if (sale.getStatus() != SaleStatus.SENT) {
            throw new APIException("Solo las ventas enviadas pueden ser marcadas como completadas.");
        }

        sale.markAsCompleted();
        Sale updatedSale = saleRepository.save(sale);
        return saleMapper.saleToSaleDTO(updatedSale);
    }

    @Override
    @Transactional
    public SaleDTO markSaleAsCanceled(Long id) {
        Sale sale = this.saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "id", id));

        if (sale.getStatus() == SaleStatus.CANCELED) {
            throw new APIException("La venta ya se encuentra cancelada.");
        }

        sale.markAsCanceled();
        Sale updatedSale = saleRepository.save(sale);

        for (SaleDetail detail : updatedSale.getSaleDetails()) {
            Variant variant = detail.getVariant();
            variant.setStock(variant.getStock() + detail.getQuantity());
            variantRepository.save(variant);
        }

        return saleMapper.saleToSaleDTO(updatedSale);
    }
}
