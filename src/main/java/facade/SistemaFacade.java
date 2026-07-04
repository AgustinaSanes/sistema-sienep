package facade;
import modelos.informe.InformeAdjunto;
import modelos.usuario.*;
import modelos.instancia.*;
import observer.*;
import servicios.informe.InformeService;
import servicios.usuario.*;
import servicios.instancia.*;
import dao.usuario.*;
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

    // BUSCAR USUARIO
    public Usuario buscarUsuario(String cedula) {
        return usuarioService.buscarPorCedula(cedula);
    }

    // LISTAR USUARIOS
    public List<Usuario> listarUsuarios() {
        return usuarioService.obtenerTodos();
    }

    // BUSCAR ESTUDIANTES
    public List<Estudiante> buscarEstudiantesPorNombre(String texto) {
        return estudianteService.buscarPorNombreApellido(texto);
    }

    public List<Estudiante> buscarEstudiantesPorCarrera(int idCarrera) {
        return estudianteService.buscarPorCarrera(idCarrera);
    }

    public List<Estudiante> buscarEstudiantesPorGrupo(int idGrupo) {
        return estudianteService.buscarPorGrupo(idGrupo);
    }

    public List<Estudiante> buscarEstudiantesPorEstado(boolean estado) {
        return estudianteService.buscarPorEstado(estado);
    }

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
    public List<Instancia> obtenerInstanciasPorEstudiante(String cedula) {
        return instanciaService.obtenerPorEstudiante(cedula);
    }

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

    // CRUD CATEGORIAS
    public void agregarCategoria(Categoria categoria) {
        categoriaService.agregarCategoria(categoria);
    }

    public void actualizarCategoria(Categoria categoria) {
        categoriaService.actualizarCategoria(categoria);
    }

    public void eliminarCategoria(int id) {
        categoriaService.eliminarCategoria(id);
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

    // ADJUNTAR ARCHIVO
    public void agregarInforme(InformeAdjunto informe) {
        informeService.agregarInforme(informe);
        notificar("ALTA_INFORME",
                "Se adjuntó informe: " + informe.getNombre(),
                informe.getEstudiante().getCedula());
    }

    // ACTUALIZAR ARCHIVO
    public void actualizarInforme(InformeAdjunto informe) {
        informeService.actualizarInforme(informe);
    }

    // ELIMINAR ARCHIVO
    public void eliminarInforme(int id) {
        informeService.eliminarInforme(id);
    }

    // OBTENER ARCHIVOS POR ESTUDIANTE
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
}