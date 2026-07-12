package proxy;
import facade.SistemaFacade;
import modelos.auditoria.Auditoria;
import modelos.informe.InformeAdjunto;
import modelos.institucion.Carrera;
import modelos.institucion.Grupo;
import modelos.institucion.ITR;
import modelos.instancia.*;
import modelos.recordatorio.*;
import modelos.usuario.*;
import util.ControlarSesion;

import java.time.LocalDate;
import java.util.List;

//- Administradores: serán aquellos que gestionan el sistema. Podrán ingresar, modificar, eliminar
//y buscar usuarios. También pueden acceder a todas las funciones disponibles en el sistema

//Equipo Educativo:
//- Psicopedagogo/a: tendrán acceso a todas las funciones del sistema, incluida la visualización de
//datos confidenciales. No podrán crear nuevos usuarios en el sistema.
//- Analista de Equipo Educativo: mismas funciones que el psicopedagogo, pero sin acceso a la
//información confidencial.

//- Estudiante: podrán acceder a su información personal no confidencial, pudiendo también
//consultar por el seguimiento de sus instancias.

// Solo lectura no confidencial
//- Funcionario UTEC: podrá acceder a la información no confidencial del sistema. Pensado para
//coordinadores de carreras, docentes y otros funcionarios de UTEC que no pertenezcan a la
//Dirección de Educación

public class PermisosProxy {
    private final SistemaFacade facade;

    public PermisosProxy(SistemaFacade facade) {
        this.facade = facade;
    }

    // VERIFICACION DE ROLES
    private void verificarRol(String... rolesPermitidos) {
        Usuario usuario = ControlarSesion.getUsuario();

        if (usuario == null) {
            throw new RuntimeException("No hay sesión activa");
        }

        String rolActual = usuario.getRol().getNombre();

        for (String rol : rolesPermitidos) {
            if (rolActual.equalsIgnoreCase(rol)) return;
        }
        throw new RuntimeException("No tiene permisos para realizar esta acción");
    }

    // Verifica si el usuario puede ver datos confidenciales (esto es solo de LECTURA;
    // la posibilidad de crear/modificar/eliminar Informes o Instancias no depende de esto,
    // sino de verificarRol como cualquier otra acción)
    private boolean puedeVerConfidencial() {
        Usuario usuario = ControlarSesion.getUsuario();

        if (usuario == null) return false;
        String rol = usuario.getRol().getNombre();
        return rol.equalsIgnoreCase("Administrador") ||
                rol.equalsIgnoreCase("Psicopedagogo");
    }

    // Si el usuario logueado es Estudiante, solo puede operar sobre su propia cedula
    private void verificarPropiaCedulaSiEstudiante(String cedula) {
        Usuario usuario = ControlarSesion.getUsuario();
        if (usuario != null && usuario.getRol().getNombre().equalsIgnoreCase("Estudiante")) {
            if (usuario.getCedula() == null || !usuario.getCedula().equalsIgnoreCase(cedula)) {
                throw new RuntimeException("Un estudiante solo puede consultar su propia información");
            }
        }
    }

    // Valida que un Estudiante solo acceda a instancias propias, y que nadie sin
    // permiso de confidencial acceda a una instancia marcada como confidencial
    private void verificarAccesoInstancia(Instancia instancia) {
        if (instancia == null) return;
        Usuario usuario = ControlarSesion.getUsuario();
        String rolActual = usuario.getRol().getNombre();

        if (rolActual.equalsIgnoreCase("Estudiante")) {
            boolean esPropia = instancia.getEstudiante() != null
                    && instancia.getEstudiante().getCedula() != null
                    && instancia.getEstudiante().getCedula().equalsIgnoreCase(usuario.getCedula());
            if (!esPropia) {
                throw new RuntimeException("Un estudiante solo puede consultar sus propias instancias");
            }
        }

        if (instancia.getComConfidencial() && !puedeVerConfidencial()) {
            throw new RuntimeException("No tiene permisos para acceder a esta instancia confidencial");
        }
    }

    // Filtra instancias confidenciales de una lista para quien no tiene permiso de verlas
    private List<Instancia> filtrarConfidenciales(List<Instancia> instancias) {
        if (!puedeVerConfidencial()) {
            for (int i = instancias.size() - 1; i >= 0; i--) {
                if (instancias.get(i).getComConfidencial()) {
                    instancias.remove(i);
                }
            }
        }
        return instancias;
    }

