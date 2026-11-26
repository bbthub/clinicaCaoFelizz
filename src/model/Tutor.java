package model;

public class Tutor extends Pessoa {

    private String endereco;
    private String tipoDeConsulta;

    public String getEndereco() {
        return endereco;
    }

    public String getTipoDeConsulta() {
        return tipoDeConsulta;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTipoDeConsulta(String tipoDeConsulta) {
        this.tipoDeConsulta = tipoDeConsulta;
    }
}