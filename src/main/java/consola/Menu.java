package consola;
import facade.SistemaFacade;
import factory.UsuarioFactory;
import modelos.usuario.*;
import servicios.institucion.*;
import servicios.instancia.CategoriaService;
import servicios.recordatorio.RecordatorioService;
import servicios.usuario.*;
import java.time.LocalDate;
import java.util.*;

public class Menu implements MenuSistema {

    private final Scanner sc = new Scanner(System.in);

    // Facade para operaciones compuestas
    private final SistemaFacade facade = new SistemaFacade();
    // Servicios simples que no necesitan Facade
    private final CategoriaService categoriaService = new CategoriaService();
    private final RolService rolService = new RolService();
    private final PermisoService permisoService = new PermisoService();
    private final CarreraService carreraService = new CarreraService();
    private final GrupoService grupoService = new GrupoService();
    private final ITRService itrService = new ITRService();
    private final RecordatorioService recordatorioService = new RecordatorioService();

    // ======================================
    // MENÚ GENERAL
    // ======================================
    @Override
    public void mostrar(Usuario usuario) {

        int opcion;

        do {
            System.out.println("=== MENÚ ===");
            System.out.println("1. Funcionarios"); //CRUD funcionarios
            System.out.println("2. Estudiantes"); //alta baja y modificacion Busquedas, documentacion, Crear instancia desde ficha de alumno.
            System.out.println("3. Perfiles"); //Roles y permisos
            System.out.println("4. Instancias"); //CRUD instancias + categorias
            System.out.println("5. Recordatorios"); //CRUD instancias + categorias
            System.out.println("6. Reportes"); //Reportes
            System.out.println("7. Institucional"); //CRUD institucional
            System.out.println("0. Cerrar sesión"); //Salir

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> gestionFuncionarios();
                case 2 -> gestionEstudiantes();
                case 3 -> gestionPerfiles();
                case 4 -> gestionInstancias();
                case 5 -> gestionRecordatorios();
                case 6 -> gestionReportes();
                case 7 -> gestionInstitucional();
                case 0 -> System.out.println("Cerrando sesión...");
                default -> System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }

    // ======================================
    // Funcionarios
    // ======================================
    private void gestionFuncionarios() {

        int opcion;

        do {
            System.out.println("--- FUNCIONARIOS ---");
            System.out.println("1. Crear funcionario");
            System.out.println("2. Modificar funcionario");
            System.out.println("3. Desactivar funcionario");
            System.out.println("4. Buscar funcionario por cédula");
            System.out.println("5. Listar funcionarios");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> crearUsuario();
                case 2 -> modificarUsuario();
                case 3 -> desactivarUsuario();
                case 4 -> buscarUsuario();
                case 5 -> listarUsuarios();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // ======================================
    // CREAR USUARIO
    // Usa Factory para crear el tipo correcto
    // Usa Facade para coordinar los servicios
    // ======================================
    private void crearUsuario() {

        try {
            System.out.println("--- CREAR USUARIO ---");

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

            // ELEGIR ROL
            List<Rol> roles = rolService.obtenerTodos();

            System.out.println("--- ROLES DISPONIBLES ---");
            for (Rol r : roles) {
                if (r.isEstado()) {
                    System.out.println(r.getId() + ". " + r.getNombre());
                }
            }

            System.out.print("Seleccione rol: ");
            int idRol = Integer.parseInt(sc.nextLine());

            Rol rol = rolService.obtenerPorId(idRol);

            if (rol == null || !rol.isEstado()) {
                System.out.println("Rol inválido");
                return;
            }

            // FACTORY — crea el tipo correcto según el rol
            UsuarioFactory factory = new UsuarioFactory();
            Usuario usuario = factory.crearUsuario(rol.getNombre());

            // DATOS COMUNES
            usuario.setCedula(cedula);
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setEmail(email);
            usuario.setContrasena(contrasena);
            usuario.setEstado(true);
            usuario.setRol(rol);

            // DATOS ESPECÍFICOS DE ESTUDIANTE
            if (usuario instanceof Estudiante estudiante) {

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

                // FACADE — coordina usuarioService + estudianteService + telefonoDAO
                facade.crearEstudiante(estudiante);

                System.out.println("Estudiante creado correctamente");

            } else if (usuario instanceof Funcionario funcionario) {

                // FACADE — coordina usuarioService + funcionarioService
                facade.crearFuncionario(funcionario);

                System.out.println("Funcionario creado correctamente");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ======================================
    // MODIFICAR USUARIO
    // Usa Facade para coordinar la actualización
    // ======================================
    private void modificarUsuario() {

        try {
            System.out.println("--- MODIFICAR USUARIO ---");

            System.out.print("Ingrese cédula: ");
            String cedula = sc.nextLine();

            // FACADE — busca en estudiantes o funcionarios
            Usuario usuario = facade.buscarUsuario(cedula);

            if (usuario == null) {
                System.out.println("Usuario no encontrado");
                return;
            }

            // DATOS GENERALES
            System.out.println("Nombre actual: " + usuario.getNombre());
            System.out.print("Nuevo nombre: ");
            usuario.setNombre(sc.nextLine());

            System.out.println("Apellido actual: " + usuario.getApellido());
            System.out.print("Nuevo apellido: ");
            usuario.setApellido(sc.nextLine());

            System.out.println("Email actual: " + usuario.getEmail());
            System.out.print("Nuevo email: ");
            usuario.setEmail(sc.nextLine());

            // DATOS ESPECÍFICOS DE ESTUDIANTE
            if (usuario instanceof Estudiante estudiante) {

                System.out.println("--- DATOS ESTUDIANTE ---");

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

            // FACADE — coordina estudianteService + usuarioService
            facade.modificarUsuario(usuario);
            System.out.println("Datos actualizados correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ======================================
    // DESACTIVAR USUARIO
    // Usa Facade para la baja lógica
    // ======================================
    private void desactivarUsuario() {

        try {
            System.out.println("--- DESACTIVAR USUARIO ---");

            System.out.print("Ingrese cédula: ");
            String cedula = sc.nextLine();

            Usuario usuario = facade.buscarUsuario(cedula);

            if (usuario == null) {
                System.out.println("Usuario no encontrado");
                return;
            }

            // FACADE — baja lógica en usuarioService
            facade.desactivarUsuario(cedula);

            System.out.println("Usuario desactivado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ======================================
    // BUSCAR USUARIO
    // ======================================
    private void buscarUsuario() {

        try {
            System.out.println("--- BUSCAR USUARIO ---");

            System.out.print("Ingrese cédula: ");
            String cedula = sc.nextLine();

            // FACADE — busca en estudiantes o funcionarios
            Usuario usuario = facade.buscarUsuario(cedula);

            if (usuario == null) {
                System.out.println("Usuario no encontrado");
                return;
            }

            System.out.println("\n=== USUARIO ===");
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

    // ======================================
    // LISTAR USUARIOS
    // ======================================
    private void listarUsuarios() {

        try {
            List<Usuario> usuarios = facade.listarUsuarios();

            if (usuarios.isEmpty()) {
                System.out.println("No hay usuarios registrados");
                return;
            }

            System.out.println("\n=== USUARIOS ===");
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

    // ======================================
    // ESTUDIANTES
    // ======================================
    private void gestionEstudiantes() {

        int opcion;

        do {
            System.out.println("--- ESTUDIANTES ---");
            System.out.println("1. Buscar por nombre o apellido");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> buscarEstudiantePorNombre();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }

    // ======================================
    // BUSCAR ESTUDIANTE POR NOMBRE
    // ======================================
    private void buscarEstudiantePorNombre() {

        try {
            System.out.println("\n--- BUSCAR ESTUDIANTES ---");
            System.out.print("Ingrese nombre o apellido: ");
            String texto = sc.nextLine();

            List<Estudiante> lista = facade.buscarEstudiantesPorNombre(texto);

            if (lista.isEmpty()) {
                System.out.println("No se encontraron estudiantes");
                return;
            }

            System.out.println("\n=== RESULTADOS ===");
            for (Estudiante e : lista) {
                System.out.println("----------------");
                System.out.println("Cédula: " + e.getCedula());
                System.out.println("Nombre: " + e.getNombre() + " " + e.getApellido());
                System.out.println("Email: " + e.getEmail());
                System.out.println("Sistema salud: " + e.getSistemaSalud());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ======================================
    // PERFILES
    // ======================================
    private void gestionPerfiles() {

        int opcion;

        do {
            System.out.println("\n=== PERFILES ===");
            System.out.println("1. Gestión de roles");
            System.out.println("2. Gestión de permisos");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> gestionRoles();
                case 2 -> gestionPermisos();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // ======================================
    // ROLES — usa RolService directo (operación simple)
    // ======================================
    private void gestionRoles() {

        int opcion;

        do {
            System.out.println("\n--- ROLES ---");
            System.out.println("1. Crear rol");
            System.out.println("2. Modificar rol");
            System.out.println("3. Eliminar rol");
            System.out.println("4. Buscar rol por ID");
            System.out.println("5. Listar roles");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> crearRol();
                case 2 -> modificarRol();
                case 3 -> eliminarRol();
                case 4 -> buscarRol();
                case 5 -> listarRoles();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    private void crearRol() {
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

    private void modificarRol() {
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

    private void eliminarRol() {
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

    private void buscarRol() {
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

    private void listarRoles() {
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

    // ======================================
    // PERMISOS — usa PermisoService directo (operación simple)
    // ======================================
    private void gestionPermisos() {

        int opcion;

        do {
            System.out.println("\n--- PERMISOS ---");
            System.out.println("1. Crear permiso");
            System.out.println("2. Modificar permiso");
            System.out.println("3. Eliminar permiso");
            System.out.println("4. Buscar permiso por ID");
            System.out.println("5. Listar permisos");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> crearPermiso();
                case 2 -> modificarPermiso();
                case 3 -> eliminarPermiso();
                case 4 -> buscarPermiso();
                case 5 -> listarPermisos();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    private void crearPermiso() {
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

    private void modificarPermiso() {
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

    private void eliminarPermiso() {
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

    private void buscarPermiso() {
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

    private void listarPermisos() {
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

    // ======================================
    // INSTANCIAS — pendiente de implementar
    // ======================================
    private void gestionInstancias() {
        System.out.println("// TODO: implementar gestión de instancias");
    }

    // ======================================
    // INSTITUCIONAL — pendiente de implementar
    // ======================================
    private void gestionInstitucional() {
        System.out.println("// TODO: implementar gestión institucional");
    }

    private void gestionRecordatorios() {
        System.out.println("// TODO: implementar gestión de recordatorios");
    }

    private void gestionReportes() {
        System.out.println("// TODO: implementar gestión de reportes");
    }
}
