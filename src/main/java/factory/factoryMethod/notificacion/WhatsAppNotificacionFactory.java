package factory.factoryMethod.notificacion;

public class WhatsAppNotificacionFactory extends NotificacionFactory {
    @Override
    public Notificacion crearNotificacion() {
        return new WhatsAppNotificacion();
    }
}
