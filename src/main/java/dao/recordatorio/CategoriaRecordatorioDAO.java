package dao.recordatorio;

import modelos.recordatorio.CategoriaRecordatorio;

import java.util.List;

public interface CategoriaRecordatorioDAO {
    void agregarCategoria(CategoriaRecordatorio categoria);
    void modificarCategoria(CategoriaRecordatorio categoria);
    void eliminarCategoria(int id);
    CategoriaRecordatorio obtenerPorId(int id);
    CategoriaRecordatorio obtenerPorNombre(String nombre);
    List<CategoriaRecordatorio> obtenerTodas();
}