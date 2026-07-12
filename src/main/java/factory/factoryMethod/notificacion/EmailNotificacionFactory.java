package factory.factoryMethod.notificacion;

public class EmailNotificacionFactory extends NotificacionFactory{
    @Override
    public Notificacion crearNotificacion(){
        return new EmailNotificacion();
    }
}
