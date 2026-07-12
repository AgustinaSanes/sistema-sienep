package dao.auditoria;
import conexionDB.ConexionBDSingleton;
import modelos.auditoria.Auditoria;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaDAOImpl implements AuditoriaDAO {

    private final Connection c;

    public AuditoriaDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void registrar(Auditoria auditoria) {
        String sql = "INSERT INTO auditorias " +
                "(cedula, accion, detalle, ent_afectada) " +
                "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, auditoria.getCedula());
            ps.setString(2, auditoria.getAccion());
            ps.setString(3, auditoria.getDetalle());
            ps.setString(4, auditoria.getEntidadAfectada());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Auditoria> obtenerTodas() {
        List<Auditoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM auditorias ORDER BY fec_hora DESC";

        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearAuditoria(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Auditoria> obtenerPorCedula(String cedula) {
        List<Auditoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM auditorias WHERE cedula = ? ORDER BY fec_hora DESC";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearAuditoria(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Auditoria> obtenerPorFecha(LocalDate fecha) {
        List<Auditoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM auditorias WHERE DATE(fec_hora) = ? ORDER BY fec_hora DESC";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapearAuditoria(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return lista;
    }

    private Auditoria mapearAuditoria(ResultSet rs) throws SQLException {
        Auditoria a = new Auditoria(
                rs.getString("cedula"),
                rs.getString("accion"),
                rs.getString("detalle"),
                rs.getString("ent_afectada")
        );
        a.setId(rs.getInt("id_auditoria"));
        a.setFechaHora(rs.getTimestamp("fec_hora").toLocalDateTime());
        return a;
    }
}