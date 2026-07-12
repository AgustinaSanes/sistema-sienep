package controlador;

import modelos.instancia.*;
import modelos.recordatorio.*;
import proxy.PermisosProxy;
import util.EntradaHelper;
import util.NotificacionHelper;

import java.time.LocalDateTime;
import java.util.*;

public class RecordatorioControlador {
    private final PermisosProxy proxy;
    private final Scanner sc = new Scanner(System.in);

    public RecordatorioControlador(PermisosProxy proxy){
        this.proxy = proxy;
    }

    public void crearRecordatorio() {
        try {
            System.out.println("--- CREAR RECORDATORIO ---");

            System.out.print("ID de instancia: ");
            String inst = sc.nextLine();

            if (inst.isBlank()) {
                System.out.println("Debe ingresar un ID de instancia");
                return;
            }

            int idInstancia;
            try {
                idInstancia = Integer.parseInt(inst);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un ID de instancia válido");
                return;
            }

            Instancia instancia = proxy.obtenerInstanciaPorId(idInstancia);

            if (instancia == null) {
                System.out.println("Instancia no encontrada");
                return;
            }

            System.out.print("Título: ");
            String titulo = sc.nextLine();

            if (titulo.isBlank()) {
                System.out.println("Debe ingresar un título");
                return;
            }

            System.out.print("Fecha (YYYY-MM-DD): ");
            String fecha = sc.nextLine().trim();

            if (fecha.isBlank()) {
                System.out.println("Debe ingresar una fecha");
                return;
            }

            System.out.print("Hora (HH:MM): ");
            String hora = sc.nextLine().trim();

            if (hora.isBlank()) {
                System.out.println("Debe ingresar una hora");
                return;
            }

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
            Integer opTipo = EntradaHelper.leerEntero(sc, "Debe ingresar una opción válida (1, 2 o 3)");
            if (opTipo == null) return;
            String tipo = switch (opTipo) {
                case 1 -> "llamada";
                case 2 -> "reunion";
                case 3 -> "tarea administrativa";
                default -> throw new RuntimeException("Tipo inválido");
            };

            // FRECUENCIA
            List<Frecuencia> frecuencias = proxy.listarFrecuencias();

            System.out.println("--- FRECUENCIAS ---");

            for (Frecuencia f : frecuencias) {
                System.out.println(f.getId() + ". " + f.getDescripcion());
            }

            System.out.print("Seleccione frecuencia: ");
            Integer idFrecuencia = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de frecuencia válido");
            if (idFrecuencia == null) return;

            Frecuencia frecuencia = proxy.obtenerFrecuenciaPorId(idFrecuencia);

            if (frecuencia == null) {
                System.out.println("Frecuencia inválida.");
                return;
            }

            List<CategoriaRecordatorio> categorias = proxy.listarCategoriasRecordatorio();

            if (categorias.isEmpty()) {
                System.out.println("No existen categorías de recordatorio.");
                return;
            }

            System.out.println("--- CATEGORÍAS ---");
            for (CategoriaRecordatorio c : categorias) {
                System.out.println(c.getId() + ". " + c.getNombre());
            }

            System.out.print("Seleccione categoría: ");
            Integer idCategoria = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de categoría válido");
            if (idCategoria == null) return;

            CategoriaRecordatorio categoria =
                    proxy.obtenerCategoriaRecordatorioPorId(idCategoria);

            if (categoria == null) {
                System.out.println("Categoría inválida.");
                return;
            }

            Recordatorio recordatorio = new Recordatorio(
                    0, idInstancia, titulo, fechaHora, tipo, true, frecuencia,categoria);

            proxy.agregarRecordatorio(recordatorio);
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
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de recordatorio válido");
            if (id == null) return;

            Recordatorio r = proxy.obtenerRecordatorioPorId(id);

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
                        String titulo = sc.nextLine().trim();

                        if (titulo.isBlank()) {
                            System.out.println("Debe ingresar un título");
                        } else {
                            r.setTitulo(titulo);
                        }
                    }
                    case 2 -> {
                        System.out.print("Nueva fecha (YYYY-MM-DD): ");
                        String fecha = sc.nextLine().trim();
                        System.out.print("Nueva hora (HH:MM): ");
                        String hora = sc.nextLine().trim();

                        if (fecha.isBlank()) {
                            System.out.println("Debe ingresar una fecha");
                        } else if (hora.isBlank()) {
                            System.out.println("Debe ingresar una hora");
                        } else {
                            try {
                                r.setFechaHora(LocalDateTime.parse(fecha + "T" + hora));
                                fechaModificada = true;
                            } catch (Exception e) {
                                System.out.println("Formato inválido. Use fecha YYYY-MM-DD y hora HH:MM");
                            }
                        }
                    }
                    case 0 -> System.out.println("Guardando...");
                    default -> System.out.println("Opción inválida");
                }

            } while (opcion != 0);

            proxy.modificarRecordatorio(r);
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
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de recordatorio válido");
            if (id == null) return;

            proxy.eliminarRecordatorio(id);
            System.out.println("Recordatorio desactivado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarRecordatorio() {
        try {
            System.out.println("--- BUSCAR RECORDATORIO ---");

            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de recordatorio válido");
            if (id == null) return;

            Recordatorio r = proxy.obtenerRecordatorioPorId(id);

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
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de instancia válido");
            if (id == null) return;

            List<Recordatorio> lista = proxy.listarRecordatoriosPorInstancia(id);

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
        System.out.println("Categoría: " + (r.getCategoria() != null ? r.getCategoria().getNombre() : "Sin categoría"));
        System.out.println("Estado: " + (r.isEstado() ? "Activo" : "Inactivo"));
    }

    public void agregarCategoriaRecordatorio() {

        try {

            System.out.println("--- AGREGAR CATEGORÍA DE RECORDATORIO ---");

            System.out.print("Nombre: ");
            String nombre = sc.nextLine().trim();

            if (nombre.isBlank()) {
                System.out.println("Debe ingresar un nombre");
                return;
            }

            CategoriaRecordatorio categoria =
                    new CategoriaRecordatorio(0, nombre, true);

            proxy.agregarCategoriaRecordatorio(categoria);

            System.out.println("Categoría agregada correctamente.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void modificarCategoriaRecordatorio() {

        try {

            System.out.println("--- MODIFICAR CATEGORÍA DE RECORDATORIO ---");

            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de categoría válido");
            if (id == null) return;

            CategoriaRecordatorio categoria = proxy.obtenerCategoriaRecordatorioPorId(id);

            if (categoria == null) {
                System.out.println("Categoría no encontrada.");
                return;
            }

            System.out.println("Nombre actual: " + categoria.getNombre());

            System.out.print("Nuevo nombre: ");

            String nombre = sc.nextLine().trim();

            if (nombre.isBlank()) {
                System.out.println("Debe ingresar un nombre");
                return;
            }

            categoria.setNombre(nombre);

            proxy.modificarCategoriaRecordatorio(categoria);

            System.out.println("Categoría modificada correctamente.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void eliminarCategoriaRecordatorio() {

        try {

            System.out.println("--- ELIMINAR CATEGORÍA DE RECORDATORIO ---");

            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de categoría válido");
            if (id == null) return;

            proxy.eliminarCategoriaRecordatorio(id);

            System.out.println("Categoría eliminada correctamente.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void buscarCategoriaRecordatorio() {

        try {

            System.out.print("ID: ");

            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de categoría válido");
            if (id == null) return;

            CategoriaRecordatorio categoria = proxy.obtenerCategoriaRecordatorioPorId(id);

            if (categoria == null) {
                System.out.println("Categoría no encontrada.");
                return;
            }

            System.out.println(categoria);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void listarCategoriasRecordatorio() {

        try {

            List<CategoriaRecordatorio> lista = proxy.listarCategoriasRecordatorio();

            if (lista.isEmpty()) {
                System.out.println("No hay categorías.");
                return;
            }

            for (CategoriaRecordatorio c : lista) {
                System.out.println(c);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
