package dao.usuario;

import conexionDB.ConexionBDSingleton;
import modelos.usuario.Estudiante;
import modelos.usuario.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAOImpl implements EstudianteDAO {

    private Connection c;

    public EstudianteDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    //Insertar estudiante
    @Override
    public void agregarEstudiante(Estudiante estudiante) {

        String sql = "INSERT INTO estudiantes " +
                "(cedula, id_grupo, foto, sis_salud, fec_nacimiento, " +
                "obs_confidenciales, motivo, obs_comentarios, " +
                "inf_esta_salud, calle, nro_puerta) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, estudiante.getCedula());
            ps.setInt(2, estudiante.getGrupo().getId());
            ps.setString(3, estudiante.getFoto());
            ps.setString(4, estudiante.getSistemaSalud());

            ps.setDate(5,
                    Date.valueOf(estudiante.getFechaNacimiento()));

            ps.setBoolean(6,
                    estudiante.isObsConfidenciales());

            ps.setString(7, estudiante.getMotivo());
            ps.setString(8, estudiante.getObsComentarios());
            ps.setString(9, estudiante.getInfoEstadoSalud());
            ps.setString(10, estudiante.getCalle());
            ps.setString(11, estudiante.getNroPuerta());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Actualizar estudiante
    @Override
    public void actualizarEstudiante(Estudiante estudiante) {

        String sql = "UPDATE estudiantes SET " +
                "id_grupo=?, foto=?, sis_salud=?, fec_nacimiento=?, " +
                "obs_confidenciales=?, motivo=?, obs_comentarios=?, " +
                "inf_esta_salud=?, calle=?, nro_puerta=? " +
                "WHERE cedula=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, estudiante.getGrupo().getId());
            ps.setString(2, estudiante.getFoto());
            ps.setString(3, estudiante.getSistemaSalud());

            ps.setDate(4,
                    Date.valueOf(estudiante.getFechaNacimiento()));

            ps.setBoolean(5,
                    estudiante.isObsConfidenciales());

            ps.setString(6, estudiante.getMotivo());
            ps.setString(7, estudiante.getObsComentarios());
            ps.setString(8, estudiante.getInfoEstadoSalud());
            ps.setString(9, estudiante.getCalle());
            ps.setString(10, estudiante.getNroPuerta());

            ps.setString(11, estudiante.getCedula());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Obtener por cedula
    @Override
    public Estudiante obtenerPorCedula(String cedula) {

        String sql =
                "SELECT u.*, e.*, r.nom_rol, r.estado AS estado_rol " +
                        "FROM usuarios u " +
                        "JOIN estudiantes e ON u.cedula = e.cedula " +
                        "JOIN roles r ON u.id_rol = r.id_rol " +
                        "WHERE u.cedula = ? AND u.estado = true";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, cedula);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Estudiante(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("contrasena"),
                        rs.getBoolean("estado"),

                        new Rol(
                                rs.getInt("id_rol"),
                                rs.getString("nom_rol"),
                                rs.getBoolean("estado_rol")
                        ),

                        rs.getString("foto"),
                        rs.getString("sis_salud"),

                        rs.getDate("fec_nacimiento")
                                .toLocalDate(),

                        rs.getString("calle"),
                        rs.getString("nro_puerta"),

                        rs.getBoolean("obs_confidenciales"),

                        rs.getString("motivo"),
                        rs.getString("obs_comentarios"),
                        rs.getString("inf_esta_salud"),

                        null
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Buscar por nombre completo
    @Override
    public List<Estudiante> buscarPorNombreApellido(String texto) {

        List<Estudiante> lista = new ArrayList<>();

        String sql =
                "SELECT u.*, e.*, r.nom_rol, r.estado AS estado_rol " +
                        "FROM usuarios u " +
                        "JOIN estudiantes e ON u.cedula = e.cedula " +
                        "JOIN roles r ON u.id_rol = r.id_rol " +
                        "WHERE LOWER(u.nombre || ' ' || u.apellido) LIKE ? " +
                        "AND u.estado = true";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1,
                    "%" + texto.toLowerCase() + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Estudiante e = new Estudiante(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("email"),
                        rs.getString("contrasena"),
                        rs.getBoolean("estado"),

                        new Rol(
                                rs.getInt("id_rol"),
                                rs.getString("nom_rol"),
                                rs.getBoolean("estado_rol")
                        ),

                        rs.getString("foto"),
                        rs.getString("sis_salud"),

                        rs.getDate("fec_nacimiento")
                                .toLocalDate(),

                        rs.getString("calle"),
                        rs.getString("nro_puerta"),

                        rs.getBoolean("obs_confidenciales"),

                        rs.getString("motivo"),
                        rs.getString("obs_comentarios"),
                        rs.getString("inf_esta_salud"),

                        null
                );

                lista.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}