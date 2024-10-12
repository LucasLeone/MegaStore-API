package grupo11.megastore.products.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.APIException;
import grupo11.megastore.exception.ResourceNotFoundException;
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
    public List<VariantDTO> getAllVariants(Long productId, String color, String size) {
        Specification<Variant> spec = Specification.where(VariantSpecification.hasStatus(EntityStatus.ACTIVE))
                .and(VariantSpecification.hasProductId(productId))
                .and(VariantSpecification.colorContains(color))
                .and(VariantSpecification.sizeContains(size));

        List<Variant> variants = variantRepository.findAll(spec);

        List<VariantDTO> dtos = new ArrayList<>();
        variants.forEach(variant -> {
            dtos.add(variantMapper.variantToVariantDTO(variant));
        });

        return dtos;
    }

    @Override
    public List<VariantDTO> getAllDeletedVariants() {
        List<Variant> variants = this.variantRepository.findByStatus(EntityStatus.DELETED);

        List<VariantDTO> dtos = new ArrayList<>();
        variants.forEach(variant -> {
            dtos.add(this.variantMapper.variantToVariantDTO(variant));
        });

        return dtos;
    }

    @Override
    public VariantDTO getVariantById(Long id) {
        Variant variant = this.variantRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Variante", "id", id));

        return this.variantMapper.variantToVariantDTO(variant);
    }

    @Override
    public VariantDTO createVariant(CreateVariantDTO variant) {
        if ((variant.getColor() != null && (variant.getColor().startsWith(" ") || variant.getColor().endsWith(" "))) ||
                (variant.getSize() != null && (variant.getSize().startsWith(" ") || variant.getSize().endsWith(" ")))) {
            throw new APIException("El color y el tamaño no pueden empezar o terminar con espacios");
        }

        this.variantRepository.findByProductIdAndColorAndSizeAndStatus(
                variant.getProductId(), variant.getColor(), variant.getSize(), EntityStatus.ACTIVE)
                .ifPresent(existingVariant -> {
                    throw new APIException("La variante ya existe");
                });

        this.variantRepository.findByProductIdAndColorAndSizeAndStatus(
                variant.getProductId(), variant.getColor(), variant.getSize(), EntityStatus.DELETED)
                .ifPresent(existingVariant -> {
                    throw new APIException("La variante ya existe pero esta eliminada");
                });

        Product product = this.productRepository.findByIdAndStatus(variant.getProductId(), EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", variant.getProductId()));

        Variant entity = new Variant();
        entity.setProduct(product);
        entity.setColor(variant.getColor());
        entity.setSize(variant.getSize());
        entity.setStock(variant.getStock());
        entity.setImage(variant.getImage());

        entity = this.variantRepository.save(entity);

        return this.variantMapper.variantToVariantDTO(entity);
    }

    @Override
    public VariantDTO updateVariant(Long id, UpdateVariantDTO variant) {
        Variant entity = this.variantRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Variante", "id", id));

        if (variant.isEmpty()) {
            throw new APIException("No se han enviado datos para actualizar");
        }

        if (variant.getColor() != null && (variant.getColor().startsWith(" ") || variant.getColor().endsWith(" "))) {
            throw new APIException("El color no puede empezar o terminar con espacios");
        }

        if (variant.getSize() != null && (variant.getSize().startsWith(" ") || variant.getSize().endsWith(" "))) {
            throw new APIException("El tamaño no puede empezar o terminar con espacios");
        }

        if (variant.getProductId() != null || variant.getColor() != null || variant.getSize() != null) {
            Long productId = variant.getProductId() != null ? variant.getProductId() : entity.getProduct().getId();
            String color = variant.getColor() != null ? variant.getColor() : entity.getColor();
            String size = variant.getSize() != null ? variant.getSize() : entity.getSize();

            this.variantRepository.findByProductIdAndColorAndSizeAndStatusAndIdNot(
                    productId, color, size, EntityStatus.ACTIVE, id)
                    .ifPresent(existingVariant -> {
                        throw new APIException("La variante ya existe");
                    });

            this.variantRepository.findByProductIdAndColorAndSizeAndStatus(
                    variant.getProductId(), variant.getColor(), variant.getSize(), EntityStatus.DELETED)
                    .ifPresent(existingVariant -> {
                        throw new APIException("La variante ya existe pero esta eliminada");
                    });
        }

        if (variant.getProductId() != null) {
            Product product = this.productRepository.findByIdAndStatus(variant.getProductId(), EntityStatus.ACTIVE)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", variant.getProductId()));

            entity.setProduct(product);
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

        return this.variantMapper.variantToVariantDTO(entity);
    }

    @Override
    public void deleteVariant(Long id) {
        Variant entity = this.variantRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Variante", "id", id));

        entity.delete();

        this.variantRepository.save(entity);
    }

    @Override
    public void restoreVariant(Long id) {
        Variant entity = this.variantRepository.findByIdAndStatus(id, EntityStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Variante", "id", id));

        entity.restore();

        this.variantRepository.save(entity);
    }
}
