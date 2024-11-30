package grupo11.megastore.sales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import grupo11.megastore.sales.interfaces.ISaleService;
import jakarta.validation.Valid;
import grupo11.megastore.sales.dto.sale.CreateSaleDTO;
import grupo11.megastore.sales.dto.sale.SaleDTO;

import java.util.List;

@RestController
@RequestMapping(path = "/sales")
public class SaleController {

    @Autowired
    private ISaleService saleService;

    @GetMapping
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        List<SaleDTO> sales = this.saleService.getAllSales();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/{id}/my-sales")
    public ResponseEntity<List<SaleDTO>> getMySales(@PathVariable Long id) {
        List<SaleDTO> sales = this.saleService.getSalesByUserId(id);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SaleDTO> createSale(@Valid @RequestBody CreateSaleDTO body) {
        SaleDTO sale = this.saleService.createSale(body);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDTO> getSaleById(@PathVariable Long id) {
        SaleDTO sale = this.saleService.getSaleById(id);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @PostMapping("/{id}/sent")
    public ResponseEntity<SaleDTO> markSaleAsSent(@PathVariable Long id) {
        SaleDTO sale = this.saleService.markSaleAsSent(id);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @PostMapping("/{id}/completed")
    public ResponseEntity<SaleDTO> markSaleAsCompleted(@PathVariable Long id) {
        SaleDTO sale = this.saleService.markSaleAsCompleted(id);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @PostMapping("/{id}/canceled")
    public ResponseEntity<SaleDTO> markSaleAsCanceled(@PathVariable Long id) {
        SaleDTO sale = this.saleService.markSaleAsCanceled(id);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }
}
