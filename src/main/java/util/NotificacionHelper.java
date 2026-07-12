package util;

import factory.factoryMethod.notificacion.EmailNotificacionFactory;
import factory.factoryMethod.notificacion.Notificacion;
import factory.factoryMethod.notificacion.NotificacionFactory;
import factory.factoryMethod.notificacion.WhatsAppNotificacionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Para no repetir logica en instancia y recordatorio
public class NotificacionHelper {

    private NotificacionHelper() {}

    public static void notificar(Scanner sc, String mensaje, String etiqueta) {

        try {
            System.out.println("--- " + etiqueta + " ---");
            System.out.println("1. Correo electrónico");
            System.out.println("2. WhatsApp");
            System.out.println("3. Múltiples canales (Correo + WhatsApp)");
            System.out.println("0. No notificar");
            System.out.print("Opción: ");

            int opcion;
            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            if (opcion == 0) {
                return;
            }

            List<NotificacionFactory> factories = new ArrayList<>();

            switch (opcion) {
                case 1 -> factories.add(new EmailNotificacionFactory());
                case 2 -> factories.add(new WhatsAppNotificacionFactory());
                case 3 -> {
                    factories.add(new EmailNotificacionFactory());
                    factories.add(new WhatsAppNotificacionFactory());
                }
                default -> {
                    System.out.println("Opción inválida, no se notificó");
                    return;
                }
            }

            for (NotificacionFactory factory : factories) {
                Notificacion notificacion = factory.crearNotificacion();
                notificacion.enviar(mensaje);
            }

        } catch (Exception e) {
            System.out.println("Error al notificar: " + e.getMessage());
        }
    }
}
