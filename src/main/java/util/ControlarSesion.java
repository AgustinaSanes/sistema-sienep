package util;
import modelos.usuario.Usuario;
import java.time.LocalDateTime;

public class ControlarSesion {
    private static Usuario usuarioLogueado;
    public static void iniciarSesion(Usuario usuario) {
        usuarioLogueado = usuario;
    }
    public static void cerrarSesion() {usuarioLogueado = null;}
    public static boolean estaAutenticado() {
        return usuarioLogueado != null;
    }
    public static Usuario getUsuario() {
        return usuarioLogueado;
    }
}