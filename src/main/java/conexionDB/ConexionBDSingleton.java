package conexionDB;
import java.sql.*;

public class ConexionBDSingleton {
    private static ConexionBDSingleton instancia;
    private final Connection conexion;

    // Conexion a DB
    private ConexionBDSingleton() {
        String url = "jdbc:postgresql://localhost:5432/proyecto";
        String usuario = "postgres";
        String contrasena = "Postgre";

        try {
            this.conexion = DriverManager.getConnection(url, usuario, contrasena);

            try (Statement st = conexion.createStatement()) {
                st.execute("SET search_path TO proyecto");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar a la base de datos", e);
        }
    }

    public static ConexionBDSingleton getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBDSingleton();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }
}