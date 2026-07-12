package consola.Menu;

import controlador.RecordatorioControlador;
import controlador.FrecuenciaControlador;
import proxy.PermisosProxy;

import java.util.Scanner;

// Menu de recordatorios
public class MenuRecordatorios {

    private final Scanner sc = new Scanner(System.in);

    private final RecordatorioControlador recordatorioControlador;
    private final FrecuenciaControlador frecuenciaControlador;


    public MenuRecordatorios(PermisosProxy proxy) {

        recordatorioControlador = new RecordatorioControlador(proxy);
        frecuenciaControlador = new FrecuenciaControlador(proxy);

    }

    public void mostrar() {

        int opcion;

        do {

            System.out.println("--- RECORDATORIOS ---");
            System.out.println("1. Crear recordatorio");
            System.out.println("2. Modificar recordatorio");
            System.out.println("3. Desactivar recordatorio");
            System.out.println("4. Buscar por ID");
            System.out.println("5. Listar por instancia");
            System.out.println("6. Categorías de recordatorio");
            System.out.println("7. Frecuencias");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> recordatorioControlador.crearRecordatorio();
                case 2 -> recordatorioControlador.modificarRecordatorio();
                case 3 -> recordatorioControlador.desactivarRecordatorio();
                case 4 -> recordatorioControlador.buscarRecordatorio();
                case 5 -> recordatorioControlador.listarRecordatoriosPorInstancia();
                case 6 -> gestionCategorias();
                case 7 -> gestionFrecuencias();
                case 0 -> System.out.println("Volviendo...");

                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // Submenus
    private void gestionCategorias() {

        int opcion;

        do {

            System.out.println("--- CATEGORÍAS DE RECORDATORIO ---");
            System.out.println("1. Agregar categoría");
            System.out.println("2. Modificar categoría");
            System.out.println("3. Eliminar categoría");
            System.out.println("4. Buscar categoría");
            System.out.println("5. Listar categorías");
            System.out.println("0. Volver");

            System.out.print("Opción: ");


            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }


            switch (opcion) {

                case 1 -> recordatorioControlador.agregarCategoriaRecordatorio();
                case 2 -> recordatorioControlador.modificarCategoriaRecordatorio();
                case 3 -> recordatorioControlador.eliminarCategoriaRecordatorio();
                case 4 -> recordatorioControlador.buscarCategoriaRecordatorio();
                case 5 -> recordatorioControlador.listarCategoriasRecordatorio();

                case 0 -> System.out.println("Volviendo...");

                default -> System.out.println("Opción inválida");
            }


        } while (opcion != 0);
    }

    private void gestionFrecuencias() {

        int opcion;

        do {

            System.out.println("--- FRECUENCIAS ---");
            System.out.println("1. Agregar frecuencia");
            System.out.println("2. Modificar frecuencia");
            System.out.println("3. Desactivar frecuencia");
            System.out.println("4. Buscar frecuencia");
            System.out.println("5. Listar frecuencias");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {

                case 1 -> frecuenciaControlador.agregarFrecuencia();
                case 2 -> frecuenciaControlador.modificarFrecuencia();
                case 3 -> frecuenciaControlador.eliminarFrecuencia();
                case 4 -> frecuenciaControlador.obtenerPorId();
                case 5 -> frecuenciaControlador.listarFrecuencias();
                case 0 -> System.out.println("Volviendo...");

                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }
}