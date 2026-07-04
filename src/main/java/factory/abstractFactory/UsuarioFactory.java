package factory.abstractFactory;

import modelos.usuario.*;

public class UsuarioFactory {
    public Usuario crearUsuario(String tipo) {
        return tipo.equalsIgnoreCase("Estudiante") ? new Estudiante() :
                tipo.equalsIgnoreCase("Funcionario") ? new Funcionario() :
                        null;
    }
}