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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<BrandDTO>> getAllBrands() {
        List<Brand> brands = this.brandRepository.findByStatus(EntityStatus.ACTIVE);

        List<BrandDTO> dtos = new ArrayList<>();
        brands.forEach(brand -> {
            dtos.add(this.brandMapper.brandToBrandDTO(brand));
        });

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BrandDTO> getBrandById(Long id) {
        Brand brand = this.brandRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", "id", id));

        BrandDTO dto = this.brandMapper.brandToBrandDTO(brand);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BrandDTO> createBrand(CreateBrandDTO brand) {
        this.brandRepository.findByNameIgnoreCaseAndStatus(brand.getName(), EntityStatus.ACTIVE)
                .ifPresent(existingBrand -> {
                    throw new APIException("La marca ya existe");
                });

        Brand entity = new Brand();
        entity.setName(brand.getName());
        entity.setDescription(brand.getDescription());

        entity = this.brandRepository.save(entity);

        BrandDTO dto = this.brandMapper.brandToBrandDTO(entity);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BrandDTO> updateBrand(Long id, UpdateBrandDTO brand) {
        Brand entity = this.brandMapper.brandDTOToBrand(this.getBrandById(id).getBody());

        if (brand.isEmpty()) {
            throw new APIException("No se han especificado campos a actualizar");
        }

        this.brandRepository.findByNameIgnoreCaseAndStatusAndIdNot(brand.getName(), EntityStatus.ACTIVE, id)
                .ifPresent(existingBrand -> {
                    throw new APIException("La marca ya existe");
                });

        if (brand.getName() != null) {
            entity.setName(brand.getName());
        }

        if (brand.getDescription() != null) {
            entity.setDescription(brand.getDescription());
        }

        entity = this.brandRepository.save(entity);

        BrandDTO dto = this.brandMapper.brandToBrandDTO(entity);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public void deleteBrand(Long id) {
        Brand entity = this.brandRepository.findByIdAndStatus(id, EntityStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Marca", "id", id));

        entity.delete();

        this.brandRepository.save(entity);
    }
}
