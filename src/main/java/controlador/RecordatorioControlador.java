package controlador;

import modelos.instancia.*;
import modelos.recordatorio.*;
import servicios.recordatorio.*;
import proxy.PermisosProxy;

import java.time.LocalDateTime;
import java.util.*;

public class RecordatorioControlador {
    private final PermisosProxy proxy;
    private final RecordatorioService recordatorioService = new RecordatorioService();
    private final Scanner sc = new Scanner(System.in);

    public RecordatorioControlador(PermisosProxy proxy){
        this.proxy = proxy;
    }

    public void crearRecordatorio() {
        try {
            System.out.println("--- CREAR RECORDATORIO ---");

            System.out.print("ID de instancia: ");
            int idInstancia = Integer.parseInt(sc.nextLine());

            Instancia instancia = proxy.obtenerInstanciaPorId(idInstancia);

            if (instancia == null) {
                System.out.println("Instancia no encontrada");
                return;
            }

            System.out.print("Título: ");
            String titulo = sc.nextLine();

            System.out.print("Fecha y hora (YYYY-MM-DDTHH:MM): ");
            LocalDateTime fechaHora = LocalDateTime.parse(sc.nextLine());

            // TIPO
            System.out.println("--- TIPO ---");
            System.out.println("1. llamada");
            System.out.println("2. reunion");
            System.out.println("3. tarea administrativa");
            System.out.print("Seleccione tipo: ");
            int opTipo = Integer.parseInt(sc.nextLine());
            String tipo = switch (opTipo) {
                case 1 -> "llamada";
                case 2 -> "reunion";
                case 3 -> "tarea administrativa";
                default -> throw new RuntimeException("Tipo inválido");
            };

            // FRECUENCIA
            List<Frecuencia> frecuencias = recordatorioService.obtenerFrecuencias();
            System.out.println("--- FRECUENCIAS ---");
            for (Frecuencia f : frecuencias) {
                System.out.println(f.getId() + ". " + f.getDescripcion());
            }
            System.out.print("Seleccione frecuencia: ");
            int idFrecuencia = Integer.parseInt(sc.nextLine());
            Frecuencia frecuencia = recordatorioService.obtenerFrecuenciaPorId(idFrecuencia);

            if (frecuencia == null) {
                System.out.println("Frecuencia inválida");
                return;
            }

            Recordatorio recordatorio = new Recordatorio(
                    0, titulo, fechaHora, tipo, true, frecuencia
            );

            recordatorioService.agregarRecordatorio(recordatorio);
            System.out.println("Recordatorio creado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarRecordatorio() {
        try {
            System.out.println("--- MODIFICAR RECORDATORIO ---");

            System.out.print("ID de recordatorio: ");
            int id = Integer.parseInt(sc.nextLine());

            Recordatorio r = recordatorioService.obtenerPorId(id);
            if (r == null) {
                System.out.println("Recordatorio no encontrado");
                return;
            }

            System.out.println("Título actual: " + r.getTitulo());
            System.out.print("Nuevo título: ");
            r.setTitulo(sc.nextLine());

            System.out.println("Fecha actual: " + r.getFechaHora());
            System.out.print("Nueva fecha (YYYY-MM-DDTHH:MM): ");
            r.setFechaHora(LocalDateTime.parse(sc.nextLine()));

            recordatorioService.actualizarRecordatorio(r);
            System.out.println("Recordatorio actualizado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void desactivarRecordatorio() {
        try {
            System.out.println("--- DESACTIVAR RECORDATORIO ---");

            System.out.print("ID de recordatorio: ");
            int id = Integer.parseInt(sc.nextLine());

            recordatorioService.eliminarRecordatorio(id);
            System.out.println("Recordatorio desactivado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarRecordatorio() {
        try {
            System.out.println("--- BUSCAR RECORDATORIO ---");

            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            Recordatorio r = recordatorioService.obtenerPorId(id);

            if (r == null) {
                System.out.println("Recordatorio no encontrado");
                return;
            }

            mostrarRecordatorio(r);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarRecordatoriosPorInstancia() {
        try {
            System.out.println("--- RECORDATORIOS DE INSTANCIA ---");

            System.out.print("ID de instancia: ");
            int id = Integer.parseInt(sc.nextLine());

            List<Recordatorio> lista = recordatorioService.obtenerPorInstancia(id);

            if (lista.isEmpty()) {
                System.out.println("No hay recordatorios");
                return;
            }

            for (Recordatorio r : lista) {
                mostrarRecordatorio(r);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void mostrarRecordatorio(Recordatorio r) {
        System.out.println("----------------");
        System.out.println("ID: " + r.getId());
        System.out.println("Título: " + r.getTitulo());
        System.out.println("Fecha: " + r.getFechaHora());
        System.out.println("Tipo: " + r.getTipo());
        System.out.println("Frecuencia: " + r.getFrecuencia().getDescripcion());
        System.out.println("Estado: " + (r.isEstado() ? "Activo" : "Inactivo"));
    }
}
