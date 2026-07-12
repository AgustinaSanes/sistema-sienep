package util;

public class ValidarCedula {

    private static final int[] FACTORES = {2, 9, 8, 7, 6, 3, 4};

    public static void validar(String cedula) {

        if (cedula == null || cedula.trim().isEmpty()) {
            throw new RuntimeException("La cédula es obligatoria");
        }

        if (!cedula.matches("\\d{8}")) {
            throw new RuntimeException("La cédula debe tener 8 dígitos");
        }

        int suma = 0;

        for (int i = 0; i < 7; i++) {
            int digito = Character.getNumericValue(cedula.charAt(i));
            suma += digito * FACTORES[i];
        }

        int resto = suma % 10;
        int digitoVerificador = (10 - resto) % 10;

        int ultimoDigito = Character.getNumericValue(cedula.charAt(7));

        if (digitoVerificador != ultimoDigito) {
            throw new RuntimeException("La cédula es inválida");
        }
    }
}