package factory.factoryMethod.exportador;

public class ExcelExportadorFactory extends ExportadorFactory {
    @Override
    public Exportador crearExportador() {
        return new ExportadorExcel();
    }
}