    // El Estudiante tiene observaciones confidenciales propias (motivo, comentarios,
    // estado de salud). Si el usuario no tiene permiso de ver confidencial, se ocultan
    // esos campos antes de devolver el objeto (nunca se borran de la base, solo no se muestran).
    private Estudiante ocultarConfidencial(Estudiante estudiante) {
        if (estudiante != null && estudiante.isObsConfidenciales() && !puedeVerConfidencial()) {
            estudiante.setMotivo(null);
            estudiante.setObsComentarios(null);
            estudiante.setInfoEstadoSalud(null);
        }
        return estudiante;
    }

    private List<Estudiante> ocultarConfidencial(List<Estudiante> estudiantes) {
        for (Estudiante e : estudiantes) {
            ocultarConfidencial(e);
        }
        return estudiantes;
    }

    // USUARIOS
    public void agregarEstudiante(Estudiante estudiante) {
        verificarRol("Administrador","Psicopedagogo");
        facade.crearEstudiante(estudiante);
    }

    public void crearFuncionario(Funcionario funcionario) {
        verificarRol("Administrador");
        facade.crearFuncionario(funcionario);
    }

    public void modificarUsuario(Usuario usuario) {
        verificarRol("Administrador");
        facade.modificarUsuario(usuario);
    }

    public void desactivarUsuario(String cedula) {
        verificarRol("Administrador");
        facade.desactivarUsuario(cedula);
    }

    public void activarUsuario(String cedula) {
        verificarRol("Administrador");
        facade.activarUsuario(cedula);
    }

    public void cambiarContrasena(String cedula, String nuevaContrasena) {
        verificarRol("Administrador");
        facade.cambiarContrasena(cedula, nuevaContrasena);
    }

