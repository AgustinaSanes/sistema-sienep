package factory.factoryMethod.notificacion;

public class WhatsAppNotificacion implements Notificacion {
    @Override
    public void enviar(String mensaje) {
        System.out.println("Enviando WhatsApp: " + mensaje);
    }
}
