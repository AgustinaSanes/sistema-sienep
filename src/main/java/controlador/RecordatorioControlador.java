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

            System.out.print("Fecha (YYYY-MM-DD): ");
            String fecha = sc.nextLine().trim();

            System.out.print("Hora (HH:MM): ");
            String hora = sc.nextLine().trim();

            LocalDateTime fechaHora;
            try {
                fechaHora = LocalDateTime.parse(fecha + "T" + hora);
            } catch (Exception e) {
                System.out.println("Formato inválido. Use fecha YYYY-MM-DD y hora HH:MM");
                return;
            }

            // TIPO
            System.out.println("--- TIPO ---");
            System.out.println("1. Llamada");
            System.out.println("2. Reunion");
            System.out.println("3. Tarea administrativa");
            System.out.print("Seleccione tipo: ");
            int opTipo = Integer.parseInt(sc.nextLine());
            String tipo = switch (opTipo) {
                case 1 -> "Llamada";
                case 2 -> "Reunion";
                case 3 -> "Tarea administrativa";
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
                    0, idInstancia, titulo, fechaHora, tipo, true, frecuencia
            );

            recordatorioService.agregarRecordatorio(recordatorio);
            System.out.println("Recordatorio creado correctamente");

            notificarRecordatorio(instancia, recordatorio);
            notificarRecordatorioCreador(instancia, recordatorio);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarRecordatorio() {
        try {
            System.out.println("--- MODIFICAR RECORDATORIO ---");

            System.out.print("Ingrese ID del recordatorio: ");
            int id = Integer.parseInt(sc.nextLine());

            Recordatorio r = recordatorioService.obtenerPorId(id);

            if (r == null) {
                System.out.println("Recordatorio no encontrado");
                return;
            }

            int opcion;
            boolean fechaModificada = false;

            do {
                System.out.println("--- ¿QUÉ DESEA MODIFICAR? ---");
                System.out.println("1. Título      [" + r.getTitulo() + "]");
                System.out.println("2. Fecha/Hora  [" + r.getFechaHora() + "]");
                System.out.println("0. Guardar y volver");
                System.out.print("Opción: ");

                try {
                    opcion = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    opcion = -1;
                }

                switch (opcion) {
                    case 1 -> {
                        System.out.print("Nuevo título: ");
                        r.setTitulo(sc.nextLine());
                    }
                    case 2 -> {
                        System.out.print("Nueva fecha (YYYY-MM-DD): ");
                        String fecha = sc.nextLine().trim();
                        System.out.print("Nueva hora (HH:MM): ");
                        String hora = sc.nextLine().trim();
                        try {
                            r.setFechaHora(LocalDateTime.parse(fecha + "T" + hora));
                            fechaModificada = true;
                        } catch (Exception e) {
                            System.out.println("Formato inválido. Use fecha YYYY-MM-DD y hora HH:MM");
                        }
                    }
                    case 0 -> System.out.println("Guardando...");
                    default -> System.out.println("Opción inválida");
                }

            } while (opcion != 0);

            recordatorioService.actualizarRecordatorio(r);
            System.out.println("Recordatorio actualizado correctamente");

            if (fechaModificada) {
                Instancia instancia = proxy.obtenerInstanciaPorId(r.getIdInstancia());
                if (instancia != null) {
                    notificarCambioRecordatorio(instancia, r);
                    notificarCambioRecordatorioCreador(instancia, r);
                }
            }

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

    // --- Notificación del recordatorio al estudiante de la instancia (Factory Method) ---
    private void notificarRecordatorio(Instancia instancia, Recordatorio recordatorio) {

        if (instancia.getEstudiante() == null) {
            return;
        }

        String mensaje = "Estimado/a " + instancia.getEstudiante().getNombre() + " "
                + instancia.getEstudiante().getApellido()
                + ", tiene un recordatorio (\"" + recordatorio.getTitulo() + "\") programado para "
                + recordatorio.getFechaHora() + " asociado a la instancia N° " + instancia.getId() + ".";

        NotificacionHelper.notificar(sc, mensaje, "NOTIFICAR AL ESTUDIANTE");
    }

    private void notificarRecordatorioCreador(Instancia instancia, Recordatorio recordatorio) {

        if (instancia.getFuncionario() == null) {
            return;
        }

        String mensaje = "Estimado/a " + instancia.getFuncionario().getNombre() + " "
                + instancia.getFuncionario().getApellido()
                + ", se registró el recordatorio (\"" + recordatorio.getTitulo() + "\") para "
                + recordatorio.getFechaHora() + " asociado a la instancia N° " + instancia.getId() + ".";

        NotificacionHelper.notificar(sc, mensaje, "NOTIFICAR AL FUNCIONARIO (CREADOR)");
    }

    // --- Notificación de reprogramación del recordatorio (Factory Method) ---
    private void notificarCambioRecordatorio(Instancia instancia, Recordatorio recordatorio) {

        if (instancia.getEstudiante() == null) {
            return;
        }

        String mensaje = "Estimado/a " + instancia.getEstudiante().getNombre() + " "
                + instancia.getEstudiante().getApellido()
                + ", el recordatorio (\"" + recordatorio.getTitulo() + "\") de la instancia N° "
                + instancia.getId() + " fue reprogramado para " + recordatorio.getFechaHora() + ".";

        NotificacionHelper.notificar(sc, mensaje, "NOTIFICAR AL ESTUDIANTE");
    }

    private void notificarCambioRecordatorioCreador(Instancia instancia, Recordatorio recordatorio) {

        if (instancia.getFuncionario() == null) {
            return;
        }

        String mensaje = "Estimado/a " + instancia.getFuncionario().getNombre() + " "
                + instancia.getFuncionario().getApellido()
                + ", el recordatorio (\"" + recordatorio.getTitulo() + "\") de la instancia N° "
                + instancia.getId() + " fue reprogramado para " + recordatorio.getFechaHora() + ".";

        NotificacionHelper.notificar(sc, mensaje, "NOTIFICAR AL FUNCIONARIO (CREADOR)");
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
