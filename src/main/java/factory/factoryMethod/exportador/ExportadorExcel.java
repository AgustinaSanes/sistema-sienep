package factory.factoryMethod.exportador;

public class ExportadorExcel implements Exportador {
    @Override
    public void exportar(String datos) {
        System.out.println("Exportando reporte a formato Excel (.xlsx)...");
        System.out.println(datos);
        System.out.println("Reporte exportado correctamente.");
    }
}
