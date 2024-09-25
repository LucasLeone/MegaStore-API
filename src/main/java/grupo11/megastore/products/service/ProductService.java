package grupo11.megastore.products.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.products.dto.IProductMapper;
import grupo11.megastore.products.dto.product.CreateProductDTO;
import grupo11.megastore.products.dto.product.ProductDTO;
import grupo11.megastore.products.dto.product.UpdateProductDTO;
import grupo11.megastore.products.interfaces.IProductService;
import grupo11.megastore.products.model.Brand;
import grupo11.megastore.products.model.Category;
import grupo11.megastore.products.model.Product;
import grupo11.megastore.products.model.Subcategory;
import grupo11.megastore.products.model.repository.BrandRepository;
import grupo11.megastore.products.model.repository.CategoryRepository;
import grupo11.megastore.products.model.repository.ProductRepository;
import grupo11.megastore.products.model.repository.SubcategoryRepository;

@Service
public class ProductService implements IProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private IProductMapper productMapper;

    @Override
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = this.productRepository.findByStatus(EntityStatus.ACTIVE);

        List<ProductDTO> dtos = new ArrayList<>();
        products.forEach(product -> {
            dtos.add(this.productMapper.productToProductDTO(product));
        });

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductDTO> getProductById(Long id) {
        Optional<Product> product = this.productRepository.findByIdAndStatus(id,EntityStatus.ACTIVE);

        if (!product.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto no existe");
        }

        ProductDTO dto = this.productMapper.productToProductDTO(product.get());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProductDTO> createProduct(CreateProductDTO product) {
        Optional<Product> existingProduct = this.productRepository.findByNameAndStatus(product.getName(), EntityStatus.ACTIVE);

        if (existingProduct.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El producto ya existe");
        }

        Optional<Category> categoryOpt = this.categoryRepository.findByIdAndStatus(product.getCategoryId(), EntityStatus.ACTIVE);
        Optional<Subcategory> subcategoryOpt = this.subcategoryRepository.findByIdAndStatus(product.getSubcategoryId(), EntityStatus.ACTIVE);
        Optional<Brand> brandOpt = this.brandRepository.findByIdAndStatus(product.getBrandId(), EntityStatus.ACTIVE);

        if (!categoryOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoría no existe");
        }

        if (!subcategoryOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La subcategoría no existe");
        }

        if (!brandOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La marca no existe");
        }

        Product entity = new Product();
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setStock(product.getStock());
        entity.setCategory(categoryOpt.get());
        entity.setSubcategory(subcategoryOpt.get());
        entity.setBrand(brandOpt.get());
        entity.setImage(product.getImage());

        entity = this.productRepository.save(entity);

        ProductDTO dto = this.productMapper.productToProductDTO(entity);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ProductDTO> updateProduct(Long id, UpdateProductDTO product) {
        Product entity = this.productMapper.productDTOToProduct(this.getProductById(id).getBody());

        if (product.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se han enviado datos para actualizar");
        }

        if (product.getName() != null) {
            entity.setName(product.getName());
        }

        if (product.getDescription() != null) {
            entity.setDescription(product.getDescription());
        }

        if (product.getPrice() != null) {
            entity.setPrice(product.getPrice());
        }

        if (product.getStock() != null) {
            entity.setStock(product.getStock());
        }

        if (product.getCategoryId() != null) {
            Optional<Category> categoryOpt = this.categoryRepository.findByIdAndStatus(product.getCategoryId(), EntityStatus.ACTIVE);

            if (!categoryOpt.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La categoría no existe");
            }

            entity.setCategory(categoryOpt.get());
        }

        if (product.getSubcategoryId() != null) {
            Optional<Subcategory> subcategoryOpt = this.subcategoryRepository.findByIdAndStatus(product.getSubcategoryId(), EntityStatus.ACTIVE);

            if (!subcategoryOpt.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La subcategoría no existe");
            }

            entity.setSubcategory(subcategoryOpt.get());
        }

        if (product.getBrandId() != null) {
            Optional<Brand> brandOpt = this.brandRepository.findByIdAndStatus(product.getBrandId(), EntityStatus.ACTIVE);

            if (!brandOpt.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La marca no existe");
            }

            entity.setBrand(brandOpt.get());
        }

        if (product.getImage() != null) {
            entity.setImage(product.getImage());
        }

        entity = this.productRepository.save(entity);

        ProductDTO dto = this.productMapper.productToProductDTO(entity);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public void deleteProduct(Long id) {
        Product entity = this.productMapper.productDTOToProduct(this.getProductById(id).getBody());

        if (entity.getStatus() == EntityStatus.DELETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El producto no existe");
        }

        entity.setStatus(EntityStatus.DELETED);

        this.productRepository.save(entity);
    }
}
