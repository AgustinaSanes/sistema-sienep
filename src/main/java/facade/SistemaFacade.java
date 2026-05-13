package facade;
import modelos.archivo.InformeAdjunto;
import modelos.usuario.*;
import modelos.instancia.*;
import servicios.usuario.*;
import servicios.instancia.*;
import servicios.archivo.*;
import dao.usuario.*;

import java.util.List;

public class SistemaFacade {

    //Servicios
    private final UsuarioService usuarioService;
    private final EstudianteService estudianteService;
    private final FuncionarioService funcionarioService;
    private final InstanciaService instanciaService;
    private final ComunService comunService;
    private final IncidenciaService incidenciaService;
    private final InformeService informeService;
    private final TelefonoDAO telefonoDAO;

    public SistemaFacade(){
        this.usuarioService = new UsuarioService();
        this.estudianteService = new EstudianteService();
        this.funcionarioService = new FuncionarioService();
        this.instanciaService = new InstanciaService();
        this.comunService = new ComunService();
        this.incidenciaService = new IncidenciaService();
        this.informeService = new InformeService();
        this.telefonoDAO = new TelefonoDAOImpl();
    }

    // ======================================
    // CREAR ESTUDIANTE
    // Coordina: usuarioService + estudianteService + telefonoDAO
    // ======================================
    public void crearEstudiante(Estudiante estudiante){
        usuarioService.agregarUsuario(estudiante);
        estudianteService.agregarEstudiante(estudiante);
        // Guardar teléfonos si tiene
        if(estudiante.getTelefono()!=null){
            for(String tel : estudiante.getTelefono()){
                telefonoDAO.agregarTelefono(estudiante.getCedula(),tel);
            }
        }
    }

    public List<Usuario> listarUsuarios() {
        return usuarioService.obtenerTodos();
    }

    public List<Estudiante> buscarEstudiantesPorNombre(String texto) {
        return estudianteService.buscarPorNombreApellido(texto);
    }

    // ======================================
    // CREAR FUNCIONARIO
    // Coordina: usuarioService + funcionarioService
    // ======================================
    public void crearFuncionario(Funcionario funcionario){
        usuarioService.agregarUsuario(funcionario);
        funcionarioService.agregarFuncionario(funcionario);
    }

    // ======================================
    // MODIFICAR USUARIO
    // Coordina: usuarioService + estudianteService si es estudiante
    // ======================================
    public void modificarUsuario(Usuario usuario){
        if(usuario instanceof Estudiante estudiante){
            estudianteService.actualizarEstudiante(estudiante);
        }
        usuarioService.actualizarUsuario(usuario);
    }

    // ======================================
    // DESACTIVAR USUARIO
    // Coordina: usuarioService
    // ======================================
    public void desactivarUsuario(String cedula){
        usuarioService.eliminarUsuario(cedula);
    }

    // ======================================
    // BUSCAR USUARIO
    // ======================================
    public Usuario buscarUsuario(String cedula){
        return usuarioService.buscarPorCedula(cedula);
    }

    // ======================================
    // CREAR INSTANCIA COMUN
    // Coordina: instanciaService + comunService
    // ======================================
    public void crearInstanciaComun(InstanciaComun comun) {
        instanciaService.agregarInstancia(comun);
        comunService.agregarComun(comun);
    }

    // ======================================
    // CREAR INCIDENCIA
    // Coordina: instanciaService + incidenciaService
    // ======================================
    public void crearIncidencia(Incidencia incidencia) {
        instanciaService.agregarInstancia(incidencia);
        incidenciaService.agregarIncidencia(incidencia);
    }

    // ======================================
    // ADJUNTAR INFORME
    // Coordina: informeService
    // ======================================
    public void agregarInforme(InformeAdjunto informe){
        informeService.agregarInforme(informe);
    }

    // ======================================
    // ELIMINAR INFORME
    // ======================================
    public void eliminarInforme(int id){
        informeService.eliminarInforme(id);
    }

    // ======================================
    // OBTENER INFORMES POR ESTUDIANTE
    // ======================================
    public List<InformeAdjunto> obtenerInformesPorEstudiante(String cedula) {
        return informeService.obtenerTodos();
    }
}
