package autenticacion;
import facade.SistemaFacade;
import modelos.usuario.*;
import observer.*;
import servicios.autenticacion.*;
import util.ControlarSesion;
import consola.*;
import java.util.Scanner;

public class Autenticar {
    private final AutenticacionService autenticacionService = new AutenticacionService();
    private final Scanner sc = new Scanner(System.in);
    private final SistemaFacade facade = new SistemaFacade();

    public void iniciar() {
        // Registrar observers
        facade.agregarObserver(new AuditoriaObserver());
        facade.agregarObserver(new EmailObserver());

        System.out.println("======================================");
        System.out.println("    BIENVENIDO A SISTEMA SIENEP     ");
        System.out.println("======================================");

        System.out.print("Cédula: ");
        String cedula = sc.nextLine();

        System.out.print("Contraseña: ");
        String contra = sc.nextLine();

        try {
            // Login
            Usuario u = autenticacionService.login(cedula, contra);

            // Control de sesión
            ControlarSesion.iniciarSesion(u);

            System.out.println(
                    "Bienvenido/a " + u.getNombre() + " " + u.getApellido()
            );

            abrirMenu(u);

        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    //Metodo mostrar menu
    private void abrirMenu(Usuario u) {
        MenuSistema menuSistema = new Menu();
        menuSistema.mostrar(u);
    }
}