package servicios.recordatorio;

import dao.recordatorio.CategoriaRecordatorioDAO;
import dao.recordatorio.CategoriaRecordatorioDAOImpl;
import modelos.recordatorio.CategoriaRecordatorio;

import java.util.List;

public class CategoriaRecordatorioService {

    private final CategoriaRecordatorioDAO dao;

    public CategoriaRecordatorioService() {
        dao = new CategoriaRecordatorioDAOImpl();
    }

    public void agregarCategoria(CategoriaRecordatorio categoria) {

        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new RuntimeException("Debe ingresar un nombre.");
        }

        if (categoria.getNombre().length() > 50) {
            throw new RuntimeException("El nombre no puede superar los 50 caracteres.");
        }

        if (dao.obtenerPorNombre(categoria.getNombre()) != null) {
            throw new RuntimeException("Ya existe una categoría con ese nombre.");
        }

        dao.agregarCategoria(categoria);
    }

    public void modificarCategoria(CategoriaRecordatorio categoria) {

        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new RuntimeException("Debe ingresar un nombre.");
        }

        if (categoria.getNombre().length() > 50) {
            throw new RuntimeException("El nombre no puede superar los 50 caracteres.");
        }

        CategoriaRecordatorio existente = dao.obtenerPorNombre(categoria.getNombre());

        if (existente != null && existente.getId() != categoria.getId()) {
            throw new RuntimeException("Ya existe una categoría con ese nombre.");
        }

        dao.modificarCategoria(categoria);
    }

    public void eliminarCategoria(int id) {
        dao.eliminarCategoria(id);
    }

    public CategoriaRecordatorio obtenerPorId(int id) {
        return dao.obtenerPorId(id);
    }

    public List<CategoriaRecordatorio> obtenerTodas() {
        return dao.obtenerTodas();
    }
}