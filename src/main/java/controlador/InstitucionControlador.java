package controlador;

import modelos.institucion.*;
import proxy.PermisosProxy;
import util.EntradaHelper;

import java.util.List;
import java.util.Scanner;

public class InstitucionControlador {
    private final PermisosProxy proxy;
    private final Scanner sc = new Scanner(System.in);

    public InstitucionControlador(PermisosProxy proxy) {
        this.proxy = proxy;
    }

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

            proxy.agregarITR(itr);
            System.out.println("ITR creado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarITR() {
        try {
            System.out.println("--- MODIFICAR ITR ---");

            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de ITR válido");
            if (id == null) return;

            ITR itr = proxy.obtenerITRPorId(id);
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

            proxy.actualizarITR(itr);
            System.out.println("ITR actualizado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void desactivarITR() {
        try {
            System.out.println("--- DESACTIVAR ITR ---");

            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de ITR válido");
            if (id == null) return;

            proxy.eliminarITR(id);
            System.out.println("ITR desactivado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarITR() {
        try {
            System.out.println("--- BUSCAR ITR ---");

            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de ITR válido");
            if (id == null) return;

            ITR itr = proxy.obtenerITRPorId(id);
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
            List<ITR> itrs = proxy.listarITRs();

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

            proxy.agregarCarrera(carrera);
            System.out.println("Carrera creada correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarCarrera() {
        try {
            System.out.println("--- MODIFICAR CARRERA ---");
            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de carrera válido");
            if (id == null) return;

            Carrera carrera = proxy.obtenerCarreraPorId(id);
            if (carrera == null) {
                System.out.println("Carrera no encontrada");
                return;
            }

            System.out.println("Nombre actual: " + carrera.getNombre());
            System.out.print("Nuevo nombre: ");
            carrera.setNombre(sc.nextLine());

            proxy.actualizarCarrera(carrera);
            System.out.println("Carrera actualizada correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void desactivarCarrera() {
        try {
            System.out.println("--- DESACTIVAR CARRERA ---");

            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de carrera válido");
            if (id == null) return;

            proxy.eliminarCarrera(id);
            System.out.println("Carrera desactivada correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarCarrera() {
        try {
            System.out.println("--- BUSCAR CARRERA ---");

            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de carrera válido");
            if (id == null) return;

            Carrera carrera = proxy.obtenerCarreraPorId(id);
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
            List<Carrera> carreras = proxy.listarCarreras();

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

    // ASOCIACION ITR - CARRERA
    public void asociarCarreraITR() {
        try {
            System.out.println("--- ASOCIAR CARRERA A ITR ---");

            System.out.print("ID de ITR: ");
            Integer idItr = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de ITR válido");
            if (idItr == null) return;

            List<Carrera> carreras = proxy.listarCarreras();
            System.out.println("--- CARRERAS DISPONIBLES ---");
            for (Carrera c : carreras) {
                if (c.isEstado()) System.out.println(c.getId() + ". " + c.getNombre());
            }

            System.out.print("ID de carrera a asociar: ");
            Integer idCarrera = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de carrera válido");
            if (idCarrera == null) return;

            proxy.asociarCarreraITR(idItr, idCarrera);
            System.out.println("Carrera asociada correctamente al ITR");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void desasociarCarreraITR() {
        try {
            System.out.println("--- DESASOCIAR CARRERA DE ITR ---");

            System.out.print("ID de ITR: ");
            Integer idItr = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de ITR válido");
            if (idItr == null) return;

            System.out.print("ID de carrera a desasociar: ");
            Integer idCarrera = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de carrera válido");
            if (idCarrera == null) return;

            proxy.desasociarCarreraITR(idItr, idCarrera);
            System.out.println("Carrera desasociada correctamente del ITR");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarCarrerasDeITR() {
        try {
            System.out.println("--- CARRERAS DE UN ITR ---");

            System.out.print("ID de ITR: ");
            Integer idItr = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de ITR válido");
            if (idItr == null) return;

            List<Integer> idsCarreras = proxy.obtenerCarrerasPorITR(idItr);
            if (idsCarreras.isEmpty()) {
                System.out.println("Este ITR no tiene carreras asociadas");
                return;
            }

            System.out.println("=== CARRERAS ASOCIADAS ===");
            for (Integer idCarrera : idsCarreras) {
                Carrera carrera = proxy.obtenerCarreraPorId(idCarrera);
                if (carrera != null) {
                    System.out.println("- " + carrera.getId() + ": " + carrera.getNombre());
                }
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

            List<Carrera> carreras = proxy.listarCarreras();

            System.out.println("--- CARRERAS DISPONIBLES ---");
            for (Carrera c : carreras) {
                if (c.isEstado()) System.out.println(c.getId() + ". " + c.getNombre());
            }

            System.out.print("Seleccione carrera: ");
            Integer idCarrera = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de carrera válido");
            if (idCarrera == null) return;

            Carrera carrera = proxy.obtenerCarreraPorId(idCarrera);

            if (carrera == null) {
                System.out.println("Carrera inválida");
                return;
            }

            Grupo grupo = new Grupo(0, nombre, true, carrera);
            proxy.agregarGrupo(grupo);
            System.out.println("Grupo creado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarGrupo() {
        try {
            System.out.println("--- MODIFICAR GRUPO ---");
            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de grupo válido");
            if (id == null) return;

            Grupo grupo = proxy.obtenerGrupoPorId(id);
            if (grupo == null) {
                System.out.println("Grupo no encontrado");
                return;
            }

            System.out.println("Nombre actual: " + grupo.getNombre());
            System.out.print("Nuevo nombre: ");
            grupo.setNombre(sc.nextLine());

            proxy.actualizarGrupo(grupo);
            System.out.println("Grupo actualizado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void desactivarGrupo() {
        try {
            System.out.println("--- DESACTIVAR GRUPO ---");

            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de grupo válido");
            if (id == null) return;

            proxy.eliminarGrupo(id);
            System.out.println("Grupo desactivado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarGrupo() {
        try {
            System.out.println("--- BUSCAR GRUPO ---");

            System.out.print("ID: ");
            Integer id = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de grupo válido");
            if (id == null) return;

            Grupo grupo = proxy.obtenerGrupoPorId(id);
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
            List<Grupo> grupos = proxy.listarGrupos();
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
