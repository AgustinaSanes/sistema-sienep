package controlador;

import factory.abstractFactory.UsuarioFactory;
import modelos.institucion.*;
import modelos.usuario.*;
import proxy.PermisosProxy;
import util.EntradaHelper;

import java.time.LocalDate;
import java.util.*;

public class EstudianteControlador {
    private final PermisosProxy proxy;
    private final InstanciaControlador instanciaControlador;
    private final Scanner sc = new Scanner(System.in);

    public EstudianteControlador(PermisosProxy proxy) {
        this.proxy = proxy;
        this.instanciaControlador = new InstanciaControlador(proxy);
    }

    public void crearEstudiante() {
        try {
            System.out.println("--- CREAR ESTUDIANTE ---");

            System.out.print("Cédula: ");
            String cedula = sc.nextLine();

            System.out.print("Nombre: ");
            String nombre = sc.nextLine();

            System.out.print("Apellido: ");
            String apellido = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Contraseña: ");
            String contrasena = sc.nextLine();

            Rol rol = proxy.obtenerRolParaEstudiante(5);

            if (rol == null || !rol.isEstado()) {
                System.out.println("No existe el rol Estudiante");
                return;
            }

            List<Carrera> carreras = proxy.listarCarrerasCatalogo();
            System.out.println("--- CARRERAS DISPONIBLES ---");
            for (Carrera c : carreras) {
                if (c.isEstado()) System.out.println(c.getId() + ". " + c.getNombre());
            }
            System.out.print("Seleccione carrera: ");
            Integer idCarrera = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de carrera válido");
            if (idCarrera == null) return;

            Carrera carrera = proxy.obtenerCarreraCatalogoPorId(idCarrera);
            if (carrera == null) { System.out.println("Carrera inválida"); return; }

            List<Grupo> grupos = proxy.listarGruposCatalogo();
            System.out.println("--- GRUPOS DISPONIBLES ---");
            for (Grupo g : grupos) {
                if (g.isEstado()) System.out.println(g.getId() + ". " + g.getNombre());
            }
            System.out.print("Seleccione grupo (Enter para omitir): ");
            String idGrupo = sc.nextLine();

            Grupo grupo = null;

            if (!idGrupo.isBlank()) {
                int idGrupoInt;
                try {
                    idGrupoInt = Integer.parseInt(idGrupo.trim());
                } catch (NumberFormatException e) {
                    System.out.println("Debe ingresar un ID de grupo válido");
                    return;
                }

                grupo = proxy.obtenerGrupoCatalogoPorId(idGrupoInt);

                if (grupo == null) {
                    System.out.println("Grupo inválido");
                    return;
                }
            }

            UsuarioFactory factory = new UsuarioFactory();
            Estudiante estudiante = (Estudiante) factory.crearUsuario("Estudiante");

            estudiante.setCedula(cedula);
            estudiante.setNombre(nombre);
            estudiante.setApellido(apellido);
            estudiante.setEmail(email);
            estudiante.setContrasena(contrasena);
            estudiante.setEstado(true);
            estudiante.setRol(rol);
            estudiante.setGrupo(grupo);

            System.out.print("Sistema de salud: ");
            estudiante.setSistemaSalud(sc.nextLine());

            System.out.print("Fecha nacimiento (YYYY-MM-DD): ");
            String fecha = sc.nextLine();

            if (fecha.isBlank()) {
                System.out.println("La fecha de nacimiento es obligatoria");
                return;
            }

            try {
                estudiante.setFechaNacimiento(LocalDate.parse(fecha));
            } catch (Exception e) {
                System.out.println("Formato inválido. Use YYYY-MM-DD");
                return;
            }

            System.out.print("Motivo: ");
            estudiante.setMotivo(sc.nextLine());

            System.out.print("Calle: ");
            estudiante.setCalle(sc.nextLine());

            System.out.print("Número puerta: ");
            estudiante.setNroPuerta(sc.nextLine());

            System.out.print("Foto (Enter para omitir): ");
            String foto = sc.nextLine();
            estudiante.setFoto(foto.isEmpty() ? null : foto);

            System.out.print("¿Observaciones confidenciales? (s/n): ");
            String obsConf = sc.nextLine().trim();

            if (!obsConf.equalsIgnoreCase("s") &&
                    !obsConf.equalsIgnoreCase("n")) {

                System.out.println("Debe responder s o n");
                return;
            }

            estudiante.setObsConfidenciales(obsConf.equalsIgnoreCase("s"));

            System.out.print("Observaciones/comentarios (Enter para omitir): ");
            String obsComentarios = sc.nextLine();
            estudiante.setObsComentarios(obsComentarios.isEmpty() ? null : obsComentarios);

            System.out.print("Información estado de salud (Enter para omitir): ");
            String infoSalud = sc.nextLine();
            estudiante.setInfoEstadoSalud(infoSalud.isEmpty() ? null : infoSalud);

            System.out.println("Ingrese los teléfonos (Enter vacío para finalizar):");

            while (true) {
                System.out.print("Teléfono: ");
                String telefono = sc.nextLine().trim();

                if (telefono.isEmpty()) {
                    break;
                }

                estudiante.agregarTelefono(telefono);
            }

            proxy.agregarEstudiante(estudiante);
            System.out.println("Estudiante creado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void modificarEstudiante() {
        try {
            System.out.println("--- MODIFICAR ESTUDIANTE ---");

            System.out.print("Ingrese cédula: ");
            String cedula = sc.nextLine();

            Usuario usuario = proxy.buscarUsuario(cedula);
            if (usuario == null) {
                System.out.println("Estudiante no encontrado");
                return;
            }

            if (!(usuario instanceof Estudiante estudiante)) {
                System.out.println("El usuario no es un estudiante");
                return;
            }

            int opcion;
            do {
                System.out.println("--- ¿QUÉ DESEA MODIFICAR? ---");
                System.out.println("1. Nombre          [" + estudiante.getNombre() + "]");
                System.out.println("2. Apellido        [" + estudiante.getApellido() + "]");
                System.out.println("3. Email           [" + estudiante.getEmail() + "]");
                System.out.println("4. Sistema salud   [" + estudiante.getSistemaSalud() + "]");
                System.out.println("5. Fecha nacimiento[" + estudiante.getFechaNacimiento() + "]");
                System.out.println("6. Motivo          [" + estudiante.getMotivo() + "]");
                System.out.println("7. Calle           [" + estudiante.getCalle() + "]");
                System.out.println("8. Número puerta   [" + estudiante.getNroPuerta() + "]");
                System.out.println("9. Teléfono        [" + estudiante.getTelefono() + "]");
                System.out.println("10. Foto                   [" + estudiante.getFoto() + "]");
                System.out.println("11. Obs. confidenciales    [" + estudiante.isObsConfidenciales() + "]");
                System.out.println("12. Obs./Comentarios       [" + estudiante.getObsComentarios() + "]");
                System.out.println("13. Info. estado de salud  [" + estudiante.getInfoEstadoSalud() + "]");
                System.out.println("14. Contraseña");
                System.out.println("15. Estado          [" + (estudiante.isEstado() ? "Activo" : "Inactivo") + "]");
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
                        estudiante.setNombre(sc.nextLine());
                    }
                    case 2 -> {
                        System.out.print("Nuevo apellido: ");
                        estudiante.setApellido(sc.nextLine());
                    }
                    case 3 -> {
                        System.out.print("Nuevo email: ");
                        estudiante.setEmail(sc.nextLine());
                    }
                    case 4 -> {
                        System.out.print("Nuevo sistema salud: ");
                        estudiante.setSistemaSalud(sc.nextLine());
                    }
                    case 5 -> {
                        System.out.print("Nueva fecha (YYYY-MM-DD): ");
                        String fecha = sc.nextLine();

                        if (fecha.isBlank()) {
                            System.out.println("La fecha de nacimiento es obligatoria");
                            break;
                        }

                        try {
                            estudiante.setFechaNacimiento(LocalDate.parse(fecha));
                        } catch (Exception e) {
                            System.out.println("Formato inválido. Use YYYY-MM-DD");
                        }
                    }
                    case 6 -> {
                        System.out.print("Nuevo motivo: ");
                        estudiante.setMotivo(sc.nextLine());
                    }
                    case 7 -> {
                        System.out.print("Nueva calle: ");
                        estudiante.setCalle(sc.nextLine());
                    }
                    case 8 -> {
                        System.out.print("Nuevo número puerta: ");
                        estudiante.setNroPuerta(sc.nextLine());
                    }

                    case 9 -> {
                        estudiante.getTelefono().clear();

                        System.out.println("Ingrese los teléfonos (Enter vacío para finalizar):");

                        while (true) {
                            System.out.print("Teléfono: ");
                            String telefono = sc.nextLine().trim();

                            if (telefono.isEmpty()) {
                                break;
                            }

                            estudiante.agregarTelefono(telefono);
                        }
                    }

                    case 10 -> {
                        System.out.print("Nueva foto (Enter para omitir): ");
                        String foto = sc.nextLine();
                        estudiante.setFoto(foto.isEmpty() ? null : foto);
                    }
                    case 11 -> {
                        System.out.print("¿Observaciones confidenciales? (s/n): ");
                        estudiante.setObsConfidenciales(sc.nextLine().equalsIgnoreCase("s"));
                    }
                    case 12 -> {
                        System.out.print("Nuevas observaciones/comentarios (Enter para omitir): ");
                        String obs = sc.nextLine();
                        estudiante.setObsComentarios(obs.isEmpty() ? null : obs);
                    }
                    case 13 -> {
                        System.out.print("Nueva información de estado de salud (Enter para omitir): ");
                        String info = sc.nextLine();
                        estudiante.setInfoEstadoSalud(info.isEmpty() ? null : info);
                    }
                    case 14 -> {
                        System.out.print("Nueva contraseña: ");
                        String nuevaContrasena = sc.nextLine();
                        try {
                            proxy.cambiarContrasena(estudiante.getCedula(), nuevaContrasena);
                            System.out.println("Contraseña actualizada correctamente");
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    case 15 -> {
                        try {
                            if (estudiante.isEstado()) {

                                System.out.print("¿Está seguro de desactivar este estudiante? (S/N): ");
                                String respuesta = sc.nextLine();

                                if (respuesta.equalsIgnoreCase("S")) {
                                    proxy.desactivarUsuario(estudiante.getCedula());
                                    estudiante.setEstado(false);
                                    System.out.println("Estudiante desactivado correctamente.");
                                } else {
                                    System.out.println("Operación cancelada.");
                                }

                            } else {

                                System.out.print("¿Desea volver a activar este estudiante? (S/N): ");
                                String respuesta = sc.nextLine();

                                if (respuesta.equalsIgnoreCase("S")) {
                                    proxy.activarUsuario(estudiante.getCedula());
                                    estudiante.setEstado(true);
                                    System.out.println("Estudiante activado correctamente.");
                                } else {
                                    System.out.println("Operación cancelada.");
                                }

                            }
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }

                    case 0 -> System.out.println("Guardando...");
                    default -> System.out.println("Opción inválida");
                }

            } while (opcion != 0);

            proxy.modificarUsuario(estudiante);
            System.out.println("Estudiante actualizado correctamente");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ===================== BÚSQUEDAS =====================

    public void buscarPorCedula() {
        try {
            System.out.println("--- BUSCAR ESTUDIANTE POR CÉDULA ---");
            System.out.print("Ingrese cédula: ");
            String cedula = sc.nextLine();

            Usuario usuario = proxy.buscarUsuario(cedula);
            if (usuario == null) { System.out.println("Estudiante no encontrado"); return; }

            System.out.println("=== ESTUDIANTE ===");
            System.out.println("Cédula: " + usuario.getCedula());
            System.out.println("Nombre: " + usuario.getNombre());
            System.out.println("Apellido: " + usuario.getApellido());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Rol: " + usuario.getRol().getNombre());
            System.out.println("Estado: " + (usuario.isEstado() ? "Activo" : "Inactivo"));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarPorNombre() {
        try {
            System.out.println("--- BUSCAR ESTUDIANTES POR NOMBRE ---");
            System.out.print("Ingrese nombre o apellido: ");
            String texto = sc.nextLine();

            List<Estudiante> lista = proxy.buscarEstudiantesPorNombre(texto);
            if (lista.isEmpty()) { System.out.println("No se encontraron estudiantes"); return; }
            mostrarListaEstudiantes(lista);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarPorCarrera() {
        try {
            System.out.println("--- BUSCAR ESTUDIANTES POR CARRERA ---");
            List<Carrera> carreras = proxy.listarCarrerasCatalogo();
            for (Carrera c : carreras) System.out.println(c.getId() + ". " + c.getNombre());

            System.out.print("Seleccione carrera: ");
            Integer idCarrera = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de carrera válido");
            if (idCarrera == null) return;

            List<Estudiante> lista = proxy.buscarEstudiantesPorCarrera(idCarrera);
            if (lista.isEmpty()) { System.out.println("No se encontraron estudiantes"); return; }
            mostrarListaEstudiantes(lista);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarPorGrupo() {
        try {
            System.out.println("--- BUSCAR ESTUDIANTES POR GRUPO ---");
            List<Grupo> grupos = proxy.listarGruposCatalogo();
            for (Grupo g : grupos) System.out.println(g.getId() + ". " + g.getNombre());

            System.out.print("Seleccione grupo: ");
            Integer idGrupo = EntradaHelper.leerEntero(sc, "Debe ingresar un ID de grupo válido");
            if (idGrupo == null) return;

            List<Estudiante> lista = proxy.buscarEstudiantesPorGrupo(idGrupo);
            if (lista.isEmpty()) { System.out.println("No se encontraron estudiantes"); return; }
            mostrarListaEstudiantes(lista);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarPorEstado() {
        try {
            System.out.println("--- BUSCAR ESTUDIANTES POR ESTADO ---");
            System.out.println("1. Activos");
            System.out.println("2. Inactivos");
            System.out.print("Opción: ");
            Integer op = EntradaHelper.leerEntero(sc, "Debe ingresar 1 o 2");
            if (op == null) return;

            List<Estudiante> lista = proxy.buscarEstudiantesPorEstado(op == 1);
            if (lista.isEmpty()) { System.out.println("No se encontraron estudiantes"); return; }
            mostrarListaEstudiantes(lista);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void instanciaFichaEstudiante() {
        try {
            System.out.println("--- FICHA DE ALUMNO ---");
            System.out.print("Ingrese cédula: ");
            String cedula = sc.nextLine();

            Estudiante estudiante = proxy.buscarEstudiantePorCedula(cedula);

            if (estudiante == null) {
                System.out.println("Estudiante no encontrado");
                return;
            }

            mostrarFicha(estudiante);

            int opcion;
            do {
                System.out.println("--- " + estudiante.getNombre() + " " + estudiante.getApellido() + " ---");
                System.out.println("1. Nueva instancia común");
                System.out.println("2. Nueva incidencia");
                System.out.println("0. Volver");
                System.out.print("Opción: ");

                try {
                    opcion = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    opcion = -1;
                }

                switch (opcion) {
                    case 1 -> instanciaControlador.crearInstanciaComun(estudiante);
                    case 2 -> instanciaControlador.crearIncidencia(estudiante);
                    case 0 -> System.out.println("Volviendo...");
                    default -> System.out.println("Opción inválida");
                }

            } while (opcion != 0);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void mostrarFicha(Estudiante e) {
        System.out.println("=== FICHA DE ALUMNO ===");
        System.out.println("Cédula: " + e.getCedula());
        System.out.println("Nombre: " + e.getNombre() + " " + e.getApellido());
        System.out.println("Email: " + e.getEmail());
        System.out.println("Estado: " + (e.isEstado() ? "Activo" : "Inactivo"));
        System.out.println("Grupo: " + (e.getGrupo() != null ? e.getGrupo().getNombre() : "-"));
        System.out.println("Sistema salud: " + e.getSistemaSalud());
        System.out.println("Fecha nacimiento: " + e.getFechaNacimiento());
        System.out.println("Motivo: " + e.getMotivo());
    }

    public void mostrarListaEstudiantes(List<Estudiante> lista) {
        System.out.println("=== RESULTADOS ===");
        for (Estudiante e : lista) {
            System.out.println("----------------");
            System.out.println("Cédula: " + e.getCedula());
            System.out.println("Nombre: " + e.getNombre() + " " + e.getApellido());
            System.out.println("Email: " + e.getEmail());
            System.out.println("Sistema salud: " + e.getSistemaSalud());
            System.out.println("Estado: " + (e.isEstado() ? "Activo" : "Inactivo"));
        }
    }
}
