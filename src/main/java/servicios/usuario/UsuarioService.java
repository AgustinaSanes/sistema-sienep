package servicios.usuario;
import dao.usuario.*;
import modelos.usuario.Estudiante;
import modelos.usuario.Usuario;
import util.EncriptarContra;
import util.ValidarCedula;
import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final EstudianteDAO estudianteDAO;
    private final FuncionarioDAO funcionarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAOImpl();
        this.estudianteDAO = new EstudianteDAOImpl();
        this.funcionarioDAO = new FuncionarioDAOImpl();
    }

    //Validar usuario
    private void validarUsuario(Usuario usuario) {
        //Usuario
        if (usuario == null) {
            throw new RuntimeException("El usuario no puede ser nulo");
        }
        // Cedula
        if (!ValidarCedula.esValida(usuario.getCedula())) {
            throw new RuntimeException("La cédula no es válida");
        }
        // Rol
        if (usuario.getRol() == null) {
            throw new RuntimeException("El rol es obligatorio");
        }
        if (usuario.getRol().getId() <= 0) {
            throw new RuntimeException("Rol inválido");
        }
        // Nombre
        if (usuario.getNombre() == null ||
                usuario.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (usuario.getNombre().length() > 50) {
            throw new RuntimeException("El nombre no puede superar los 50 caracteres");
        }
        // Apellido
        if (usuario.getApellido() == null ||
                usuario.getApellido().trim().isEmpty()) {

            throw new RuntimeException("El apellido es obligatorio");
        }
        if (usuario.getApellido().length() > 50) {
            throw new RuntimeException("El apellido no puede superar los 50 caracteres");
        }
        // Email
        if (usuario.getEmail() == null ||
                usuario.getEmail().trim().isEmpty()) {
            throw new RuntimeException("El email es obligatorio");
        }
        if (usuario.getEmail().length() > 100) {
            throw new RuntimeException("El email no puede superar los 100 caracteres");
        }
        if (!usuario.getEmail().matches(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("Formato de email inválido");
        }
        // Contraseña
        if (usuario.getContrasena() == null ||
                usuario.getContrasena().trim().isEmpty()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }
        if (usuario.getContrasena().length() > 8) {
            throw new RuntimeException("La contraseña no puede superar los 8 caracteres");
        }
    }

    //Agregar usuario
    public void agregarUsuario(Usuario usuario) {

        validarUsuario(usuario);

        if (usuarioDAO.buscarPorCedula(usuario.getCedula()) != null) {
            throw new RuntimeException("Ya existe un usuario con esa cédula");
        }

        // Encriptar contraseña antes de guardar
        usuario.setContrasena(
                EncriptarContra.encriptar(usuario.getContrasena())
        );
        usuarioDAO.agregarUsuario(usuario);
    }

    //Validar actualizacion del usuario
    private void validarUsuarioActualizacion(Usuario usuario) {
        // Usuario
        if (usuario == null) {
            throw new RuntimeException("El usuario no puede ser nulo");
        }
        // Cédula
        if (!ValidarCedula.esValida(usuario.getCedula())) {
            throw new RuntimeException("La cédula no es válida");
        }
        // Rol
        if (usuario.getRol() == null) {
            throw new RuntimeException("El rol es obligatorio");
        }
        if (usuario.getRol().getId() <= 0) {
            throw new RuntimeException("Rol inválido");
        }
        // Nombre
        if (usuario.getNombre() == null ||
                usuario.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (usuario.getNombre().length() > 50) {
            throw new RuntimeException("El nombre no puede superar los 50 caracteres");
        }
        // Apellido
        if (usuario.getApellido() == null ||
                usuario.getApellido().trim().isEmpty()) {
            throw new RuntimeException("El apellido es obligatorio");
        }
        if (usuario.getApellido().length() > 50) {
            throw new RuntimeException("El apellido no puede superar los 50 caracteres");
        }
        // Email
        if (usuario.getEmail() == null ||
                usuario.getEmail().trim().isEmpty()) {
            throw new RuntimeException("El email es obligatorio");
        }
        if (usuario.getEmail().length() > 100) {
            throw new RuntimeException("El email no puede superar los 100 caracteres");
        }
        if (!usuario.getEmail().matches(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("Formato de email inválido");
        }
    }

    //Actualizar usuario
    public void actualizarUsuario(Usuario usuario) {

        validarUsuarioActualizacion(usuario);

        Usuario existente = usuarioDAO.buscarPorCedula(usuario.getCedula());

        if (existente == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Mantener contraseña actual
        usuario.setContrasena(existente.getContrasena());

        usuarioDAO.actualizarUsuario(usuario);
    }

    //Baja lógica
    public void eliminarUsuario(String cedula) {

        if (!ValidarCedula.esValida(cedula)) {
            throw new RuntimeException("Cédula inválida");
        }
        usuarioDAO.eliminarUsuario(cedula);
    }

    // Buscar por cédula
    public Usuario buscarPorCedula(String cedula) {

        if (!ValidarCedula.esValida(cedula)) {
            throw new RuntimeException("Cédula inválida");
        }

        Estudiante estudiante = estudianteDAO.obtenerPorCedula(cedula);
        if (estudiante != null) {return estudiante;}

        return funcionarioDAO.obtenerPorCedula(cedula);

    }

    // Obtener todos
    public List<Usuario> obtenerTodos() {
        return usuarioDAO.obtenerTodos();
    }
}