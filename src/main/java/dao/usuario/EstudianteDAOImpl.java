package dao.usuario;

import conexionDB.ConexionBDSingleton;
import modelos.usuario.Estudiante;
import modelos.usuario.Rol;
import dao.institucion.GrupoDAOImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAOImpl implements EstudianteDAO {

    private final Connection c;

    public EstudianteDAOImpl() {
        this.c = ConexionBDSingleton.getInstancia().getConexion();
    }

    @Override
    public void agregarEstudiante(Estudiante estudiante) {

        String sql = "INSERT INTO estudiantes " +
                "(cedula, id_grupo, foto, sis_salud, fec_nacimiento, " +
                "obs_confidenciales, motivo, obs_comentarios, " +
                "inf_esta_salud, calle, nro_puerta) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, estudiante.getCedula());
            ps.setObject(2, estudiante.getGrupo() != null ? estudiante.getGrupo().getId() : null);            ps.setString(3, estudiante.getFoto());
            ps.setString(4, estudiante.getSistemaSalud());
            ps.setDate(5, Date.valueOf(estudiante.getFechaNacimiento()));
            ps.setBoolean(6, estudiante.isObsConfidenciales());
            ps.setString(7, estudiante.getMotivo());
            ps.setString(8, estudiante.getObsComentarios());
            ps.setString(9, estudiante.getInfoEstadoSalud());
            ps.setString(10, estudiante.getCalle());
            ps.setString(11, estudiante.getNroPuerta());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarEstudiante(Estudiante estudiante) {

        String sql = "UPDATE estudiantes SET " +
                "id_grupo=?, foto=?, sis_salud=?, fec_nacimiento=?, " +
                "obs_confidenciales=?, motivo=?, obs_comentarios=?, " +
                "inf_esta_salud=?, calle=?, nro_puerta=? " +
                "WHERE cedula=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setObject(1, estudiante.getGrupo() != null ? estudiante.getGrupo().getId() : null);            ps.setString(2, estudiante.getFoto());
            ps.setString(3, estudiante.getSistemaSalud());
            ps.setDate(4, Date.valueOf(estudiante.getFechaNacimiento()));
            ps.setBoolean(5, estudiante.isObsConfidenciales());
            ps.setString(6, estudiante.getMotivo());
            ps.setString(7, estudiante.getObsComentarios());
            ps.setString(8, estudiante.getInfoEstadoSalud());
            ps.setString(9, estudiante.getCalle());
            ps.setString(10, estudiante.getNroPuerta());
            ps.setString(11, estudiante.getCedula());
            ps.executeUpdate();
            TelefonoDAO telefonoDAO = new TelefonoDAOImpl();

            List<String> telefonosBD = telefonoDAO.obtenerPorEstudiante(estudiante.getCedula());

            for (String telefono : telefonosBD) {
                if (!estudiante.getTelefono().contains(telefono)) {
                    telefonoDAO.eliminarTelefono(estudiante.getCedula(), telefono);
                }
            }

            for (String telefono : estudiante.getTelefono()) {
                if (!telefonosBD.contains(telefono)) {
                    telefonoDAO.agregarTelefono(estudiante.getCedula(), telefono);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public Estudiante obtenerPorCedula(String cedula) {

        //La consulta SQL trae los datos de la BD y los guarda en rs
        String sql =
                "SELECT u.*, e.*, r.nom_rol, r.estado AS estado_rol " +
                        "FROM usuarios u " +
                        "JOIN estudiantes e ON u.cedula = e.cedula " +
                        "JOIN roles r ON u.id_rol = r.id_rol " +
                        "WHERE u.cedula = ? AND u.estado = true";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, cedula);

            ResultSet rs = ps.executeQuery();

            //Lee esos datos del rs y construye el objeto Estudiante
            //Es la traducción de "lenguaje de base de datos" a "lenguaje Java
            if (rs.next()) {
                return mapearEstudiante(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<Estudiante> buscarPorNombreApellido(String texto) {

        List<Estudiante> lista = new ArrayList<>();

        String sql =
                "SELECT u.*, e.*, r.nom_rol, r.estado AS estado_rol " +
                        "FROM usuarios u " +
                        "JOIN estudiantes e ON u.cedula = e.cedula " +
                        "JOIN roles r ON u.id_rol = r.id_rol " +
                        "WHERE LOWER(u.nombre || ' ' || u.apellido) LIKE ? " +
                        "AND u.estado = true " +
                        "ORDER BY u.apellido, u.nombre";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1,
                    "%" + texto.toLowerCase() + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearEstudiante(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public List<Estudiante> buscarPorCarrera(int idCarrera){
        List<Estudiante> lista = new ArrayList<>();

        String sql = "SELECT u.*, e.*, r.nom_rol, r.estado AS estado_rol " +
                "FROM usuarios u " +
                "JOIN estudiantes e ON u.cedula = e.cedula " +
                "JOIN roles r ON u.id_rol = r.id_rol " +
                "JOIN grupos g ON e.id_grupo = g.id_grupo " +
                "WHERE g.id_carrera = ? AND u.estado = true " +
                "ORDER BY g.nom_grupo, u.apellido, u.nombre";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idCarrera);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) lista.add(mapearEstudiante(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Estudiante> buscarPorGrupo(int idGrupo) {

        List<Estudiante> lista = new ArrayList<>();

        String sql = "SELECT u.*, e.*, r.nom_rol, r.estado AS estado_rol " +
                "FROM usuarios u " +
                "JOIN estudiantes e ON u.cedula = e.cedula " +
                "JOIN roles r ON u.id_rol = r.id_rol " +
                "WHERE e.id_grupo = ? AND u.estado = true " +
                "ORDER BY u.apellido, u.nombre";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, idGrupo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) lista.add(mapearEstudiante(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Estudiante> buscarPorEstado(boolean estado) {

        List<Estudiante> lista = new ArrayList<>();

        String sql = "SELECT u.*, e.*, r.nom_rol, r.estado AS estado_rol " +
                "FROM usuarios u " +
                "JOIN estudiantes e ON u.cedula = e.cedula " +
                "JOIN roles r ON u.id_rol = r.id_rol " +
                "WHERE u.estado = ? " +
                "ORDER BY u.apellido, u.nombre";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setBoolean(1, estado);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) lista.add(mapearEstudiante(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return lista;
    }

    private Estudiante mapearEstudiante(ResultSet rs) throws SQLException{
        Estudiante estudiante = new Estudiante(
                rs.getString("cedula"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("email"),
                rs.getString("contrasena"),
                rs.getBoolean("estado"),
                //Estudiante hereda de Usuario que tiene un atributo Rol rol.
                new Rol(
                        rs.getInt("id_rol"),
                        rs.getString("nom_rol"),
                        rs.getBoolean("estado_rol")
                ),
                rs.getString("foto"),
                rs.getString("sis_salud"),
                rs.getDate("fec_nacimiento").toLocalDate(),
                rs.getString("calle"),
                rs.getString("nro_puerta"),
                rs.getBoolean("obs_confidenciales"),
                rs.getString("motivo"),
                rs.getString("obs_comentarios"),
                rs.getString("inf_esta_salud")
        );
        GrupoDAOImpl grupoDAO = new GrupoDAOImpl();
        estudiante.setGrupo(grupoDAO.obtenerPorId(rs.getInt("id_grupo")));

        TelefonoDAOImpl telefonoDAO = new TelefonoDAOImpl();
        for (String telefono : telefonoDAO.obtenerPorEstudiante(estudiante.getCedula())) {
            estudiante.agregarTelefono(telefono);
        }

        return estudiante;
    }
}