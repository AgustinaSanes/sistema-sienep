package autenticacion;
import consola.Menu.MenuPrincipal;
import consola.Menu.MenuSistema;
import facade.SistemaFacade;
import modelos.usuario.*;
import observer.*;
import servicios.autenticacion.*;
import util.ControlarSesion;

import java.util.Scanner;

public class Autenticar {
    private final AutenticacionService autenticacionService = new AutenticacionService();
    private final Scanner sc = new Scanner(System.in);

    public void iniciar() {
        System.out.println("======================================");
        System.out.println("    BIENVENIDO A SISTEMA SIENEP     ");
        System.out.println("======================================");

        System.out.print("Usuario: ");
        String cedula = sc.nextLine();

        System.out.print("Contraseña: ");
        String contra = sc.nextLine();

        try {
            Usuario u = autenticacionService.login(cedula, contra);

            ControlarSesion.iniciarSesion(u);

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

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void abrirMenu(Usuario u, SistemaFacade facade) {
        MenuSistema menuSistema = new MenuPrincipal(facade);
        menuSistema.mostrar(u);
    }
}
