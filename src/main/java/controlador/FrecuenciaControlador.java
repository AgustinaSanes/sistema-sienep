package controlador;

import modelos.recordatorio.Frecuencia;
import proxy.PermisosProxy;
import util.EntradaHelper;

import java.util.List;
import java.util.Scanner;

public class FrecuenciaControlador {

    private final PermisosProxy proxy;
    private final Scanner sc = new Scanner(System.in);

    public FrecuenciaControlador(PermisosProxy proxy) {
        this.proxy = proxy;
    }


    public void agregarFrecuencia() {

        try {

            System.out.println("--- AGREGAR FRECUENCIA ---");

            System.out.print("Descripción: ");
            String descripcion = sc.nextLine();

            Frecuencia frecuencia =
                    new Frecuencia(0, descripcion, true);

            proxy.agregarFrecuencia(frecuencia);

            System.out.println("Frecuencia agregada correctamente.");

        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }


    public void listarFrecuencias(){

        List<Frecuencia> lista = proxy.listarFrecuencias();

        for(Frecuencia f : lista){
            System.out.println(
                    f.getId() + " - " + f.getDescripcion()
            );
        }
    }


    public void modificarFrecuencia(){

        try {

            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID válido");
            if (id == null) return;

            Frecuencia frecuencia =
                    proxy.obtenerFrecuenciaPorId(id);

            if(frecuencia == null){
                System.out.println("No existe");
                return;
            }

            System.out.print("Nueva descripción: ");
            frecuencia.setDescripcion(sc.nextLine());

            proxy.modificarFrecuencia(frecuencia);

            System.out.println("Frecuencia modificada.");

        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }


    public void eliminarFrecuencia(){

        try {

            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID válido");
            if (id == null) return;

            proxy.eliminarFrecuencia(id);

            System.out.println("Frecuencia eliminada.");

        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void obtenerPorId(){

        try {
            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID válido");
            if (id == null) return;

            Frecuencia f = proxy.obtenerFrecuenciaPorId(id);

            if(f == null){
                System.out.println("Frecuencia no encontrada");
                return;
            }

            System.out.println(
                    f.getId() + " - " + f.getDescripcion()
            );
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}