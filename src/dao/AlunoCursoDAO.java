package dao;

import util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlunoCursoDAO {
    public AlunoCursoDTO buscarAlunoCursoPorRa(String ra) {
        Connection conn = ConnectionFactory.getConnection();
        if (conn == null) {
            return null;
        }

        String sql = "SELECT a.nome AS nome_aluno, c.nome_curso, c.campus, c.semestre " +
                "FROM alunos a " +
                "JOIN cursos c ON a.curso_id = c.id " +
                "WHERE a.ra = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ra);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nomeAluno = rs.getString("nome_aluno");
                String nomeCurso = rs.getString("nome_curso");
                String campus = rs.getString("campus");
                String semestre = rs.getString("semestre");
                return new AlunoCursoDTO(nomeAluno, nomeCurso, campus, semestre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.closeConnection(conn);
        }

        return null;
    }
}