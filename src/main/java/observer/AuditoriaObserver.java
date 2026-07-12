package observer;
import servicios.usuario.AuditoriaService;

public class AuditoriaObserver implements Observer {

    private final AuditoriaService auditoriaService;

    public AuditoriaObserver() {
        this.auditoriaService = new AuditoriaService();
    }

    @Override
    public void actualizar(String evento, String detalle, String cedula) {
        // Determina la entidad afectada según el evento
        String entidad = determinarEntidad(evento);
        auditoriaService.registrar(cedula, evento, detalle, entidad);
    }

    private String determinarEntidad(String evento) {
        if (evento.contains("ESTUDIANTE")) return "estudiantes";
        if (evento.contains("FUNCIONARIO")) return "funcionarios";
        if (evento.contains("INSTANCIA")) return "instancias";
        if (evento.contains("INCIDENCIA")) return "incidencias";
        if (evento.contains("INFORME")) return "informes_adjuntos";
        if (evento.contains("LOGIN")) return "usuarios";
        if (evento.contains("RECORDATORIO")) return "recordatorios";
        if (evento.contains("CATEGORIA")) return "categorias";
        if (evento.contains("FRECUENCIA")) return "frecuencias";
        if (evento.contains("ROL")) return "roles";
        if (evento.contains("PERMISO")) return "permisos";
        if (evento.contains("ITR")) return "itrs";
        if (evento.contains("CARRERA")) return "carreras";
        if (evento.contains("GRUPO")) return "grupos";
        return "sistema";
    }
}