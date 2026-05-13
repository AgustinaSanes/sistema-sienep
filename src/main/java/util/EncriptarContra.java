package util;

public class EncriptarContra {

    private static final int DESPLAZAMIENTO = 3;

    public static String encriptar(String texto) {
        StringBuilder resultado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            resultado.append((char) (c + DESPLAZAMIENTO));
        }
        return resultado.toString();
    }

    public static String desencriptar(String texto) {
        StringBuilder resultado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            resultado.append((char) (c - DESPLAZAMIENTO));
        }
        return resultado.toString();
    }
}