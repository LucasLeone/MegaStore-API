package grupo11.megastore.products.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.BadRequestException;
import grupo11.megastore.products.dto.IVariantMapper;
import grupo11.megastore.products.dto.variant.CreateVariantDTO;
import grupo11.megastore.products.dto.variant.UpdateVariantDTO;
import grupo11.megastore.products.dto.variant.VariantDTO;
import grupo11.megastore.products.interfaces.IVariantService;
import grupo11.megastore.products.model.Product;
import grupo11.megastore.products.model.Variant;
import grupo11.megastore.products.model.repository.ProductRepository;
import grupo11.megastore.products.model.repository.VariantRepository;
import grupo11.megastore.products.specification.VariantSpecification;

@Service
public class VariantService implements IVariantService {

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IVariantMapper variantMapper;

    @Override
    public ResponseEntity<List<VariantDTO>> getAllVariants(Long productId, String color, String size) {
        Specification<Variant> spec = Specification.where(VariantSpecification.hasStatus(EntityStatus.ACTIVE))
                .and(VariantSpecification.hasProductId(productId))
                .and(VariantSpecification.colorContains(color))
                .and(VariantSpecification.sizeContains(size));

        List<Variant> variants = variantRepository.findAll(spec);

        List<VariantDTO> dtos = new ArrayList<>();
        variants.forEach(variant -> {
            dtos.add(variantMapper.variantToVariantDTO(variant));
        });

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<VariantDTO> getVariantById(Long id) {
        Optional<Variant> variant = this.variantRepository.findByIdAndStatus(id, EntityStatus.ACTIVE);

        if (!variant.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La variante no existe");
        }

        VariantDTO dto = this.variantMapper.variantToVariantDTO(variant.get());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<VariantDTO> createVariant(CreateVariantDTO variant) {
        Optional<Variant> existingVariant = this.variantRepository.findByProductIdAndColorAndSizeAndStatus(
                variant.getProductId(), variant.getColor(), variant.getSize(), EntityStatus.ACTIVE);

        if (existingVariant.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La variante ya existe");
        }

        Optional<Product> productOpt = this.productRepository.findByIdAndStatus(variant.getProductId(),
                EntityStatus.ACTIVE);

        if (!productOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto no existe");
        }

        Variant entity = new Variant();
        entity.setProduct(productOpt.get());
        entity.setColor(variant.getColor());
        entity.setSize(variant.getSize());
        entity.setStock(variant.getStock());
        entity.setImage(variant.getImage());

        entity = this.variantRepository.save(entity);

        VariantDTO dto = this.variantMapper.variantToVariantDTO(entity);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<VariantDTO> updateVariant(Long id, UpdateVariantDTO variant) {
        Variant entity = this.variantMapper.variantDTOToVariant(this.getVariantById(id).getBody());

        if (variant.isEmpty()) {
            throw new BadRequestException("No se han enviado datos para actualizar");
        }

        Optional<Variant> existingVariant = this.variantRepository.findByProductIdAndColorAndSizeAndStatusAndIdNot(
                variant.getProductId(), variant.getColor(), variant.getSize(), EntityStatus.ACTIVE, id);

        if (existingVariant.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La variante ya existe");
        }

        if (variant.getProductId() != null) {
            Optional<Product> productOpt = this.productRepository.findByIdAndStatus(variant.getProductId(),
                    EntityStatus.ACTIVE);

            if (!productOpt.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto no existe");
            }

            entity.setProduct(productOpt.get());
        }

        if (variant.getColor() != null) {
            entity.setColor(variant.getColor());
        }

        if (variant.getSize() != null) {
            entity.setSize(variant.getSize());
        }

        if (variant.getStock() != null) {
            entity.setStock(variant.getStock());
        }

        if (variant.getImage() != null) {
            entity.setImage(variant.getImage());
        }

        entity = this.variantRepository.save(entity);

        VariantDTO dto = this.variantMapper.variantToVariantDTO(entity);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public void deleteVariant(Long id) {
        Variant entity = this.variantMapper.variantDTOToVariant(this.getVariantById(id).getBody());

        if (entity.getStatus() == EntityStatus.DELETED) {
            throw new BadRequestException("La variante no existe");
        }

        entity.setStatus(EntityStatus.DELETED);

        this.variantRepository.save(entity);
    }
}
