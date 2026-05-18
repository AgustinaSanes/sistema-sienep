package proxy;
import facade.SistemaFacade;
import modelos.archivo.InformeAdjunto;
import modelos.instancia.*;
import modelos.usuario.*;
import util.ControlarSesion;
import java.util.List;

public class PermisosProxy {

    private final SistemaFacade facade;

    public PermisosProxy(SistemaFacade facade) {this.facade = facade;}

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

    // Verifica si el usuario puede ver datos confidenciales
    private boolean puedeVerConfidencial() {
        Usuario usuario = ControlarSesion.getUsuario();

        if (usuario == null) return false;
        String rol = usuario.getRol().getNombre();
        return rol.equalsIgnoreCase("Administrador") ||
                rol.equalsIgnoreCase("Psicopedagogo");
    }


    // USUARIOS
    // Solo Administrador puede crear/modificar/eliminar usuarios
    public void crearEstudiante(Estudiante estudiante) {
        verificarRol("Administrador");
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

    // Todos pueden buscar usuarios menos Estudiante
    public Usuario buscarUsuario(String cedula) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.buscarUsuario(cedula);
    }

    public List<Usuario> listarUsuarios() {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        return facade.listarUsuarios();
    }

    // ESTUDIANTES
    public List<Estudiante> buscarEstudiantesPorNombre(String texto) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.buscarEstudiantesPorNombre(texto);
    }

    public List<Estudiante> buscarEstudiantesPorCarrera(int idCarrera) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.buscarEstudiantesPorCarrera(idCarrera);
    }

    public List<Estudiante> buscarEstudiantesPorGrupo(int idGrupo) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.buscarEstudiantesPorGrupo(idGrupo);
    }

    public List<Estudiante> buscarEstudiantesPorEstado(boolean estado) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        return facade.buscarEstudiantesPorEstado(estado);
    }

    // INSTANCIAS
    // Psicopedagogo y Analista pueden crear/modificar
    // Estudiante y Funcionario UTEC solo lectura no confidencial
    public void crearInstanciaComun(InstanciaComun comun) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.crearInstanciaComun(comun);
    }

    public void crearIncidencia(Incidencia incidencia) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.crearIncidencia(incidencia);
    }

    public void clonarInstancia(Instancia instancia) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.clonarInstancia(instancia);
    }

    public Instancia obtenerInstanciaPorId(int id) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.obtenerInstanciaPorId(id);
    }

    public List<Instancia> obtenerInstanciasPorEstudiante(String cedula) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.obtenerInstanciasPorEstudiante(cedula);
    }

    public void modificarInstancia(Instancia instancia) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.modificarInstancia(instancia);
    }

    public void eliminarInstancia(int id) {
        verificarRol("Administrador", "Psicopedagogo");
        facade.eliminarInstancia(id);
    }

    // INFORMES
    // Solo Psicopedagogo y Admin pueden ver confidenciales
    public void agregarInforme(InformeAdjunto informe) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.agregarInforme(informe);
    }

    public void eliminarInforme(int id) {
        verificarRol("Administrador", "Psicopedagogo");
        facade.eliminarInforme(id);
    }

    public List<InformeAdjunto> obtenerInformesPorEstudiante(String cedula) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");

        List<InformeAdjunto> informes = facade.obtenerInformesPorEstudiante(cedula);

        // Si no puede ver confidenciales, filtra los informes confidenciales
        if (!puedeVerConfidencial()) {
            informes.removeIf(InformeAdjunto::isConfidencial);
        }

        return informes;
    }

    // METODO HELPER — puede ver confidencial?
    public boolean puedeVerDatosConfidenciales() {
        return puedeVerConfidencial();
    }
}