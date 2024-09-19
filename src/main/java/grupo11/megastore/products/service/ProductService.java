package grupo11.megastore.products.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import grupo11.megastore.products.dto.ProductDTO;
import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.mapper.ProductMapper;
import grupo11.megastore.products.model.Brand;
import grupo11.megastore.products.model.Category;
import grupo11.megastore.products.model.Product;
import grupo11.megastore.products.model.Subcategory;
import grupo11.megastore.products.repository.BrandRepository;
import grupo11.megastore.products.repository.CategoryRepository;
import grupo11.megastore.products.repository.ProductRepository;
import grupo11.megastore.products.repository.SubcategoryRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Base64;

@Service
@Transactional
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
    private ProductMapper productMapper;

    @Override
    public List<ProductDTO> listAll() {
        List<Product> products = productRepository.findByStatus(Product.Status.ACTIVE);
        return products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getById(Integer id) {
        Product product = productRepository.findById(id)
                .filter(p -> p.getStatus() == Product.Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found."));
        return productMapper.toDTO(product);
    }

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        if (productRepository.findByName(productDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Product with name " + productDTO.getName() + " already exists.");
        }

        Product product = productMapper.toEntity(productDTO);

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + productDTO.getCategoryId()));
        Subcategory subcategory = subcategoryRepository.findById(productDTO.getSubcategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found with id " + productDTO.getSubcategoryId()));
        Brand brand = brandRepository.findById(productDTO.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id " + productDTO.getBrandId()));

        product.setCategory(category);
        product.setSubcategory(subcategory);
        product.setBrand(brand);

        product.setStatus(Product.Status.ACTIVE);

        Product savedProduct = productRepository.save(product);

        return productMapper.toDTO(savedProduct);
    }

    @Override
    public ProductDTO update(ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productDTO.getId() + " not found."));
        
        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setStock(productDTO.getStock());

        if (!existingProduct.getCategory().getId().equals(productDTO.getCategoryId())) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + productDTO.getCategoryId()));
            existingProduct.setCategory(category);
        }

        if (!existingProduct.getSubcategory().getId().equals(productDTO.getSubcategoryId())) {
            Subcategory subcategory = subcategoryRepository.findById(productDTO.getSubcategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subcategory not found with id " + productDTO.getSubcategoryId()));
            existingProduct.setSubcategory(subcategory);
        }

        if (!existingProduct.getBrand().getId().equals(productDTO.getBrandId())) {
            Brand brand = brandRepository.findById(productDTO.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id " + productDTO.getBrandId()));
            existingProduct.setBrand(brand);
        }

        if (productDTO.getStatus() != null) {
            existingProduct.setStatus(Product.Status.valueOf(productDTO.getStatus()));
        }

        if (productDTO.getImageBase64() != null && !productDTO.getImageBase64().isEmpty()) {
            existingProduct.setImage(Base64.getDecoder().decode(productDTO.getImageBase64()));
        }

        Product updatedProduct = productRepository.save(existingProduct);

        return productMapper.toDTO(updatedProduct);
    }

    @Override
    public void delete(Integer id) {
        Product product = productRepository.findById(id)
                .filter(p -> p.getStatus() == Product.Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found."));
        product.markAsDeleted();
        productRepository.save(product);
    }

    @Override
    public boolean isNameExists(String name) {
        return productRepository.findByName(name).isPresent();
    }
}
