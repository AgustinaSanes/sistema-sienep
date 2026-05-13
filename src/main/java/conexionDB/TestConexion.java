package conexionDB;

public class TestConexion {
    public static void main(String[] args) {
        ConexionBDSingleton conexion = ConexionBDSingleton.getInstancia();

        if (conexion.getConexion() != null) {
            System.out.println("Conexión exitosa a PostgreSQL");
        } else {
            System.out.println("No se pudo conectar");
        }
    }
}