package dao.instancia;
import conexionDB.ConexionBDSingleton;
import modelos.instancia.*;
import java.sql.*;

public class ComunDAOImpl implements ComunDAO {

    private final Connection c;

    public ComunDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarComun(InstanciaComun com) {

        String sql = "INSERT INTO inst_comunes (id_instancia, id_categoria) VALUES (?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, com.getId());
            ps.setInt(2, com.getCategoria().getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarComun(InstanciaComun com) {

        String sql = "UPDATE inst_comunes SET id_categoria = ? WHERE id_instancia = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, com.getCategoria().getId());
            ps.setInt(2, com.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public InstanciaComun obtenerPorInstancia(int idInstancia) {
        String sql = "SELECT * FROM inst_comunes WHERE id_instancia = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idInstancia);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("id_categoria"),
                        null,
                        true
                );
                InstanciaComun com = new InstanciaComun(
                        idInstancia, false, null, null,
                        null, true, null, null, categoria
                );
                return com;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return null;
    }
}