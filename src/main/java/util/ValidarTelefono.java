package util;

public class ValidarTelefono {

    public static void validar(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new RuntimeException("El teléfono es obligatorio");
        }

        if (!telefono.matches("\\d{1,9}")) {
            throw new RuntimeException("El teléfono debe tener hasta 9 dígitos");
        }
    }
}