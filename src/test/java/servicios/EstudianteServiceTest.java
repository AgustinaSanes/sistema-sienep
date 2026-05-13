package servicios;

import modelos.usuario.Estudiante;
import modelos.institucion.Grupo;
import modelos.institucion.Carrera;
import org.junit.jupiter.api.Test;
import servicios.usuario.EstudianteService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EstudianteServiceTest {

    private final EstudianteService service = new EstudianteService();

    // Agregar estudiante ok
    @Test
    void testAgregarEstudiante() {

        Estudiante e = crearValido("11111111");

        assertDoesNotThrow(() ->
                service.agregarEstudiante(e)
        );
    }

    // Agregar estudiante duplicado
    @Test
    void testDuplicadoEstudiante() {

        Estudiante e1 = crearValido("22222222");
        Estudiante e2 = crearValido("22222222");

        service.agregarEstudiante(e1);

        Exception ex = assertThrows(
                RuntimeException.class,
                () -> service.agregarEstudiante(e2)
        );

        assertEquals("El estudiante ya existe", ex.getMessage());
    }

    // Cédula inválida
    @Test
    void testCedulaInvalida() {

        Estudiante e = crearValido("123");

        Exception ex = assertThrows(
                RuntimeException.class,
                () -> service.agregarEstudiante(e)
        );

        assertEquals("Cédula inválida", ex.getMessage());
    }

    // Menor de edad
    @Test
    void testMenorEdad() {

        Estudiante e = crearValido("33333333");
        e.setFechaNacimiento(LocalDate.now());

        Exception ex = assertThrows(
                RuntimeException.class,
                () -> service.agregarEstudiante(e)
        );

        assertEquals("Debe ser mayor de edad", ex.getMessage());
    }

    // Búsqueda vacía
    @Test
    void testBusquedaVacia() {

        Exception ex = assertThrows(
                RuntimeException.class,
                () -> service.buscarPorNombreApellido("")
        );

        assertEquals("Texto vacío", ex.getMessage());
    }

    // Estudiante sin grupo
    @Test
    void testEstudianteSinGrupo() {

        Estudiante e = crearValidoSinGrupo("99999999");

        assertDoesNotThrow(() ->
                service.agregarEstudiante(e)
        );
    }

    //Crer
    private Estudiante crearValido(String cedula) {

        Carrera carrera = new Carrera();
        carrera.setId(1);
        carrera.setNombre("Ingeniería");

        Grupo grupo = new Grupo(
                1,
                "A1",
                true,
                carrera
        );

        Estudiante e = new Estudiante();
        e.setCedula(cedula);
        e.setNombre("Juan");
        e.setApellido("Perez");
        e.setGrupo(grupo);
        e.setSistemaSalud("ASSE");
        e.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        e.setMotivo("Test");

        return e;
    }

    //Crear estudiante sin grupo
    private Estudiante crearValidoSinGrupo(String cedula) {

        Estudiante e = new Estudiante();
        e.setCedula(cedula);
        e.setNombre("Juan");
        e.setApellido("Perez");

        e.setGrupo(null);

        e.setSistemaSalud("ASSE");
        e.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        e.setMotivo("Test");

        return e;
    }
}