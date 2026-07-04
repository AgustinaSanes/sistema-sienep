package consola.Menu;

import facade.SistemaFacade;
import modelos.usuario.Usuario;
import proxy.PermisosProxy;
import util.ControlarSesion;

import java.util.Scanner;

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

    public MenuPrincipal(SistemaFacade facade) {

        PermisosProxy proxy = new PermisosProxy(facade);

        menuFuncionarios = new MenuFuncionarios(proxy);
        menuEstudiantes = new MenuEstudiantes(proxy);
        menuPerfiles = new MenuPerfiles(proxy);
        menuInstancias = new MenuInstancias(proxy);
        menuRecordatorios = new MenuRecordatorios(proxy);
        menuReportes = new MenuReportes();
        menuInstitucional = new MenuInstitucional();
        menuAuditorias = new MenuAuditorias();
    }

    @Override
    public void mostrar(Usuario usuario) {

        int opcion;

        do {
            System.out.println("=== MENÚ ===");
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
}