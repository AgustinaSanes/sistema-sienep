package util;
import java.time.LocalDate;
import java.time.Period;

public class ValidarEdad {

    public static boolean esMayorDeEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return false;
        }

        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

        return edad >= 18;
    }
}