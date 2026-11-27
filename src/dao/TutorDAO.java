
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

                verificar.setString(1, tutor.getCpf());

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
                stmt.setString(3, tutor.getCpf());

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
                tutor.setCpf(rs.getString("cpf"));
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
    public void atualizar(Tutor tutor, String cpfAntigo) {

        String sqlTutor = "UPDATE Tutor SET endereço = COALESCE(?, endereço), " +
                "tipoDeConsulta = COALESCE(?, tipoDeConsulta) " +
                "WHERE Pessoa_cpf = ?";

        String sqlPessoa = "UPDATE Pessoa SET nome = COALESCE(?, nome), " +
                "dataDeNascimento = COALESCE(?, dataDeNascimento), " +
                "telefone = COALESCE(?, telefone) " +
                "WHERE cpf = ?";
        try (Connection conn = ConnectionFactory.getConnection()) {

            conn.setAutoCommit(false);

            try (PreparedStatement stmtTutor = conn.prepareStatement(sqlTutor);
                    PreparedStatement stmtPessoa = conn.prepareStatement(sqlPessoa)) {

                // Atualizar dados do Tutor
                stmtTutor.setString(1, tutor.getEndereco());
                stmtTutor.setString(2, tutor.getTipoDeConsulta());
                stmtTutor.setString(3, cpfAntigo);

                // Atualizar dados da Pessoa
                stmtPessoa.setString(1, tutor.getNome());
                stmtPessoa.setString(2, tutor.getDataDeNascimento());
                stmtPessoa.setString(3, tutor.getTelefone());
                stmtPessoa.setString(4, cpfAntigo);

                int linhasTutor = stmtTutor.executeUpdate();
                int linhasPessoa = stmtPessoa.executeUpdate();

                if (linhasTutor > 0 && linhasPessoa > 0) {
                    conn.commit();
                    System.out.println("Tutor atualizado com sucesso!");
                } else {
                    conn.rollback();
                    System.out.println("Tutor não encontrado.");
                }

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar tutor: " + e.getMessage());
        }
    }

    // EXCLUIR
    public void excluir(String cpf) {

        String sql = "DELETE FROM Tutor WHERE Pessoa_cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

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
