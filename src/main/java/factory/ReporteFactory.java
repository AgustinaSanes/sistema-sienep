package factory;

public class ReporteFactory {
    public static Reporte crearReporte(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "estudiante" -> new ReporteEstudiante();
            case "general" -> new ReporteGeneral();
            default -> throw new RuntimeException("Tipo de reporte inválido");
        };
    }
}