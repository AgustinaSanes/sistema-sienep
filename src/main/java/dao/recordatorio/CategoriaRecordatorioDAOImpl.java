package dao.recordatorio;

import conexionDB.ConexionBDSingleton;
import modelos.recordatorio.CategoriaRecordatorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaRecordatorioDAOImpl implements CategoriaRecordatorioDAO {

    private final Connection c;

    public CategoriaRecordatorioDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarCategoria(CategoriaRecordatorio categoria) {

        String sql = "INSERT INTO reco_categorias (nombre) VALUES (?)";

        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categoria.getNombre());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                categoria.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public void modificarCategoria(CategoriaRecordatorio categoria) {

        String sql = """
                UPDATE reco_categorias
                SET nombre = ?
                WHERE id_cate_recordatorio = ?
                """;

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarCategoria(int id) {

        String sql = """
                UPDATE reco_categorias
                SET estado = false
                WHERE id_cate_recordatorio = ?
                """;

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public CategoriaRecordatorio obtenerPorId(int id) {

        String sql = """
                SELECT *
                FROM reco_categorias
                WHERE id_cate_recordatorio = ?
                  AND estado = true
                """;

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new CategoriaRecordatorio(
                        rs.getInt("id_cate_recordatorio"),
                        rs.getString("nombre"),
                        rs.getBoolean("estado")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public CategoriaRecordatorio obtenerPorNombre(String nombre) {

        String sql = """
                SELECT *
                FROM reco_categorias
                WHERE LOWER(nombre) = LOWER(?)
                  AND estado = true
                """;

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nombre);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new CategoriaRecordatorio(
                        rs.getInt("id_cate_recordatorio"),
                        rs.getString("nombre"),
                        rs.getBoolean("estado")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<CategoriaRecordatorio> obtenerTodas() {

        List<CategoriaRecordatorio> lista = new ArrayList<>();

        String sql = """
                SELECT *
                FROM reco_categorias
                WHERE estado = true
                ORDER BY nombre
                """;

        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                lista.add(new CategoriaRecordatorio(
                        rs.getInt("id_cate_recordatorio"),
                        rs.getString("nombre"),
                        rs.getBoolean("estado")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }

        return lista;
    }
}