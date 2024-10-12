package grupo11.megastore.products.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.APIException;
import grupo11.megastore.exception.ResourceNotFoundException;
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
import grupo11.megastore.products.specification.ProductSpecification;

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
    public List<ProductDTO> getAllProducts(Long categoryId, Long subcategoryId, Long brandId, String name) {
        Specification<Product> spec = Specification.where(ProductSpecification.hasStatus(EntityStatus.ACTIVE))
                .and(ProductSpecification.hasCategoryId(categoryId))
                .and(ProductSpecification.hasSubcategoryId(subcategoryId))
                .and(ProductSpecification.hasBrandId(brandId))
                .and(ProductSpecification.nameContains(name));

        List<Product> products = productRepository.findAll(spec);

        List<ProductDTO> dtos = new ArrayList<>();
        products.forEach(product -> {
            dtos.add(productMapper.productToProductDTO(product));
        });

        return dtos;
    }

    @Override
    public List<ProductDTO> getAllDeletedProducts() {
        List<Product> products = this.productRepository.findByStatus(EntityStatus.DELETED);

        List<ProductDTO> dtos = new ArrayList<>();
        products.forEach(product -> {
            dtos.add(productMapper.productToProductDTO(product));
        });

        return dtos;
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = this.productRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        return this.productMapper.productToProductDTO(product);
    }

    @Override
    public ProductDTO createProduct(CreateProductDTO product) {
        if (product.getName() != null && (product.getName().startsWith(" ") || product.getName().endsWith(" "))) {
            throw new APIException("El nombre del producto no puede empezar o terminar con espacios");
        }

        this.productRepository.findByNameIgnoreCaseAndStatus(product.getName(), EntityStatus.ACTIVE)
                .ifPresent(existingProduct -> {
                    throw new APIException("El producto ya existe");
                });

        this.productRepository.findByNameIgnoreCaseAndStatus(product.getName(), EntityStatus.DELETED)
                .ifPresent(existingProduct -> {
                    throw new APIException("El producto ya existe pero esta eliminado");
                });

        Category category = this.categoryRepository.findByIdAndStatus(product.getCategoryId(), EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", product.getCategoryId()));

        Subcategory subcategory = this.subcategoryRepository
                .findByIdAndStatus(product.getSubcategoryId(), EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategoría", "id", product.getSubcategoryId()));

        Brand brand = this.brandRepository.findByIdAndStatus(product.getBrandId(), EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", "id", product.getBrandId()));

        Product entity = new Product();
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setCategory(category);
        entity.setSubcategory(subcategory);
        entity.setBrand(brand);

        entity = this.productRepository.save(entity);

        return this.productMapper.productToProductDTO(entity);
    }

    @Override
    public ProductDTO updateProduct(Long id, UpdateProductDTO product) {
        Product entity = this.productRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        if (product.isEmpty()) {
            throw new APIException("No se han enviado datos para actualizar");
        }

        if (product.getName() != null) {
            if (product.getName().startsWith(" ") || product.getName().endsWith(" ")) {
                throw new APIException("El nombre del producto no puede empezar o terminar con espacios");
            }

            this.productRepository.findByNameIgnoreCaseAndStatusAndIdNot(product.getName(), EntityStatus.ACTIVE, id)
                    .ifPresent(existingProduct -> {
                        throw new APIException("El producto ya existe");
                    });

            this.productRepository.findByNameIgnoreCaseAndStatus(product.getName(), EntityStatus.DELETED)
                    .ifPresent(existingProduct -> {
                        throw new APIException("El producto ya existe pero esta eliminado");
                    });

            entity.setName(product.getName());
        }

        if (product.getDescription() != null) {
            entity.setDescription(product.getDescription());
        }

        if (product.getPrice() != null) {
            entity.setPrice(product.getPrice());
        }

        if (product.getCategoryId() != null) {
            Category category = this.categoryRepository.findByIdAndStatus(product.getCategoryId(), EntityStatus.ACTIVE)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría", "id", product.getCategoryId()));

            entity.setCategory(category);
        }

        if (product.getSubcategoryId() != null) {
            Subcategory subcategory = this.subcategoryRepository
                    .findByIdAndStatus(product.getSubcategoryId(), EntityStatus.ACTIVE)
                    .orElseThrow(() -> new ResourceNotFoundException("Subcategoría", "id", product.getSubcategoryId()));

            entity.setSubcategory(subcategory);
        }

        if (product.getBrandId() != null) {
            Brand brand = this.brandRepository.findByIdAndStatus(product.getBrandId(), EntityStatus.ACTIVE)
                    .orElseThrow(() -> new ResourceNotFoundException("Marca", "id", product.getBrandId()));

            entity.setBrand(brand);
        }

        entity = this.productRepository.save(entity);

        return this.productMapper.productToProductDTO(entity);
    }

    @Override
    public void deleteProduct(Long id) {
        Product entity = this.productRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        entity.delete();

        this.productRepository.save(entity);
    }

    @Override
    public void restoreProduct(Long id) {
        Product entity = this.productRepository.findByIdAndStatus(id, EntityStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));

        entity.restore();

        this.productRepository.save(entity);
    }
}
