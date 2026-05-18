package dao.instancia;

import conexionDB.ConexionBDSingleton;
import modelos.instancia.Instancia;
import modelos.instancia.InstanciaComun;
import modelos.usuario.Estudiante;
import modelos.usuario.Funcionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstanciaDAOImpl implements InstanciaDAO {

    private final Connection c;

    public InstanciaDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarInstancia(Instancia instancia) {

        String sql = "INSERT INTO instancias " +
                "(ced_estudiante, ced_funcionario, titulo, comentario, com_confidencial, fec_hora, estado) " +
                "VALUES (?,?,?,?,?,?,?)";

        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, instancia.getEstudiante().getCedula());
            ps.setString(2, instancia.getFuncionario().getCedula());
            ps.setString(3, instancia.getTitulo());
            ps.setString(4, instancia.getComentario());
            ps.setBoolean(5, instancia.getComConfidencial());
            ps.setTimestamp(6, Timestamp.valueOf(instancia.getFechaHora()));
            ps.setBoolean(7, instancia.isEstado());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                instancia.setId(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizarInstancia(Instancia instancia) {

        String sql = "UPDATE instancias SET " +
                "titulo=?, comentario=?, com_confidencial=?, fec_hora=?, estado=? " +
                "WHERE id_instancia=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, instancia.getTitulo());
            ps.setString(2, instancia.getComentario());
            ps.setBoolean(3, instancia.getComConfidencial());
            ps.setTimestamp(4, Timestamp.valueOf(instancia.getFechaHora()));
            ps.setBoolean(5, instancia.isEstado());
            ps.setInt(6, instancia.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarInstancia(int id) {

        String sql = "UPDATE instancias SET estado = false WHERE id_instancia = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Instancia obtenerPorId(int id) {
        String sql = "SELECT * FROM instancias WHERE id_instancia = ? AND estado = true";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setCedula(rs.getString("ced_estudiante"));

                Funcionario funcionario = new Funcionario();
                funcionario.setCedula(rs.getString("ced_funcionario"));

                // Lo dejamos sin tipo específico, InstanciaComun o Incidencia
                // se resuelve en el service con ComunDAO o IncidenciaDAO
                return new InstanciaComun(
                        rs.getInt("id_instancia"),
                        rs.getBoolean("com_confidencial"),
                        rs.getString("titulo"),
                        rs.getTimestamp("fec_hora").toLocalDateTime(),
                        rs.getString("comentario"),
                        rs.getBoolean("estado"),
                        estudiante,
                        funcionario,
                        null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Instancia> obtenerPorEstudiante(String cedula) {
        List<Instancia> lista = new ArrayList<>();
        String sql = "SELECT * FROM instancias WHERE ced_estudiante = ? AND estado = true";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setCedula(rs.getString("ced_estudiante"));

                Funcionario funcionario = new Funcionario();
                funcionario.setCedula(rs.getString("ced_funcionario"));

                InstanciaComun inst = new InstanciaComun(
                        rs.getInt("id_instancia"),
                        rs.getBoolean("com_confidencial"),
                        rs.getString("titulo"),
                        rs.getTimestamp("fec_hora").toLocalDateTime(),
                        rs.getString("comentario"),
                        rs.getBoolean("estado"),
                        estudiante,
                        funcionario,
                        null
                );
                lista.add(inst);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}