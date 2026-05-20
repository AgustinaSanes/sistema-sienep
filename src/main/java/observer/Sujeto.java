package observer;

public interface Sujeto {
    void agregarObserver(Observer observer);
    void quitarObserver(Observer observer);
    void notificar(String evento,String detalle,String cedula);
}
