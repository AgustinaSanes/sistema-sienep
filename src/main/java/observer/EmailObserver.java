package observer;

public class EmailObserver implements Observer {

    @Override
    public void actualizar(String evento, String detalle, String cedula) {
        //TODO integrar con servicio de email real
        System.out.println("EMAIL — Evento: " + evento + " | Usuario: " + cedula + " | " + detalle);
    }
}