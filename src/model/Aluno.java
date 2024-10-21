package model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import dao.AlunoDAO;

public class Aluno {
    private int id;
    private String ra;
    private String nome;
    private String cpf;
    private String celular;
    private LocalDate dataNascimento;
    private String email;
    private String endereco;
    private String municipio;
    private String uf;
    private Curso curso;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Aluno(int id, String ra, String nome, String cpf, String celular, LocalDate dataNascimento,
                 String email, String endereco, String municipio, String uf, Curso curso) {
        this.id = id;
        this.ra = ra;
        this.nome = nome;
        this.cpf = cpf;
        this.celular = celular;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.endereco = endereco;
        this.municipio = municipio;
        this.uf = uf;
        this.curso = curso;
    }

    public Aluno() {}

    public Curso getCurso() {
        return curso;
    }

    public String getNomeCurso() {
        return curso != null ? curso.getNomeCurso() : "Curso não disponível"; // Método para obter o nome do curso
    }

    public String getCampus() {
        return curso != null ? curso.getCampus() : "Campus não disponível"; // Método para obter o campus
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        try {
            this.dataNascimento = LocalDate.parse(dataNascimento, FORMATTER);
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

    @Override
    public String toString() {
        return String.format("RA: %s, Nome: %s, Data de Nascimento: %s, Curso: %s, Campus: %s",
                ra, nome, dataNascimento.format(FORMATTER), getNomeCurso(), getCampus());
    }
}
