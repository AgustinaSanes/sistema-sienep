package controlador;

import modelos.usuario.*;
import servicios.usuario.*;

import java.util.*;

public class PerfilControlador {
    private final RolService rolService = new RolService();
    private final PermisoService permisoService = new PermisoService();
    private final Scanner sc = new Scanner(System.in);

    // ROLES
    public void crearRol() {
        try {
            System.out.println("--- CREAR ROL ---");

            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            Rol rol = new Rol();
            rol.setNombre(nombre);
            rol.setEstado(true);

            rolService.agregarRol(rol);
            System.out.println("Rol creado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarRol() {
        try {
            System.out.println("--- MODIFICAR ROL ---");

            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            Rol rol = rolService.obtenerPorId(id);

            if (rol == null) {
                System.out.println("Rol no encontrado");
                return;
            }

            System.out.println("Nombre actual: " + rol.getNombre());
            System.out.print("Nuevo nombre: ");
            rol.setNombre(sc.nextLine());

            rolService.actualizarRol(rol);
            System.out.println("Rol actualizado");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void eliminarRol() {
        try {
            System.out.println("--- ELIMINAR ROL ---");

            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            rolService.eliminarRol(id);
            System.out.println("Rol eliminado");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarRol() {
        try {
            System.out.println("--- BUSCAR ROL ---");

            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            Rol rol = rolService.obtenerPorId(id);
            if (rol == null) {
                System.out.println("No encontrado");
                return;
            }

            System.out.println("ID: " + rol.getId());
            System.out.println("Nombre: " + rol.getNombre());
            System.out.println("Estado: " + (rol.isEstado() ? "Activo" : "Inactivo"));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarRoles() {
        List<Rol> roles = rolService.obtenerTodos();

        if (roles.isEmpty()) {
            System.out.println("No hay roles");
            return;
        }

        for (Rol r : roles) {
            System.out.println("----------------");
            System.out.println("ID: " + r.getId());
            System.out.println("Nombre: " + r.getNombre());
            System.out.println("Estado: " + (r.isEstado() ? "Activo" : "Inactivo"));
        }
    }

    // PERMISOS
    public void crearPermiso() {
        try {
            System.out.println("--- CREAR PERMISO ---");

            System.out.print("Descripción: ");
            String desc = sc.nextLine();

            Permiso p = new Permiso();
            p.setDescripcion(desc);

            permisoService.agregarPermiso(p);
            System.out.println("Permiso creado");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarPermiso() {
        try {
            System.out.println("--- MODIFICAR PERMISO ---");

            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            Permiso p = permisoService.obtenerPorId(id);

            if (p == null) {
                System.out.println("Permiso no encontrado");
                return;
            }

            System.out.println("Actual: " + p.getDescripcion());
            System.out.print("Nuevo: ");
            p.setDescripcion(sc.nextLine());

            permisoService.actualizarPermiso(p);
            System.out.println("Permiso actualizado");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void eliminarPermiso() {
        try {
            System.out.println("--- ELIMINAR PERMISO ---");

            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            permisoService.eliminarPermiso(id);
            System.out.println("Permiso eliminado");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarPermiso() {
        try {
            System.out.println("--- BUSCAR PERMISO ---");

            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            Permiso p = permisoService.obtenerPorId(id);

            if (p == null) {
                System.out.println("Permiso no encontrado");
                return;
            }

            System.out.println("ID: " + p.getId());
            System.out.println("Descripción: " + p.getDescripcion());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarPermisos() {
        List<Permiso> permisos = permisoService.obtenerTodos();

        if (permisos.isEmpty()) {
            System.out.println("No hay permisos");
            return;
        }

        for (Permiso p : permisos) {
            System.out.println("----------------");
            System.out.println("ID: " + p.getId());
            System.out.println("Descripción: " + p.getDescripcion());
        }
    }
}
