package dao;

import model.Aluno;
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

    public boolean adicionarAluno(Aluno aluno, String dataNascimentoStr) {
        LocalDate dataNascimento = converterParaLocalDate(dataNascimentoStr);
        if (dataNascimento == null) {
            System.out.println("Erro: Data de nascimento inválida.");
            return false;
        }

        aluno.setDataNascimento(String.valueOf(dataNascimento));

        if (!aluno.isRaValido(this)) {
            System.out.println("Erro: RA já existe.");
            return false;
        }

        String sql = "INSERT INTO alunos (ra, nome, cpf, celular, data_nascimento, email, endereco, municipio, uf) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aluno.getRa());
            stmt.setString(2, aluno.getNome());
            stmt.setString(3, aluno.getCpf());
            stmt.setString(4, aluno.getCelular());
            stmt.setDate(5, Date.valueOf(aluno.getDataNascimento()));
            stmt.setString(6, aluno.getEmail());
            stmt.setString(7, aluno.getEndereco());
            stmt.setString(8, aluno.getMunicipio());
            stmt.setString(9, aluno.getUf());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Aluno buscarAlunoPorRA(String ra) {
        String sql = "SELECT * FROM alunos WHERE ra = ?";
        Aluno aluno = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ra);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                String celular = rs.getString("celular");
                LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
                String email = rs.getString("email");
                String endereco = rs.getString("endereco");
                String municipio = rs.getString("municipio");
                String uf = rs.getString("uf");
                aluno = new Aluno(ra, nome, cpf, celular, dataNascimento, email, endereco, municipio, uf);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aluno;
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
    public int buscarIdCursoPorRA(String ra) {
        int idCurso = -1;
        String sql = "SELECT id_curso FROM alunos WHERE ra = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistema_cadastro_aluno");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ra);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                idCurso = rs.getInt("id_curso"); // Corrigido para usar o nome correto da coluna
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idCurso;
    }


    public List<Aluno> listarAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM alunos";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String ra = rs.getString("ra");
                String nome = rs.getString("nome");
                String cpf = rs.getString("cpf");
                String celular = rs.getString("celular");
                LocalDate dataNascimento = rs.getDate("data_nascimento").toLocalDate();
                String email = rs.getString("email");
                String endereco = rs.getString("endereco");
                String municipio = rs.getString("municipio");
                String uf = rs.getString("uf");
                Aluno aluno = new Aluno(ra, nome, cpf, celular, dataNascimento, email, endereco, municipio, uf);
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alunos;
    }

    public boolean alunoExiste(String ra) {
        String sql = "SELECT * FROM alunos WHERE ra = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ra);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Simples e direto
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean alterarAluno(Aluno aluno) {
        String sql = "UPDATE alunos SET nome = ?, cpf = ?, celular = ?, data_nascimento = ?, email = ?, endereco = ?, municipio = ?, uf = ? WHERE ra = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getCpf());
            stmt.setString(3, aluno.getCelular());
            stmt.setDate(4, Date.valueOf(aluno.getDataNascimento()));
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

    public boolean excluirAluno(String ra) {
        String sql = "DELETE FROM alunos WHERE ra = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ra);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private LocalDate converterParaLocalDate(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return LocalDate.parse(data, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean verificarConexao() {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getConnection();
            return connection != null; // Retorna true se a conexão foi estabelecida
        } finally {
            ConnectionFactory.closeConnection(connection); // Certifique-se de fechar a conexão
        }
    }
}
