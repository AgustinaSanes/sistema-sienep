package dao.usuario;
import modelos.usuario.Funcionario;
import java.util.List;

public interface FuncionarioDAO {
    void agregarFuncionario(Funcionario funcionario);
    Funcionario obtenerPorCedula(String cedula);
    List<Funcionario> obtenerTodos();
}