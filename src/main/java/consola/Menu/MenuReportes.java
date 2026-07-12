package consola.Menu;

import controlador.ReporteControlador;
import proxy.PermisosProxy;

import java.util.Scanner;

// Menu de reportes
public class MenuReportes {

    private final Scanner sc = new Scanner(System.in);
    private final ReporteControlador reporteControlador;

    public MenuReportes(PermisosProxy proxy) {
        this.reporteControlador = new ReporteControlador(proxy);
    }

    public void mostrar() {

        int opcion;

        do {

            System.out.println("--- REPORTES ---");
            System.out.println("1. Reporte por estudiante");
            System.out.println("2. Reporte general");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> reporteControlador.reporteEstudiante();
                case 2 -> reporteControlador.reporteGeneral();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }
}