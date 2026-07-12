package servicios.usuario;
import dao.usuario.FuncionarioDAO;
import dao.usuario.FuncionarioDAOImpl;
import modelos.usuario.Funcionario;
import util.ValidarCedula;
import java.util.List;

public class FuncionarioService {

    private final FuncionarioDAO funcionarioDAO;

    public FuncionarioService() {
        this.funcionarioDAO = new FuncionarioDAOImpl();
    }

    private void validarFuncionario(Funcionario funcionario) {
        if (funcionario == null) {
            throw new RuntimeException("El funcionario no puede ser nulo");
        }

        ValidarCedula.validar(funcionario.getCedula());
    }

    public void agregarFuncionario(Funcionario funcionario) {
        validarFuncionario(funcionario);
        if (funcionarioDAO.obtenerPorCedula(funcionario.getCedula()) != null) {
            throw new RuntimeException("El funcionario ya existe");
        }
        funcionarioDAO.agregarFuncionario(funcionario);
    }

    public Funcionario obtenerPorCedula(String cedula) {

        ValidarCedula.validar(cedula);

        return funcionarioDAO.obtenerPorCedula(cedula);
    }

    public List<Funcionario> obtenerTodos() {
        return funcionarioDAO.obtenerTodos();
    }
}