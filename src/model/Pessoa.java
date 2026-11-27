package model;

public class Pessoa {
private String cpf;
private String dataDeNascimento;
private String nome;
private String telefone;

public String getCpf() {
    return cpf;
}
public String getDataDeNascimento() {
    return dataDeNascimento;
}
public String getNome() {
    return nome;
}
public String getTelefone() {
    return telefone;
}
public void setCpf(String cpf) {
    this.cpf = cpf;
}
public void setDataDeNascimento(String dataDeNascimento) {
    this.dataDeNascimento = dataDeNascimento;
}
public void setNome(String nome) {
    this.nome = nome;
}
public void setTelefone(String telefone) {
    this.telefone = telefone;
}
}
