package consola.Menu;

import controlador.PerfilControlador;
import proxy.PermisosProxy;

import java.util.Scanner;

// Menu de perfiles
public class MenuPerfiles {

    private final Scanner sc = new Scanner(System.in);
    private final PerfilControlador perfilControlador;

    public MenuPerfiles(PermisosProxy proxy) {
        this.perfilControlador = new PerfilControlador(proxy);
    }

    public void mostrar() {
        gestionPerfiles();
    }

    private void gestionPerfiles() {

        int opcion;

        do {
            System.out.println("=== PERFILES ===");
            System.out.println("1. Gestión de roles");
            System.out.println("2. Gestión de permisos");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> gestionRoles();
                case 2 -> gestionPermisos();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // Submenus
    private void gestionRoles() {

        int opcion;

        do {
            System.out.println("--- ROLES ---");
            System.out.println("1. Crear rol");
            System.out.println("2. Modificar rol");
            System.out.println("3. Eliminar rol");
            System.out.println("4. Buscar rol por ID");
            System.out.println("5. Listar roles");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> perfilControlador.crearRol();
                case 2 -> perfilControlador.modificarRol();
                case 3 -> perfilControlador.eliminarRol();
                case 4 -> perfilControlador.buscarRol();
                case 5 -> perfilControlador.listarRoles();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    private void gestionPermisos() {

        int opcion;

        do {
            System.out.println("--- PERMISOS ---");
            System.out.println("1. Crear permiso");
            System.out.println("2. Modificar permiso");
            System.out.println("3. Eliminar permiso");
            System.out.println("4. Buscar permiso");
            System.out.println("5. Listar permisos");
            System.out.println("6. Asignar permiso a rol");
            System.out.println("7. Quitar permiso de rol");
            System.out.println("8. Ver permisos de un rol");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> perfilControlador.crearPermiso();
                case 2 -> perfilControlador.modificarPermiso();
                case 3 -> perfilControlador.eliminarPermiso();
                case 4 -> perfilControlador.buscarPermiso();
                case 5 -> perfilControlador.listarPermisos();
                case 6 -> perfilControlador.asignarPermiso();
                case 7 -> perfilControlador.quitarPermiso();
                case 8 -> perfilControlador.obtenerPermisosRol();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }
}