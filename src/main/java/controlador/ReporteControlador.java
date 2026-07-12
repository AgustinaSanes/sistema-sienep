package controlador;

import factory.factoryMethod.exportador.*;
import proxy.PermisosProxy;

import java.util.Scanner;

public class ReporteControlador {
    private final PermisosProxy proxy;
    private final Scanner sc = new Scanner(System.in);

    public ReporteControlador(PermisosProxy proxy) {
        this.proxy = proxy;
    }

    public void reporteEstudiante() {
        try {
            System.out.print("Cédula del estudiante: ");

            String cedula = sc.nextLine();

            String contenido = proxy.generarReporteEstudiante(cedula);
            reporteSalida(contenido);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void reporteGeneral() {
        try {
            System.out.print("Filtrar por rol (Enter para mostrar todos): ");
            String filtroRol = sc.nextLine();

            String contenido = proxy.generarReporteGeneral(filtroRol);

            reporteSalida(contenido);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void reporteSalida(String contenido) {

        int opcion;

        do {
            System.out.println("--- SALIDA DEL REPORTE ---");
            System.out.println("1. Ver en pantalla");
            System.out.println("2. Exportar a PDF");
            System.out.println("3. Exportar a CSV");
            System.out.println("4. Exportar a Excel");
            System.out.println("0. Volver");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> System.out.println(contenido);
                case 2 -> exportar(new PDFExportadorFactory(), contenido);
                case 3 -> exportar(new CSVExportadorFactory(), contenido);
                case 4 -> exportar(new ExcelExportadorFactory(), contenido);
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    private void exportar(ExportadorFactory factory, String datos) {
        Exportador exportador = factory.crearExportador();
        exportador.exportar(datos);
    }
}
