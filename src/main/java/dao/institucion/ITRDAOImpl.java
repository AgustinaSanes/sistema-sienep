package dao.institucion;
import conexionDB.ConexionBDSingleton;
import modelos.institucion.ITR;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ITRDAOImpl implements ITRDAO {

    private final Connection c;

    public ITRDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarITR(ITR itr) {
        String sql = "INSERT INTO itrs (nom_itr, calle, nro_puerta, ciudad, departamento, telefono) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, itr.getNombre());
            ps.setString(2, itr.getCalle());
            ps.setString(3, itr.getNroPuerta());
            ps.setString(4, itr.getCiudad());
            ps.setString(5, itr.getDepartamento());
            ps.setString(6, itr.getTelefono());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizarITR(ITR itr) {
        String sql = "UPDATE itrs SET nom_itr = ?, calle = ?, nro_puerta = ?, ciudad = ?, departamento = ?, telefono = ? WHERE id_itr = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, itr.getNombre());
            ps.setString(2, itr.getCalle());
            ps.setString(3, itr.getNroPuerta());
            ps.setString(4, itr.getCiudad());
            ps.setString(5, itr.getDepartamento());
            ps.setString(6, itr.getTelefono());
            ps.setInt(7, itr.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarITR(int id) {
        String sql = "UPDATE itrs SET estado = false WHERE id_itr = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ITR obtenerPorId(int id) {
        String sql = "SELECT * FROM itrs WHERE id_itr = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new ITR(
                        rs.getInt("id_itr"),
                        rs.getString("nom_itr"),
                        rs.getString("calle"),
                        rs.getString("nro_puerta"),
                        rs.getString("ciudad"),
                        rs.getString("departamento"),
                        rs.getString("telefono"),
                        rs.getBoolean("estado")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ITR> obtenerTodos() {
        List<ITR> lista = new ArrayList<>();
        String sql = "SELECT * FROM itrs";

        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                ITR itr = new ITR(
                        rs.getInt("id_itr"),
                        rs.getString("nom_itr"),
                        rs.getString("calle"),
                        rs.getString("nro_puerta"),
                        rs.getString("ciudad"),
                        rs.getString("departamento"),
                        rs.getString("telefono"),
                        rs.getBoolean("estado")
                );

                lista.add(itr);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}