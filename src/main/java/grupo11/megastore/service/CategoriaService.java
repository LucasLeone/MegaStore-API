package grupo11.megastore.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import grupo11.megastore.DTO.CategoriaDTO;
import grupo11.megastore.Mapper.CategoriaMapper;
import grupo11.megastore.model.Categoria;
import grupo11.megastore.repository.CategoriaRepository;

@Service

public class CategoriaService implements ICategoriaService{
    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);
    @Autowired
    private CategoriaRepository modelRepository;

    @Override
    public List<CategoriaDTO> listar() {
        List<Categoria> categorias = modelRepository.findByEstado(Categoria.COMUN);
        return categorias.stream().map(CategoriaMapper::toDTO).toList();
    }

    @Override
    public Categoria buscarPorId(Integer id) {
        return modelRepository.findById(id).orElse(null);
    }

    @Override
    public CategoriaDTO guardar(CategoriaDTO modelDTO) {
        Categoria model = CategoriaMapper.toEntity(modelDTO);
        return CategoriaMapper.toDTO(modelRepository.save(model));
    }
    @Override
    public Categoria guardar(Categoria model) {
        return modelRepository.save(model);
    }

    @Override
    public void eliminar(Categoria model) {
        model.asEliminar();
        modelRepository.save(model);
    }
}
