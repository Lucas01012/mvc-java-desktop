package dao;

import model.Curso;
import model.MatriculaCompleta;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatriculasDAO {

    public boolean salvarNotasEFaltas(int alunoId, String disciplina, String nota, int faltas) {
        String query = "INSERT INTO matriculas (aluno_id, disciplina, nota, faltas) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, alunoId);
            pst.setString(2, disciplina);
            pst.setString(3, nota);
            pst.setInt(4, faltas);
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao salvar Notas e Faltas: " + e.getMessage());
        }
        return false;
    }

    public List<MatriculaCompleta> buscarNotasEFaltas(int alunoId) {
        List<MatriculaCompleta> matriculasList = new ArrayList<>();
        String query = "SELECT disciplina, nota, faltas FROM matriculas WHERE aluno_id = ?";

        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, alunoId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String disciplina = rs.getString("disciplina");
                String nota = rs.getString("nota");
                int faltas = rs.getInt("faltas");

                MatriculaCompleta matricula = new MatriculaCompleta(disciplina, nota, faltas);
                matriculasList.add(matricula);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Notas e Faltas: " + e.getMessage());
        }
        return matriculasList;
    }

}


