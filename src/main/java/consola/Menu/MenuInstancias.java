package consola.Menu;

import controlador.InstanciaControlador;
import proxy.PermisosProxy;

import java.util.Scanner;

// Menu de instancia
public class MenuInstancias {

    private final Scanner sc = new Scanner(System.in);
    private final InstanciaControlador instanciaControlador;

    public MenuInstancias(PermisosProxy proxy) {
        instanciaControlador = new InstanciaControlador(proxy);
    }

    public void mostrar() {
        gestionInstancias();
    }

    private void gestionInstancias() {

        int opcion;

        do {
            System.out.println("--- INSTANCIAS ---");
            System.out.println("1. Instancia común");
            System.out.println("2. Incidencia");
            System.out.println("3. Búsqueda");
            System.out.println("4. Categorías");

            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> gestionComun();
                case 2 -> gestionIncidencia();
                case 3 -> busqueda();
                case 4 -> gestionCategorias();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // Submenus
    private void gestionComun() {
        int opcion;
        do {
            System.out.println("--- INSTANCIA COMÚN ---");
            System.out.println("1. Crear instancia");
            System.out.println("2. Modificar instancia");
            System.out.println("3. Desactivar instancia");
            System.out.println("4. Clonar instancia");
            System.out.println("5. Categorizar instancia");
            System.out.println("0. Volver");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> instanciaControlador.crearInstanciaComun();
                case 2 -> instanciaControlador.modificarInstanciaComun();
                case 3 -> instanciaControlador.desactivarInstancia();
                case 4 -> instanciaControlador.clonarInstancia();
                case 5 -> instanciaControlador.categorizarInstancia();

                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }

    private void gestionCategorias() {

        int opcion;

        do {

            System.out.println("--- CATEGORÍAS ---");
            System.out.println("1. Agregar categoría");
            System.out.println("2. Modificar categoría");
            System.out.println("3. Eliminar categoría");
            System.out.println("4. Buscar categoría por ID");
            System.out.println("5. Listar categorías");
            System.out.println("6. Asignar categoría a instancia");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> instanciaControlador.agregarCategoria();
                case 2 -> instanciaControlador.modificarCategoria();
                case 3 -> instanciaControlador.eliminarCategoria();
                case 4 -> instanciaControlador.buscarCategoria();
                case 5 -> instanciaControlador.listarCategorias();
                case 6 -> instanciaControlador.categorizarInstancia();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    private void gestionIncidencia() {
        int opcion;
        do {
            System.out.println("--- INCIDENCIAS ---");
            System.out.println("1. Crear incidencia");
            System.out.println("2. Modificar incidencia");
            System.out.println("3. Desactivar incidencia");
            System.out.println("4. Gestionar involucrados");

            System.out.println("0. Volver");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> instanciaControlador.crearIncidencia();
                case 2 -> instanciaControlador.modificarIncidencia();
                case 3 -> instanciaControlador.desactivarInstancia();
                case 4 -> gestionInvolucrados();

                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }

    private void gestionInvolucrados() {
        int opcion;
        do {
            System.out.println("--- INVOLUCRADOS ---");
            System.out.println("1. Agregar involucrado");
            System.out.println("2. Eliminar involucrado");
            System.out.println("3. Ver involucrados");
            System.out.println("0. Volver");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> instanciaControlador.agregarInvolucrado();
                case 2 -> instanciaControlador.eliminarInvolucrado();
                case 3 -> instanciaControlador.verInvolucrados();

                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }

    private void busqueda() {
        int opcion;
        do {
            System.out.println("--- BÚSQUEDA DE INSTANCIAS ---");
            System.out.println("1. Buscar instancia por ID");
            System.out.println("2. Listar instancias de un estudiante");
            System.out.println("3. Buscar instancias por fecha");
            System.out.println("4. Buscar instancias por descripción");
            System.out.println("5. Filtrar por categoría");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }
            switch (opcion) {

                case 1 -> instanciaControlador.buscarInstancia();
                case 2 -> instanciaControlador.listarInstanciasPorEstudiante();
                case 3 -> instanciaControlador.buscarInstanciasPorFecha();
                case 4 -> instanciaControlador.buscarInstanciasPorDescripcion();
                case 5 -> instanciaControlador.filtrarPorCategoria();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }
}