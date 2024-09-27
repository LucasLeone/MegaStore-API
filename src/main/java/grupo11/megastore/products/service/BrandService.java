package grupo11.megastore.products.service;

import grupo11.megastore.constant.EntityStatus;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

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
        Optional<Brand> brand = this.brandRepository.findByIdAndStatus(id, EntityStatus.ACTIVE);

        if (brand.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La marca no existe");
        }

        BrandDTO dto = this.brandMapper.brandToBrandDTO(brand.get());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BrandDTO> createBrand(CreateBrandDTO brand) {
        Optional<Brand> existingBrand = this.brandRepository.findByNameIgnoreCaseAndStatus(brand.getName(), EntityStatus.ACTIVE);

        if (existingBrand.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La marca ya existe");
        }

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se han especificado campos a actualizar");
        }

        Optional<Brand> existingBrand = this.brandRepository.findByNameIgnoreCaseAndStatusAndIdNot(brand.getName(),
                EntityStatus.ACTIVE, id);

        if (existingBrand.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La marca ya existe");
        }

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
        Brand entity = this.brandMapper.brandDTOToBrand(this.getBrandById(id).getBody());

        if (entity.getStatus() == EntityStatus.DELETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La marca no existe/ya ha sido eliminada");
        }

        entity.delete();

        this.brandRepository.save(entity);
    }
}
