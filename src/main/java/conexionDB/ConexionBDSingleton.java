package conexionDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBDSingleton {

    private static ConexionBDSingleton instancia;
    private Connection conexion;
    private String url = "jdbc:postgresql://localhost:5432/proyecto";;
    private String usuario = "postgres";
    private String contrasena = "Postgre";

    private ConexionBDSingleton() {
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