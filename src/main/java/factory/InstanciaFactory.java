package factory;

import modelos.instancia.*;

public class InstanciaFactory {
    public Instancia crearInstancia(String tipo) {
        return tipo.equalsIgnoreCase("Incidencia") ? new Incidencia() :
                tipo.equalsIgnoreCase("Comun") ? new InstanciaComun() :
                        null;
    }
}
