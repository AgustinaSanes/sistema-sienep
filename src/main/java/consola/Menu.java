package consola;
import controlador.*;
import facade.SistemaFacade;
import modelos.usuario.*;
import proxy.PermisosProxy;
import servicios.institucion.*;
import servicios.instancia.*;
import servicios.recordatorio.*;
import servicios.usuario.*;
import java.util.*;

public class Menu implements MenuSistema {
    //Scanner
    private final Scanner sc = new Scanner(System.in);
    // Proxy para control de permisos
    private final PermisosProxy proxy = new PermisosProxy(new SistemaFacade());
    // Servicios simples que no necesitan Proxy
    private final CategoriaService categoriaService = new CategoriaService();
    private final RolService rolService = new RolService();
    private final PermisoService permisoService = new PermisoService();
    private final CarreraService carreraService = new CarreraService();
    private final GrupoService grupoService = new GrupoService();
    private final ITRService itrService = new ITRService();
    private final RecordatorioService recordatorioService = new RecordatorioService();

    // MENÚ GENERAL
    @Override
    public void mostrar(Usuario usuario) {

        int opcion;

        do {
            System.out.println("=== MENÚ ===");
            System.out.println("1. Funcionarios");
            System.out.println("2. Estudiantes");
            System.out.println("3. Perfiles");
            System.out.println("4. Instancias");
            System.out.println("5. Recordatorios");
            System.out.println("6. Reportes");
            System.out.println("7. Institucional");
            System.out.println("0. Cerrar sesión");

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

    // FUNCIONARIOS
    private final FuncionarioControlador funcionarioControlador = new FuncionarioControlador();

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
                case 1 -> funcionarioControlador.crearFuncionario();
                case 2 -> funcionarioControlador.modificarFuncionario();
                case 3 -> funcionarioControlador.desactivarFuncionario();
                case 4 -> funcionarioControlador.buscarFuncionario();
                case 5 -> funcionarioControlador.listarFuncionarios();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // ESTUDIANTES
    private final EstudianteControlador estudianteControlador = new EstudianteControlador();

    private void gestionEstudiantes() {

        int opcion;

        do {
            System.out.println("--- ESTUDIANTES ---");
            System.out.println("1. Crear estudiante");
            System.out.println("2. Modificar estudiante");
            System.out.println("3. Desactivar estudiante");
            System.out.println("4. Buscar por cédula");
            System.out.println("5. Buscar por nombre");
            System.out.println("6. Buscar por carrera");
            System.out.println("7. Buscar por grupo");
            System.out.println("8. Buscar por estado");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> estudianteControlador.crearEstudiante();
                case 2 -> estudianteControlador.modificarEstudiante();
                case 3 -> estudianteControlador.desactivarEstudiante();
                case 4 -> estudianteControlador.buscarPorCedula();
                case 5 -> estudianteControlador.buscarPorNombre();
                case 6 -> estudianteControlador.buscarPorCarrera();
                case 7 -> estudianteControlador.buscarPorGrupo();
                case 8 -> estudianteControlador.buscarPorEstado();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }
        } while (opcion != 0);
    }

    // PERFILES
    private final PerfilControlador perfilControlador = new PerfilControlador();

    private void gestionPerfiles() {

        int opcion;

        do {
            System.out.println("=== PERFILES ===");
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

    private void gestionRoles() {

        int opcion;

        do {
            System.out.println("--- ROLES ---");
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
                case 1 -> perfilControlador.crearRol();
                case 2 -> perfilControlador.modificarRol();
                case 3 -> perfilControlador.eliminarRol();
                case 4 -> perfilControlador.buscarRol();
                case 5 -> perfilControlador.listarRoles();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    private void gestionPermisos() {

        int opcion;

        do {
            System.out.println("--- PERMISOS ---");
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
                case 1 -> perfilControlador.crearPermiso();
                case 2 -> perfilControlador.modificarPermiso();
                case 3 -> perfilControlador.eliminarPermiso();
                case 4 -> perfilControlador.buscarPermiso();
                case 5 -> perfilControlador.listarPermisos();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // INSTANCIAS
    private final InstanciaControlador instanciaControlador = new InstanciaControlador();

    private void gestionInstancias() {

        int opcion;

        do {
            System.out.println("--- INSTANCIAS ---");
            System.out.println("1. Crear instancia común");
            System.out.println("2. Crear incidencia");
            System.out.println("3. Buscar instancia por ID");
            System.out.println("4. Listar instancias de un estudiante");
            System.out.println("5. Modificar instancia");
            System.out.println("6. Desactivar instancia");
            System.out.println("7. Clonar instancia");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> instanciaControlador.crearInstanciaComun();
                case 2 -> instanciaControlador.crearIncidencia();
                case 3 -> instanciaControlador.buscarInstancia();
                case 4 -> instanciaControlador.listarInstanciasPorEstudiante();
                case 5 -> instanciaControlador.modificarInstancia();
                case 6 -> instanciaControlador.desactivarInstancia();
                case 7 -> instanciaControlador.clonarInstancia();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // RECORDATORIOS
    private final RecordatorioControlador recordatorioControlador = new RecordatorioControlador();

    private void gestionRecordatorios() {

        int opcion;

        do {
            System.out.println("--- RECORDATORIOS ---");
            System.out.println("1. Crear recordatorio");
            System.out.println("2. Modificar recordatorio");
            System.out.println("3. Desactivar recordatorio");
            System.out.println("4. Buscar por ID");
            System.out.println("5. Listar por instancia");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> recordatorioControlador.crearRecordatorio();
                case 2 -> recordatorioControlador.modificarRecordatorio();
                case 3 -> recordatorioControlador.desactivarRecordatorio();
                case 4 -> recordatorioControlador.buscarRecordatorio();
                case 5 -> recordatorioControlador.listarRecordatoriosPorInstancia();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // REPORTES
    private final ReporteControlador reporteControlador = new ReporteControlador();

    private void gestionReportes() {

        int opcion;

        do {
            System.out.println("--- REPORTES ---");
            System.out.println("1. Reporte por estudiante");
            System.out.println("2. Reporte general");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> reporteControlador.reporteEstudiante();
                case 2 -> reporteControlador.reporteGeneral();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // INSTITUCIONAL
    private final InstitucionControlador institucionControlador = new InstitucionControlador();

    private void gestionInstitucional() {

        int opcion;

        do {
            System.out.println("--- INSTITUCIONAL ---");
            System.out.println("1. Gestión de ITR");
            System.out.println("2. Gestión de Carreras");
            System.out.println("3. Gestión de Grupos");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> gestionITR();
                case 2 -> gestionCarreras();
                case 3 -> gestionGrupos();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    private void gestionITR() {

        int opcion;

        do {
            System.out.println("--- ITR ---");
            System.out.println("1. Crear ITR");
            System.out.println("2. Modificar ITR");
            System.out.println("3. Desactivar ITR");
            System.out.println("4. Buscar ITR por ID");
            System.out.println("5. Listar ITRs");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> institucionControlador.crearITR();
                case 2 -> institucionControlador.modificarITR();
                case 3 -> institucionControlador.desactivarITR();
                case 4 -> institucionControlador.buscarITR();
                case 5 -> institucionControlador.listarITRs();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // CARRERAS
    private void gestionCarreras() {

        int opcion;

        do {
            System.out.println("--- CARRERAS ---");
            System.out.println("1. Crear carrera");
            System.out.println("2. Modificar carrera");
            System.out.println("3. Desactivar carrera");
            System.out.println("4. Buscar carrera por ID");
            System.out.println("5. Listar carreras");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> institucionControlador.crearCarrera();
                case 2 -> institucionControlador.modificarCarrera();
                case 3 -> institucionControlador.desactivarCarrera();
                case 4 -> institucionControlador.buscarCarrera();
                case 5 -> institucionControlador.listarCarreras();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }

    // GRUPOS
    private void gestionGrupos() {

        int opcion;

        do {
            System.out.println("--- GRUPOS ---");
            System.out.println("1. Crear grupo");
            System.out.println("2. Modificar grupo");
            System.out.println("3. Desactivar grupo");
            System.out.println("4. Buscar grupo por ID");
            System.out.println("5. Listar grupos");
            System.out.println("0. Volver");

            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> institucionControlador.crearGrupo();
                case 2 -> institucionControlador.modificarGrupo();
                case 3 -> institucionControlador.desactivarGrupo();
                case 4 -> institucionControlador.buscarGrupo();
                case 5 -> institucionControlador.listarGrupos();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida");
            }

        } while (opcion != 0);
    }
}