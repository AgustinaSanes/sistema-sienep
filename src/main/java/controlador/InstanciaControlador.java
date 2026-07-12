package controlador;

import factory.abstractFactory.InstanciaFactory;
import modelos.instancia.*;
import modelos.usuario.*;
import proxy.PermisosProxy;
import util.ControlarSesion;
import util.EntradaHelper;
import util.NotificacionHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class InstanciaControlador {

    private final PermisosProxy proxy;
    private final InstanciaFactory instanciaFactory = new InstanciaFactory();
    private final Scanner sc = new Scanner(System.in);

    public InstanciaControlador(PermisosProxy proxy) {
        this.proxy = proxy;
    }

    public void crearInstanciaComun() {
        try {
            System.out.println("--- CREAR INSTANCIA COMÚN ---");

            System.out.print("Cédula estudiante: ");
            String cedEst = sc.nextLine();

            Estudiante estudiante = proxy.buscarEstudiantePorCedula(cedEst);

            if (estudiante == null) {
                System.out.println("Estudiante no encontrado");
                return;
            }

            crearInstanciaComun(estudiante);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Sobrecarga para crear instancia desde ficha alumno
    public void crearInstanciaComun(Estudiante estudiante) {

        try {
            System.out.println("--- CREAR INSTANCIA COMÚN ---");
            System.out.println("Estudiante: " + estudiante.getNombre() + " "
                    + estudiante.getApellido() + " (Cédula: " + estudiante.getCedula() + ")");

            Funcionario funcionario = (Funcionario) ControlarSesion.getUsuario();

            System.out.print("Título: ");
            String titulo = sc.nextLine();

            System.out.print("Comentario: ");
            String comentario = sc.nextLine();

            System.out.print("¿Es confidencial? (s/n): ");
            String respConfidencial = sc.nextLine().trim().toLowerCase();

            if (!respConfidencial.equals("s") && !respConfidencial.equals("n")) {
                System.out.println("Opción inválida. Debe ingresar s o n.");
                return;
            }

            boolean confidencial = respConfidencial.equals("s");

            LocalDateTime fechaHora = pedirFechaHora();

            List<Categoria> categorias = proxy.listarCategorias();

            System.out.println("--- CATEGORÍAS ---");

            for (Categoria c : categorias) {
                if (c.isEstado()) {
                    System.out.println(c.getId() + ". " + c.getNombre());
                }
            }

            System.out.print("Seleccione categoría: ");
            Integer idCategoria = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de categoría válido");
            if (idCategoria == null) return;

            Categoria categoria = proxy.obtenerCategoriaPorId(idCategoria);

            if (categoria == null) {
                System.out.println("Categoría inválida");
                return;
            }

            Instancia instancia =
                    instanciaFactory.crearInstancia("Comun");

            if (instancia instanceof InstanciaComun comun) {

                comun.setComConfidencial(confidencial);
                comun.setTitulo(titulo);
                comun.setFechaHora(fechaHora);
                comun.setComentario(comentario);
                comun.setEstado(true);
                comun.setEstudiante(estudiante);
                comun.setFuncionario(funcionario);
                comun.setCategoria(categoria);

                proxy.crearInstanciaComun(comun);

                System.out.println("Instancia creada correctamente");

                notificarParticipante(estudiante, comun.getId());
                notificarCreador(funcionario, comun.getId());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void crearIncidencia() {

        try {
            System.out.println("--- CREAR INCIDENCIA ---");

            System.out.print("Cédula estudiante: ");
            String cedEst = sc.nextLine();

            Estudiante estudiante = proxy.buscarEstudiantePorCedula(cedEst);

            if (estudiante == null) {
                System.out.println("Estudiante no encontrado");
                return;
            }

            crearIncidencia(estudiante);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Sobrecarga para crear incidencia desde ficha alumno
    public void crearIncidencia(Estudiante estudiante) {

        try {

            System.out.println("--- CREAR INCIDENCIA ---");
            System.out.println("Estudiante: " + estudiante.getNombre() + " "
                    + estudiante.getApellido() + " (Cédula: " + estudiante.getCedula() + ")");

            Funcionario funcionario = (Funcionario) ControlarSesion.getUsuario();

            System.out.print("Título: ");
            String titulo = sc.nextLine();

            System.out.print("Comentario: ");
            String comentario = sc.nextLine();

            System.out.print("¿Es confidencial? (s/n): ");
            String respConfidencial = sc.nextLine().trim().toLowerCase();

            if (!respConfidencial.equals("s") && !respConfidencial.equals("n")) {
                System.out.println("Opción inválida. Debe ingresar s o n.");
                return;
            }

            boolean confidencial = respConfidencial.equals("s");

            System.out.print("Lugar: ");
            String lugar = sc.nextLine();

            LocalDateTime fechaHora = pedirFechaHora();

            Instancia instancia =
                    instanciaFactory.crearInstancia("Incidencia");

            if (instancia instanceof Incidencia incidencia) {

                incidencia.setComConfidencial(confidencial);
                incidencia.setTitulo(titulo);
                incidencia.setFechaHora(fechaHora);
                incidencia.setComentario(comentario);
                incidencia.setEstado(true);
                incidencia.setEstudiante(estudiante);
                incidencia.setFuncionario(funcionario);
                incidencia.setLugar(lugar);

                System.out.print("Agregar involucrado: ");
                String inv = sc.nextLine();

                while (!inv.isEmpty()) {
                    incidencia.agregarInvolucrado(inv);

                    System.out.print(
                            "Otro involucrado (Enter para terminar): "
                    );

                    inv = sc.nextLine();
                }

                proxy.crearIncidencia(incidencia);

                System.out.println("Incidencia creada correctamente");

                notificarParticipante(estudiante, incidencia.getId());
                notificarCreador(funcionario, incidencia.getId());
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarInstancia() {

        try {

            System.out.println("--- BUSCAR INSTANCIA ---");

            System.out.print("ID de instancia: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de instancia válido");
            if (id == null) return;

            Instancia instancia = proxy.obtenerInstanciaPorId(id);

            if (instancia == null) {
                System.out.println("Instancia no encontrada");
                return;
            }

            mostrarInstancia(instancia);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarInstanciasPorEstudiante() {

        try {

            System.out.println("--- INSTANCIAS DE ESTUDIANTE ---");

            System.out.print("Cédula estudiante: ");
            String cedula = sc.nextLine();

            List<Instancia> lista =
                    proxy.obtenerInstanciasPorEstudiante(cedula);

            if (lista.isEmpty()) {
                System.out.println("No hay instancias");
                return;
            }

            for (Instancia i : lista) {
                mostrarInstancia(i);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarInstanciasPorFecha() {

        try {

            System.out.println("--- BUSCAR INSTANCIAS POR FECHA ---");

            System.out.print("Fecha (YYYY-MM-DD): ");
            String input = sc.nextLine().trim();

            if (input.isBlank()) {
                System.out.println("Debe ingresar una fecha");
                return;
            }

            LocalDate fecha = LocalDate.parse(input);

            List<Instancia> lista = proxy.buscarInstanciasPorFecha(fecha);

            if (lista.isEmpty()) {
                System.out.println("No se encontraron instancias para esa fecha.");
                return;
            }

            for (Instancia i : lista) {
                mostrarInstancia(i);
            }

        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha inválido. Use YYYY-MM-DD.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarInstanciasPorDescripcion() {

        try {

            System.out.println("--- BUSCAR INSTANCIAS POR DESCRIPCIÓN ---");

            System.out.print("Ingrese texto a buscar: ");
            String descripcion = sc.nextLine().trim();

            List<Instancia> lista =
                    proxy.buscarInstanciasPorDescripcion(descripcion);

            if (lista.isEmpty()) {
                System.out.println("No se encontraron instancias.");
                return;
            }

            for (Instancia i : lista) {
                mostrarInstancia(i);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarInstanciaComun() {
        modificarInstancia(false);
    }

    public void modificarIncidencia() {
        modificarInstancia(true);
    }

    private void modificarInstancia(boolean esperaIncidencia) {

        try {
            System.out.println("--- MODIFICAR INSTANCIA ---");

            System.out.print("Ingrese ID de la instancia: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de instancia válido");
            if (id == null) return;

            Instancia instancia = proxy.obtenerInstanciaPorId(id);

            if (instancia == null) {
                System.out.println("Instancia no encontrada");
                return;
            }

            boolean esIncidencia = instancia instanceof Incidencia;

            if (esperaIncidencia && !esIncidencia) {
                System.out.println("El ID ingresado no pertenece a una instancia de incidencia");
                return;
            }

            if (!esperaIncidencia && esIncidencia) {
                System.out.println("El ID ingresado no pertenece a una instancia común");
                return;
            }

            System.out.println("Tipo: " + (esIncidencia ? "Incidencia" : "Instancia Común"));

            int opcion;
            do {
                System.out.println("--- ¿QUÉ DESEA MODIFICAR? ---");
                System.out.println("1. Título      [" + instancia.getTitulo() + "]");
                System.out.println("2. Comentario  [" + instancia.getComentario() + "]");
                System.out.println("3. Fecha/Hora  [" + instancia.getFechaHora() + "]");

                if (instancia instanceof Incidencia incidencia) {
                    System.out.println("4. Lugar       [" + incidencia.getLugar() + "]");
                }

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
                        instancia.setTitulo(sc.nextLine());
                    }
                    case 2 -> {
                        System.out.print("Nuevo comentario: ");
                        instancia.setComentario(sc.nextLine());
                    }
                    case 3 -> {
                        try {
                            instancia.setFechaHora(pedirFechaHora());
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    case 4 -> {
                        if (instancia instanceof Incidencia incidencia) {
                            System.out.print("Nuevo lugar: ");
                            String lugar = sc.nextLine();
                            if (lugar == null || lugar.trim().isEmpty()) {
                                System.out.println("El lugar no puede estar vacío");
                            } else {
                                incidencia.setLugar(lugar);
                            }
                        } else {
                            System.out.println("Opción inválida");
                        }
                    }
                    case 0 -> System.out.println("Guardando...");
                    default -> System.out.println("Opción inválida");
                }

            } while (opcion != 0);

            proxy.modificarInstancia(instancia);

            System.out.println("Instancia actualizada correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void desactivarInstancia() {

        try {

            System.out.println("--- DESACTIVAR INSTANCIA ---");

            System.out.print("ID de instancia: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de instancia válido");
            if (id == null) return;

            proxy.eliminarInstancia(id);

            System.out.println(
                    "Instancia desactivada correctamente"
            );

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void clonarInstancia() {

        try {

            System.out.println("--- CLONAR INSTANCIA ---");

            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de instancia válido");
            if (id == null) return;

            Instancia instancia =
                    proxy.obtenerInstanciaPorId(id);

            if (instancia == null) {
                System.out.println(
                        "Instancia no encontrada"
                );
                return;
            }

            proxy.clonarInstancia(instancia);

            System.out.println(
                    "Instancia clonada correctamente"
            );

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void agregarInvolucrado() {

        try {

            System.out.print("ID incidencia: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de incidencia válido");
            if (id == null) return;

            Instancia instancia =
                    proxy.obtenerInstanciaPorId(id);

            if (!(instancia instanceof Incidencia)) {
                System.out.println(
                        "Incidencia no encontrada"
                );
                return;
            }

            System.out.print(
                    "Nombre involucrado: "
            );

            String involucrado = sc.nextLine();

            proxy.agregarInvolucrado(
                    id,
                    involucrado
            );

            System.out.println(
                    "Involucrado agregado"
            );

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void eliminarInvolucrado() {

        try {

            System.out.print("ID incidencia: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de incidencia válido");
            if (id == null) return;

            Instancia instancia =
                    proxy.obtenerInstanciaPorId(id);

            if (!(instancia instanceof Incidencia)) {
                System.out.println(
                        "Incidencia no encontrada"
                );
                return;
            }

            System.out.print(
                    "Nombre involucrado: "
            );

            String involucrado =
                    sc.nextLine();

            proxy.eliminarInvolucrado(
                    id,
                    involucrado
            );

            System.out.println(
                    "Involucrado eliminado"
            );

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void verInvolucrados() {

        try {
            System.out.print("ID incidencia: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de incidencia válido");
            if (id == null) return;

            Instancia instancia = proxy.obtenerInstanciaPorId(id);

            if (!(instancia instanceof Incidencia)) {
                System.out.println("Incidencia no encontrada");
                return;
            }

            List<String> involucrados = proxy.obtenerInvolucrados(id);

            if (involucrados.isEmpty()) {
                System.out.println("No hay involucrados");
                return;
            }

            System.out.println("--- INVOLUCRADOS ---");

            for (String inv : involucrados) {
                System.out.println(inv);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void agregarCategoria() {
        try {
            System.out.println("--- AGREGAR CATEGORÍA ---");
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            Categoria categoria = new Categoria(0, nombre, true);
            proxy.agregarCategoria(categoria);
            System.out.println("Categoría agregada correctamente");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarCategoria() {
        try {
            System.out.println("--- MODIFICAR CATEGORÍA ---");
            System.out.print("ID de categoría: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de categoría válido");
            if (id == null) return;

            Categoria categoria = proxy.obtenerCategoriaPorId(id);
            if (categoria == null) {
                System.out.println("Categoría no encontrada");
                return;
            }

            System.out.println("Nombre actual: " + categoria.getNombre());
            System.out.print("Nuevo nombre: ");
            categoria.setNombre(sc.nextLine());

            proxy.actualizarCategoria(categoria);
            System.out.println("Categoría modificada correctamente");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void eliminarCategoria() {
        try {
            System.out.println("--- ELIMINAR CATEGORÍA ---");
            System.out.print("ID de categoría: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de categoría válido");
            if (id == null) return;

            proxy.eliminarCategoria(id);
            System.out.println("Categoría eliminada correctamente");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarCategoria() {
        try {
            System.out.println("--- BUSCAR CATEGORÍA ---");
            System.out.print("ID de categoría: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de categoría válido");
            if (id == null) return;

            Categoria categoria = proxy.obtenerCategoriaPorId(id);
            if (categoria == null) {
                System.out.println("Categoría no encontrada");
                return;
            }

            System.out.println("ID: " + categoria.getId());
            System.out.println("Nombre: " + categoria.getNombre());
            System.out.println("Estado: " + (categoria.isEstado() ? "Activa" : "Inactiva"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarCategorias() {
        try {
            System.out.println("--- LISTADO DE CATEGORÍAS ---");
            List<Categoria> categorias = proxy.listarCategorias();

            if (categorias.isEmpty()) {
                System.out.println("No hay categorías registradas");
                return;
            }

            for (Categoria c : categorias) {
                System.out.println(c.getId() + ". " + c.getNombre()
                        + " [" + (c.isEstado() ? "Activa" : "Inactiva") + "]");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void categorizarInstancia() {
        try {
            System.out.println("--- CATEGORIZAR INSTANCIA ---");

            System.out.print("ID de instancia: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de instancia válido");
            if (id == null) return;

            Instancia instancia = proxy.obtenerInstanciaPorId(id);

            if (!(instancia instanceof InstanciaComun comun)) {
                System.out.println("La instancia no es una instancia común");
                return;
            }

            if (comun.getCategoria() != null) {
                System.out.println("Categoría actual: " + comun.getCategoria().getNombre());
            }

            List<Categoria> categorias = proxy.listarCategorias();
            System.out.println("--- CATEGORÍAS DISPONIBLES ---");
            for (Categoria c : categorias) {
                if (c.isEstado()) {
                    System.out.println(c.getId() + ". " + c.getNombre());
                }
            }

            System.out.print("Seleccione nueva categoría: ");
            Integer idCategoria = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de categoría válido");
            if (idCategoria == null) return;

            proxy.categorizarInstancia(id, idCategoria);
            System.out.println("Instancia categorizada correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void filtrarPorCategoria() {
        try {
            System.out.println("--- FILTRAR POR CATEGORÍA ---");

            List<Categoria> categorias = proxy.listarCategorias();
            System.out.println("--- CATEGORÍAS ---");
            for (Categoria c : categorias) {
                if (c.isEstado()) {
                    System.out.println(c.getId() + ". " + c.getNombre());
                }
            }

            System.out.print("Seleccione categoría: ");
            Integer idCategoria = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de categoría válido");
            if (idCategoria == null) return;

            List<Instancia> lista = proxy.listarInstanciasPorCategoria(idCategoria);

            if (lista.isEmpty()) {
                System.out.println("No hay instancias para esa categoría");
                return;
            }

            for (Instancia i : lista) {
                mostrarInstancia(i);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private LocalDateTime pedirFechaHora() {

        System.out.print("Fecha (YYYY-MM-DD): ");
        String fecha = sc.nextLine().trim();

        System.out.print("Hora (HH:MM): ");
        String hora = sc.nextLine().trim();

        if (fecha.isEmpty() || hora.isEmpty()) {
            throw new RuntimeException("La fecha y la hora son obligatorias");
        }

        try {
            LocalDateTime fechaHora = LocalDateTime.parse(fecha + "T" + hora);

            if (fechaHora.isBefore(LocalDateTime.now())) {
                throw new RuntimeException("La fecha y hora no pueden ser anteriores a la fecha y hora actual");
            }

            return fechaHora;

        } catch (DateTimeParseException e) {
            throw new RuntimeException("Formato inválido. Use fecha YYYY-MM-DD y hora HH:MM");
        }
    }

    private void notificarParticipante(Estudiante estudiante, int idInstancia) {
        String mensaje = "Estimado/a " + estudiante.getNombre() + " " + estudiante.getApellido()
                + ", se generó la instancia N° " + idInstancia + ".";
        NotificacionHelper.notificar(sc, mensaje, "NOTIFICAR AL ESTUDIANTE");
    }

    private void notificarCreador(Funcionario funcionario, int idInstancia) {
        String mensaje = "Estimado/a " + funcionario.getNombre() + " " + funcionario.getApellido()
                + ", se registró correctamente la instancia N° " + idInstancia + " a su nombre.";
        NotificacionHelper.notificar(sc, mensaje, "NOTIFICAR AL FUNCIONARIO (CREADOR)");
    }

    public void mostrarInstancia(Instancia i) {
        System.out.println("----------------");
        System.out.println("ID: " + i.getId());
        System.out.println("Título: " + i.getTitulo());
        System.out.println("Fecha: " + i.getFechaHora());
        System.out.println("Comentario: " + i.getComentario());
        System.out.println("Confidencial: "
                + (i.getComConfidencial() ? "Sí" : "No"));

        System.out.println("Estado: "
                + (i.isEstado() ? "Activo" : "Inactivo"));

        if (i instanceof InstanciaComun comun) {

            System.out.println(
                    "Tipo: Instancia Común"
            );

            if (comun.getCategoria() != null) {
                System.out.println(
                        "Categoría: "
                                + comun.getCategoria().getNombre()
                );
            }

        } else if (i instanceof Incidencia inc) {

            System.out.println(
                    "Tipo: Incidencia"
            );

            System.out.println(
                    "Lugar: "
                            + inc.getLugar()
            );
        }
    }
}