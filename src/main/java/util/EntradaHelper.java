package util;

import java.util.Scanner;

// Utilidad para leer enteros sin pasar (NumberFormatException) hacia el usuario final
public class EntradaHelper {

    private EntradaHelper() {}
    public static Integer leerEntero(Scanner sc, String mensajeError) {
        String input = sc.nextLine().trim();

        if (input.isBlank()) {
            System.out.println(mensajeError);
            return null;
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(mensajeError);
            return null;
        }
    }
}