    // Todos pueden buscar usuarios menos Estudiante
    public Usuario buscarUsuario(String cedula) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.buscarUsuario(cedula);
    }

    public List<Usuario> listarUsuarios() {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.listarUsuarios();
    }

    // ESTUDIANTES
    public List<Estudiante> buscarEstudiantesPorNombre(String texto) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return ocultarConfidencial(facade.buscarEstudiantesPorNombre(texto));
    }

    public List<Estudiante> buscarEstudiantesPorCarrera(int idCarrera) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return ocultarConfidencial(facade.buscarEstudiantesPorCarrera(idCarrera));
    }

    public List<Estudiante> buscarEstudiantesPorGrupo(int idGrupo) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return ocultarConfidencial(facade.buscarEstudiantesPorGrupo(idGrupo));
    }

    public List<Estudiante> buscarEstudiantesPorEstado(boolean estado) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return ocultarConfidencial(facade.buscarEstudiantesPorEstado(estado));
    }

    // Devuelve el Estudiante completo (a diferencia de buscarUsuario, que no
    // instancia el subtipo correcto y no sirve para castear a Estudiante)
    public Estudiante buscarEstudiantePorCedula(String cedula) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC", "Estudiante");
        verificarPropiaCedulaSiEstudiante(cedula);
        return ocultarConfidencial(facade.buscarEstudiantePorCedula(cedula));
    }

    // INSTANCIAS
    // Admin, Psicopedagogo y Analista pueden crear/modificar
    // Estudiante y Funcionario UTEC solo lectura no confidencial
    public void crearInstanciaComun(InstanciaComun comun) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.crearInstanciaComun(comun);
    }

    public void crearIncidencia(Incidencia incidencia) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.crearIncidencia(incidencia);
    }

    public void agregarInvolucrado(int idInstancia, String involucrado) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.agregarInvolucrado(idInstancia, involucrado);
    }

    public void eliminarInvolucrado(int idInstancia, String involucrado) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.eliminarInvolucrado(idInstancia, involucrado);
    }

    public List<String> obtenerInvolucrados(int idInstancia) {

        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC", "Estudiante");
        Instancia instancia = facade.obtenerInstanciaPorId(idInstancia);
        verificarAccesoInstancia(instancia);

        return facade.obtenerInvolucrados(idInstancia);
    }

    public void clonarInstancia(Instancia instancia) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.clonarInstancia(instancia);
    }

    public Instancia obtenerInstanciaPorId(int id) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC", "Estudiante");
        Instancia instancia = facade.obtenerInstanciaPorId(id);
        verificarAccesoInstancia(instancia);
        return instancia;
    }

    public List<Instancia> buscarInstanciasPorFecha(LocalDate fecha) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return filtrarConfidenciales(facade.buscarInstanciasPorFecha(fecha));
    }

    public List<Instancia> buscarInstanciasPorDescripcion(String descripcion) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return filtrarConfidenciales(facade.buscarInstanciasPorDescripcion(descripcion));
    }

    public List<Instancia> obtenerInstanciasPorEstudiante(String cedula) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC", "Estudiante");
        verificarPropiaCedulaSiEstudiante(cedula);
        return filtrarConfidenciales(facade.obtenerInstanciasPorEstudiante(cedula));
    }

    public void modificarInstancia(Instancia instancia) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.modificarInstancia(instancia);
    }

    public void eliminarInstancia(int id) {
        verificarRol("Administrador", "Psicopedagogo");
        facade.eliminarInstancia(id);
    }

    public void categorizarInstancia(int idInstancia, int idCategoria) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.categorizarInstancia(idInstancia, idCategoria);
    }

    public List<Instancia> listarInstanciasPorCategoria(int idCategoria) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return filtrarConfidenciales(facade.listarInstanciasPorCategoria(idCategoria));
    }

    // CRUD CATEGORIAS
    public void agregarCategoria(Categoria categoria) {
        verificarRol("Administrador", "Psicopedagogo");
        facade.agregarCategoria(categoria);
    }

    public void actualizarCategoria(Categoria categoria) {
        verificarRol("Administrador", "Psicopedagogo");
        facade.actualizarCategoria(categoria);
    }

    public void eliminarCategoria(int id) {
        verificarRol("Administrador", "Psicopedagogo");
        facade.eliminarCategoria(id);
    }

    public Categoria obtenerCategoriaPorId(int id) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.obtenerCategoriaPorId(id);
    }

    public List<Categoria> listarCategorias() {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.listarCategorias();
    }

    // Informes
    // Solo Psicopedagogo y Admin pueden ver confidenciales
    public void agregarInforme(InformeAdjunto informe) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.agregarInforme(informe);
    }

    public void actualizarInforme(InformeAdjunto informe) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.actualizarInforme(informe);
    }

    public void eliminarInforme(int id) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.eliminarInforme(id);
    }
    public List<InformeAdjunto> obtenerInformePorEstudiante(String cedula) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC", "Estudiante");
        verificarPropiaCedulaSiEstudiante(cedula);

        List<InformeAdjunto> informes = facade.obtenerInformesPorEstudiante(cedula);

        // Si no puede ver confidenciales, filtra los informes confidenciales
        if (!puedeVerConfidencial()) {
            for (int i = informes.size() - 1; i >= 0; i--) {
                if (informes.get(i).isConfidencial()) {
                    informes.remove(i);
                }
            }
        }
        return informes;
    }

    public InformeAdjunto obtenerInformePorId(int id) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");

        InformeAdjunto informe = facade.obtenerInformePorId(id);

        // ocultar confidenciales si no tiene permisos
        if (informe != null && informe.isConfidencial() && !puedeVerConfidencial()) {
            throw new RuntimeException("No tiene permisos para ver este informe");
        }

        return informe;
    }

    public List<InformeAdjunto> obtenerTodosInformes() {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");

        List<InformeAdjunto> informes = facade.listarInformes();

        if (!puedeVerConfidencial()) {
            for (int i = informes.size() - 1; i >= 0; i--) {
                if (informes.get(i).isConfidencial()) {
                    informes.remove(i);
                }
            }
        }
        return informes;
    }

    public void agregarCategoriaRecordatorio(CategoriaRecordatorio categoria) {
        verificarRol("Administrador", "Psicopedagogo");
        facade.agregarCategoriaRecordatorio(categoria);
    }

    public void modificarCategoriaRecordatorio(CategoriaRecordatorio categoria) {
        verificarRol("Administrador", "Psicopedagogo");
        facade.modificarCategoriaRecordatorio(categoria);
    }

    public void eliminarCategoriaRecordatorio(int id) {
        verificarRol("Administrador", "Psicopedagogo");
        facade.eliminarCategoriaRecordatorio(id);
    }

    public CategoriaRecordatorio obtenerCategoriaRecordatorioPorId(int id) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.obtenerCategoriaRecordatorio(id);
    }

    public List<CategoriaRecordatorio> listarCategoriasRecordatorio() {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.listarCategoriasRecordatorio();
    }
    // FRECUENCIAS

    public void agregarFrecuencia(Frecuencia frecuencia) {
        verificarRol("Administrador", "Psicopedagogo");
        facade.agregarFrecuencia(frecuencia);
    }

    public void modificarFrecuencia(Frecuencia frecuencia) {
        verificarRol("Administrador", "Psicopedagogo");
        facade.modificarFrecuencia(frecuencia);
    }

    public void eliminarFrecuencia(int id) {
        verificarRol("Administrador", "Psicopedagogo");
        facade.eliminarFrecuencia(id);
    }

    public Frecuencia obtenerFrecuenciaPorId(int id) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.obtenerFrecuenciaPorId(id);
    }

    public List<Frecuencia> listarFrecuencias() {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.listarFrecuencias();
    }
    // RECORDATORIOS

    // Crear recordatorios
    public void agregarRecordatorio(Recordatorio recordatorio) {
        verificarRol(
                "Administrador",
                "Psicopedagogo");

        facade.agregarRecordatorio(recordatorio);
    }


    // Modificar recordatorios
    public void modificarRecordatorio(Recordatorio recordatorio) {

        verificarRol(
                "Administrador",
                "Psicopedagogo");

        facade.modificarRecordatorio(recordatorio);
    }


    // Baja lógica
    public void eliminarRecordatorio(int id) {

        verificarRol(
                "Administrador",
                "Psicopedagogo");

        facade.eliminarRecordatorio(id);
    }

    public Recordatorio obtenerRecordatorioPorId(int id) {

        verificarRol(
                "Administrador",
                "Psicopedagogo",
                "Analista de Equipo Educativo",
                "Funcionario UTEC"
        );

        return facade.obtenerRecordatorioPorId(id);
    }


    // Listar por instancia
    public List<Recordatorio> listarRecordatoriosPorInstancia(int idInstancia) {

        verificarRol(
                "Administrador",
                "Psicopedagogo",
                "Analista de Equipo Educativo",
                "Funcionario UTEC",
                "Estudiante"
        );

        // Si es Estudiante, valida que la instancia sea propia (y no confidencial)
        Instancia instancia = facade.obtenerInstanciaPorId(idInstancia);
        verificarAccesoInstancia(instancia);

        return facade.listarRecordatoriosPorInstancia(idInstancia);
    }

    // AUDITORIA
    // Información sensible de trazabilidad del sistema: solo Administrador
    public List<Auditoria> listarAuditorias() {
        verificarRol("Administrador");
        return facade.listarAuditorias();
    }

    public List<Auditoria> buscarAuditoriaPorCedula(String cedula) {
        verificarRol("Administrador");
        return facade.buscarAuditoriaPorCedula(cedula);
    }

    public List<Auditoria> buscarAuditoriaPorFecha(LocalDate fecha) {
        verificarRol("Administrador");
        return facade.buscarAuditoriaPorFecha(fecha);
    }

    // PERFILES: ROLES Y PERMISOS
    // Configuración del sistema: solo Administrador
    public void agregarRol(Rol rol) {
        verificarRol("Administrador");
        facade.agregarRol(rol);
    }

    public void actualizarRol(Rol rol) {
        verificarRol("Administrador");
        facade.actualizarRol(rol);
    }

    public void eliminarRol(int id) {
        verificarRol("Administrador");
        facade.eliminarRol(id);
    }

    public Rol obtenerRolPorId(int id) {
        verificarRol("Administrador");
        return facade.obtenerRolPorId(id);
    }

    public List<Rol> listarRoles() {
        verificarRol("Administrador");
        return facade.listarRoles();
    }

    public void agregarPermiso(Permiso permiso) {
        verificarRol("Administrador");
        facade.agregarPermiso(permiso);
    }

    public void actualizarPermiso(Permiso permiso) {
        verificarRol("Administrador");
        facade.actualizarPermiso(permiso);
    }

    public void eliminarPermiso(int id) {
        verificarRol("Administrador");
        facade.eliminarPermiso(id);
    }

    public Permiso obtenerPermisoPorId(int id) {
        verificarRol("Administrador");
        return facade.obtenerPermisoPorId(id);
    }

    public List<Permiso> listarPermisos() {
        verificarRol("Administrador");
        return facade.listarPermisos();
    }

    public void asignarPermisoARol(int idRol, int idPermiso) {
        verificarRol("Administrador");
        facade.asignarPermisoARol(idRol, idPermiso);
    }

    public void quitarPermisoDeRol(int idRol, int idPermiso) {
        verificarRol("Administrador");
        facade.quitarPermisoDeRol(idRol, idPermiso);
    }

    public List<Permiso> obtenerPermisosRol(int idRol) {
        verificarRol("Administrador");
        return facade.obtenerPermisosRol(idRol);
    }

    // INSTITUCIONAL: ITR, CARRERAS, GRUPOS
    // Configuración institucional: solo Administrador
    public void agregarITR(ITR itr) {
        verificarRol("Administrador");
        facade.agregarITR(itr);
    }

    public void actualizarITR(ITR itr) {
        verificarRol("Administrador");
        facade.actualizarITR(itr);
    }

    public void eliminarITR(int id) {
        verificarRol("Administrador");
        facade.eliminarITR(id);
    }

    public ITR obtenerITRPorId(int id) {
        verificarRol("Administrador");
        return facade.obtenerITRPorId(id);
    }

    public List<ITR> listarITRs() {
        verificarRol("Administrador");
        return facade.listarITRs();
    }

    public void agregarCarrera(Carrera carrera) {
        verificarRol("Administrador");
        facade.agregarCarrera(carrera);
    }

    public void actualizarCarrera(Carrera carrera) {
        verificarRol("Administrador");
        facade.actualizarCarrera(carrera);
    }

    public void eliminarCarrera(int id) {
        verificarRol("Administrador");
        facade.eliminarCarrera(id);
    }

    public Carrera obtenerCarreraPorId(int id) {
        verificarRol("Administrador");
        return facade.obtenerCarreraPorId(id);
    }

    public List<Carrera> listarCarreras() {
        verificarRol("Administrador");
        return facade.listarCarreras();
    }

    public void asociarCarreraITR(int idItr, int idCarrera) {
        verificarRol("Administrador");
        facade.asociarCarreraITR(idItr, idCarrera);
    }

    public void desasociarCarreraITR(int idItr, int idCarrera) {
        verificarRol("Administrador");
        facade.desasociarCarreraITR(idItr, idCarrera);
    }

    public List<Integer> obtenerCarrerasPorITR(int idItr) {
        verificarRol("Administrador");
        return facade.obtenerCarrerasPorITR(idItr);
    }

    public List<Integer> obtenerITRsPorCarrera(int idCarrera) {
        verificarRol("Administrador");
        return facade.obtenerITRsPorCarrera(idCarrera);
    }

    public void agregarGrupo(Grupo grupo) {
        verificarRol("Administrador");
        facade.agregarGrupo(grupo);
    }

    public void actualizarGrupo(Grupo grupo) {
        verificarRol("Administrador");
        facade.actualizarGrupo(grupo);
    }

    public void eliminarGrupo(int id) {
        verificarRol("Administrador");
        facade.eliminarGrupo(id);
    }

    public Grupo obtenerGrupoPorId(int id) {
        verificarRol("Administrador");
        return facade.obtenerGrupoPorId(id);
    }

    public List<Grupo> listarGrupos() {
        verificarRol("Administrador");
        return facade.listarGrupos();
    }

    // REPORTES
    // Mismos niveles que la consulta general de usuarios/estudiantes
    public String generarReporteEstudiante(String cedula) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.generarReporteEstudiante(cedula);
    }

    public String generarReporteGeneral(String filtroRol) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.generarReporteGeneral(filtroRol);
    }

    // CATÁLOGOS DE REFERENCIA
    // Lecturas usadas dentro de flujos ya controlados (alta/búsqueda de estudiantes),
    // no forman parte de la gestión institucional (que es exclusiva de Administrador).
    public List<Carrera> listarCarrerasCatalogo() {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.listarCarreras();
    }

    public Carrera obtenerCarreraCatalogoPorId(int id) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.obtenerCarreraPorId(id);
    }

    public List<Grupo> listarGruposCatalogo() {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.listarGrupos();
    }

    public Grupo obtenerGrupoCatalogoPorId(int id) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.obtenerGrupoPorId(id);
    }

    // Rol "Estudiante" asignado automáticamente al dar de alta un estudiante.
    // Mismo nivel de acceso que agregarEstudiante (no es gestión de roles).
    public Rol obtenerRolParaEstudiante(int id) {
        verificarRol("Administrador", "Psicopedagogo");
        return facade.obtenerRolPorId(id);
    }
}