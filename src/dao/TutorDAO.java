
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.ConnectionFactory;
import model.Tutor;

public class TutorDAO {

    // CADASTRAR
    
public void cadastrar(Tutor tutor) {

    String sqlVerificar = "SELECT cpf FROM Pessoa WHERE cpf = ?";
    String sqlCadastrar = "INSERT INTO Tutor (endereço, tipoDeConsulta, Pessoa_cpf) VALUES (?, ?, ?)";

    try (Connection conn = ConnectionFactory.getConnection()) {

        // Verificar se a pessoa existe
        try (PreparedStatement verificar = conn.prepareStatement(sqlVerificar)) {

            verificar.setInt(1, tutor.getCpf());

            try (ResultSet rs = verificar.executeQuery()) {

                if (!rs.next()) {
                    System.out.println("Não foi possível cadastrar o tutor!");
                    System.out.println("Esse CPF não está cadastrado como pessoa.");
                    return;
                }
            }
        }

        // Cadastrar tutor
        try (PreparedStatement stmt = conn.prepareStatement(sqlCadastrar)) {

            stmt.setString(1, tutor.getEndereco());
            stmt.setString(2, tutor.getTipoDeConsulta());
            stmt.setInt(3, tutor.getCpf());

            stmt.executeUpdate();

            System.out.println("Tutor cadastrado com sucesso!");
        }

    } catch (SQLException e) {
        System.out.println("Erro ao cadastrar tutor: " + e.getMessage());
    }
}


    // LISTAR TODOS
    public List<Tutor> listarTodos() {

        String sql = "SELECT t.endereço, t.tipoDeConsulta, " +
                     "p.cpf, p.dataDeNascimento, p.nome, p.telefone " +
                     "FROM Tutor t " +
                     "INNER JOIN Pessoa p ON t.Pessoa_cpf = p.cpf";

        List<Tutor> listaTutor = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Tutor tutor = new Tutor();

                tutor.setEndereco(rs.getString("endereço"));
                tutor.setTipoDeConsulta(rs.getString("tipoDeConsulta"));
                tutor.setCpf(rs.getInt("cpf"));
                tutor.setDataDeNascimento(rs.getString("dataDeNascimento"));
                tutor.setNome(rs.getString("nome"));
                tutor.setTelefone(rs.getString("telefone"));

                listaTutor.add(tutor);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar tutores: " + e.getMessage());
        }

        return listaTutor;
    }

    // ATUALIZAR
    public void atualizar(Tutor tutor, String enderecoAntigo, int cpfAntigo) {

        String sql = "UPDATE Tutor SET endereço = ?, tipoDeConsulta = ?, Pessoa_cpf = ? " +
                     "WHERE endereço = ? AND Pessoa_cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tutor.getEndereco());
            stmt.setString(2, tutor.getTipoDeConsulta());
            stmt.setInt(3, tutor.getCpf());
            stmt.setString(4, enderecoAntigo);
            stmt.setInt(5, cpfAntigo);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Tutor atualizado com sucesso!");
            } else {
                System.out.println("Tutor não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar tutor: " + e.getMessage());
        }
    }

    // EXCLUIR
    public void excluir(String endereco, int cpf) {

        String sql = "DELETE FROM Tutor WHERE endereço = ? AND Pessoa_cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, endereco);
            stmt.setInt(2, cpf);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Tutor excluído com sucesso!");
            } else {
                System.out.println("Tutor não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir tutor: " + e.getMessage());
        }
    }
}
