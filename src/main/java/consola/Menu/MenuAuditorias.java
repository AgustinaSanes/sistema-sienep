package consola.Menu;
import controlador.AuditoriaControlador;
import proxy.PermisosProxy;
import java.util.Scanner;

// Menu de auditoria
public class MenuAuditorias {
    private final Scanner sc = new Scanner(System.in);
    private final AuditoriaControlador auditoriaControlador;

    public MenuAuditorias(PermisosProxy proxy) {
        this.auditoriaControlador = new AuditoriaControlador(proxy);
    }

    public void mostrar() {

        int opcion;

        do {
            System.out.println("--- AUDITORÍAS ---");
            System.out.println("1. Listar auditorías");
            System.out.println("2. Buscar por usuario");
            System.out.println("3. Buscar por fecha");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> auditoriaControlador.listarAuditorias();
                case 2 -> auditoriaControlador.buscarPorUsuario();
                case 3 -> auditoriaControlador.buscarPorFecha();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }
}
