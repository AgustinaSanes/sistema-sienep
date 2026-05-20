package factory;
import dao.usuario.UsuarioDAOImpl;
import modelos.usuario.Usuario;
import java.util.List;

public class ReporteGeneral implements Reporte {

    @Override
    public void generar(String parametro) {
        UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();
        List<Usuario> usuarios = usuarioDAO.obtenerTodos();

        System.out.println("========================================");
        System.out.println("REPORTE GENERAL DEL SISTEMA");
        System.out.println("========================================");
        System.out.println("Total usuarios activos: " + usuarios.size());
        System.out.println("----------------------------------------");
        for (Usuario u : usuarios) {
            System.out.println(u.getCedula() + " | " +
                    u.getNombre() + " " + u.getApellido() + " | " +
                    u.getRol().getNombre());
        }
        System.out.println("========================================");
    }
}