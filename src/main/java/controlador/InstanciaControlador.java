package controlador;

import modelos.instancia.*;
import modelos.usuario.*;
import proxy.PermisosProxy;
import util.ControlarSesion;
import servicios.instancia.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class InstanciaControlador {
    private final PermisosProxy proxy;
    private final CategoriaService categoriaService = new CategoriaService();
    private final Scanner sc = new Scanner(System.in);

    public InstanciaControlador(PermisosProxy proxy){
        this.proxy = proxy;
    }

    public void crearInstanciaComun() {
        try {
            System.out.println("--- CREAR INSTANCIA COMÚN ---");

            System.out.print("Cédula estudiante: ");
            String cedEst = sc.nextLine();

            Estudiante estudiante = (Estudiante) proxy.buscarUsuario(cedEst);
            if (estudiante == null) {
                System.out.println("Estudiante no encontrado");
                return;
            }

            Funcionario funcionario = (Funcionario) proxy.buscarUsuario(
                    ControlarSesion.getUsuario().getCedula()
            );

            System.out.print("Título: ");
            String titulo = sc.nextLine();

            System.out.print("Comentario: ");
            String comentario = sc.nextLine();

            System.out.print("¿Es confidencial? (s/n): ");
            boolean confidencial = sc.nextLine().equalsIgnoreCase("s");

            // ELEGIR CATEGORÍA
            List<Categoria> categorias = categoriaService.obtenerTodas();

            System.out.println("--- CATEGORÍAS ---");
            for (Categoria c : categorias) {
                if (c.isEstado()) System.out.println(c.getId() + ". " + c.getNombre());
            }

            System.out.print("Seleccione categoría: ");
            Categoria categoria = categoriaService.obtenerPorId(Integer.parseInt(sc.nextLine()));

            if (categoria == null) {
                System.out.println("Categoría inválida");
                return;
            }

            InstanciaComun instancia = new InstanciaComun(
                    0, confidencial, titulo,
                    LocalDateTime.now(), comentario,
                    true, estudiante, funcionario, categoria
            );

            proxy.crearInstanciaComun(instancia);
            System.out.println("Instancia creada correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void crearIncidencia() {
        try {
            System.out.println("--- CREAR INCIDENCIA ---");

            System.out.print("Cédula estudiante: ");
            String cedEst = sc.nextLine();
            Estudiante estudiante = (Estudiante) proxy.buscarUsuario(cedEst);

            if (estudiante == null) {
                System.out.println("Estudiante no encontrado");
                return;
            }

            Funcionario funcionario = (Funcionario) proxy.buscarUsuario(
                    ControlarSesion.getUsuario().getCedula()
            );

            System.out.print("Título: ");
            String titulo = sc.nextLine();

            System.out.print("Comentario: ");
            String comentario = sc.nextLine();

            System.out.print("¿Es confidencial? (s/n): ");
            boolean confidencial = sc.nextLine().equalsIgnoreCase("s");

            System.out.print("Lugar: ");
            String lugar = sc.nextLine();

            Incidencia incidencia = new Incidencia(
                    0, confidencial, titulo,
                    LocalDateTime.now(), comentario,
                    true, estudiante, funcionario, lugar
            );

            System.out.print("Agregar involucrado (Enter para omitir): ");
            String inv = sc.nextLine();

            while (!inv.isEmpty()) {
                incidencia.agregarInvolucrado(inv);
                System.out.print("Otro involucrado (Enter para terminar): ");
                inv = sc.nextLine();
            }

            proxy.crearIncidencia(incidencia);
            System.out.println("Incidencia creada correctamente");

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

            List<Instancia> lista = proxy.obtenerInstanciasPorEstudiante(cedula);
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

            System.out.print("ID de instancia: ");
            int id = Integer.parseInt(sc.nextLine());

            Instancia instancia = proxy.obtenerInstanciaPorId(id);
            if (instancia == null) {
                System.out.println("Instancia no encontrada");
                return;
            }

            System.out.println("Título actual: " + instancia.getTitulo());
            System.out.print("Nuevo título: ");
            instancia.setTitulo(sc.nextLine());

            System.out.println("Comentario actual: " + instancia.getComentario());
            System.out.print("Nuevo comentario: ");
            instancia.setComentario(sc.nextLine());

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
            System.out.println("Instancia desactivada correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void clonarInstancia() {
        try {
            System.out.println("--- CLONAR INSTANCIA ---");

            System.out.print("ID de instancia a clonar: ");
            int id = Integer.parseInt(sc.nextLine());

            Instancia instancia = proxy.obtenerInstanciaPorId(id);
            if (instancia == null) {
                System.out.println("Instancia no encontrada");
                return;
            }

            proxy.clonarInstancia(instancia);
            System.out.println("Instancia clonada correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void mostrarInstancia(Instancia i) {
        System.out.println("----------------");
        System.out.println("ID: " + i.getId());
        System.out.println("Título: " + i.getTitulo());
        System.out.println("Fecha: " + i.getFechaHora());
        System.out.println("Comentario: " + i.getComentario());
        System.out.println("Confidencial: " + (i.getComConfidencial() ? "Sí" : "No"));
        System.out.println("Estado: " + (i.isEstado() ? "Activo" : "Inactivo"));

        if (i instanceof InstanciaComun comun) {
            System.out.println("Tipo: Instancia Común");
            if (comun.getCategoria() != null) {
                System.out.println("Categoría: " + comun.getCategoria().getNombre());
            }
        } else if (i instanceof Incidencia inc) {
            System.out.println("Tipo: Incidencia");
            System.out.println("Lugar: " + inc.getLugar());
        }
    }
}
