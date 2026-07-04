package controlador;

import factory.abstractFactory.UsuarioFactory;
import modelos.usuario.*;
import proxy.PermisosProxy;
import servicios.usuario.RolService;
import java.util.*;

public class FuncionarioControlador {
    private final PermisosProxy proxy;
    private final RolService rolService = new RolService();
    private final Scanner sc = new Scanner(System.in);

    public FuncionarioControlador(PermisosProxy proxy){
        this.proxy = proxy;
    }

    public void crearFuncionario() {
        try {
            System.out.println("--- CREAR FUNCIONARIO ---");

            System.out.print("Cédula: ");
            String cedula = sc.nextLine();

            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            System.out.print("Apellido: ");
            String apellido = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Contraseña: ");
            String contrasena = sc.nextLine();

            List<Rol> roles = rolService.obtenerTodos();

            System.out.println("--- ROLES DISPONIBLES ---");

            for (Rol r : roles) {
                if (r.isEstado() && !r.getNombre().equalsIgnoreCase("Estudiante")) {
                    System.out.println(r.getId() + ". " + r.getNombre());
                }
            }

            System.out.print("Seleccione rol: ");

            Rol rol = rolService.obtenerPorId(
                    Integer.parseInt(sc.nextLine())
            );

            if (rol == null || !rol.isEstado() || rol.getNombre().equalsIgnoreCase("Estudiante")) {
                System.out.println("Rol inválido");
                return;
            }

            //FACTORY
            UsuarioFactory factory = new UsuarioFactory();
            Funcionario funcionario = (Funcionario) factory.crearUsuario("Funcionario");

            funcionario.setCedula(cedula);
            funcionario.setNombre(nombre);
            funcionario.setApellido(apellido);
            funcionario.setEmail(email);
            funcionario.setContrasena(contrasena);
            funcionario.setEstado(true);
            funcionario.setRol(rol);

            //PROXY
            proxy.crearFuncionario(funcionario);
            System.out.println("Funcionario creado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarFuncionario() {
        try {
            System.out.println("--- MODIFICAR FUNCIONARIO ---");

            System.out.print("Ingrese cédula: ");
            String cedula = sc.nextLine();

            Usuario usuario = proxy.buscarUsuario(cedula);
            if (usuario == null) {
                System.out.println("Funcionario no encontrado");
                return;
            }

            int opcion;
            do {
                System.out.println("--- ¿QUÉ DESEA MODIFICAR? ---");
                System.out.println("1. Nombre    [" + usuario.getNombre() + "]");
                System.out.println("2. Apellido  [" + usuario.getApellido() + "]");
                System.out.println("3. Email     [" + usuario.getEmail() + "]");
                System.out.println("0. Guardar y volver");
                System.out.print("Opción: ");

                try {
                    opcion = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    opcion = -1;
                }

                switch (opcion) {
                    case 1 -> {
                        System.out.print("Nuevo nombre: ");
                        usuario.setNombre(sc.nextLine());
                    }
                    case 2 -> {
                        System.out.print("Nuevo apellido: ");
                        usuario.setApellido(sc.nextLine());
                    }
                    case 3 -> {
                        System.out.print("Nuevo email: ");
                        usuario.setEmail(sc.nextLine());
                    }
                    case 0 -> System.out.println("Guardando...");
                    default -> System.out.println("Opción inválida");
                }

            } while (opcion != 0);

            proxy.modificarUsuario(usuario);
            System.out.println("Funcionario actualizado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public void desactivarFuncionario() {
        try {
            System.out.println("--- DESACTIVAR FUNCIONARIO ---");

            System.out.print("Ingrese cédula: ");
            String cedula = sc.nextLine();

            Usuario usuario = proxy.buscarUsuario(cedula);
            if (usuario == null) {
                System.out.println("Funcionario no encontrado");
                return;
            }

            proxy.desactivarUsuario(cedula);
            System.out.println("Funcionario desactivado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarFuncionario() {
        try {
            System.out.println("--- BUSCAR FUNCIONARIO ---");

            System.out.print("Ingrese cédula: ");
            String cedula = sc.nextLine();

            Usuario usuario = proxy.buscarUsuario(cedula);
            if (usuario == null) {
                System.out.println("Funcionario no encontrado");
                return;
            }

            System.out.println("=== FUNCIONARIO ===");
            System.out.println("Cédula: " + usuario.getCedula());
            System.out.println("Nombre: " + usuario.getNombre());
            System.out.println("Apellido: " + usuario.getApellido());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Rol: " + usuario.getRol().getNombre());
            System.out.println("Estado: " + (usuario.isEstado() ? "Activo" : "Inactivo"));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarFuncionarios() {
        try {
            List<Usuario> usuarios = proxy.listarUsuarios();

            if (usuarios.isEmpty()) {
                System.out.println("No hay funcionarios registrados");
                return;
            }

            System.out.println("=== FUNCIONARIOS ===");
            for (Usuario u : usuarios) {
                System.out.println("----------------");
                System.out.println("Cédula: " + u.getCedula());
                System.out.println("Nombre: " + u.getNombre() + " " + u.getApellido());
                System.out.println("Email: " + u.getEmail());
                System.out.println("Rol: " + u.getRol().getNombre());
                System.out.println("Estado: " + (u.isEstado() ? "Activo" : "Inactivo"));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
