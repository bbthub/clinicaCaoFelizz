package model;

public class Pet {
    private int idPet;
    private String nome;
    private String raca;
    private String dataDeNascimento;
    private double peso;
    private String CpfTutor;
    private String enderecoTutor;

    public String getNome() {
        return nome;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public double getPeso() {
        return peso;
    }

    public String getRaca() {
        return raca;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getCpfTutor() {
        return CpfTutor;
    }

    public void setCpfTutor(String cpfTutor) {
        CpfTutor = cpfTutor;
    }

    public String getEnderecoTutor() {
        return enderecoTutor;
    }

    public void setEnderecoTutor(String enderecoTutor) {
        this.enderecoTutor = enderecoTutor;
    }

    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

}
