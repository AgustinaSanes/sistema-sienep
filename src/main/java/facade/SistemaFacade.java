package facade;
import modelos.archivo.InformeAdjunto;
import modelos.usuario.*;
import modelos.instancia.*;
import observer.*;
import servicios.usuario.*;
import servicios.instancia.*;
import servicios.archivo.*;
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
        private final TelefonoDAO telefonoDAO;

        public SistemaFacade() {
            this.usuarioService = new UsuarioService();
            this.estudianteService = new EstudianteService();
            this.funcionarioService = new FuncionarioService();
            this.instanciaService = new InstanciaService();
            this.comunService = new ComunService();
            this.incidenciaService = new IncidenciaService();
            this.informeService = new InformeService();
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

        // ADJUNTAR INFORME
        public void agregarInforme(InformeAdjunto informe) {
            informeService.agregarInforme(informe);
            notificar("ALTA_INFORME",
                    "Se adjuntó informe: " + informe.getNombre(),
                    informe.getEstudiante().getCedula());
        }

        // ELIMINAR INFORME
        public void eliminarInforme(int id) {
            informeService.eliminarInforme(id);
        }

        // OBTENER INFORMES POR ESTUDIANTE
        public List<InformeAdjunto> obtenerInformesPorEstudiante(String cedula) {
            return informeService.obtenerPorEstudiante(cedula);
        }
    }