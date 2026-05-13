package servicios.usuario;
import dao.usuario.FuncionarioDAO;
import dao.usuario.FuncionarioDAOImpl;
import dao.usuario.UsuarioDAO;
import dao.usuario.UsuarioDAOImpl;
import modelos.usuario.Funcionario;
import util.ValidarCedula;
import java.util.List;

public class FuncionarioService {

    private final FuncionarioDAO funcionarioDAO;
    private final UsuarioDAO usuarioDAO;

    public FuncionarioService() {
        this.funcionarioDAO = new FuncionarioDAOImpl();
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    private void validarFuncionario(Funcionario funcionario) {
        if (funcionario == null) {
            throw new RuntimeException("El funcionario no puede ser nulo");
        }
        if (!ValidarCedula.esValida(funcionario.getCedula())) {
            throw new RuntimeException("Cédula inválida");
        }
    }

    public void agregarFuncionario(Funcionario funcionario) {
        validarFuncionario(funcionario);
        if (funcionarioDAO.obtenerPorCedula(funcionario.getCedula()) != null) {
            throw new RuntimeException("El funcionario ya existe");
        }
        funcionarioDAO.agregarFuncionario(funcionario);
    }

    public Funcionario obtenerPorCedula(String cedula) {
        if (!ValidarCedula.esValida(cedula)) {
            throw new RuntimeException("Cédula inválida");
        }
        return funcionarioDAO.obtenerPorCedula(cedula);
    }

    public List<Funcionario> obtenerTodos() {
        return funcionarioDAO.obtenerTodos();
    }
}