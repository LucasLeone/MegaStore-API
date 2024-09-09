package grupo11.megastore.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import grupo11.megastore.DTO.CategoriaDTO;
import grupo11.megastore.Mapper.CategoriaMapper;
import grupo11.megastore.exception.RecursoNoEncontradoExcepcion;
import grupo11.megastore.service.ICategoriaService;
import grupo11.megastore.model.Categoria;

@RestController

public class CategoriaController {
    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);
    @Autowired
    private ICategoriaService modelService;
    
    @GetMapping({"/categorias"})
    public List<CategoriaDTO> listar() {
        return modelService.listar();
    }

    @GetMapping("/categorias/{id}")
    public ResponseEntity<CategoriaDTO> buscarPorId(@PathVariable Integer id) {
        Categoria model = modelService.buscarPorId(id);
    
        if (model == null) {
            throw new RecursoNoEncontradoExcepcion("No se encontró el id: " + id);
        }
        CategoriaDTO modelDTO = CategoriaMapper.toDTO(model);
        return ResponseEntity.ok(modelDTO);
    }    

    @PostMapping("/categorias")
    public CategoriaDTO guardar(@RequestBody CategoriaDTO model) {
        return modelService.guardar(model);
    }

    @PatchMapping("/categorias/{id}")
    public ResponseEntity<CategoriaDTO> actualizarParcial(@PathVariable Integer id, @RequestBody CategoriaDTO modelDTO) {
        Categoria categoriaExistente = modelService.buscarPorId(id);
        if (categoriaExistente == null) {
            throw new RecursoNoEncontradoExcepcion("No se encontró la categoría con id: " + id);
        }
    
        if (modelDTO.getNombre() != null) {
            categoriaExistente.setNombre(modelDTO.getNombre());
        }
    
        if (modelDTO.getDescripcion() != null) {
            categoriaExistente.setDescripcion(modelDTO.getDescripcion());
        }
    
        Categoria categoriaActualizada = modelService.guardar(categoriaExistente);
    
        return ResponseEntity.ok(CategoriaMapper.toDTO(categoriaActualizada));
    }    

    @DeleteMapping("/categorias/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Categoria model = modelService.buscarPorId(id);
        if (model == null) {
            throw new RecursoNoEncontradoExcepcion("No se encontro el id: " + id);
        }

        model.asEliminar();
        modelService.eliminar(model);
        return ResponseEntity.ok().build();
    }
}
