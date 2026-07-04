package controlador;

import factory.abstractFactory.InstanciaFactory;
import modelos.instancia.*;
import modelos.usuario.*;
import proxy.PermisosProxy;
import servicios.instancia.CategoriaService;
import util.ControlarSesion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class InstanciaControlador {

    private final PermisosProxy proxy;
    private final CategoriaService categoriaService = new CategoriaService();
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

            Funcionario funcionario = (Funcionario) ControlarSesion.getUsuario();

            System.out.print("Título: ");
            String titulo = sc.nextLine();

            System.out.print("Comentario: ");
            String comentario = sc.nextLine();

            System.out.print("¿Es confidencial? (s/n): ");
            boolean confidencial = sc.nextLine().equalsIgnoreCase("s");

            List<Categoria> categorias = categoriaService.obtenerTodas();

            System.out.println("--- CATEGORÍAS ---");

            for (Categoria c : categorias) {
                if (c.isEstado()) {
                    System.out.println(c.getId() + ". " + c.getNombre());
                }
            }

            System.out.print("Seleccione categoría: ");
            Categoria categoria =
                    categoriaService.obtenerPorId(
                            Integer.parseInt(sc.nextLine())
                    );

            if (categoria == null) {
                System.out.println("Categoría inválida");
                return;
            }

            Instancia instancia =
                    instanciaFactory.crearInstancia("Comun");

            if (instancia instanceof InstanciaComun comun) {

                comun.setComConfidencial(confidencial);
                comun.setTitulo(titulo);
                comun.setFechaHora(LocalDateTime.now());
                comun.setComentario(comentario);
                comun.setEstado(true);
                comun.setEstudiante(estudiante);
                comun.setFuncionario(funcionario);
                comun.setCategoria(categoria);

                proxy.crearInstanciaComun(comun);

                System.out.println("Instancia creada correctamente");
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

            Funcionario funcionario = (Funcionario) ControlarSesion.getUsuario();

            System.out.print("Título: ");
            String titulo = sc.nextLine();

            System.out.print("Comentario: ");
            String comentario = sc.nextLine();

            System.out.print("¿Es confidencial? (s/n): ");
            boolean confidencial = sc.nextLine().equalsIgnoreCase("s");

            System.out.print("Lugar: ");
            String lugar = sc.nextLine();

            Instancia instancia =
                    instanciaFactory.crearInstancia("Incidencia");

            if (instancia instanceof Incidencia incidencia) {

                incidencia.setComConfidencial(confidencial);
                incidencia.setTitulo(titulo);
                incidencia.setFechaHora(LocalDateTime.now());
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
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarInstancia() {

        try {

            System.out.println("--- BUSCAR INSTANCIA ---");

            System.out.print("ID de instancia: ");
            int id = Integer.parseInt(sc.nextLine());

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

    public void modificarInstancia() {

        try {
            System.out.println("--- MODIFICAR INSTANCIA ---");

            System.out.print("Ingrese ID de la instancia: ");
            int id = Integer.parseInt(sc.nextLine());

            Instancia instancia = proxy.obtenerInstanciaPorId(id);

            if (instancia == null) {
                System.out.println("Instancia no encontrada");
                return;
            }

            int opcion;
            do {
                System.out.println("--- ¿QUÉ DESEA MODIFICAR? ---");
                System.out.println("1. Título      [" + instancia.getTitulo() + "]");
                System.out.println("2. Comentario  [" + instancia.getComentario() + "]");
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
            int id = Integer.parseInt(sc.nextLine());

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
            int id = Integer.parseInt(sc.nextLine());

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
            int id = Integer.parseInt(sc.nextLine());

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
            int id = Integer.parseInt(sc.nextLine());

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
            int id = Integer.parseInt(sc.nextLine());

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
            int id = Integer.parseInt(sc.nextLine());

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
            int id = Integer.parseInt(sc.nextLine());

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
            int id = Integer.parseInt(sc.nextLine());

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
            int id = Integer.parseInt(sc.nextLine());

            Instancia instancia = proxy.obtenerInstanciaPorId(id);

            if (!(instancia instanceof InstanciaComun comun)) {
                System.out.println("La instancia no es una instancia común");
                return;
            }

            if (comun.getCategoria() != null) {
                System.out.println("Categoría actual: " + comun.getCategoria().getNombre());
            }

            List<Categoria> categorias = categoriaService.obtenerTodas();
            System.out.println("--- CATEGORÍAS DISPONIBLES ---");
            for (Categoria c : categorias) {
                if (c.isEstado()) {
                    System.out.println(c.getId() + ". " + c.getNombre());
                }
            }

            System.out.print("Seleccione nueva categoría: ");
            int idCategoria = Integer.parseInt(sc.nextLine());

            proxy.categorizarInstancia(id, idCategoria);
            System.out.println("Instancia categorizada correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void filtrarPorCategoria() {
        try {
            System.out.println("--- FILTRAR POR CATEGORÍA ---");

            List<Categoria> categorias = categoriaService.obtenerTodas();
            System.out.println("--- CATEGORÍAS ---");
            for (Categoria c : categorias) {
                if (c.isEstado()) {
                    System.out.println(c.getId() + ". " + c.getNombre());
                }
            }

            System.out.print("Seleccione categoría: ");
            int idCategoria = Integer.parseInt(sc.nextLine());

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

    private void notificarParticipante(Estudiante estudiante, int idInstancia) {
        String mensaje = "Estimado/a " + estudiante.getNombre() + " " + estudiante.getApellido()
                + ", se generó la instancia N° " + idInstancia + ".";
        NotificacionHelper.notificar(sc, mensaje);
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