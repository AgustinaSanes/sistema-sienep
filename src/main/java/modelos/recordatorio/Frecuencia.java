package modelos.recordatorio;

public class Frecuencia {

    private int id;
    private String descripcion;
    private boolean estado;

    public Frecuencia() {}

    public Frecuencia(int id, String descripcion, boolean estado){
        this.id = id;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public int getId(){
        return id;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public boolean isEstado(){
        return estado;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    public void setEstado(boolean estado){
        this.estado = estado;
    }


    @Override
    public String toString() {
        return "ID: " + id +
                " | Descripcion: " + descripcion +
                " | Estado: " + (estado ? "Activo":"Inactivo");
    }
}