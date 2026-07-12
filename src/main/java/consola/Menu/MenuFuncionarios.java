package consola.Menu;

import controlador.FuncionarioControlador;
import proxy.PermisosProxy;

import java.util.Scanner;

// Menu de funcionario
public class MenuFuncionarios {

    private final Scanner sc = new Scanner(System.in);
    private final FuncionarioControlador funcionarioControlador;

    public MenuFuncionarios(PermisosProxy proxy) {
        funcionarioControlador = new FuncionarioControlador(proxy);
    }

    public void mostrar() {

        int opcion;

        do {
            System.out.println("--- FUNCIONARIOS ---");
            System.out.println("1. Crear funcionario");
            System.out.println("2. Modificar funcionario");
            System.out.println("3. Buscar funcionario por cédula");
            System.out.println("4. Listar funcionarios");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> funcionarioControlador.crearFuncionario();
                case 2 -> funcionarioControlador.modificarFuncionario();
                case 3 -> funcionarioControlador.buscarFuncionario();
                case 4 -> funcionarioControlador.listarFuncionarios();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }
}
