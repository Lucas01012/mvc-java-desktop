package dao;

import model.Aluno;
import model.Curso;
import util.ConnectionFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.sql.Date; // Importação para usar java.sql.Date
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    public String buscarNomeAlunoPorRA(String ra) {
        String sql = "SELECT nome FROM alunos WHERE ra = ?";
        String nome = null; // Inicializa o nome como nulo
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ra);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nome = rs.getString("nome"); // Apenas obtém o nome
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nome;
    }
    public boolean atualizarAluno(Aluno aluno) {
        String sql = "UPDATE alunos SET nome = ?, cpf = ?, celular = ?, data_nascimento = ?, email = ?, endereco = ?, municipio = ?, uf = ? WHERE ra = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getCpf());
            stmt.setString(3, aluno.getCelular());
            stmt.setString(4, aluno.getDataNascimento().toString()); // Certifique-se de estar no formato correto
            stmt.setString(5, aluno.getEmail());
            stmt.setString(6, aluno.getEndereco());
            stmt.setString(7, aluno.getMunicipio());
            stmt.setString(8, aluno.getUf());
            stmt.setString(9, aluno.getRa());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Aluno> getAllAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM alunos";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String ra = rs.getString("ra");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                String celular = rs.getString("celular");
                LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
                String email = rs.getString("email");
                String endereco = rs.getString("endereco");
                String municipio = rs.getString("municipio");
                String uf = rs.getString("uf");

                Aluno aluno = new Aluno(id, ra, nome, cpf, celular, dataNascimento, email, endereco, municipio, uf, null);
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alunos;
    }

    public Aluno buscarAlunoPorRA(String ra) {
        String sql = "SELECT * FROM alunos WHERE ra = ?";
        Aluno aluno = null; // Inicializa como nulo
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ra);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                String celular = rs.getString("celular");
                LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
                String email = rs.getString("email");
                String endereco = rs.getString("endereco");
                String municipio = rs.getString("municipio");
                String uf = rs.getString("uf");

                aluno = new Aluno(id, ra, nome, cpf, celular, dataNascimento, email, endereco, municipio, uf, null); // Se não precisar do curso
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aluno;
    }


    public String buscarNomeCursoPorIdCurso(int idCurso) {
        String sql = "SELECT nome_curso FROM cursos WHERE id = ?";
        String nomeCurso = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCurso);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nomeCurso = rs.getString("nome_curso");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nomeCurso;
    }


    public boolean adicionarAluno(Aluno aluno) {
        Connection conn = ConnectionFactory.getConnection();
        if (conn == null) {
            return false;
        }

        String sql = "INSERT INTO alunos (ra, nome, cpf, celular, data_nascimento, email, endereco, municipio, uf) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aluno.getRa());
            stmt.setString(2, aluno.getNome());
            stmt.setString(3, aluno.getCpf());
            stmt.setString(4, aluno.getCelular());

            // Converter LocalDate para String no formato adequado para o banco de dados
            stmt.setString(5, aluno.getDataNascimento().toString()); // Agora usa toString() para LocalDate

            stmt.setString(6, aluno.getEmail());
            stmt.setString(7, aluno.getEndereco());
            stmt.setString(8, aluno.getMunicipio());
            stmt.setString(9, aluno.getUf());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

}
