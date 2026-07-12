package consola.Menu;

import controlador.InstanciaControlador;
import controlador.RecordatorioControlador;
import modelos.informe.InformeAdjunto;
import modelos.instancia.Instancia;
import modelos.recordatorio.Recordatorio;
import modelos.usuario.Estudiante;
import proxy.PermisosProxy;
import util.ControlarSesion;

import java.util.List;
import java.util.Scanner;

// Menú exclusivo para el ingreso de estudiante
public class MenuPrincipalEstudiante {

    private final Scanner sc = new Scanner(System.in);
    private final PermisosProxy proxy;
    private final InstanciaControlador instanciaControlador;
    private final RecordatorioControlador recordatorioControlador;

    public MenuPrincipalEstudiante(PermisosProxy proxy) {
        this.proxy = proxy;
        this.instanciaControlador = new InstanciaControlador(proxy);
        this.recordatorioControlador = new RecordatorioControlador(proxy);
    }

    public void mostrar() {

        int opcion;

        do {
            System.out.println("=== MI PERFIL (ESTUDIANTE) ===");
            System.out.println("1. Ver mis datos");
            System.out.println("2. Ver mis instancias");
            System.out.println("3. Ver detalle y seguimiento de una instancia");
            System.out.println("4. Ver mis informes");
            System.out.println("0. Cerrar sesión");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> verMisDatos();
                case 2 -> verMisInstancias();
                case 3 -> verDetalleInstancia();
                case 4 -> verMisInformes();
                case 0 -> {
                    ControlarSesion.cerrarSesion();
                    System.out.println("Cerrando sesión...");
                }
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // Submenus
    private String miCedula() {
        return ControlarSesion.getUsuario().getCedula();
    }

    private void verMisDatos() {
        try {
            Estudiante e = proxy.buscarEstudiantePorCedula(miCedula());
            if (e == null) {
                System.out.println("No se encontró tu ficha");
                return;
            }
            System.out.println("=== MIS DATOS ===");
            System.out.println("Cédula: " + e.getCedula());
            System.out.println("Nombre: " + e.getNombre() + " " + e.getApellido());
            System.out.println("Email: " + e.getEmail());
            System.out.println("Estado: " + (e.isEstado() ? "Activo" : "Inactivo"));
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void verMisInstancias() {
        try {
            List<Instancia> instancias = proxy.obtenerInstanciasPorEstudiante(miCedula());
            if (instancias.isEmpty()) {
                System.out.println("No tenés instancias registradas");
                return;
            }
            System.out.println("=== MIS INSTANCIAS ===");
            for (Instancia i : instancias) {
                instanciaControlador.mostrarInstancia(i);
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void verDetalleInstancia() {
        try {
            System.out.print("ID de instancia: ");
            int id = Integer.parseInt(sc.nextLine());

            Instancia instancia = proxy.obtenerInstanciaPorId(id);
            if (instancia == null) {
                System.out.println("Instancia no encontrada");
                return;
            }

            instanciaControlador.mostrarInstancia(instancia);

            List<Recordatorio> recordatorios = proxy.listarRecordatoriosPorInstancia(id);
            if (recordatorios.isEmpty()) {
                System.out.println("Sin seguimiento / recordatorios asociados");
            } else {
                System.out.println("--- Seguimiento / Recordatorios ---");
                for (Recordatorio r : recordatorios) {
                    recordatorioControlador.mostrarRecordatorio(r);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private void verMisInformes() {
        try {
            List<InformeAdjunto> informes = proxy.obtenerInformePorEstudiante(miCedula());
            if (informes.isEmpty()) {
                System.out.println("No tenés informes disponibles");
                return;
            }
            System.out.println("=== MIS INFORMES ===");
            for (InformeAdjunto inf : informes) {
                System.out.println("- " + inf.getNombre() + " [" + inf.getTipoArchivo() + "] (" + inf.getCategoria() + ")");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
