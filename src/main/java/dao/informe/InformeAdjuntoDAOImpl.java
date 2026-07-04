package dao.informe;
import conexionDB.ConexionBDSingleton;
import modelos.informe.InformeAdjunto;
import modelos.usuario.Estudiante;
import java.sql.*;
import java.util.*;

public class InformeAdjuntoDAOImpl implements InformeAdjuntoDAO {

    private final Connection c;

    public InformeAdjuntoDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarInforme(InformeAdjunto informe) {
        String sql = "INSERT INTO info_adjuntos " +
                "(cedula, nom_archivo, tip_archivo, rut_archivo, categoria, confidencial) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, informe.getEstudiante().getCedula());
            ps.setString(2, informe.getNombre());
            ps.setString(3, informe.getTipoArchivo());
            ps.setString(4, informe.getRutaArchivo());
            ps.setString(5, informe.getCategoria());
            ps.setBoolean(6, informe.isConfidencial());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizarInforme(InformeAdjunto informe) {
        String sql = "UPDATE info_adjuntos SET " +
                "nom_archivo=?, tip_archivo=?, rut_archivo=?, categoria=?, confidencial=? " +
                "WHERE id_informe=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, informe.getNombre());
            ps.setString(2, informe.getTipoArchivo());
            ps.setString(3, informe.getRutaArchivo());
            ps.setString(4, informe.getCategoria());
            ps.setBoolean(5, informe.isConfidencial());
            ps.setInt(6, informe.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarInforme(int id) {
        String sql = "UPDATE info_adjuntos SET estado = false WHERE id_informe = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InformeAdjunto obtenerPorId(int id) {
        String sql = "SELECT * FROM info_adjuntos WHERE id_informe = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setCedula(rs.getString("cedula"));

                return new InformeAdjunto(
                        rs.getInt("id_informe"),
                        rs.getString("nom_archivo"),
                        rs.getString("tip_archivo"),
                        rs.getString("rut_archivo"),
                        rs.getString("categoria"),
                        rs.getBoolean("confidencial"),
                        rs.getBoolean("estado"),
                        estudiante
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<InformeAdjunto> obtenerTodos() {
        List<InformeAdjunto> lista = new ArrayList<>();
        String sql = "SELECT * FROM info_adjuntos WHERE estado = true";

        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setCedula(rs.getString("cedula"));

                InformeAdjunto informe = new InformeAdjunto(
                        rs.getInt("id_informe"),
                        rs.getString("nom_archivo"),
                        rs.getString("tip_archivo"),
                        rs.getString("rut_archivo"),
                        rs.getString("categoria"),
                        rs.getBoolean("confidencial"),
                        rs.getBoolean("estado"),
                        estudiante
                );
                lista.add(informe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<InformeAdjunto> obtenerPorEstudiante(String cedula) {
        List<InformeAdjunto> lista = new ArrayList<>();
        String sql = "SELECT * FROM info_adjuntos WHERE cedula = ? AND estado = true";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setCedula(cedula);

                lista.add(new InformeAdjunto(
                        rs.getInt("id_informe"),
                        rs.getString("nom_archivo"),
                        rs.getString("tip_archivo"),
                        rs.getString("rut_archivo"),
                        rs.getString("categoria"),
                        rs.getBoolean("confidencial"),
                        rs.getBoolean("estado"),
                        estudiante
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}