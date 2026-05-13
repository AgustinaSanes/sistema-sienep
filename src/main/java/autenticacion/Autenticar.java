package autenticacion;

import modelos.usuario.Usuario;
import servicios.autenticacion.AutenticacionService;
import util.ControlarSesion;
import consola.*;
import java.util.Scanner;

public class Autenticar {

    private final AutenticacionService autenticacionService =
            new AutenticacionService();

    private final Scanner sc = new Scanner(System.in);

    public void iniciar() {

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

    private void abrirMenu(Usuario u) {
        MenuSistema menuSistema = new Menu();
        menuSistema.mostrar(u);
    }
}