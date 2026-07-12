package factory.factoryMethod.exportador;

public class CSVExportadorFactory extends ExportadorFactory {
    @Override
    public Exportador crearExportador(){
        return new ExportadorCSV();
    }
}
