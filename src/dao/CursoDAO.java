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


    public Curso buscarCursoPorId(int id) {
        String sql = "SELECT * FROM cursos WHERE id = ?";
        Curso curso = null;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nomeCurso = rs.getString("nome_curso");
                String campus = rs.getString("campus");
                String periodo = rs.getString("periodo");
                curso = new Curso(id, nomeCurso, campus, periodo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return curso;
    }

    public boolean atualizarCurso(Curso curso) {
        String sql = "UPDATE cursos SET nome_curso = ?, campus = ?, periodo = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, curso.getNomeCurso());
            stmt.setString(2, curso.getCampus());
            stmt.setString(3, curso.getPeriodo());
            stmt.setInt(4, curso.getId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Curso> listarCursos() {
        String sql = "SELECT * FROM cursos";
        List<Curso> cursos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nomeCurso = rs.getString("nome_curso");
                String campus = rs.getString("campus");
                String periodo = rs.getString("periodo");

                Curso curso = new Curso(id,nomeCurso, campus, periodo);
                cursos.add(curso);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cursos;
    }
}

