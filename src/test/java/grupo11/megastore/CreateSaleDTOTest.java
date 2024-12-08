package grupo11.megastore;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import grupo11.megastore.sales.dto.sale.CreateSaleDTO;
import grupo11.megastore.sales.dto.sale.ShippingInfoDTO;
import grupo11.megastore.sales.dto.saleDetail.CreateSaleDetailDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CreateSaleDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // Tests 1.3.1
    @Test
    void testTarjetaCreditoYEnvioEstandar() {
        CreateSaleDTO dto = new CreateSaleDTO();
        dto.setPaymentMethod("Tarjeta de crédito");
        dto.setShippingMethod("Envío estándar");
        dto.setUserId(1L);
        dto.setShippingCost(new BigDecimal("10.00"));
        
        CreateSaleDetailDTO saleDetail1 = new CreateSaleDetailDTO();
        saleDetail1.setVariantId(100L);
        saleDetail1.setQuantity(2);
        
        CreateSaleDetailDTO saleDetail2 = new CreateSaleDetailDTO();
        saleDetail2.setVariantId(101L);
        saleDetail2.setQuantity(1);
        
        dto.setSaleDetails(Arrays.asList(saleDetail1, saleDetail2));
        
        ShippingInfoDTO shippingInfo = new ShippingInfoDTO();
        shippingInfo.setFullName("Juan Pérez");
        shippingInfo.setAddress("Calle Falsa 123");
        shippingInfo.setCity("Ciudad");
        shippingInfo.setState("Provincia");
        shippingInfo.setPostalCode("12345");
        shippingInfo.setCountry("País");
        
        dto.setShippingInfo(shippingInfo);
        
        Set<ConstraintViolation<CreateSaleDTO>> violations = validator.validate(dto);
        
        assertTrue(violations.isEmpty(), 
            "No debería haber violaciones para combinación válida: Tarjeta de crédito + Envío estándar");
    }

    @Test
    void testPayPalYEnvioExpress() {
        CreateSaleDTO dto = new CreateSaleDTO();
        dto.setPaymentMethod("PayPal");
        dto.setShippingMethod("Envío express");
        dto.setUserId(2L);
        dto.setShippingCost(new BigDecimal("15.00"));
        
        CreateSaleDetailDTO saleDetail1 = new CreateSaleDetailDTO();
        saleDetail1.setVariantId(102L);
        saleDetail1.setQuantity(3);
        
        dto.setSaleDetails(Arrays.asList(saleDetail1));
        
        ShippingInfoDTO shippingInfo = new ShippingInfoDTO();
        shippingInfo.setFullName("María López");
        shippingInfo.setAddress("Avenida Siempre Viva 742");
        shippingInfo.setCity("Springfield");
        shippingInfo.setState("Estado");
        shippingInfo.setPostalCode("54321");
        shippingInfo.setCountry("País");
        
        dto.setShippingInfo(shippingInfo);
        
        Set<ConstraintViolation<CreateSaleDTO>> violations = validator.validate(dto);
        
        assertTrue(violations.isEmpty(), 
            "No debería haber violaciones para combinación válida: PayPal + Envío express");
    }

    @Test
    void testTransferenciaBancariaYEnvioEstandar() {
        CreateSaleDTO dto = new CreateSaleDTO();
        dto.setPaymentMethod("Transferencia bancaria");
        dto.setShippingMethod("Envío estándar");
        dto.setUserId(3L);
        dto.setShippingCost(new BigDecimal("10.00"));
        
        CreateSaleDetailDTO saleDetail1 = new CreateSaleDetailDTO();
        saleDetail1.setVariantId(103L);
        saleDetail1.setQuantity(1);
        
        CreateSaleDetailDTO saleDetail2 = new CreateSaleDetailDTO();
        saleDetail2.setVariantId(104L);
        saleDetail2.setQuantity(4);
        
        dto.setSaleDetails(Arrays.asList(saleDetail1, saleDetail2));
        
        ShippingInfoDTO shippingInfo = new ShippingInfoDTO();
        shippingInfo.setFullName("Carlos García");
        shippingInfo.setAddress("Boulevard de los Sueños Rotos 456");
        shippingInfo.setCity("Metropolis");
        shippingInfo.setState("Región");
        shippingInfo.setPostalCode("67890");
        shippingInfo.setCountry("País");
        
        dto.setShippingInfo(shippingInfo);
        
        Set<ConstraintViolation<CreateSaleDTO>> violations = validator.validate(dto);
        
        assertTrue(violations.isEmpty(), 
            "No debería haber violaciones para combinación válida: Transferencia bancaria + Envío estándar");
    }
}
