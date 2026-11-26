
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.ConnectionFactory;
import model.Veterinario;

public class VeterinarioDAO {

    // CADASTRAR
    public void cadastrar(Veterinario veterinario) {

        String sqlVerificar = "SELECT cpf FROM Pessoa WHERE cpf = ?";
        String sqlCadastrar = "INSERT INTO Veterinario (crmv, Pessoa_cpf) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {

            // Verificar se a pessoa existe
            try (PreparedStatement verificar = conn.prepareStatement(sqlVerificar)) {

                verificar.setInt(1, veterinario.getCpf());

                try (ResultSet rs = verificar.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println("Não foi possível cadastrar o veterinário!");
                        System.out.println("Esse CPF não está cadastrado como pessoa.");
                        return;
                    }
                }
            }

            // Cadastrar veterinário
            try (PreparedStatement stmt = conn.prepareStatement(sqlCadastrar)) {

                stmt.setInt(1, veterinario.getCrmv());
                stmt.setInt(2, veterinario.getCpf());

                stmt.executeUpdate();

                System.out.println("Veterinário cadastrado com sucesso!");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar veterinário: " + e.getMessage());
        }
    }

    // LISTAR TODOS
    public List<Veterinario> listarTodos() {

        String sql = "SELECT v.crmv, p.cpf, p.nome, " +
                "p.dataDeNascimento, p.telefone " +
                "FROM Veterinario v " +
                "INNER JOIN Pessoa p ON v.Pessoa_cpf = p.cpf";

        List<Veterinario> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Veterinario veterinario = new Veterinario();

                veterinario.setCrmv(rs.getInt("crmv"));
                veterinario.setCpf(rs.getInt("cpf"));
                veterinario.setNome(rs.getString("nome"));
                veterinario.setDataDeNascimento(rs.getString("dataDeNascimento"));
                veterinario.setTelefone(rs.getString("telefone"));

                lista.add(veterinario);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar veterinários: " + e.getMessage());
        }

        return lista;
    }

    // ATUALIZAR
    public void atualizar(Veterinario veterinario, int crmvAntigo,
            int cpfAntigo, String novoTelefone) {

        String sqlVet = "UPDATE Veterinario SET crmv = ? "
                + "WHERE crmv = ? AND Pessoa_cpf = ?";

        String sqlPessoa = "UPDATE Pessoa SET telefone = ? WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {

            try (PreparedStatement stmt = conn.prepareStatement(sqlVet)) {

                stmt.setInt(1, veterinario.getCrmv());
                stmt.setInt(2, crmvAntigo);
                stmt.setInt(3, cpfAntigo);

                int linhas = stmt.executeUpdate();

                if (linhas == 0) {
                    System.out.println("Veterinário não encontrado.");
                    return;
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlPessoa)) {

                stmt.setString(1, novoTelefone);
                stmt.setInt(2, cpfAntigo);

                stmt.executeUpdate();
            }

            System.out.println("Veterinário atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar veterinário: " + e.getMessage());
        }
    }

    // EXCLUIR
    public void excluir(int crmv, int cpf) {

        String sql = "DELETE FROM Veterinario WHERE crmv = ? AND Pessoa_cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, crmv);
            stmt.setInt(2, cpf);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Veterinário excluído com sucesso!");
            } else {
                System.out.println("Veterinário não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir veterinário: " + e.getMessage());
        }
    }
}
