package consola.Menu;

import facade.SistemaFacade;
import modelos.usuario.Usuario;
import proxy.PermisosProxy;
import util.ControlarSesion;

import java.util.Scanner;

//Menus al ingresar por rol
public class MenuPrincipal implements MenuSistema {

    private final Scanner sc = new Scanner(System.in);

    private final MenuFuncionarios menuFuncionarios;
    private final MenuEstudiantes menuEstudiantes;
    private final MenuPerfiles menuPerfiles;
    private final MenuInstancias menuInstancias;
    private final MenuRecordatorios menuRecordatorios;
    private final MenuReportes menuReportes;
    private final MenuInstitucional menuInstitucional;
    private final MenuAuditorias menuAuditorias;
    private final MenuPrincipalEstudiante menuPrincipalEstudiante;

    public MenuPrincipal(SistemaFacade facade) {

        PermisosProxy proxy = new PermisosProxy(facade);

        menuFuncionarios = new MenuFuncionarios(proxy);
        menuEstudiantes = new MenuEstudiantes(proxy);
        menuPerfiles = new MenuPerfiles(proxy);
        menuInstancias = new MenuInstancias(proxy);
        menuRecordatorios = new MenuRecordatorios(proxy);
        menuReportes = new MenuReportes(proxy);
        menuInstitucional = new MenuInstitucional(proxy);
        menuAuditorias = new MenuAuditorias(proxy);
        menuPrincipalEstudiante = new MenuPrincipalEstudiante(proxy);
    }

    @Override
    public void mostrar(Usuario usuario) {

        String rol = usuario.getRol().getNombre();

        // Cada rol entra directo al menú que le corresponde
        if (rol.equalsIgnoreCase("Administrador")) {
            menuAdministrador();
        } else if (rol.equalsIgnoreCase("Psicopedagogo")
                || rol.equalsIgnoreCase("Analista de Equipo Educativo")) {
            menuStaffEducativo(rol);
        } else if (rol.equalsIgnoreCase("Funcionario UTEC")) {
            menuFuncionarioUtec();
        } else if (rol.equalsIgnoreCase("Estudiante")) {
            menuPrincipalEstudiante.mostrar();
        } else {
            System.out.println("Rol sin menú asignado: " + rol);
        }
    }

    // ADMINISTRADOR: acceso a todo el sistema
    private void menuAdministrador() {

        int opcion;

        do {
            System.out.println("=== MENÚ ADMINISTRADOR ===");
            System.out.println("1. Funcionarios");
            System.out.println("2. Estudiantes");
            System.out.println("3. Perfiles");
            System.out.println("4. Instancias");
            System.out.println("5. Recordatorios");
            System.out.println("6. Reportes");
            System.out.println("7. Institucional");
            System.out.println("8. Auditorías");
            System.out.println("0. Cerrar sesión");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> menuFuncionarios.mostrar();
                case 2 -> menuEstudiantes.mostrar();
                case 3 -> menuPerfiles.mostrar();
                case 4 -> menuInstancias.mostrar();
                case 5 -> menuRecordatorios.mostrar();
                case 6 -> menuReportes.mostrar();
                case 7 -> menuInstitucional.mostrar();
                case 8 -> menuAuditorias.mostrar();
                case 0 -> {
                    ControlarSesion.cerrarSesion();
                    System.out.println("Cerrando sesión...");
                }
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // PSICOPEDAGOGO / ANALISTA DE EQUIPO EDUCATIVO:
    // sin gestión administrativa (funcionarios, perfiles, institucional, auditorías)
    private void menuStaffEducativo(String rol) {

        int opcion;

        do {
            System.out.println("=== MENÚ " + rol.toUpperCase() + " ===");
            System.out.println("1. Estudiantes");
            System.out.println("2. Instancias");
            System.out.println("3. Recordatorios");
            System.out.println("4. Reportes");
            System.out.println("0. Cerrar sesión");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> menuEstudiantes.mostrar();
                case 2 -> menuInstancias.mostrar();
                case 3 -> menuRecordatorios.mostrar();
                case 4 -> menuReportes.mostrar();
                case 0 -> {
                    ControlarSesion.cerrarSesion();
                    System.out.println("Cerrando sesión...");
                }
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // FUNCIONARIO UTEC: mismos rubros que el staff educativo, pero solo de consulta
    // (escrituras bloqueadas por PermisosProxy si intenta)
    private void menuFuncionarioUtec() {

        int opcion;

        do {
            System.out.println("=== MENÚ FUNCIONARIO UTEC ===");
            System.out.println("1. Estudiantes");
            System.out.println("2. Instancias");
            System.out.println("3. Recordatorios");
            System.out.println("4. Reportes");
            System.out.println("0. Cerrar sesión");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> menuEstudiantes.mostrar();
                case 2 -> menuInstancias.mostrar();
                case 3 -> menuRecordatorios.mostrar();
                case 4 -> menuReportes.mostrar();
                case 0 -> {
                    ControlarSesion.cerrarSesion();
                    System.out.println("Cerrando sesión...");
                }
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }
}
