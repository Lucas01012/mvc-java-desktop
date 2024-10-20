package dao;

import model.Curso;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO {

    public boolean adicionarCurso(Curso curso) {
        String sql = "INSERT INTO cursos (nome_curso, campus, periodo) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.out.println("Erro: Não foi possível conectar ao banco de dados.");
                return false;
            }

            stmt.setString(1, curso.getNomeCurso());
            stmt.setString(2, curso.getCampus());
            stmt.setString(3, curso.getPeriodo());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Curso buscarCursoPorRA(String ra) {
        Curso curso = null;
        String sql = "SELECT c.nome_curso, c.campus, c.periodo " +
                "FROM cursos c " +
                "JOIN alunos a ON a.id_curso = c.id_curso " +
                "WHERE a.ra = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ra);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                curso = new Curso(rs.getString("nome_curso"), rs.getString("campus"), rs.getString("periodo"));
            } else {
                System.out.println("RA não encontrado: " + ra);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar curso por RA: " + e.getMessage());
            e.printStackTrace();
        }
        return curso;
    }

    public Curso buscarCursoPorId(int idCurso) {
        Curso curso = null;
        String sql = "SELECT nome_curso, campus, periodo FROM cursos WHERE id_curso = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCurso);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String nomeCurso = rs.getString("nome_curso");
                String campus = rs.getString("campus");
                String periodo = rs.getString("periodo");
                curso = new Curso(nomeCurso, campus, periodo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return curso;
    }

    public List<Curso> listarCursos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT * FROM cursos";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nomeCurso = rs.getString("nome_curso");
                String campus = rs.getString("campus");
                String periodo = rs.getString("periodo");

                Curso curso = new Curso(nomeCurso, campus, periodo);
                cursos.add(curso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cursos;
    }
}