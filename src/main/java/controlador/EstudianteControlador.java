package controlador;

import factory.abstractFactory.UsuarioFactory;
import modelos.institucion.*;
import modelos.usuario.*;
import proxy.PermisosProxy;
import servicios.usuario.RolService;
import servicios.institucion.*;
import dao.usuario.TelefonoDAO;
import dao.usuario.TelefonoDAOImpl;

import java.time.LocalDate;
import java.util.*;

public class EstudianteControlador {
    private final PermisosProxy proxy;
    private final RolService rolService = new RolService();
    private final CarreraService carreraService = new CarreraService();
    private final GrupoService grupoService = new GrupoService();
    private final TelefonoDAO telefonoDAO = new TelefonoDAOImpl();
    private final Scanner sc = new Scanner(System.in);

    public EstudianteControlador(PermisosProxy proxy) {
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

            Rol rol = rolService.obtenerPorId(4);

            if (rol == null || !rol.isEstado()) {
                System.out.println("No existe el rol Estudiante");
                return;
            }

            List<Carrera> carreras = carreraService.obtenerTodas();
            System.out.println("--- CARRERAS DISPONIBLES ---");
            for (Carrera c : carreras) {
                if (c.isEstado()) System.out.println(c.getId() + ". " + c.getNombre());
            }
            System.out.print("Seleccione carrera: ");
            Carrera carrera = carreraService.obtenerPorId(Integer.parseInt(sc.nextLine()));
            if (carrera == null) { System.out.println("Carrera inválida"); return; }

            List<Grupo> grupos = grupoService.obtenerTodos();
            System.out.println("--- GRUPOS DISPONIBLES ---");
            for (Grupo g : grupos) {
                if (g.isEstado()) System.out.println(g.getId() + ". " + g.getNombre());
            }
            System.out.print("Seleccione grupo: ");
            Grupo grupo = grupoService.obtenerPorId(Integer.parseInt(sc.nextLine()));
            if (grupo == null) { System.out.println("Grupo inválido"); return; }

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

            System.out.print("Teléfono: ");
            String tel = sc.nextLine();
            if (!tel.isEmpty()) estudiante.agregarTelefono(tel);

            proxy.agregarEstudiante(estudiante);
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

            if (!(usuario instanceof Estudiante estudiante)) {
                System.out.println("El usuario no es un estudiante");
                return;
            }

            int opcion;
            do {
                System.out.println("--- ¿QUÉ DESEA MODIFICAR? ---");
                System.out.println("1. Nombre          [" + estudiante.getNombre() + "]");
                System.out.println("2. Apellido        [" + estudiante.getApellido() + "]");
                System.out.println("3. Email           [" + estudiante.getEmail() + "]");
                System.out.println("4. Sistema salud   [" + estudiante.getSistemaSalud() + "]");
                System.out.println("5. Fecha nacimiento[" + estudiante.getFechaNacimiento() + "]");
                System.out.println("6. Motivo          [" + estudiante.getMotivo() + "]");
                System.out.println("7. Calle           [" + estudiante.getCalle() + "]");
                System.out.println("8. Número puerta   [" + estudiante.getNroPuerta() + "]");
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
                        estudiante.setNombre(sc.nextLine());
                    }
                    case 2 -> {
                        System.out.print("Nuevo apellido: ");
                        estudiante.setApellido(sc.nextLine());
                    }
                    case 3 -> {
                        System.out.print("Nuevo email: ");
                        estudiante.setEmail(sc.nextLine());
                    }
                    case 4 -> {
                        System.out.print("Nuevo sistema salud: ");
                        estudiante.setSistemaSalud(sc.nextLine());
                    }
                    case 5 -> {
                        System.out.print("Nueva fecha (YYYY-MM-DD): ");
                        estudiante.setFechaNacimiento(java.time.LocalDate.parse(sc.nextLine()));
                    }
                    case 6 -> {
                        System.out.print("Nuevo motivo: ");
                        estudiante.setMotivo(sc.nextLine());
                    }
                    case 7 -> {
                        System.out.print("Nueva calle: ");
                        estudiante.setCalle(sc.nextLine());
                    }
                    case 8 -> {
                        System.out.print("Nuevo número puerta: ");
                        estudiante.setNroPuerta(sc.nextLine());
                    }
                    case 0 -> System.out.println("Guardando...");
                    default -> System.out.println("Opción inválida");
                }

            } while (opcion != 0);

            proxy.modificarUsuario(estudiante);
            System.out.println("Estudiante actualizado correctamente");

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
            if (usuario == null) { System.out.println("Estudiante no encontrado"); return; }

            proxy.desactivarUsuario(cedula);
            System.out.println("Estudiante desactivado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ===================== TELÉFONOS =====================

    public void agregarTelefono() {
        try {
            System.out.println("--- AGREGAR TELÉFONO ---");
            System.out.print("Cédula del estudiante: ");
            String cedula = sc.nextLine();

            System.out.print("Número de teléfono: ");
            String numero = sc.nextLine();

            telefonoDAO.agregarTelefono(cedula, numero);
            System.out.println("Teléfono agregado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void eliminarTelefono() {
        try {
            System.out.println("--- ELIMINAR TELÉFONO ---");
            System.out.print("Cédula del estudiante: ");
            String cedula = sc.nextLine();

            List<String> telefonos = telefonoDAO.obtenerPorEstudiante(cedula);

            if (telefonos.isEmpty()) {
                System.out.println("No hay teléfonos registrados");
                return;
            }

            System.out.println("Teléfonos registrados:");

            for (String t : telefonos) {
                System.out.println(t);
            }

            System.out.print("Número del teléfono a eliminar: ");
            String numero = sc.nextLine();

            telefonoDAO.eliminarTelefono(cedula, numero);

            System.out.println("Teléfono eliminado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarTelefonos() {
        try {
            System.out.println("--- TELÉFONOS DEL ESTUDIANTE ---");
            System.out.print("Cédula del estudiante: ");
            String cedula = sc.nextLine();

            List<String> telefonos = telefonoDAO.obtenerPorEstudiante(cedula);

            if (telefonos.isEmpty()) {
                System.out.println("No hay teléfonos registrados");
                return;
            }

            for (String t : telefonos) {
                System.out.println(t);
                }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ===================== BÚSQUEDAS =====================

    public void buscarPorCedula() {
        try {
            System.out.println("--- BUSCAR ESTUDIANTE POR CÉDULA ---");
            System.out.print("Ingrese cédula: ");
            String cedula = sc.nextLine();

            Usuario usuario = proxy.buscarUsuario(cedula);
            if (usuario == null) { System.out.println("Estudiante no encontrado"); return; }

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
            if (lista.isEmpty()) { System.out.println("No se encontraron estudiantes"); return; }
            mostrarListaEstudiantes(lista);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarPorCarrera() {
        try {
            System.out.println("--- BUSCAR ESTUDIANTES POR CARRERA ---");
            List<Carrera> carreras = carreraService.obtenerTodas();
            for (Carrera c : carreras) System.out.println(c.getId() + ". " + c.getNombre());

            System.out.print("Seleccione carrera: ");
            int idCarrera = Integer.parseInt(sc.nextLine());

            List<Estudiante> lista = proxy.buscarEstudiantesPorCarrera(idCarrera);
            if (lista.isEmpty()) { System.out.println("No se encontraron estudiantes"); return; }
            mostrarListaEstudiantes(lista);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarPorGrupo() {
        try {
            System.out.println("--- BUSCAR ESTUDIANTES POR GRUPO ---");
            List<Grupo> grupos = grupoService.obtenerTodos();
            for (Grupo g : grupos) System.out.println(g.getId() + ". " + g.getNombre());

            System.out.print("Seleccione grupo: ");
            int idGrupo = Integer.parseInt(sc.nextLine());

            List<Estudiante> lista = proxy.buscarEstudiantesPorGrupo(idGrupo);
            if (lista.isEmpty()) { System.out.println("No se encontraron estudiantes"); return; }
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
            if (lista.isEmpty()) { System.out.println("No se encontraron estudiantes"); return; }
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
