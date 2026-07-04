package factory.factoryMethod.reporte;

import dao.usuario.UsuarioDAOImpl;
import modelos.usuario.Usuario;
import java.util.List;

public class ReporteGeneral implements Reporte {

    @Override
    public String generar(String r) {
        UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();
        List<Usuario> usuarios = usuarioDAO.obtenerTodos();

        String reporte = "========================================\n";
        reporte += "REPORTE GENERAL DEL SISTEMA\n";
        reporte += "========================================\n";
        reporte += "Total usuarios activos: " + usuarios.size() + "\n";
        reporte += "----------------------------------------\n";

        for (Usuario u : usuarios) {
            reporte += u.getCedula() + " | "
                    + u.getNombre() + " "
                    + u.getApellido() + " | "
                    + u.getRol().getNombre() + "\n";
        }

        reporte += "========================================";

        return reporte;
    }
}