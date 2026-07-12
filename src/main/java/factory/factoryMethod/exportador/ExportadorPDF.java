package factory.factoryMethod.exportador;

public class ExportadorPDF implements Exportador {
    @Override
    public void exportar(String datos){
        System.out.println("Exportando reporte a formato PDF (.pdf)...");
        System.out.println(datos);
        System.out.println("Reporte exportado correctamente.");
    }
}
