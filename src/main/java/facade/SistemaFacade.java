package facade;
import modelos.auditoria.Auditoria;
import modelos.informe.InformeAdjunto;
import modelos.institucion.*;
import modelos.recordatorio.*;
import modelos.usuario.*;
import modelos.instancia.*;
import observer.*;
import servicios.informe.InformeService;
import servicios.institucion.*;
import servicios.recordatorio.*;
import servicios.usuario.*;
import servicios.instancia.*;
import dao.usuario.*;
import factory.factoryMethod.reporte.Reporte;
import factory.factoryMethod.reporte.ReporteFactory;
import util.ControlarSesion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SistemaFacade implements Sujeto {

    // Observers
    private final List<Observer> observers = new ArrayList<>();

    // Servicios
    private final UsuarioService usuarioService;
    private final EstudianteService estudianteService;
    private final FuncionarioService funcionarioService;
    private final InstanciaService instanciaService;
    private final ComunService comunService;
    private final IncidenciaService incidenciaService;
    private final InformeService informeService;
    private final CategoriaService categoriaService;
    private final TelefonoDAO telefonoDAO;
    private final CategoriaRecordatorioService categoriaRecordatorioService;
    private final FrecuenciaService frecuenciaService;
    private final RecordatorioService recordatorioService;
    private final AuditoriaService auditoriaService;
    private final RolService rolService;
    private final PermisoService permisoService;
    private final RolPermisoService rolPermisoService;
    private final ITRService itrService;
    private final CarreraService carreraService;
    private final GrupoService grupoService;

    public SistemaFacade() {
        this.usuarioService = new UsuarioService();
        this.estudianteService = new EstudianteService();
        this.funcionarioService = new FuncionarioService();
        this.instanciaService = new InstanciaService();
        this.comunService = new ComunService();
        this.incidenciaService = new IncidenciaService();
        this.informeService = new InformeService();
        this.categoriaService = new CategoriaService();
        this.telefonoDAO = new TelefonoDAOImpl();
        this.categoriaRecordatorioService = new CategoriaRecordatorioService();
        this.frecuenciaService = new FrecuenciaService();
        this.recordatorioService = new RecordatorioService();
        this.auditoriaService = new AuditoriaService();
        this.rolService = new RolService();
        this.permisoService = new PermisoService();
        this.rolPermisoService = new RolPermisoService();
        this.itrService = new ITRService();
        this.carreraService = new CarreraService();
        this.grupoService = new GrupoService();
    }

    // IMPLEMENTACION SUJETO — Observer
    @Override
    public void agregarObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void quitarObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notificar(String evento, String detalle, String cedula) {
        for (Observer o : observers) {
            o.actualizar(evento, detalle, cedula);
        }
    }

    // Obtiene la cédula del funcionario logueado para dejar registro de quién
    // realizó la acción en la auditoría/notificación, en vez de mandar null.
    private String obtenerCedulaFuncionarioActual() {
        Usuario usuarioActual = ControlarSesion.getUsuario();
        return (usuarioActual != null) ? usuarioActual.getCedula() : null;
    }

    // CREAR ESTUDIANTE
    public void crearEstudiante(Estudiante estudiante) {
        usuarioService.agregarUsuario(estudiante);
        estudianteService.agregarEstudiante(estudiante);

        if (estudiante.getTelefono() != null) {
            for (String tel : estudiante.getTelefono()) {
                telefonoDAO.agregarTelefono(estudiante.getCedula(), tel);
            }
        }

        notificar("ALTA_ESTUDIANTE",
                "Se creó el estudiante " + estudiante.getNombre() + " " + estudiante.getApellido(),
                estudiante.getCedula());
    }

    // CREAR FUNCIONARIO
    public void crearFuncionario(Funcionario funcionario) {
        usuarioService.agregarUsuario(funcionario);
        funcionarioService.agregarFuncionario(funcionario);

        notificar("ALTA_FUNCIONARIO",
                "Se creó el funcionario " + funcionario.getNombre() + " " + funcionario.getApellido(),
                funcionario.getCedula());
    }

    // MODIFICAR USUARIO
    public void modificarUsuario(Usuario usuario) {

        if (usuario instanceof Estudiante estudiante) {
            estudianteService.actualizarEstudiante(estudiante);
        }

        usuarioService.actualizarUsuario(usuario);

        notificar("MODIFICACION_USUARIO",
                "Se modificó el usuario " + usuario.getNombre() + " " + usuario.getApellido(),
                usuario.getCedula());
    }

    // DESACTIVAR USUARIO
    public void desactivarUsuario(String cedula) {
        usuarioService.eliminarUsuario(cedula);

        notificar("BAJA_USUARIO",
                "Se desactivó el usuario con cédula " + cedula,
                cedula);
    }

    // ACTIVAR USUARIO
    public void activarUsuario(String cedula) {
        usuarioService.activarUsuario(cedula);

        notificar("ALTA_USUARIO",
                "Se activó el usuario con cédula " + cedula,
                cedula);
    }

    // CAMBIAR CONTRASEÑA
    public void cambiarContrasena(String cedula, String nuevaContrasena) {
        usuarioService.cambiarContrasena(cedula, nuevaContrasena);
    }

    // BUSCAR USUARIO
    public Usuario buscarUsuario(String cedula) {
        return usuarioService.buscarPorCedula(cedula);
    }

    // LISTAR USUARIOS
    public List<Usuario> listarUsuarios() {
        return usuarioService.obtenerTodos();
    }

    // BUSCAR ESTUDIANTES
    public List<Estudiante> buscarEstudiantesPorNombre(String texto) {return estudianteService.buscarPorNombreApellido(texto);}
    public List<Estudiante> buscarEstudiantesPorCarrera(int idCarrera) {return estudianteService.buscarPorCarrera(idCarrera);}
    public List<Estudiante> buscarEstudiantesPorGrupo(int idGrupo) {
        return estudianteService.buscarPorGrupo(idGrupo);
    }
    public List<Estudiante> buscarEstudiantesPorEstado(boolean estado) {return estudianteService.buscarPorEstado(estado);}
    public Estudiante buscarEstudiantePorCedula(String cedula) {
        return estudianteService.obtenerPorCedula(cedula);
    }

    // CREAR INSTANCIA COMUN
    public void crearInstanciaComun(InstanciaComun comun) {
        instanciaService.agregarInstancia(comun);
        comunService.agregarComun(comun);

        notificar("ALTA_INSTANCIA",
                "Se creó instancia común: " + comun.getTitulo(),
                comun.getFuncionario().getCedula());
    }

    // CLONAR INSTANCIA
    public void clonarInstancia(Instancia instancia) {
        Instancia clon = (Instancia) instancia.clonar();

        if (clon instanceof InstanciaComun comun) {
            crearInstanciaComun(comun);
        } else if (clon instanceof Incidencia incidencia) {
            crearIncidencia(incidencia);
        }

        notificar("ALTA_INSTANCIA",
                "Se clonó instancia: " + instancia.getTitulo(),
                instancia.getFuncionario().getCedula());
    }

    // OBTENER INSTANCIA POR ID
    public Instancia obtenerInstanciaPorId(int id) {
        return instanciaService.obtenerPorId(id);
    }

    // OBTENER INSTANCIA POR ESTUDIANTE
    public List<Instancia> obtenerInstanciasPorEstudiante(String cedula) {return instanciaService.obtenerPorEstudiante(cedula);}

    public List<Instancia> buscarInstanciasPorFecha(LocalDate fecha) {
        return instanciaService.buscarPorFecha(fecha);
    }

    public List<Instancia> buscarInstanciasPorDescripcion(String descripcion) {return instanciaService.buscarPorDescripcion(descripcion);}

    // MODIFICAR INSTANCIA
    public void modificarInstancia(Instancia instancia) {
        instanciaService.actualizarInstancia(instancia);
        notificar("MODIFICACION_INSTANCIA",
                "Se modificó instancia: " + instancia.getTitulo(),
                instancia.getFuncionario().getCedula());
    }

    // ELIMINAR INSTANCIA
    public void eliminarInstancia(int id) {
        instanciaService.eliminarInstancia(id);
        notificar(
                "BAJA_INSTANCIA",
                "Se eliminó la instancia con id " + id,
                obtenerCedulaFuncionarioActual());
    }

    // CREAR INCIDENCIA
    public void crearIncidencia(Incidencia incidencia) {
        instanciaService.agregarInstancia(incidencia);
        incidenciaService.agregarIncidencia(incidencia);

        notificar("ALTA_INCIDENCIA",
                "Se creó incidencia: " + incidencia.getTitulo(),
                incidencia.getFuncionario().getCedula());
    }

    public List<Instancia> listarInstanciasPorCategoria(int idCategoria) {
        return instanciaService.obtenerPorCategoria(idCategoria);
    }

    public void agregarCategoria(Categoria categoria) {
        categoriaService.agregarCategoria(categoria);

        notificar(
                "ALTA_CATEGORIA",
                "Se creó la categoría " + categoria.getNombre(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void actualizarCategoria(Categoria categoria) {
        categoriaService.actualizarCategoria(categoria);

        notificar(
                "MODIFICACION_CATEGORIA",
                "Se modificó la categoría " + categoria.getNombre(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void eliminarCategoria(int id) {
        categoriaService.eliminarCategoria(id);

        notificar(
                "BAJA_CATEGORIA",
                "Se eliminó la categoría " + id,
                obtenerCedulaFuncionarioActual()
        );
    }

    public Categoria obtenerCategoriaPorId(int id) {
        return categoriaService.obtenerPorId(id);
    }

    public List<Categoria> listarCategorias() {
        return categoriaService.obtenerTodas();
    }

    public void categorizarInstancia(int idInstancia, int idCategoria) {

        Instancia instancia = instanciaService.obtenerPorId(idInstancia);

        if (!(instancia instanceof InstanciaComun comun)) {
            throw new RuntimeException("Solo se pueden categorizar instancias comunes");
        }

        Categoria categoria = categoriaService.obtenerPorId(idCategoria);

        if (categoria == null || !categoria.isEstado()) {
            throw new RuntimeException("Categoría inválida o inactiva");
        }

        comun.setCategoria(categoria);
        comunService.actualizarComun(comun);

        notificar(
                "CATEGORIZAR_INSTANCIA",
                "Instancia " + idInstancia + " categorizada como " + categoria.getNombre(),
                instancia.getFuncionario().getCedula()
        );
    }

    public void agregarInvolucrado(int idInstancia, String involucrado) {
        incidenciaService.agregarInvolucrado(idInstancia, involucrado);
    }

    public void eliminarInvolucrado(int idInstancia, String involucrado) {
        incidenciaService.eliminarInvolucrado(idInstancia, involucrado);
    }

    public List<String> obtenerInvolucrados(int idInstancia) {
        return incidenciaService.obtenerInvolucrados(idInstancia);
    }

    // AGREGAR INFORME
    public void agregarInforme(InformeAdjunto informe) {
        informeService.agregarInforme(informe);
        notificar("ALTA_INFORME",
                "Se adjuntó informe: " + informe.getNombre(),
                informe.getEstudiante().getCedula());
    }

    //MODIFICAR INFORME
    public void actualizarInforme(InformeAdjunto informe) {
        informeService.actualizarInforme(informe);

        notificar(
                "MODIFICACION_INFORME",
                "Se modificó el informe " + informe.getNombre(),
                informe.getEstudiante().getCedula()
        );
    }

    //ELIMINAR INFORME
    public void eliminarInforme(int id) {

        informeService.eliminarInforme(id);

        notificar(
                "BAJA_INFORME",
                "Se eliminó el informe " + id,
                obtenerCedulaFuncionarioActual()
        );
    }

    // OBTENER INFORMES POR ESTUDIANTE
    public List<InformeAdjunto> obtenerInformesPorEstudiante(String cedula) {
        return informeService.obtenerPorEstudiante(cedula);
    }

    // OBTENER INFORME POR ID
    public InformeAdjunto obtenerInformePorId(int id) {
        return informeService.obtenerPorId(id);
    }

    //LISTAR TODOS LOS INFORMES
    public List<InformeAdjunto> listarInformes() {
        return informeService.obtenerTodos();
    }

    //RECORDATORIOS
    public void agregarRecordatorio(Recordatorio recordatorio) {
        recordatorioService.agregarRecordatorio(recordatorio);

        notificar(
                "ALTA_RECORDATORIO",
                "Se creó un recordatorio",
                obtenerCedulaFuncionarioActual());
    }

    public void modificarRecordatorio(Recordatorio recordatorio) {
        recordatorioService.actualizarRecordatorio(recordatorio);

        notificar(
                "MODIFICACION_RECORDATORIO",
                "Se modificó un recordatorio",
                obtenerCedulaFuncionarioActual()
        );
    }

    public void eliminarRecordatorio(int id) {
        recordatorioService.eliminarRecordatorio(id);

        notificar(
                "BAJA_RECORDATORIO",
                "Se eliminó el recordatorio " + id,
                obtenerCedulaFuncionarioActual()
        );
    }

    public Recordatorio obtenerRecordatorioPorId(int id) {
        return recordatorioService.obtenerPorId(id);
    }

    public List<Recordatorio> listarRecordatoriosPorInstancia(int idInstancia) {
        return recordatorioService.obtenerPorInstancia(idInstancia);
    }

    public void agregarCategoriaRecordatorio(CategoriaRecordatorio categoria) {
        categoriaRecordatorioService.agregarCategoria(categoria);

        notificar(
                "ALTA_CATEGORIA_RECORDATORIO",
                "Se creó la categoría de recordatorio " + categoria.getNombre(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void modificarCategoriaRecordatorio(CategoriaRecordatorio categoria) {
        categoriaRecordatorioService.modificarCategoria(categoria);

        notificar(
                "MODIFICACION_CATEGORIA_RECORDATORIO",
                "Se modificó la categoría de recordatorio " + categoria.getNombre(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void eliminarCategoriaRecordatorio(int id) {
        categoriaRecordatorioService.eliminarCategoria(id);

        notificar(
                "BAJA_CATEGORIA_RECORDATORIO",
                "Se eliminó la categoría de recordatorio ID: " + id,
                obtenerCedulaFuncionarioActual()
        );
    }

    public CategoriaRecordatorio obtenerCategoriaRecordatorio(int id) {
        return categoriaRecordatorioService.obtenerPorId(id);
    }

    public List<CategoriaRecordatorio> listarCategoriasRecordatorio() {
        return categoriaRecordatorioService.obtenerTodas();
    }

    // FRECUENCIAS
    public void agregarFrecuencia(Frecuencia frecuencia) {
        frecuenciaService.agregarFrecuencia(frecuencia);

        notificar(
                "ALTA_FRECUENCIA",
                "Se creó la frecuencia " + frecuencia.getDescripcion(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void modificarFrecuencia(Frecuencia frecuencia) {
        frecuenciaService.modificarFrecuencia(frecuencia);

        notificar(
                "MODIFICACION_FRECUENCIA",
                "Se modificó la frecuencia " + frecuencia.getDescripcion(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void eliminarFrecuencia(int id) {
        frecuenciaService.eliminarFrecuencia(id);

        notificar(
                "BAJA_FRECUENCIA",
                "Se eliminó la frecuencia " + id,
                obtenerCedulaFuncionarioActual()
        );
    }

    public Frecuencia obtenerFrecuenciaPorId(int id) {
        return frecuenciaService.obtenerPorId(id);
    }

    public List<Frecuencia> listarFrecuencias() {
        return frecuenciaService.obtenerTodas();
    }

    // AUDITORIA
    public List<Auditoria> listarAuditorias() {
        return auditoriaService.obtenerTodas();
    }

    public List<Auditoria> buscarAuditoriaPorCedula(String cedula) {
        return auditoriaService.obtenerPorCedula(cedula);
    }

    public List<Auditoria> buscarAuditoriaPorFecha(LocalDate fecha) {
        return auditoriaService.obtenerPorFecha(fecha);
    }

    // ROLES
    public void agregarRol(Rol rol) {
        rolService.agregarRol(rol);

        notificar(
                "ALTA_ROL",
                "Se creó el rol " + rol.getNombre(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void actualizarRol(Rol rol) {
        rolService.actualizarRol(rol);

        notificar(
                "MODIFICACION_ROL",
                "Se modificó el rol " + rol.getNombre(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void eliminarRol(int id) {
        rolService.eliminarRol(id);

        notificar(
                "BAJA_ROL",
                "Se eliminó el rol " + id,
                obtenerCedulaFuncionarioActual()
        );
    }

    public Rol obtenerRolPorId(int id) {
        return rolService.obtenerPorId(id);
    }

    public List<Rol> listarRoles() {
        return rolService.obtenerTodos();
    }

    // PERMISOS
    public void agregarPermiso(Permiso permiso) {
        permisoService.agregarPermiso(permiso);

        notificar(
                "ALTA_PERMISO",
                "Se creó el permiso " + permiso.getDescripcion(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void actualizarPermiso(Permiso permiso) {
        permisoService.actualizarPermiso(permiso);

        notificar(
                "MODIFICACION_PERMISO",
                "Se modificó el permiso " + permiso.getDescripcion(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void eliminarPermiso(int id) {
        permisoService.eliminarPermiso(id);

        notificar(
                "BAJA_PERMISO",
                "Se eliminó el permiso " + id,
                obtenerCedulaFuncionarioActual()
        );
    }

    public Permiso obtenerPermisoPorId(int id) {
        return permisoService.obtenerPorId(id);
    }

    public List<Permiso> listarPermisos() {
        return permisoService.obtenerTodos();
    }

    // ROL-PERMISO
    public void asignarPermisoARol(int idRol, int idPermiso) {
        rolPermisoService.asignarPermiso(idRol, idPermiso);

        notificar(
                "ASIGNAR_PERMISO",
                "Permiso " + idPermiso + " asignado al rol " + idRol,
                obtenerCedulaFuncionarioActual()
        );
    }

    public void quitarPermisoDeRol(int idRol, int idPermiso) {
        rolPermisoService.quitarPermiso(idRol, idPermiso);

        notificar(
                "QUITAR_PERMISO",
                "Permiso " + idPermiso + " quitado del rol " + idRol,
                obtenerCedulaFuncionarioActual()
        );
    }

    public List<Permiso> obtenerPermisosRol(int idRol) {
        return rolPermisoService.obtenerPermisosRol(idRol);
    }

    // ITR
    public void agregarITR(ITR itr) {
        itrService.agregarITR(itr);

        notificar(
                "ALTA_ITR",
                "Se creó el ITR " + itr.getNombre(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void actualizarITR(ITR itr) {
        itrService.actualizarITR(itr);

        notificar(
                "MODIFICACION_ITR",
                "Se modificó el ITR " + itr.getNombre(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void eliminarITR(int id) {
        itrService.eliminarITR(id);

        notificar(
                "BAJA_ITR",
                "Se eliminó el ITR " + id,
                obtenerCedulaFuncionarioActual()
        );
    }

    public ITR obtenerITRPorId(int id) {
        return itrService.obtenerPorId(id);
    }

    public List<ITR> listarITRs() {
        return itrService.obtenerTodos();
    }

    // CARRERAS
    public void agregarCarrera(Carrera carrera) {
        carreraService.agregarCarrera(carrera);

        notificar(
                "ALTA_CARRERA",
                "Se creó la carrera " + carrera.getNombre(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void actualizarCarrera(Carrera carrera) {
        carreraService.actualizarCarrera(carrera);

        notificar(
                "MODIFICACION_CARRERA",
                "Se modificó la carrera " + carrera.getNombre(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void eliminarCarrera(int id) {
        carreraService.eliminarCarrera(id);

        notificar(
                "BAJA_CARRERA",
                "Se eliminó la carrera " + id,
                obtenerCedulaFuncionarioActual()
        );
    }

    public Carrera obtenerCarreraPorId(int id) {
        return carreraService.obtenerPorId(id);
    }

    public List<Carrera> listarCarreras() {
        return carreraService.obtenerTodas();
    }

    // ASOCIACION ITR - CARRERA
    public void asociarCarreraITR(int idItr, int idCarrera) {
        itrService.agregarCarreraITR(idItr, idCarrera);

        notificar(
                "ASOCIACION_CARRERA_ITR",
                "Se asoció la carrera " + idCarrera + " al ITR " + idItr,
                obtenerCedulaFuncionarioActual()
        );
    }

    public void desasociarCarreraITR(int idItr, int idCarrera) {
        itrService.eliminarCarreraITR(idItr, idCarrera);

        notificar(
                "DESASOCIACION_CARRERA_ITR",
                "Se desasoció la carrera " + idCarrera + " del ITR " + idItr,
                obtenerCedulaFuncionarioActual()
        );
    }

    public List<Integer> obtenerCarrerasPorITR(int idItr) {
        return itrService.obtenerCarrerasPorITR(idItr);
    }

    public List<Integer> obtenerITRsPorCarrera(int idCarrera) {
        return itrService.obtenerITRsPorCarrera(idCarrera);
    }

    // GRUPOS
    public void agregarGrupo(Grupo grupo) {
        grupoService.agregarGrupo(grupo);

        notificar(
                "ALTA_GRUPO",
                "Se creó el grupo " + grupo.getNombre(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void actualizarGrupo(Grupo grupo) {
        grupoService.actualizarGrupo(grupo);

        notificar(
                "MODIFICACION_GRUPO",
                "Se modificó el grupo " + grupo.getNombre(),
                obtenerCedulaFuncionarioActual()
        );
    }

    public void eliminarGrupo(int id) {
        grupoService.eliminarGrupo(id);

        notificar(
                "BAJA_GRUPO",
                "Se eliminó el grupo " + id,
                obtenerCedulaFuncionarioActual()
        );
    }

    public Grupo obtenerGrupoPorId(int id) {
        return grupoService.obtenerPorId(id);
    }

    public List<Grupo> listarGrupos() {
        return grupoService.obtenerTodos();
    }

    // REPORTES
    public String generarReporteEstudiante(String cedula) {
        Reporte reporte = ReporteFactory.crearReporte("estudiante");
        return reporte.generar(cedula);
    }

    public String generarReporteGeneral(String filtroRol) {
        Reporte reporte = ReporteFactory.crearReporte("general");
        return reporte.generar(filtroRol);
    }
}