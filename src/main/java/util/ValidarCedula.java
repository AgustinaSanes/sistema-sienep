package util;

public class ValidarCedula {

    private static final int[] FACTORES = {2, 9, 8, 7, 6, 3, 4};

    public static boolean esValida(String cedula) {

        if (cedula == null || !cedula.matches("\\d{8}")) {
            return false;
        }

        int suma = 0;

        for (int i = 0; i < 7; i++) {
            int digito = Character.getNumericValue(cedula.charAt(i));
            suma += digito * FACTORES[i];
        }

        int resto = suma % 10;
        int digitoVerificador = (10 - resto) % 10;

        int ultimoDigito = Character.getNumericValue(cedula.charAt(7));

        return digitoVerificador == ultimoDigito;
    }
}