package factory;
import dao.instancia.*;
import dao.usuario.EstudianteDAOImpl;
import modelos.instancia.Instancia;
import modelos.usuario.Estudiante;
import java.util.List;

public class ReporteEstudiante implements Reporte{

    @Override
    public void generar(String cedula) {
        EstudianteDAOImpl estudianteDAO = new EstudianteDAOImpl();
        InstanciaDAOImpl instanciaDAO = new InstanciaDAOImpl();

        Estudiante e = estudianteDAO.obtenerPorCedula(cedula);
        if (e == null) {
            System.out.println("Estudiante no encontrado");
            return;
        }

        System.out.println("========================================");
        System.out.println("REPORTE DE ESTUDIANTE");
        System.out.println("========================================");
        System.out.println("Cédula: " + e.getCedula());
        System.out.println("Nombre: " + e.getNombre() + " " + e.getApellido());
        System.out.println("Email: " + e.getEmail());
        System.out.println("Sistema salud: " + e.getSistemaSalud());
        System.out.println("----------------------------------------");
        System.out.println("INSTANCIAS:");

        List<Instancia> instancias = instanciaDAO.obtenerPorEstudiante(cedula);
        if (instancias.isEmpty()) {
            System.out.println("Sin instancias registradas");
        } else {
            for (Instancia i : instancias) {
                System.out.println("  - " + i.getTitulo() + " | " + i.getFechaHora());
            }
        }
        System.out.println("========================================");
    }
}