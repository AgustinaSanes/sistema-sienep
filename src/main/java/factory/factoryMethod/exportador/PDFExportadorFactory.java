package factory.factoryMethod.exportador;

public class PDFExportadorFactory extends ExportadorFactory {
    @Override
    public Exportador crearExportador(){
        return new ExportadorPDF();
    }
}
