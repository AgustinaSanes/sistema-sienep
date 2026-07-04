package factory.factoryMethod.reporte;

import dao.instancia.InstanciaDAOImpl;
import dao.usuario.EstudianteDAOImpl;
import modelos.instancia.Instancia;
import modelos.usuario.Estudiante;
import java.util.List;

public class ReporteEstudiante implements Reporte {

    @Override
    public String generar(String cedula) {
        EstudianteDAOImpl estudianteDAO = new EstudianteDAOImpl();
        InstanciaDAOImpl instanciaDAO = new InstanciaDAOImpl();

        Estudiante e = estudianteDAO.obtenerPorCedula(cedula);

        if (e == null) {
            return "Estudiante no encontrado";
        }

        List<Instancia> instancias = instanciaDAO.obtenerPorEstudiante(cedula);

        String reporte = "========================================\n";
        reporte += "REPORTE DE ESTUDIANTE\n";
        reporte += "========================================\n";
        reporte += "Cédula: " + e.getCedula() + "\n";
        reporte += "Nombre: " + e.getNombre() + " " + e.getApellido() + "\n";
        reporte += "Email: " + e.getEmail() + "\n";
        reporte += "Sistema salud: " + e.getSistemaSalud() + "\n";
        reporte += "----------------------------------------\n";
        reporte += "INSTANCIAS:\n";

        if (instancias.isEmpty()) {
            reporte += "Sin instancias registradas\n";
        } else {
            for (Instancia i : instancias) {
                reporte += "- " + i.getTitulo() + " | " + i.getFechaHora() + "\n";
            }
        }

        reporte += "========================================";

        return reporte;
    }
}