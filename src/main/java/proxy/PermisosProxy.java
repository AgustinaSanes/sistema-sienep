package proxy;
import facade.SistemaFacade;
import modelos.informe.InformeAdjunto;
import modelos.instancia.*;
import modelos.usuario.*;
import util.ControlarSesion;
import java.util.List;

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

    // Verifica si el usuario puede ver datos confidenciales
    private boolean puedeVerConfidencial() {
        Usuario usuario = ControlarSesion.getUsuario();

        if (usuario == null) return false;
        String rol = usuario.getRol().getNombre();
        return rol.equalsIgnoreCase("Administrador") ||
                rol.equalsIgnoreCase("Psicopedagogo");
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
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.buscarEstudiantesPorEstado(estado);
    }

    public Estudiante buscarEstudiantePorCedula(String cedula) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.buscarEstudiantePorCedula(cedula);
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
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.obtenerInvolucrados(idInstancia);
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

    public void categorizarInstancia(int idInstancia, int idCategoria) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo");
        facade.categorizarInstancia(idInstancia, idCategoria);
    }

    public List<Instancia> listarInstanciasPorCategoria(int idCategoria) {
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");
        return facade.listarInstanciasPorCategoria(idCategoria);
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
        verificarRol("Administrador", "Psicopedagogo", "Analista de Equipo Educativo", "Funcionario UTEC");

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

    public boolean puedeVerDatosConfidenciales() {
        return puedeVerConfidencial();
    }
}