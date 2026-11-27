
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.ConnectionFactory;
import model.Pessoa;

public class PessoaDAO {

    // Cadastrar pessoa
    public void cadastrar(Pessoa pessoa) {

        String sql = "INSERT INTO pessoa (cpf, dataDeNascimento, nome, telefone) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pessoa.getCpf());
            stmt.setString(2, pessoa.getDataDeNascimento());
            stmt.setString(3, pessoa.getNome());
            stmt.setString(4, pessoa.getTelefone());

            stmt.executeUpdate();

            System.out.println("Pessoa cadastrada com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar pessoa: " + e.getMessage());
        }
    }

    // Listar todas as pessoas
    public List<Pessoa> listarTodos() {

        String sql = "SELECT * FROM pessoa";
        List<Pessoa> listaPessoa = new ArrayList<>();

        
        

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Pessoa pessoa = new Pessoa();

                pessoa.setCpf(rs.getString("cpf"));
                pessoa.setDataDeNascimento(rs.getString("dataDeNascimento"));
                pessoa.setNome(rs.getString("nome"));
                pessoa.setTelefone(rs.getString("telefone"));

                listaPessoa.add(pessoa);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar pessoas: " + e.getMessage());
        }

        return listaPessoa;
    }

    // Atualizar pessoa
    public void atualizar(Pessoa pessoa, String cpfAntigo) {

        String sql = "UPDATE pessoa SET cpf = ?, dataDeNascimento = ?, nome = ?, telefone = ? WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pessoa.getCpf());
            stmt.setString(2, pessoa.getDataDeNascimento());
            stmt.setString(3, pessoa.getNome());
            stmt.setString(4, pessoa.getTelefone());
            stmt.setString(5, cpfAntigo);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Pessoa atualizada com sucesso!");
            } else {
                System.out.println("Nenhuma pessoa encontrada com esse CPF.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar pessoa: " + e.getMessage());
        }
    }

    // Excluir pessoa
    public void excluir(String cpf) {

        String sql = "DELETE FROM pessoa WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Pessoa excluída com sucesso!");
            } else {
                System.out.println("Nenhuma pessoa encontrada com esse CPF.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir pessoa: " + e.getMessage());
        }
    }

}
