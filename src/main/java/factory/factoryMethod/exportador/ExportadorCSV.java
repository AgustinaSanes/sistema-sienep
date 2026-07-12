package factory.factoryMethod.exportador;

public class ExportadorCSV implements Exportador{
    @Override
    public void exportar(String datos) {
        System.out.println("Exportando reporte a formato CSV (.csv)...");
        System.out.println(datos);
        System.out.println("Reporte exportado correctamente.");
    }
}
