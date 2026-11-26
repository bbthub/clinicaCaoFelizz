
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import config.ConnectionFactory;
import model.Pet;

public class PetDAO {

    // CADASTRAR
    public void cadastrar(Pet pet) {

        String sqlVerificar = "SELECT 1 FROM Tutor WHERE endereço = ? AND Pessoa_cpf = ?";

        String sqlCadastrar = "INSERT INTO Pet (nome, raça, dataDeNascimento, peso, cpfTutor, Tutor_endereço) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {

            // Verificar se o tutor existe
            try (PreparedStatement verificar = conn.prepareStatement(sqlVerificar)) {

                verificar.setString(1, pet.getEnderecoTutor());
                verificar.setInt(2, pet.getCpfTutor());

                try (ResultSet rs = verificar.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println("Não foi possível cadastrar o pet!");
                        System.out.println("O tutor informado não está cadastrado.");
                        return;
                    }
                }
            }

            // Cadastrar pet
            try (PreparedStatement stmt = conn.prepareStatement(sqlCadastrar)) {

                stmt.setString(1, pet.getNome());
                stmt.setString(2, pet.getRaca());
                stmt.setString(3, pet.getDataDeNascimento());
                stmt.setDouble(4, pet.getPeso());
                stmt.setInt(5, pet.getCpfTutor());
                stmt.setString(6, pet.getEnderecoTutor());

                stmt.executeUpdate();

                System.out.println("Pet cadastrado com sucesso!");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar pet: " + e.getMessage());
        }
    }

    // LISTAR TODOS
    public List<Pet> listarTodos() {

        String sql = "SELECT * FROM Pet";

        List<Pet> listaPet = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Pet pet = new Pet();

                pet.setNome(rs.getString("nome"));
                pet.setRaca(rs.getString("raça"));
                pet.setDataDeNascimento(rs.getString("dataDeNascimento"));
                pet.setPeso(rs.getDouble("peso"));
                pet.setCpfTutor(rs.getInt("cpfTutor"));
                pet.setEnderecoTutor(rs.getString("Tutor_endereço"));

                listaPet.add(pet);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar pets: " + e.getMessage());
        }

        return listaPet;
    }

    // ATUALIZAR
    public void atualizar(String nomeAntigo, String enderecoTutorAntigo,
            String novoNome, String novaRaca, String novaData, Double novoPeso) {

        StringBuilder sql = new StringBuilder("UPDATE Pet SET ");
        List<Object> valores = new ArrayList<>();

        if (novoNome != null) {
            sql.append("nome = ?, ");
            valores.add(novoNome);
        }

        if (novaRaca != null) {
            sql.append("raça = ?, ");
            valores.add(novaRaca);
        }

        if (novaData != null) {
            sql.append("dataDeNascimento = ?, ");
            valores.add(novaData);
        }

        if (novoPeso != null) {
            sql.append("peso = ?, ");
            valores.add(novoPeso);
        }

        if (valores.isEmpty()) {
            System.out.println("Nenhum campo foi informado para atualização.");
            return;
        }

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE nome = ? AND Tutor_endereço = ?");

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int indice = 1;

            for (Object valor : valores) {
                stmt.setObject(indice++, valor);
            }

            stmt.setString(indice++, nomeAntigo);
            stmt.setString(indice, enderecoTutorAntigo);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Pet atualizado com sucesso!");
            } else {
                System.out.println("Pet não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar pet: " + e.getMessage());
        }
    }

    // EXCLUIR
    public void excluir(String nome, String enderecoTutor) {

        String sql = "DELETE FROM Pet WHERE nome = ? AND Tutor_endereço = ?";

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, enderecoTutor);

            int linhas = stmt.executeUpdate();

            if (linhas > 0) {
                System.out.println("Pet excluído com sucesso!");
            } else {
                System.out.println("Pet não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao excluir pet: " + e.getMessage());
        }
    }

    // Busca pets do tutor
    public List<String> listarPetsDoTutor(String endereco, int cpf) {

        String sql = "SELECT nome FROM Pet WHERE Tutor_endereço = ? AND cpfTutor = ?";

        List<String> pets = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, endereco);
            stmt.setInt(2, cpf);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    pets.add(rs.getString("nome"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar pets do tutor: " + e.getMessage());
        }

        return pets;
    }
}
