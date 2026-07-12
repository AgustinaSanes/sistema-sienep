package dao.recordatorio;
import conexionDB.ConexionBDSingleton;
import modelos.recordatorio.CategoriaRecordatorio;
import modelos.recordatorio.Recordatorio;
import modelos.recordatorio.Frecuencia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordatorioDAOImpl implements RecordatorioDAO {

    private final Connection c;

    public RecordatorioDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarRecordatorio(Recordatorio recordatorio) {
        String sql = "INSERT INTO recordatorios " +
                "(id_instancia, id_frecuencia, id_cate_recordatorio, titulo, fec_hora, tipo, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, recordatorio.getIdInstancia());
            ps.setInt(2, recordatorio.getFrecuencia().getId());
            ps.setInt(3, recordatorio.getCategoria().getId());
            ps.setString(4, recordatorio.getTitulo());
            ps.setTimestamp(5, Timestamp.valueOf(recordatorio.getFechaHora()));
            ps.setString(6, recordatorio.getTipo());
            ps.setBoolean(7, recordatorio.isEstado());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                recordatorio.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarRecordatorio(Recordatorio recordatorio) {
        String sql = "UPDATE recordatorios SET " +
                "id_frecuencia=?, id_cate_recordatorio=?, titulo=?, fec_hora=?, tipo=?, estado=? " +
                "WHERE id_recordatorio=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, recordatorio.getFrecuencia().getId());
            ps.setInt(2, recordatorio.getCategoria().getId());
            ps.setString(3, recordatorio.getTitulo());
            ps.setTimestamp(4, Timestamp.valueOf(recordatorio.getFechaHora()));
            ps.setString(5, recordatorio.getTipo());
            ps.setBoolean(6, recordatorio.isEstado());
            ps.setInt(7, recordatorio.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarRecordatorio(int id) {
        String sql = "UPDATE recordatorios SET estado = false WHERE id_recordatorio = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public Recordatorio obtenerPorId(int id) {
        String sql =
                "SELECT r.*, " +
                        "f.descripcion AS desc_frecuencia, " +
                        "f.estado AS estado_frecuencia, " +
                        "cr.nombre AS nombre_categoria, " +
                        "cr.estado AS estado_categoria " +
                        "FROM recordatorios r " +
                        "JOIN frecuencias f ON r.id_frecuencia = f.id_frecuencia " +
                        "JOIN reco_categorias cr ON r.id_cate_recordatorio = cr.id_cate_recordatorio " +
                        "WHERE r.id_recordatorio = ? AND r.estado = true";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearRecordatorio(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Recordatorio> obtenerPorInstancia(int idInstancia) {
        List<Recordatorio> lista = new ArrayList<>();
        String sql =
                "SELECT r.*, " +
                        "f.descripcion AS desc_frecuencia, " +
                        "f.estado AS estado_frecuencia, " +
                        "cr.nombre AS nombre_categoria, " +
                        "cr.estado AS estado_categoria " +
                        "FROM recordatorios r " +
                        "JOIN frecuencias f ON r.id_frecuencia = f.id_frecuencia " +
                        "JOIN reco_categorias cr ON r.id_cate_recordatorio = cr.id_cate_recordatorio " +
                        "WHERE r.id_instancia = ? AND r.estado = true " +
                        "ORDER BY r.fec_hora";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idInstancia);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearRecordatorio(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return lista;
    }

    private Recordatorio mapearRecordatorio(ResultSet rs) throws SQLException {

        Frecuencia frecuencia = new Frecuencia(
                rs.getInt("id_frecuencia"),
                rs.getString("desc_frecuencia"),
                rs.getBoolean("estado_frecuencia")
        );

        CategoriaRecordatorio categoria = new CategoriaRecordatorio(
                rs.getInt("id_cate_recordatorio"),
                rs.getString("nombre_categoria"),
                rs.getBoolean("estado_categoria")
        );

        return new Recordatorio(
                rs.getInt("id_recordatorio"),
                rs.getInt("id_instancia"),
                rs.getString("titulo"),
                rs.getTimestamp("fec_hora").toLocalDateTime(),
                rs.getString("tipo"),
                rs.getBoolean("estado"),
                frecuencia,
                categoria
        );
    }
}