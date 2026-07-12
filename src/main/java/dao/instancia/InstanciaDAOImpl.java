package dao.instancia;

import conexionDB.ConexionBDSingleton;
import dao.usuario.EstudianteDAO;
import dao.usuario.EstudianteDAOImpl;
import dao.usuario.FuncionarioDAO;
import dao.usuario.FuncionarioDAOImpl;
import modelos.instancia.Incidencia;
import modelos.instancia.Instancia;
import modelos.instancia.InstanciaComun;
import modelos.instancia.Categoria;
import dao.instancia.InvolucradoDAO;
import dao.instancia.InvolucradoDAOImpl;
import modelos.usuario.Estudiante;
import modelos.usuario.Funcionario;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InstanciaDAOImpl implements InstanciaDAO {

    private final Connection c;
    private final EstudianteDAO estudianteDAO = new EstudianteDAOImpl();
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAOImpl();
    private final InvolucradoDAO involucradoDAO = new InvolucradoDAOImpl();

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
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
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
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarInstancia(int id) {

        String sql = "UPDATE instancias SET estado = false WHERE id_instancia = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    public Instancia obtenerPorId(int id) {
        String sql =
                "SELECT i.*, inc.lugar, com.id_categoria, cat.nombre AS cat_nombre, cat.estado AS cat_estado " +
                        "FROM instancias i " +
                        "LEFT JOIN incidencias inc ON i.id_instancia = inc.id_instancia " +
                        "LEFT JOIN inst_comunes com ON i.id_instancia = com.id_instancia " +
                        "LEFT JOIN categorias cat ON com.id_categoria = cat.id_categoria " +
                        "WHERE i.id_instancia = ? AND i.estado = true";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Estudiante estudiante = estudianteDAO.obtenerPorCedula(rs.getString("ced_estudiante"));

                Funcionario funcionario = funcionarioDAO.obtenerPorCedula(rs.getString("ced_funcionario"));

                String lugar = rs.getString("lugar");

                if (lugar != null) {
                    Incidencia inc = new Incidencia(
                            rs.getInt("id_instancia"),
                            rs.getBoolean("com_confidencial"),
                            rs.getString("titulo"),
                            rs.getTimestamp("fec_hora").toLocalDateTime(),
                            rs.getString("comentario"),
                            rs.getBoolean("estado"),
                            estudiante,
                            funcionario,
                            lugar
                    );
                    for (String inv : involucradoDAO.obtenerPorIncidencia(inc.getId())) {
                        inc.agregarInvolucrado(inv);
                    }
                    return inc;
                } else {
                    int idCategoria = rs.getInt("id_categoria");
                    Categoria categoria = rs.wasNull() ? null
                            : new Categoria(idCategoria, rs.getString("cat_nombre"), rs.getBoolean("cat_estado"));
                    return new InstanciaComun(
                            rs.getInt("id_instancia"),
                            rs.getBoolean("com_confidencial"),
                            rs.getString("titulo"),
                            rs.getTimestamp("fec_hora").toLocalDateTime(),
                            rs.getString("comentario"),
                            rs.getBoolean("estado"),
                            estudiante,
                            funcionario,
                            categoria
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Instancia> obtenerPorEstudiante(String cedula) {
        List<Instancia> lista = new ArrayList<>();
        String sql =
                "SELECT i.*, inc.lugar " +
                        "FROM instancias i " +
                        "LEFT JOIN incidencias inc ON i.id_instancia = inc.id_instancia " +
                        "WHERE i.ced_estudiante = ? AND i.estado = true " +
                        "ORDER BY i.fec_hora DESC";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Estudiante estudiante = estudianteDAO.obtenerPorCedula(rs.getString("ced_estudiante"));

                Funcionario funcionario = funcionarioDAO.obtenerPorCedula(rs.getString("ced_funcionario"));

                String lugar = rs.getString("lugar");
                Instancia inst;

                if (lugar != null) {
                    inst = new Incidencia(
                            rs.getInt("id_instancia"),
                            rs.getBoolean("com_confidencial"),
                            rs.getString("titulo"),
                            rs.getTimestamp("fec_hora").toLocalDateTime(),
                            rs.getString("comentario"),
                            rs.getBoolean("estado"),
                            estudiante,
                            funcionario,
                            lugar
                    );
                    for (String inv : involucradoDAO.obtenerPorIncidencia(inst.getId())) {
                        ((Incidencia) inst).agregarInvolucrado(inv);
                    }
                } else {
                    inst = new InstanciaComun(
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
                lista.add(inst);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Instancia> obtenerPorCategoria(int idCategoria) {
        List<Instancia> lista = new ArrayList<>();
        String sql =
                "SELECT i.*, ic.id_categoria " +
                        "FROM instancias i " +
                        "JOIN inst_comunes ic ON i.id_instancia = ic.id_instancia " +
                        "WHERE ic.id_categoria = ? AND i.estado = true " +
                        "ORDER BY i.fec_hora DESC";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Estudiante estudiante = estudianteDAO.obtenerPorCedula(rs.getString("ced_estudiante"));

                Funcionario funcionario = funcionarioDAO.obtenerPorCedula(rs.getString("ced_funcionario"));

                Categoria categoria = new Categoria(rs.getInt("id_categoria"), null, true);

                InstanciaComun inst = new InstanciaComun(
                        rs.getInt("id_instancia"),
                        rs.getBoolean("com_confidencial"),
                        rs.getString("titulo"),
                        rs.getTimestamp("fec_hora").toLocalDateTime(),
                        rs.getString("comentario"),
                        rs.getBoolean("estado"),
                        estudiante,
                        funcionario,
                        categoria
                );
                lista.add(inst);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
        return lista;
    }
    @Override
    public List<Instancia> buscarPorFecha(LocalDate fecha) {

        List<Instancia> lista = new ArrayList<>();

        String sql =
                "SELECT i.*, inc.lugar, com.id_categoria " +
                        "FROM instancias i " +
                        "LEFT JOIN incidencias inc ON i.id_instancia = inc.id_instancia " +
                        "LEFT JOIN inst_comunes com ON i.id_instancia = com.id_instancia " +
                        "WHERE DATE(i.fec_hora) = ? AND i.estado = true " +
                        "ORDER BY i.fec_hora DESC";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(fecha));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Estudiante estudiante = estudianteDAO.obtenerPorCedula(rs.getString("ced_estudiante"));

                Funcionario funcionario = funcionarioDAO.obtenerPorCedula(rs.getString("ced_funcionario"));

                String lugar = rs.getString("lugar");
                Instancia inst;

                if (lugar != null) {

                    inst = new Incidencia(
                            rs.getInt("id_instancia"),
                            rs.getBoolean("com_confidencial"),
                            rs.getString("titulo"),
                            rs.getTimestamp("fec_hora").toLocalDateTime(),
                            rs.getString("comentario"),
                            rs.getBoolean("estado"),
                            estudiante,
                            funcionario,
                            lugar
                    );
                    for (String inv : involucradoDAO.obtenerPorIncidencia(inst.getId())) {
                        ((Incidencia) inst).agregarInvolucrado(inv);
                    }

                } else {

                    int idCategoria = rs.getInt("id_categoria");
                    Categoria categoria = rs.wasNull() ? null : new Categoria(idCategoria, null, true);

                    inst = new InstanciaComun(
                            rs.getInt("id_instancia"),
                            rs.getBoolean("com_confidencial"),
                            rs.getString("titulo"),
                            rs.getTimestamp("fec_hora").toLocalDateTime(),
                            rs.getString("comentario"),
                            rs.getBoolean("estado"),
                            estudiante,
                            funcionario,
                            categoria
                    );

                }

                lista.add(inst);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public List<Instancia> buscarPorDescripcion(String descripcion) {

        List<Instancia> lista = new ArrayList<>();

        String sql =
                "SELECT i.*, inc.lugar, com.id_categoria " +
                        "FROM instancias i " +
                        "LEFT JOIN incidencias inc ON i.id_instancia = inc.id_instancia " +
                        "LEFT JOIN inst_comunes com ON i.id_instancia = com.id_instancia " +
                        "WHERE i.titulo ILIKE ? AND i.estado = true " +
                        "ORDER BY i.titulo";

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, "%" + descripcion + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Estudiante estudiante = estudianteDAO.obtenerPorCedula(rs.getString("ced_estudiante"));

                Funcionario funcionario = funcionarioDAO.obtenerPorCedula(rs.getString("ced_funcionario"));

                String lugar = rs.getString("lugar");
                Instancia inst;

                if (lugar != null) {

                    inst = new Incidencia(
                            rs.getInt("id_instancia"),
                            rs.getBoolean("com_confidencial"),
                            rs.getString("titulo"),
                            rs.getTimestamp("fec_hora").toLocalDateTime(),
                            rs.getString("comentario"),
                            rs.getBoolean("estado"),
                            estudiante,
                            funcionario,
                            lugar
                    );
                    for (String inv : involucradoDAO.obtenerPorIncidencia(inst.getId())) {
                        ((Incidencia) inst).agregarInvolucrado(inv);
                    }

                } else {

                    int idCategoria = rs.getInt("id_categoria");
                    Categoria categoria = rs.wasNull() ? null : new Categoria(idCategoria, null, true);

                    inst = new InstanciaComun(
                            rs.getInt("id_instancia"),
                            rs.getBoolean("com_confidencial"),
                            rs.getString("titulo"),
                            rs.getTimestamp("fec_hora").toLocalDateTime(),
                            rs.getString("comentario"),
                            rs.getBoolean("estado"),
                            estudiante,
                            funcionario,
                            categoria
                    );

                }

                lista.add(inst);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }

        return lista;
    }
}