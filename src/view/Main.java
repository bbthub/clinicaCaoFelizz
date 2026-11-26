
package view;

import java.util.List;
import java.util.Scanner;

import dao.PessoaDAO;
import dao.PetDAO;
import dao.TutorDAO;
import dao.VeterinarioDAO;

import model.Pessoa;
import model.Pet;
import model.Tutor;
import model.Veterinario;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    static PessoaDAO pessoaDAO = new PessoaDAO();
    static TutorDAO tutorDAO = new TutorDAO();
    static PetDAO petDAO = new PetDAO();
    static VeterinarioDAO veterinarioDAO = new VeterinarioDAO();

    public static void main(String[] args) {

        int opcao;

        do {
            System.out.println("\n========== SISTEMA CLINICA CAO FELIZ ==========");
            System.out.println("1 - Tutor");
            System.out.println("2 - Pet");
            System.out.println("3 - Veterinário");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {

                case 1:
                    menuTutor();
                    break;

                case 2:
                    menuPet();
                    break;

                case 3:
                    menuVeterinario();
                    break;

                case 0:
                    System.out.println("Encerrando o sistema...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);

        scanner.close();
    }

    // verifica se já tem o cpf, ai n precisa reconfirmar os dados.

    public static void verificarPessoa(Pessoa pessoa) {

        System.out.print("CPF: ");
        int cpf = Integer.parseInt(scanner.nextLine());

        List<Pessoa> pessoas = pessoaDAO.listarTodos();

        // TESTE: mostrar os CPFs que vieram do banco
        System.out.println("\n===== CPFs ENCONTRADOS =====");

        for (Pessoa p : pessoas) {
            System.out.println(p.getCpf());
        }

        System.out.println("============================");

        boolean encontrada = false;

        for (Pessoa p : pessoas) {

            if (p.getCpf() == cpf) {
                encontrada = true;
                break;
            }
        }

        pessoa.setCpf(cpf);

        if (encontrada) {

            System.out.println("Pessoa já cadastrada!");
            System.out.println("Utilizando os dados existentes.");

        } else {

            System.out.println("Pessoa não encontrada.");
            System.out.println("Vamos cadastrar seus dados.");

            System.out.print("Data de nascimento (AAAA-MM-DD): ");
            pessoa.setDataDeNascimento(scanner.nextLine());

            System.out.print("Nome: ");
            pessoa.setNome(scanner.nextLine());

            System.out.print("Telefone: ");
            pessoa.setTelefone(scanner.nextLine());

            pessoaDAO.cadastrar(pessoa);
        }
    }

    // MENU TUTOR

    public static void menuTutor() {

        int opcao;

        do {
            System.out.println("\n========== MENU TUTOR ==========");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar todos");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {

                case 1: {
                    Tutor tutor = new Tutor();

                    verificarPessoa(tutor);

                    System.out.print("Endereço: ");
                    tutor.setEndereco(scanner.nextLine());

                    System.out.print("Tipo de consulta: ");
                    tutor.setTipoDeConsulta(scanner.nextLine());

                    tutorDAO.cadastrar(tutor);
                    break;
                }

                case 2: {
                    List<Tutor> tutores = tutorDAO.listarTodos();

                    for (Tutor t : tutores) {
                        System.out.println("\nNome: " + t.getNome());
                        System.out.println("CPF: " + t.getCpf());
                        System.out.println("Telefone: " + t.getTelefone());
                        System.out.println("Endereço: " + t.getEndereco());
                        System.out.println("Tipo de consulta: " + t.getTipoDeConsulta());
                    }
                    break;
                }

                case 3: {
                    Tutor tutor = new Tutor();

                    System.out.print("Endereço antigo: ");
                    String enderecoAntigo = scanner.nextLine();

                    System.out.print("CPF antigo: ");
                    int cpfAntigo = Integer.parseInt(scanner.nextLine());

                    System.out.print("Novo endereço: ");
                    tutor.setEndereco(scanner.nextLine());

                    System.out.print("Novo tipo de consulta: ");
                    tutor.setTipoDeConsulta(scanner.nextLine());

                    // Verifica né
                    verificarPessoa(tutor);

                    tutorDAO.atualizar(tutor, enderecoAntigo, cpfAntigo);
                    break;
                }
                case 4: {
                    System.out.print("Endereço: ");
                    String endereco = scanner.nextLine();

                    System.out.print("CPF: ");
                    int cpf = Integer.parseInt(scanner.nextLine());

                    // Buscar pets associados ao tutor
                    List<String> pets = petDAO.listarPetsDoTutor(endereco, cpf);

                    if (pets.isEmpty()) {

                        // Não existem pets: excluir tutor diretamente
                        tutorDAO.excluir(endereco, cpf);

                    } else {

                        System.out.println("\nEste tutor possui os seguintes pets:");

                        for (int i = 0; i < pets.size(); i++) {
                            System.out.println((i + 1) + " - " + pets.get(i));
                        }

                        System.out.println("\nÉ necessário excluir os pets antes de excluir o tutor.");
                        System.out.print("Deseja excluir todos esses pets? (S/N): ");

                        String resposta = scanner.nextLine();

                        if (resposta.equalsIgnoreCase("S")) {

                            // Excluir todos os pets associados
                            for (String nomePet : pets) {
                                petDAO.excluir(nomePet, endereco);
                            }

                            // Tentar excluir o tutor depois
                            tutorDAO.excluir(endereco, cpf);

                        } else {
                            System.out.println("Exclusão do tutor cancelada.");
                        }
                    }

                    break;
                }

                case 0:
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    // MENU PET

    public static void menuPet() {

        int opcao;

        do {
            System.out.println("\n========== MENU PET ==========");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar todos");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {

                case 1: {
                    Pet pet = new Pet();

                    System.out.print("Nome: ");
                    pet.setNome(scanner.nextLine());

                    System.out.print("Raça: ");
                    pet.setRaca(scanner.nextLine());

                    System.out.print("Data de nascimento (AAAA-MM-DD): ");
                    pet.setDataDeNascimento(scanner.nextLine());

                    System.out.print("Peso: ");
                    pet.setPeso(Double.parseDouble(scanner.nextLine()));

                    System.out.print("CPF do tutor: ");
                    pet.setCpfTutor(Integer.parseInt(scanner.nextLine()));

                    System.out.print("Endereço do tutor: ");
                    pet.setEnderecoTutor(scanner.nextLine());

                    petDAO.cadastrar(pet);
                    break;
                }

                case 2: {
                    List<Pet> pets = petDAO.listarTodos();

                    for (Pet p : pets) {
                        System.out.println("\nNome: " + p.getNome());
                        System.out.println("Raça: " + p.getRaca());
                        System.out.println("Nascimento: " + p.getDataDeNascimento());
                        System.out.println("Peso: " + p.getPeso());
                        System.out.println("CPF do tutor: " + p.getCpfTutor());
                        System.out.println("Endereço do tutor: " + p.getEnderecoTutor());
                    }
                    break;
                }

                case 3: {
                    System.out.print("Nome antigo: ");
                    String nomeAntigo = scanner.nextLine();

                    System.out.print("Endereço antigo do tutor: ");
                    String enderecoAntigo = scanner.nextLine();

                    System.out.println("\nDeixe em branco os campos que não deseja alterar.");

                    System.out.print("Novo nome: ");
                    String novoNome = scanner.nextLine();

                    System.out.print("Nova raça: ");
                    String novaRaca = scanner.nextLine();

                    System.out.print("Nova data de nascimento: ");
                    String novaData = scanner.nextLine();

                    System.out.print("Novo peso: ");
                    String pesoTexto = scanner.nextLine();

                    if (novoNome.isBlank())
                        novoNome = null;
                    if (novaRaca.isBlank())
                        novaRaca = null;
                    if (novaData.isBlank())
                        novaData = null;

                    Double novoPeso = pesoTexto.isBlank()
                            ? null
                            : Double.parseDouble(pesoTexto);

                    petDAO.atualizar(
                            nomeAntigo,
                            enderecoAntigo,
                            novoNome,
                            novaRaca,
                            novaData,
                            novoPeso);

                    break;
                }
                case 4: {
                    System.out.print("Nome do pet: ");
                    String nome = scanner.nextLine();

                    System.out.print("Endereço do tutor: ");
                    String endereco = scanner.nextLine();

                    petDAO.excluir(nome, endereco);
                    break;
                }

                case 0:
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    // MENU VETERINÁRIO

    public static void menuVeterinario() {

        int opcao;

        do {
            System.out.println("\n========== MENU VETERINÁRIO ==========");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar todos");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {

                case 1: {
                    Veterinario veterinario = new Veterinario();

                    verificarPessoa(veterinario);

                    System.out.print("CRMV: ");
                    veterinario.setCrmv(Integer.parseInt(scanner.nextLine()));

                    veterinarioDAO.cadastrar(veterinario);
                    break;
                }

                case 2: {
                    List<Veterinario> veterinarios = veterinarioDAO.listarTodos();

                    for (Veterinario v : veterinarios) {
                        System.out.println("\nNome: " + v.getNome());
                        System.out.println("CPF: " + v.getCpf());
                        System.out.println("CRMV: " + v.getCrmv());
                        System.out.println("Telefone: " + v.getTelefone());
                        System.out.println("Nascimento: " + v.getDataDeNascimento());
                    }
                    break;
                }

                case 3: {
                    Veterinario veterinario = new Veterinario();

                    System.out.print("CRMV antigo: ");
                    int crmvAntigo = Integer.parseInt(scanner.nextLine());

                    System.out.print("CPF do veterinário: ");
                    int cpfAntigo = Integer.parseInt(scanner.nextLine());

                    System.out.print("Novo CRMV: ");
                    veterinario.setCrmv(Integer.parseInt(scanner.nextLine()));

                    System.out.print("Novo telefone: ");
                    String novoTelefone = scanner.nextLine();

                    veterinarioDAO.atualizar(
                            veterinario, crmvAntigo, cpfAntigo, novoTelefone);

                    break;
                }
                case 4: {
                    System.out.print("CRMV: ");
                    int crmv = Integer.parseInt(scanner.nextLine());

                    System.out.print("CPF: ");
                    int cpf = Integer.parseInt(scanner.nextLine());

                    veterinarioDAO.excluir(crmv, cpf);
                    break;
                }

                case 0:
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }
}
