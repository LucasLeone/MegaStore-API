package grupo11.megastore.service;

import grupo11.megastore.DTO.CategoriaDTO;
import grupo11.megastore.model.Categoria;

import java.util.List;

public interface ICategoriaService {
    public List<CategoriaDTO> listar();
    public Categoria buscarPorId(Integer id);
    public CategoriaDTO guardar(CategoriaDTO model);
    public Categoria guardar(Categoria model);
    public void eliminar(Categoria model);
}
