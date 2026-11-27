
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
        String cpf = (scanner.nextLine());

        List<Pessoa> pessoas = pessoaDAO.listarTodos();

        // TESTE: mostrar os CPFs que vieram do banco
        System.out.println("\n===== CPFs ENCONTRADOS =====");

        for (Pessoa p : pessoas) {
            System.out.println(p.getCpf());
        }

        System.out.println("============================");

        boolean encontrada = false;

        for (Pessoa p : pessoas) {

            if (p.getCpf().equals(cpf)) {
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

                    System.out.print("CPF do tutor que deseja atualizar: ");
                    String cpfAntigo = scanner.nextLine();

                    System.out.println("\nDeixe vazio os campos que não deseja alterar.");

                    System.out.print("Novo nome: ");
                    String nome = scanner.nextLine();
                    tutor.setNome(nome.isBlank() ? null : nome);

                    System.out.print("Nova data de nascimento: ");
                    String data = scanner.nextLine();
                    tutor.setDataDeNascimento(data.isBlank() ? null : data);

                    System.out.print("Novo telefone: ");
                    String telefone = scanner.nextLine();
                    tutor.setTelefone(telefone.isBlank() ? null : telefone);

                    System.out.print("Novo endereço: ");
                    String endereco = scanner.nextLine();
                    tutor.setEndereco(endereco.isBlank() ? null : endereco);

                    System.out.print("Novo tipo de consulta: ");
                    String tipo = scanner.nextLine();
                    tutor.setTipoDeConsulta(tipo.isBlank() ? null : tipo);

                    tutorDAO.atualizar(tutor, cpfAntigo);

                    break;
                }
                case 4: {

                    System.out.print("CPF do tutor: ");
                    String cpf = scanner.nextLine();

                    List<Pet> pets = petDAO.listarPetsDoTutor(cpf);

                    if (pets.isEmpty()) {

                        tutorDAO.excluir(cpf);

                    } else {

                        System.out.println("\nEste tutor possui os seguintes pets:");

                        for (Pet pet : pets) {
                            System.out.println("ID: " + pet.getIdPet()
                                    + " - " + pet.getNome());
                        }

                        System.out.println("\nÉ necessário excluir os pets antes de excluir o tutor.");
                        System.out.print("Deseja excluir todos esses pets? (S/N): ");

                        String resposta = scanner.nextLine();

                        if (resposta.equalsIgnoreCase("S")) {

                            for (Pet pet : pets) {
                                petDAO.excluir(pet.getIdPet());
                            }

                            tutorDAO.excluir(cpf);

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
                    String nome = scanner.nextLine();

                    if (nome.isBlank()) {
                        System.out.println("Erro: o nome é obrigatório!");
                        break;
                    }
                    pet.setNome(nome);

                    System.out.print("Raça: ");
                    String raca = scanner.nextLine();

                    if (raca.isBlank()) {
                        System.out.println("Erro: a raça é obrigatória!");
                        break;
                    }
                    pet.setRaca(raca);

                    System.out.print("Data de nascimento (AAAA-MM-DD): ");
                    String data = scanner.nextLine();

                    if (data.isBlank()) {
                        System.out.println("Erro: a data de nascimento é obrigatória!");
                        break;
                    }
                    pet.setDataDeNascimento(data);

                    System.out.print("Peso: ");
                    String pesoTexto = scanner.nextLine();

                    try {
                        if (pesoTexto.isBlank()) {
                            System.out.println("Erro: o peso é obrigatório!");
                            break;
                        }

                        double peso = Double.parseDouble(pesoTexto);

                        if (peso <= 0) {
                            System.out.println("Erro: o peso deve ser maior que zero!");
                            break;
                        }

                        pet.setPeso(peso);

                    } catch (NumberFormatException e) {
                        System.out.println("Erro: digite um peso numérico válido!");
                        break;
                    }

                    System.out.print("CPF do tutor: ");
                    String cpf = scanner.nextLine();

                    if (cpf.isBlank()) {
                        System.out.println("Erro: o CPF do tutor é obrigatório!");
                        break;
                    }
                    pet.setCpfTutor(cpf);

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

                    }
                    break;
                }

                case 3: {

                    List<Pet> pets = petDAO.listarTodos();

                    if (pets.isEmpty()) {
                        System.out.println("Não existem pets cadastrados.");
                        break;
                    }

                    System.out.println("\n========== PETS CADASTRADOS ==========");

                    for (Pet pet : pets) {
                        System.out.println("ID: " + pet.getIdPet()
                                + " | Nome: " + pet.getNome());
                    }

                    // Escolher pet
                    System.out.print("\nDigite o ID do Pet que deseja atualizar: ");
                    String idTexto = scanner.nextLine();

                    int idPet;

                    try {
                        idPet = Integer.parseInt(idTexto);

                        if (idPet <= 0) {
                            System.out.println("Erro: digite um ID válido!");
                            break;
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Erro: o ID deve ser um número inteiro!");
                        break;
                    }

                    boolean existe = false;

                    for (Pet pet : pets) {
                        if (pet.getIdPet() == idPet) {
                            existe = true;
                            break;
                        }
                    }

                    if (!existe) {
                        System.out.println("Pet não encontrado!");
                        break;
                    }

                    System.out.println("\nDeixe em branco os campos que não deseja alterar.");

                    System.out.print("Novo nome: ");
                    String novoNome = scanner.nextLine();
                    if (novoNome.isBlank())
                        novoNome = null;

                    System.out.print("Nova raça: ");
                    String novaRaca = scanner.nextLine();
                    if (novaRaca.isBlank())
                        novaRaca = null;

                    System.out.print("Nova data de nascimento: ");
                    String novaData = scanner.nextLine();
                    if (novaData.isBlank())
                        novaData = null;

                    System.out.print("Novo peso: ");
                    String pesoTexto = scanner.nextLine();

                    Double novoPeso = null;

                    if (!pesoTexto.isBlank()) {
                        try {
                            novoPeso = Double.parseDouble(pesoTexto);

                            if (novoPeso <= 0) {
                                System.out.println("Erro: o peso deve ser maior que zero!");
                                break;
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("Erro: digite um peso numérico válido!");
                            break;
                        }
                    }

                    petDAO.atualizar(
                            idPet,
                            novoNome,
                            novaRaca,
                            novaData,
                            novoPeso);

                    break;
                }
                case 4: {
                    List<Pet> pets = petDAO.listarTodos();

                    if (pets.isEmpty()) {
                        System.out.println("Não existem pets cadastrados.");
                        break;
                    }

                    System.out.println("\n========== PETS CADASTRADOS ==========");

                    for (Pet pet : pets) {
                        System.out.println("ID: " + pet.getIdPet()
                                + " | Nome: " + pet.getNome());
                    }

                    System.out.print("\nDigite o ID do pet que deseja excluir: ");
                    String idTexto = scanner.nextLine();

                    int idPet;

                    try {
                        idPet = Integer.parseInt(idTexto);

                        if (idPet <= 0) {
                            System.out.println("Erro: digite um ID válido!");
                            break;
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Erro: o ID deve ser um número inteiro!");
                        break;
                    }

                    boolean existe = false;

                    for (Pet pet : pets) {
                        if (pet.getIdPet() == idPet) {
                            existe = true;
                            break;
                        }
                    }

                    if (!existe) {
                        System.out.println("Pet não encontrado!");
                        break;
                    }

                    System.out.print("Tem certeza que deseja excluir este pet? (S/N): ");
                    String resposta = scanner.nextLine();

                    if (resposta.equalsIgnoreCase("S")) {
                        petDAO.excluir(idPet);
                    } else {
                        System.out.println("Exclusão cancelada.");
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

                    System.out.print("CPF do veterinário: ");
                    String cpfAntigo = scanner.nextLine();

                    System.out.println("\nDeixe vazio os campos que não deseja alterar.");

                    System.out.print("Novo CRMV: ");
                    String crmvTexto = scanner.nextLine();

                    if (!crmvTexto.isBlank()) {
                        veterinario.setCrmv(Integer.parseInt(crmvTexto));
                    }

                    System.out.print("Novo nome: ");
                    String nome = scanner.nextLine();
                    veterinario.setNome(nome.isBlank() ? null : nome);

                    System.out.print("Nova data de nascimento: ");
                    String data = scanner.nextLine();
                    veterinario.setDataDeNascimento(data.isBlank() ? null : data);

                    System.out.print("Novo telefone: ");
                    String telefone = scanner.nextLine();
                    veterinario.setTelefone(telefone.isBlank() ? null : telefone);

                    veterinarioDAO.atualizar(veterinario, cpfAntigo);

                    break;
                }
                case 4: {

                    System.out.print("CPF: ");
                    String cpf = (scanner.nextLine());

                    veterinarioDAO.excluir(cpf);
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
