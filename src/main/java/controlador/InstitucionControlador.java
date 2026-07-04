package controlador;

import modelos.institucion.*;
import servicios.institucion.*;

import java.util.List;
import java.util.Scanner;

public class InstitucionControlador {
    private final ITRService itrService = new ITRService();
    private final CarreraService carreraService = new CarreraService();
    private final GrupoService grupoService = new GrupoService();
    private final Scanner sc = new Scanner(System.in);

    // ITRs
    public void crearITR() {
        try {
            System.out.println("--- CREAR ITR ---");

            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            System.out.print("Calle: ");
            String calle = sc.nextLine();

            System.out.print("Número puerta: ");
            String nroPuerta = sc.nextLine();

            System.out.print("Ciudad: ");
            String ciudad = sc.nextLine();

            System.out.print("Departamento: ");
            String departamento = sc.nextLine();

            System.out.print("Teléfono: ");
            String telefono = sc.nextLine();

            ITR itr = new ITR(0, nombre, calle, nroPuerta, ciudad, departamento, telefono, true);

            itrService.agregarITR(itr);
            System.out.println("ITR creado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarITR() {
        try {
            System.out.println("--- MODIFICAR ITR ---");

            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            ITR itr = itrService.obtenerPorId(id);
            if (itr == null) {
                System.out.println("ITR no encontrado");
                return;
            }

            int opcion;
            do {
                System.out.println("--- ¿QUÉ DESEA MODIFICAR? ---");
                System.out.println("1. Nombre        [" + itr.getNombre() + "]");
                System.out.println("2. Calle         [" + itr.getCalle() + "]");
                System.out.println("3. Número puerta [" + itr.getNroPuerta() + "]");
                System.out.println("4. Ciudad        [" + itr.getCiudad() + "]");
                System.out.println("5. Departamento  [" + itr.getDepartamento() + "]");
                System.out.println("6. Teléfono      [" + itr.getTelefono() + "]");
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
                        itr.setNombre(sc.nextLine());
                    }
                    case 2 -> {
                        System.out.print("Nueva calle: ");
                        itr.setCalle(sc.nextLine());
                    }
                    case 3 -> {
                        System.out.print("Nuevo número puerta: ");
                        itr.setNroPuerta(sc.nextLine());
                    }
                    case 4 -> {
                        System.out.print("Nueva ciudad: ");
                        itr.setCiudad(sc.nextLine());
                    }
                    case 5 -> {
                        System.out.print("Nuevo departamento: ");
                        itr.setDepartamento(sc.nextLine());
                    }
                    case 6 -> {
                        System.out.print("Nuevo teléfono: ");
                        itr.setTelefono(sc.nextLine());
                    }
                    case 0 -> System.out.println("Guardando...");
                    default -> System.out.println("Opción inválida");
                }

            } while (opcion != 0);

            itrService.actualizarITR(itr);
            System.out.println("ITR actualizado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void desactivarITR() {
        try {
            System.out.println("--- DESACTIVAR ITR ---");

            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            itrService.eliminarITR(id);
            System.out.println("ITR desactivado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarITR() {
        try {
            System.out.println("--- BUSCAR ITR ---");

            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            ITR itr = itrService.obtenerPorId(id);
            if (itr == null) {
                System.out.println("ITR no encontrado");
                return;
            }

            System.out.println("=== ITR ===");
            System.out.println("ID: " + itr.getId());
            System.out.println("Nombre: " + itr.getNombre());
            System.out.println("Calle: " + itr.getCalle() + " " + itr.getNroPuerta());
            System.out.println("Ciudad: " + itr.getCiudad());
            System.out.println("Departamento: " + itr.getDepartamento());
            System.out.println("Teléfono: " + itr.getTelefono());
            System.out.println("Estado: " + (itr.isEstado() ? "Activo" : "Inactivo"));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarITRs() {
        try {
            List<ITR> itrs = itrService.obtenerTodos();

            if (itrs.isEmpty()) {
                System.out.println("No hay ITRs registrados");
                return;
            }

            System.out.println("=== ITRs ===");
            for (ITR i : itrs) {
                System.out.println("----------------");
                System.out.println("ID: " + i.getId());
                System.out.println("Nombre: " + i.getNombre());
                System.out.println("Ciudad: " + i.getCiudad());
                System.out.println("Estado: " + (i.isEstado() ? "Activo" : "Inactivo"));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // CARRERAS
    public void crearCarrera() {
        try {
            System.out.println("--- CREAR CARRERA ---");

            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            Carrera carrera = new Carrera();
            carrera.setNombre(nombre);
            carrera.setEstado(true);

            carreraService.agregarCarrera(carrera);
            System.out.println("Carrera creada correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarCarrera() {
        try {
            System.out.println("--- MODIFICAR CARRERA ---");
            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            Carrera carrera = carreraService.obtenerPorId(id);
            if (carrera == null) {
                System.out.println("Carrera no encontrada");
                return;
            }

            System.out.println("Nombre actual: " + carrera.getNombre());
            System.out.print("Nuevo nombre: ");
            carrera.setNombre(sc.nextLine());

            carreraService.actualizarCarrera(carrera);
            System.out.println("Carrera actualizada correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void desactivarCarrera() {
        try {
            System.out.println("--- DESACTIVAR CARRERA ---");

            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            carreraService.eliminarCarrera(id);
            System.out.println("Carrera desactivada correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarCarrera() {
        try {
            System.out.println("--- BUSCAR CARRERA ---");

            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            Carrera carrera = carreraService.obtenerPorId(id);
            if (carrera == null) {
                System.out.println("Carrera no encontrada");
                return;
            }

            System.out.println("=== CARRERA ===");
            System.out.println("ID: " + carrera.getId());
            System.out.println("Nombre: " + carrera.getNombre());
            System.out.println("Estado: " + (carrera.isEstado() ? "Activo" : "Inactivo"));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarCarreras() {
        try {
            List<Carrera> carreras = carreraService.obtenerTodas();

            if (carreras.isEmpty()) {
                System.out.println("No hay carreras registradas");
                return;
            }

            System.out.println("=== CARRERAS ===");
            for (Carrera c : carreras) {
                System.out.println("----------------");
                System.out.println("ID: " + c.getId());
                System.out.println("Nombre: " + c.getNombre());
                System.out.println("Estado: " + (c.isEstado() ? "Activo" : "Inactivo"));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // GRUPOS
    public void crearGrupo() {
        try {
            System.out.println("--- CREAR GRUPO ---");

            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            List<Carrera> carreras = carreraService.obtenerTodas();

            System.out.println("--- CARRERAS DISPONIBLES ---");
            for (Carrera c : carreras) {
                if (c.isEstado()) System.out.println(c.getId() + ". " + c.getNombre());
            }

            System.out.print("Seleccione carrera: ");
            Carrera carrera = carreraService.obtenerPorId(Integer.parseInt(sc.nextLine()));

            if (carrera == null) {
                System.out.println("Carrera inválida");
                return;
            }

            Grupo grupo = new Grupo(0, nombre, true, carrera);
            grupoService.agregarGrupo(grupo);
            System.out.println("Grupo creado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarGrupo() {
        try {
            System.out.println("--- MODIFICAR GRUPO ---");
            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            Grupo grupo = grupoService.obtenerPorId(id);
            if (grupo == null) {
                System.out.println("Grupo no encontrado");
                return;
            }

            System.out.println("Nombre actual: " + grupo.getNombre());
            System.out.print("Nuevo nombre: ");
            grupo.setNombre(sc.nextLine());

            grupoService.actualizarGrupo(grupo);
            System.out.println("Grupo actualizado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void desactivarGrupo() {
        try {
            System.out.println("--- DESACTIVAR GRUPO ---");

            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            grupoService.eliminarGrupo(id);
            System.out.println("Grupo desactivado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarGrupo() {
        try {
            System.out.println("--- BUSCAR GRUPO ---");

            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());

            Grupo grupo = grupoService.obtenerPorId(id);
            if (grupo == null) {
                System.out.println("Grupo no encontrado");
                return;
            }

            System.out.println("=== GRUPO ===");
            System.out.println("ID: " + grupo.getId());
            System.out.println("Nombre: " + grupo.getNombre());
            System.out.println("Carrera: " + grupo.getCarrera().getNombre());
            System.out.println("Estado: " + (grupo.isEstado() ? "Activo" : "Inactivo"));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarGrupos() {
        try {
            List<Grupo> grupos = grupoService.obtenerTodos();
            if (grupos.isEmpty()) {
                System.out.println("No hay grupos registrados");
                return;
            }

            System.out.println("=== GRUPOS ===");
            for (Grupo g : grupos) {
                System.out.println("----------------");
                System.out.println("ID: " + g.getId());
                System.out.println("Nombre: " + g.getNombre());
                System.out.println("Estado: " + (g.isEstado() ? "Activo" : "Inactivo"));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
