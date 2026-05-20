package controlador;

import factory.Reporte;
import factory.ReporteFactory;

import java.util.Scanner;

public class ReporteControlador {
    private final Scanner sc = new Scanner(System.in);

    public void reporteEstudiante() {
        try {
            System.out.print("Cédula del estudiante: ");

            String cedula = sc.nextLine();

            Reporte reporte = ReporteFactory.crearReporte("estudiante");
            reporte.generar(cedula);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void reporteGeneral() {
        try {
            Reporte reporte = ReporteFactory.crearReporte("general");
            reporte.generar("");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
