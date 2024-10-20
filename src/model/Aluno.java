package model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import dao.AlunoDAO;

public class Aluno {
    private String ra;
    private String nome;
    private String cpf;
    private String celular;
    private LocalDate dataNascimento; // Alterado para LocalDate
    private String email;
    private String endereco;
    private String municipio;
    private String uf;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Aluno(String ra, String nome, String cpf, String celular, LocalDate dataNascimento, String email, String endereco, String municipio, String uf) {
        this.ra = ra;
        this.nome = nome;
        this.cpf = cpf;
        this.celular = celular;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.endereco = endereco;
        this.municipio = municipio;
        this.uf = uf;
    }

    public Aluno() {

    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    public LocalDate getDataNascimento(){
        return dataNascimento;
    }

    // Getters e Setters para todos os campos
    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }


    public void setDataNascimento(String dataNascimento) {
        try {
            this.dataNascimento = LocalDate.parse(dataNascimento, FORMATTER); // Converte a String para LocalDate
        } catch (DateTimeParseException e) {
            System.err.println("Data de nascimento inválida: " + e.getMessage());
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    // Método para validar se o RA é único
    public boolean isRaValido(AlunoDAO alunoDAO) {
        List<Aluno> alunos = alunoDAO.listarAlunos();
        for (Aluno aluno : alunos) {
            if (aluno.getRa().equals(this.ra)) {
                return false; // RA já existe
            }
        }
        return true; // RA é único
    }

    // Método para representar o aluno como uma String
    @Override
    public String toString() {
        return String.format("RA: %s, Nome: %s, Data de Nascimento: %s", ra, nome, dataNascimento.format(FORMATTER));
    }
}