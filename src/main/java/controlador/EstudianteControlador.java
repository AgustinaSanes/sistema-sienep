package controlador;

import factory.UsuarioFactory;
import modelos.institucion.*;
import modelos.usuario.*;
import proxy.PermisosProxy;
import servicios.usuario.RolService;
import servicios.institucion.*;

import java.time.LocalDate;
import java.util.*;

public class EstudianteControlador {
    private final PermisosProxy proxy;
    private final RolService rolService = new RolService();
    private final Scanner sc = new Scanner(System.in);
    private final CarreraService carreraService = new CarreraService();
    private final GrupoService grupoService = new GrupoService();

    public EstudianteControlador(PermisosProxy proxy){
        this.proxy = proxy;
    }

    public void crearEstudiante() {
        try {
            System.out.println("--- CREAR ESTUDIANTE ---");

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
                if (r.isEstado()) System.out.println(r.getId() + ". " + r.getNombre());
            }

            System.out.print("Seleccione rol: ");

            Rol rol = rolService.obtenerPorId(Integer.parseInt(sc.nextLine()));
            if (rol == null || !rol.isEstado()) {
                System.out.println("Rol inválido");
                return;
            }

            List<Carrera> carreras = carreraService.obtenerTodas();

            System.out.println("--- CARRERAS DISPONIBLES ---");
            for (Carrera c : carreras) {
                if (c.isEstado()) System.out.println(c.getId() + ". " + c.getNombre());
            }

            System.out.print("Seleccione carrera: ");
            Carrera carrera = carreraService.obtenerPorId(Integer.parseInt(sc.nextLine()));

            if (carrera == null) {
                System.out.println("Carrera inválida");
                return;
            }

            List<Grupo> grupos = grupoService.obtenerTodos();

            System.out.println("--- GRUPOS DISPONIBLES ---");
            for (Grupo g : grupos) {
                if (g.isEstado()) System.out.println(g.getId() + ". " + g.getNombre());
            }

            System.out.print("Seleccione grupo: ");
            Grupo grupo = grupoService.obtenerPorId(Integer.parseInt(sc.nextLine()));

            if (grupo == null) {
                System.out.println("Grupo inválido");
                return;
            }

            // FACTORY
            UsuarioFactory factory = new UsuarioFactory();
            Estudiante estudiante = (Estudiante) factory.crearUsuario("Estudiante");

            estudiante.setCedula(cedula);
            estudiante.setNombre(nombre);
            estudiante.setApellido(apellido);
            estudiante.setEmail(email);
            estudiante.setContrasena(contrasena);
            estudiante.setEstado(true);
            estudiante.setRol(rol);
            estudiante.setGrupo(grupo);
            System.out.print("Sistema de salud: ");
            estudiante.setSistemaSalud(sc.nextLine());
            System.out.print("Fecha nacimiento (YYYY-MM-DD): ");
            estudiante.setFechaNacimiento(LocalDate.parse(sc.nextLine()));
            System.out.print("Motivo: ");
            estudiante.setMotivo(sc.nextLine());
            System.out.print("Calle: ");
            estudiante.setCalle(sc.nextLine());
            System.out.print("Número puerta: ");
            estudiante.setNroPuerta(sc.nextLine());
            System.out.print("Teléfono (Enter para omitir): ");
            String tel = sc.nextLine();
            if (!tel.isEmpty()) estudiante.agregarTelefono(tel);

            // PROXY
            proxy.crearEstudiante(estudiante);
            System.out.println("Estudiante creado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarEstudiante() {
        try {
            System.out.println("--- MODIFICAR ESTUDIANTE ---");

            System.out.print("Ingrese cédula: ");
            String cedula = sc.nextLine();

            Usuario usuario = proxy.buscarUsuario(cedula);
            if (usuario == null) {
                System.out.println("Estudiante no encontrado");
                return;
            }

            System.out.println("Nombre actual: " + usuario.getNombre());
            System.out.print("Nuevo nombre: ");
            usuario.setNombre(sc.nextLine());

            System.out.println("Apellido actual: " + usuario.getApellido());
            System.out.print("Nuevo apellido: ");
            usuario.setApellido(sc.nextLine());

            System.out.println("Email actual: " + usuario.getEmail());
            System.out.print("Nuevo email: ");
            usuario.setEmail(sc.nextLine());

            if (usuario instanceof Estudiante estudiante) {

                System.out.println("Sistema salud actual: " + estudiante.getSistemaSalud());
                System.out.print("Nuevo sistema salud: ");
                estudiante.setSistemaSalud(sc.nextLine());

                System.out.println("Fecha nacimiento actual: " + estudiante.getFechaNacimiento());
                System.out.print("Nueva fecha (YYYY-MM-DD): ");
                estudiante.setFechaNacimiento(LocalDate.parse(sc.nextLine()));

                System.out.println("Motivo actual: " + estudiante.getMotivo());
                System.out.print("Nuevo motivo: ");
                estudiante.setMotivo(sc.nextLine());

                System.out.println("Calle actual: " + estudiante.getCalle());
                System.out.print("Nueva calle: ");
                estudiante.setCalle(sc.nextLine());

                System.out.println("Número actual: " + estudiante.getNroPuerta());
                System.out.print("Nuevo número: ");
                estudiante.setNroPuerta(sc.nextLine());
            }

            proxy.modificarUsuario(usuario);
            System.out.println("Datos actualizados correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void desactivarEstudiante() {
        try {
            System.out.println("--- DESACTIVAR ESTUDIANTE ---");

            System.out.print("Ingrese cédula: ");
            String cedula = sc.nextLine();

            Usuario usuario = proxy.buscarUsuario(cedula);
            if (usuario == null) {
                System.out.println("Estudiante no encontrado");
                return;
            }

            proxy.desactivarUsuario(cedula);
            System.out.println("Estudiante desactivado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarPorCedula() {
        try {
            System.out.println("--- BUSCAR ESTUDIANTE POR CÉDULA ---");

            System.out.print("Ingrese cédula: ");
            String cedula = sc.nextLine();

            Usuario usuario = proxy.buscarUsuario(cedula);
            if (usuario == null) {
                System.out.println("Estudiante no encontrado");
                return;
            }

            System.out.println("=== ESTUDIANTE ===");
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

    public void buscarPorNombre() {
        try {
            System.out.println("--- BUSCAR ESTUDIANTES POR NOMBRE ---");

            System.out.print("Ingrese nombre o apellido: ");
            String texto = sc.nextLine();

            List<Estudiante> lista = proxy.buscarEstudiantesPorNombre(texto);
            if (lista.isEmpty()) {
                System.out.println("No se encontraron estudiantes");
                return;
            }
            mostrarListaEstudiantes(lista);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarPorCarrera() {
        try {
            System.out.println("--- BUSCAR ESTUDIANTES POR CARRERA ---");

            List<Carrera> carreras = carreraService.obtenerTodas();
            for (Carrera c : carreras) {
                System.out.println(c.getId() + ". " + c.getNombre());
            }

            System.out.print("Seleccione carrera: ");
            int idCarrera = Integer.parseInt(sc.nextLine());

            List<Estudiante> lista = proxy.buscarEstudiantesPorCarrera(idCarrera);
            if (lista.isEmpty()) {
                System.out.println("No se encontraron estudiantes");
                return;
            }
            mostrarListaEstudiantes(lista);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarPorGrupo() {
        try {
            System.out.println("--- BUSCAR ESTUDIANTES POR GRUPO ---");

            List<Grupo> grupos = grupoService.obtenerTodos();
            for (Grupo g : grupos) {
                System.out.println(g.getId() + ". " + g.getNombre());
            }
            System.out.print("Seleccione grupo: ");
            int idGrupo = Integer.parseInt(sc.nextLine());

            List<Estudiante> lista = proxy.buscarEstudiantesPorGrupo(idGrupo);
            if (lista.isEmpty()) {
                System.out.println("No se encontraron estudiantes");
                return;
            }
            mostrarListaEstudiantes(lista);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarPorEstado() {
        try {
            System.out.println("--- BUSCAR ESTUDIANTES POR ESTADO ---");

            System.out.println("1. Activos");
            System.out.println("2. Inactivos");
            System.out.print("Opción: ");
            int op = Integer.parseInt(sc.nextLine());

            List<Estudiante> lista = proxy.buscarEstudiantesPorEstado(op == 1);
            if (lista.isEmpty()) {
                System.out.println("No se encontraron estudiantes");
                return;
            }
            mostrarListaEstudiantes(lista);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void mostrarListaEstudiantes(List<Estudiante> lista) {
        System.out.println("=== RESULTADOS ===");

        for (Estudiante e : lista) {
            System.out.println("----------------");
            System.out.println("Cédula: " + e.getCedula());
            System.out.println("Nombre: " + e.getNombre() + " " + e.getApellido());
            System.out.println("Email: " + e.getEmail());
            System.out.println("Sistema salud: " + e.getSistemaSalud());
            System.out.println("Estado: " + (e.isEstado() ? "Activo" : "Inactivo"));
        }
    }
}
