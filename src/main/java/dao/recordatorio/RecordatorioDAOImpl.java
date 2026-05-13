package dao.recordatorio;
import conexionDB.ConexionBDSingleton;
import modelos.recordatorio.Recordatorio;
import modelos.recordatorio.Frecuencia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordatorioDAOImpl implements RecordatorioDAO {

    private Connection c;

    public RecordatorioDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarRecordatorio(Recordatorio recordatorio) {
        String sql = "INSERT INTO recordatorios " +
                "(id_instancia, id_frecuencia, titulo, fec_hora, tipo, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, recordatorio.getId());
            ps.setInt(2, recordatorio.getFrecuencia().getId());
            ps.setString(3, recordatorio.getTitulo());
            ps.setTimestamp(4, Timestamp.valueOf(recordatorio.getFechaHora()));
            ps.setString(5, recordatorio.getTipo());
            ps.setBoolean(6, recordatorio.isEstado());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                recordatorio.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizarRecordatorio(Recordatorio recordatorio) {
        String sql = "UPDATE recordatorios SET " +
                "id_frecuencia=?, titulo=?, fec_hora=?, tipo=?, estado=? " +
                "WHERE id_recordatorio=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, recordatorio.getFrecuencia().getId());
            ps.setString(2, recordatorio.getTitulo());
            ps.setTimestamp(3, Timestamp.valueOf(recordatorio.getFechaHora()));
            ps.setString(4, recordatorio.getTipo());
            ps.setBoolean(5, recordatorio.isEstado());
            ps.setInt(6, recordatorio.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarRecordatorio(int id) {
        String sql = "UPDATE recordatorios SET estado = false WHERE id_recordatorio = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Recordatorio obtenerPorId(int id) {
        String sql = "SELECT r.*, f.descripcion AS desc_frecuencia " +
                "FROM recordatorios r " +
                "JOIN frecuencias f ON r.id_frecuencia = f.id_frecuencia " +
                "WHERE r.id_recordatorio = ? AND r.estado = true";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Recordatorio(
                        rs.getInt("id_recordatorio"),
                        rs.getString("titulo"),
                        rs.getTimestamp("fec_hora").toLocalDateTime(),
                        rs.getString("tipo"),
                        rs.getBoolean("estado"),
                        new Frecuencia(
                                rs.getInt("id_frecuencia"),
                                rs.getString("desc_frecuencia")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Recordatorio> obtenerPorInstancia(int idInstancia) {
        List<Recordatorio> lista = new ArrayList<>();
        String sql = "SELECT r.*, f.descripcion AS desc_frecuencia " +
                "FROM recordatorios r " +
                "JOIN frecuencias f ON r.id_frecuencia = f.id_frecuencia " +
                "WHERE r.id_instancia = ? AND r.estado = true";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idInstancia);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Recordatorio rec = new Recordatorio(
                        rs.getInt("id_recordatorio"),
                        rs.getString("titulo"),
                        rs.getTimestamp("fec_hora").toLocalDateTime(),
                        rs.getString("tipo"),
                        rs.getBoolean("estado"),
                        new Frecuencia(
                                rs.getInt("id_frecuencia"),
                                rs.getString("desc_frecuencia")
                        )
                );
                lista.add(rec);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}