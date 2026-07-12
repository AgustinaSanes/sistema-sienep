package controlador;

import modelos.informe.InformeAdjunto;
import modelos.usuario.Estudiante;
import proxy.PermisosProxy;
import util.EntradaHelper;

import java.util.List;
import java.util.Scanner;

public class InformeControlador {
    private final PermisosProxy proxy;
    private final Scanner sc = new Scanner(System.in);

    public InformeControlador(PermisosProxy proxy) {
        this.proxy = proxy;
    }

    public void agregarInforme() {

        try {
            System.out.println("--- AGREGAR INFORME ---");

            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            System.out.print("Tipo informe (PDF/JPG/PNG): ");
            String tipo = sc.nextLine();

            System.out.print("Ruta informe: ");
            String ruta = sc.nextLine();

            System.out.print("Categoría: ");
            String categoria = sc.nextLine();

            System.out.print("¿Es confidencial? (s/n): ");
            String respConfidencial = sc.nextLine().trim().toLowerCase();

            if (!respConfidencial.equals("s") && !respConfidencial.equals("n")) {
                System.out.println("Opción inválida. Debe ingresar s o n.");
                return;
            }

            boolean confidencial = respConfidencial.equals("s");

            System.out.print("Cédula estudiante: ");
            String cedula = sc.nextLine();

            Estudiante estudiante = proxy.buscarEstudiantePorCedula(cedula);

            if(estudiante == null){
                System.out.println("Estudiante no encontrado");
                return;
            }

            InformeAdjunto informe =
                    new InformeAdjunto(
                            0,
                            nombre,
                            tipo,
                            ruta,
                            categoria,
                            confidencial,
                            true,
                            estudiante
                    );

            proxy.agregarInforme(informe);
            System.out.println("Informe agregado correctamente");

        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarInforme() {
        try {
            System.out.println("--- MODIFICAR INFORME ---");

            System.out.print("Ingrese ID del informe: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de informe válido");
            if (id == null) return;

            InformeAdjunto informe = proxy.obtenerInformePorId(id);

            if (informe == null) {
                System.out.println("Informe no encontrado");
                return;
            }

            int opcion;
            do {
                System.out.println("--- ¿QUÉ DESEA MODIFICAR? ---");
                System.out.println("1. Nombre          [" + informe.getNombre() + "]");
                System.out.println("2. Tipo archivo    [" + informe.getTipoArchivo() + "]");
                System.out.println("3. Ruta archivo    [" + informe.getRutaArchivo() + "]");
                System.out.println("4. Categoría       [" + informe.getCategoria() + "]");
                System.out.println("5. Confidencial    [" + (informe.isConfidencial() ? "si" : "no") + "]");
                System.out.println("6. Estudiante asociado [" +
                        (informe.getEstudiante() != null ? informe.getEstudiante().getCedula() : "Sin asociar") + "]");
                System.out.println("0. Guardar y volver");
                System.out.print("Opción: ");

                try {
                    opcion = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    opcion = -1;
                }

                switch (opcion) {
                    case 1 -> {
                        System.out.print("Nuevo nombre: ");
                        informe.setNombre(sc.nextLine());
                    }
                    case 2 -> {
                        System.out.print("Nuevo tipo de archivo: ");
                        informe.setTipoArchivo(sc.nextLine());
                    }
                    case 3 -> {
                        System.out.print("Nueva ruta del archivo: ");
                        informe.setRutaArchivo(sc.nextLine());
                    }
                    case 4 -> {
                        System.out.print("Nueva categoría: ");
                        informe.setCategoria(sc.nextLine());
                    }
                    case 5 -> {
                        System.out.print("¿Es confidencial? (s/n): ");
                        String r = sc.nextLine().trim().toLowerCase();

                        if (r.equals("s")) {
                            informe.setConfidencial(true);
                        } else if (r.equals("n")) {
                            informe.setConfidencial(false);
                        } else {
                            System.out.println("Opción inválida. Debe ingresar s o n.");
                        }
                    }

                    case 6 -> {
                        System.out.print("Cédula del nuevo estudiante asociado: ");
                        String cedula = sc.nextLine();

                        Estudiante nuevoEstudiante = proxy.buscarEstudiantePorCedula(cedula);

                        if (nuevoEstudiante == null) {
                            System.out.println("Estudiante no encontrado");
                        } else {
                            informe.setEstudiante(nuevoEstudiante);
                        }
                    }

                    case 0 -> System.out.println("Guardando...");
                    default -> System.out.println("Opción inválida");
                }

            } while (opcion != 0);

            proxy.actualizarInforme(informe);
            System.out.println("Informe actualizado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void eliminarInforme() {

        try {
            System.out.print("ID informe: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de informe válido");
            if (id == null) return;

            proxy.eliminarInforme(id);
            System.out.println("Informe eliminado");

        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarInforme() {

        try {
            System.out.print("ID informe: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de informe válido");
            if (id == null) return;

            InformeAdjunto informe = proxy.obtenerInformePorId(id);

            if(informe == null){
                System.out.println("Informe no encontrado");
                return;
            }

            mostrarInforme(informe);

        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarInformesPorEstudiante() {

        try {
            System.out.print("Cédula estudiante: ");
            String cedula = sc.nextLine();

            List<InformeAdjunto> informes = proxy.obtenerInformePorEstudiante(cedula);

            if(informes.isEmpty()){
                System.out.println("No hay informes");
                return;
            }

            for(InformeAdjunto informe : informes){
                mostrarInforme(informe);
            }

        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarInformes() {

        try {
            List<InformeAdjunto> informes = proxy.obtenerTodosInformes();

            if(informes.isEmpty()){
                System.out.println("No hay informes");
                return;
            }

            for(InformeAdjunto informe : informes){
                mostrarInforme(informe);
            }

        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void mostrarInforme(InformeAdjunto informe){
        System.out.println("---------------------");
        System.out.println("ID: " + informe.getId());
        System.out.println("Nombre: " + informe.getNombre());
        System.out.println("Tipo: " + informe.getTipoArchivo());
        System.out.println("Ruta: " + informe.getRutaArchivo());
        System.out.println("Categoría: " + informe.getCategoria());
        System.out.println("Confidencial: " + informe.isConfidencial());

        if(informe.getEstudiante() != null){
            System.out.println("Estudiante: " + informe.getEstudiante().getCedula());
        }
    }
}