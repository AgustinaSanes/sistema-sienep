package autenticacion;
import consola.Menu.MenuPrincipal;
import consola.Menu.MenuSistema;
import facade.SistemaFacade;
import modelos.usuario.*;
import observer.*;
import servicios.autenticacion.*;
import servicios.usuario.AuditoriaService;
import util.ControlarSesion;

import java.util.Scanner;

public class Autenticar {
    private static final int INTENTOS_MAXIMOS = 3;
    private final AutenticacionService autenticacionService = new AutenticacionService();
    private final AuditoriaService auditoriaService = new AuditoriaService();
    private final Scanner sc = new Scanner(System.in);

    // Inicia el sistema y la autenticacion
    public void iniciar() {
        System.out.println("======================================");
        System.out.println("    BIENVENIDO A SISTEMA SIENEP     ");
        System.out.println("======================================");

        for (int intento = 1; intento <= INTENTOS_MAXIMOS; intento++) {

            System.out.print("Usuario: ");
            String cedula = sc.nextLine();

            System.out.print("Contraseña: ");
            String contra = sc.nextLine();

            if (cedula == null || cedula.trim().isEmpty()) {
                System.out.println("El usuario es obligatorio");
                continue;
            }
            if (contra == null || contra.trim().isEmpty()) {
                System.out.println("La contraseña es obligatoria");
                continue;
            }

            try {
                Usuario u = autenticacionService.login(cedula, contra);

                ControlarSesion.iniciarSesion(u);

                registrarAuditoriaLogin(cedula, "LOGIN_EXITOSO", "Inicio de sesión exitoso");

                System.out.println(
                        "Bienvenido/a " + u.getNombre() + " " + u.getApellido()
                );

                SistemaFacade facade = new SistemaFacade();
                AuditoriaObserver auditoriaObserver = new AuditoriaObserver();
                EmailObserver emailObserver = new EmailObserver();

                facade.agregarObserver(auditoriaObserver);
                facade.agregarObserver(emailObserver);

                abrirMenu(u, facade);

                facade.quitarObserver(auditoriaObserver);
                facade.quitarObserver(emailObserver);

                return;

            } catch (RuntimeException e) {
                registrarAuditoriaLogin(cedula, "LOGIN_FALLIDO", e.getMessage());
                int restantes = INTENTOS_MAXIMOS - intento;
                if (restantes > 0) {
                    System.out.println(e.getMessage() + " (te quedan " + restantes + " intento(s))");
                } else {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println("Se superó el número máximo de intentos. Cerrando el sistema.");
    }

    // Registra el intentos/login en auditoria
    private void registrarAuditoriaLogin(String cedula, String evento, String detalle) {
        try {
            auditoriaService.registrar(cedula, evento, detalle, "usuarios");
        } catch (Exception ex) {
            System.out.println("Aviso: no se pudo registrar el intento en auditoría");
        }
    }

    // Abre menu
    private void abrirMenu(Usuario u, SistemaFacade facade) {
        MenuSistema menuSistema = new MenuPrincipal(facade);
        menuSistema.mostrar(u);
    }
}
