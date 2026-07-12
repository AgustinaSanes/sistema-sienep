package controlador;

import modelos.auditoria.Auditoria;
import proxy.PermisosProxy;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.Scanner;

public class AuditoriaControlador {

    private final PermisosProxy proxy;
    private final Scanner sc = new Scanner(System.in);

    public AuditoriaControlador(PermisosProxy proxy) {
        this.proxy = proxy;
    }

    public void listarAuditorias() {
        try {
            System.out.println("--- TODAS LAS AUDITORÍAS ---");
            List<Auditoria> lista = proxy.listarAuditorias();

            if (lista.isEmpty()) {
                System.out.println("No hay registros de auditoría");
                return;
            }
            for (Auditoria a : lista) mostrarAuditoria(a);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarPorUsuario() {
        try {
            System.out.println("--- AUDITORÍA POR USUARIO ---");
            System.out.print("Cédula: ");
            String cedula = sc.nextLine();

            List<Auditoria> lista = proxy.buscarAuditoriaPorCedula(cedula);

            if (lista.isEmpty()) {
                System.out.println("No hay registros para esa cédula");
                return;
            }
            for (Auditoria a : lista) mostrarAuditoria(a);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void buscarPorFecha() {
        try {
            System.out.println("--- AUDITORÍA POR FECHA ---");
            System.out.print("Fecha (YYYY-MM-DD): ");
            String entrada = sc.nextLine();

            if (entrada.isBlank()) {
                System.out.println("Debe ingresar una fecha");
                return;
            }

            LocalDate fecha = LocalDate.parse(entrada);
            List<Auditoria> lista = proxy.buscarAuditoriaPorFecha(fecha);

            if (lista.isEmpty()) {
                System.out.println("No hay registros para esa fecha");
                return;
            }
            for (Auditoria a : lista) mostrarAuditoria(a);

        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha inválido. Use YYYY-MM-DD");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void mostrarAuditoria(Auditoria a) {
        System.out.println("----------------");
        System.out.println("ID: " + a.getId());
        System.out.println("Cédula: " + a.getCedula());
        System.out.println("Acción: " + a.getAccion());
        System.out.println("Detalle: " + a.getDetalle());
        System.out.println("Entidad: " + a.getEntidadAfectada());
        System.out.println("Fecha: " + a.getFechaHora());
    }
}