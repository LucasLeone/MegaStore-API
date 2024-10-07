package grupo11.megastore.products.service;

import grupo11.megastore.constant.EntityStatus;
import grupo11.megastore.exception.APIException;
import grupo11.megastore.exception.ResourceNotFoundException;
import grupo11.megastore.products.dto.brand.BrandDTO;
import grupo11.megastore.products.dto.brand.CreateBrandDTO;
import grupo11.megastore.products.dto.brand.UpdateBrandDTO;
import grupo11.megastore.products.interfaces.IBrandService;
import grupo11.megastore.products.dto.IBrandMapper;
import grupo11.megastore.products.model.Brand;
import grupo11.megastore.products.model.repository.BrandRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class BrandService implements IBrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private IBrandMapper brandMapper;

    @Override
    public List<BrandDTO> getAllBrands() {
        List<Brand> brands = this.brandRepository.findByStatus(EntityStatus.ACTIVE);

        List<BrandDTO> dtos = new ArrayList<>();
        brands.forEach(brand -> {
            dtos.add(this.brandMapper.brandToBrandDTO(brand));
        });

        return dtos;
    }

    @Override
    public List<BrandDTO> getAllDeletedBrands() {
        List<Brand> brands = this.brandRepository.findByStatus(EntityStatus.DELETED);

        List<BrandDTO> dtos = new ArrayList<>();
        brands.forEach(brand -> {
            dtos.add(this.brandMapper.brandToBrandDTO(brand));
        });

        return dtos;
    }

    @Override
    public BrandDTO getBrandById(Long id) {
        Brand brand = this.brandRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", "id", id));

        return this.brandMapper.brandToBrandDTO(brand);
    }

    @Override
    public BrandDTO createBrand(CreateBrandDTO brand) {
        if (brand.getName() != null && (brand.getName().startsWith(" ") || brand.getName().endsWith(" "))) {
            throw new APIException("El nombre de la marca no puede empezar o terminar con espacios");
        }

        this.brandRepository.findByNameIgnoreCaseAndStatus(brand.getName(), EntityStatus.ACTIVE)
                .ifPresent(existingBrand -> {
                    throw new APIException("La marca ya existe");
                });

        Brand entity = new Brand();
        entity.setName(brand.getName());
        entity.setDescription(brand.getDescription());

        entity = this.brandRepository.save(entity);

        return this.brandMapper.brandToBrandDTO(entity);
    }

    @Override
    public BrandDTO updateBrand(Long id, UpdateBrandDTO brand) {
        Brand entity = this.brandRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", "id", id));

        if (brand.isEmpty()) {
            throw new APIException("No se han especificado campos a actualizar");
        }

        if (brand.getName() != null) {
            if (brand.getName().startsWith(" ") || brand.getName().endsWith(" ")) {
                throw new APIException("El nombre de la marca no puede empezar o terminar con espacios");
            }

            this.brandRepository.findByNameIgnoreCaseAndStatusAndIdNot(brand.getName(), EntityStatus.ACTIVE, id)
                    .ifPresent(existingBrand -> {
                        throw new APIException("La marca ya existe");
                    });

            entity.setName(brand.getName());
        }

        if (brand.getDescription() != null) {
            entity.setDescription(brand.getDescription());
        }

        entity = this.brandRepository.save(entity);

        return this.brandMapper.brandToBrandDTO(entity);
    }

    @Override
    public void deleteBrand(Long id) {
        Brand entity = this.brandRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", "id", id));

        entity.delete();

        this.brandRepository.save(entity);
    }
}
