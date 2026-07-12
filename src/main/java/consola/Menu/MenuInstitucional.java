package consola.Menu;

import controlador.InstitucionControlador;
import proxy.PermisosProxy;

import java.util.Scanner;

// Menu de institucion
public class MenuInstitucional {

    private final Scanner sc = new Scanner(System.in);
    private final InstitucionControlador institucionControlador;

    public MenuInstitucional(PermisosProxy proxy) {
        this.institucionControlador = new InstitucionControlador(proxy);
    }

    public void mostrar() {
        gestionInstitucional();
    }

    private void gestionInstitucional() {

        int opcion;

        do {
            System.out.println("--- INSTITUCIONAL ---");
            System.out.println("1. Gestión de ITR");
            System.out.println("2. Gestión de Carreras");
            System.out.println("3. Gestión de Grupos");
            System.out.println("4. Asociación ITR - Carrera");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> gestionITR();
                case 2 -> gestionCarreras();
                case 3 -> gestionGrupos();
                case 4 -> gestionAsociacionItrCarrera();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // Submenus
    private void gestionITR() {

        int opcion;

        do {
            System.out.println("--- ITR ---");
            System.out.println("1. Crear ITR");
            System.out.println("2. Modificar ITR");
            System.out.println("3. Desactivar ITR");
            System.out.println("4. Buscar ITR por ID");
            System.out.println("5. Listar ITRs");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> institucionControlador.crearITR();
                case 2 -> institucionControlador.modificarITR();
                case 3 -> institucionControlador.desactivarITR();
                case 4 -> institucionControlador.buscarITR();
                case 5 -> institucionControlador.listarITRs();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    private void gestionCarreras() {

        int opcion;

        do {
            System.out.println("--- CARRERAS ---");
            System.out.println("1. Crear carrera");
            System.out.println("2. Modificar carrera");
            System.out.println("3. Desactivar carrera");
            System.out.println("4. Buscar carrera por ID");
            System.out.println("5. Listar carreras");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> institucionControlador.crearCarrera();
                case 2 -> institucionControlador.modificarCarrera();
                case 3 -> institucionControlador.desactivarCarrera();
                case 4 -> institucionControlador.buscarCarrera();
                case 5 -> institucionControlador.listarCarreras();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    private void gestionAsociacionItrCarrera() {

        int opcion;

        do {
            System.out.println("--- ASOCIACIÓN ITR - CARRERA ---");
            System.out.println("1. Asociar carrera a un ITR");
            System.out.println("2. Desasociar carrera de un ITR");
            System.out.println("3. Listar carreras de un ITR");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> institucionControlador.asociarCarreraITR();
                case 2 -> institucionControlador.desasociarCarreraITR();
                case 3 -> institucionControlador.listarCarrerasDeITR();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    private void gestionGrupos() {

        int opcion;

        do {
            System.out.println("--- GRUPOS ---");
            System.out.println("1. Crear grupo");
            System.out.println("2. Modificar grupo");
            System.out.println("3. Desactivar grupo");
            System.out.println("4. Buscar grupo por ID");
            System.out.println("5. Listar grupos");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> institucionControlador.crearGrupo();
                case 2 -> institucionControlador.modificarGrupo();
                case 3 -> institucionControlador.desactivarGrupo();
                case 4 -> institucionControlador.buscarGrupo();
                case 5 -> institucionControlador.listarGrupos();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }
}