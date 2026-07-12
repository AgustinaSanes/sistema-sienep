package consola.Menu;

import controlador.InformeControlador;
import controlador.EstudianteControlador;
import proxy.PermisosProxy;

import java.util.Scanner;

// Menu de estudiante
public class MenuEstudiantes {

    private final Scanner sc = new Scanner(System.in);

    private final EstudianteControlador estudianteControlador;
    private final InformeControlador informeControlador;

    public MenuEstudiantes(PermisosProxy proxy) {
        estudianteControlador = new EstudianteControlador(proxy);
        informeControlador = new InformeControlador(proxy);
    }

    public void mostrar() {
        gestionEstudiantes();
    }

    private void gestionEstudiantes() {

        int opcion;

        do {
            System.out.println("--- ESTUDIANTES ---");
            System.out.println("1. Crear estudiante");
            System.out.println("2. Modificar estudiante");
            System.out.println("3. Crear instancia");
            System.out.println("4. Informes");
            System.out.println("5. Búsqueda");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> estudianteControlador.crearEstudiante();
                case 2 -> estudianteControlador.modificarEstudiante();
                case 3 -> estudianteControlador.instanciaFichaEstudiante();
                case 4 -> gestionarInformes();
                case 5 -> busquedaEstudiantes();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // Submenus
    private void gestionarInformes() {

        int opcion;

        do {
            System.out.println("--- INFORMES ADJUNTOS ---");
            System.out.println("1. Agregar informe");
            System.out.println("2. Modificar informe");
            System.out.println("3. Eliminar informe");
            System.out.println("4. Buscar informe por ID");
            System.out.println("5. Listar informes por estudiante");
            System.out.println("6. Listar todos los informes");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> informeControlador.agregarInforme();
                case 2 -> informeControlador.modificarInforme();
                case 3 -> informeControlador.eliminarInforme();
                case 4 -> informeControlador.buscarInforme();
                case 5 -> informeControlador.listarInformesPorEstudiante();
                case 6 -> informeControlador.listarInformes();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    private void busquedaEstudiantes() {

        int opcion;

        do {
            System.out.println("--- BÚSQUEDA DE ESTUDIANTES ---");
            System.out.println("1. Buscar por cédula");
            System.out.println("2. Buscar por nombre");
            System.out.println("3. Buscar por carrera");
            System.out.println("4. Buscar por grupo");
            System.out.println("5. Buscar por estado");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> estudianteControlador.buscarPorCedula();
                case 2 -> estudianteControlador.buscarPorNombre();
                case 3 -> estudianteControlador.buscarPorCarrera();
                case 4 -> estudianteControlador.buscarPorGrupo();
                case 5 -> estudianteControlador.buscarPorEstado();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }
}