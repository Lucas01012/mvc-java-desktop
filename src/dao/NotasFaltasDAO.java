package dao;
import model.NotasFaltas;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotasFaltasDAO {
	  public boolean adicionarNotasFaltas(NotasFaltas notasFaltas) {
	        Connection conn = ConnectionFactory.getConnection();
	        if (conn == null) {
	            return false;
	        }

	        String sql = "INSERT INTO notas_faltas (ra_aluno, nome_curso, semestre, nota, faltas) VALUES (?, ?, ?, ?, ?)";
	        
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, notasFaltas.getRaAluno());
	            stmt.setString(2, notasFaltas.getNomeCurso());
	            stmt.setInt(3, notasFaltas.getSemestre());
	            stmt.setDouble(4, notasFaltas.getNota());
	            stmt.setInt(5, notasFaltas.getFaltas());

	            stmt.executeUpdate();
	            return true;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        } finally {
	            ConnectionFactory.closeConnection(conn);
	        }
	    }

	    // Método para listar notas e faltas
	    public List<NotasFaltas> listarNotasFaltas() {
	        Connection conn = ConnectionFactory.getConnection();
	        List<NotasFaltas> notasFaltasList = new ArrayList<>();
	        if (conn == null) {
	            return notasFaltasList;
	        }

	        String sql = "SELECT * FROM notas_faltas";

	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            ResultSet rs = stmt.executeQuery();
	            while (rs.next()) {
	                String raAluno = rs.getString("ra_aluno");
	                String nomeCurso = rs.getString("nome_curso");
	                int semestre = rs.getInt("semestre");
	                double nota = rs.getDouble("nota");
	                int faltas = rs.getInt("faltas");

	                NotasFaltas notasFaltas = new NotasFaltas(raAluno, nomeCurso, semestre, nota, faltas);
	                notasFaltasList.add(notasFaltas);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            ConnectionFactory.closeConnection(conn);
	        }

	        return notasFaltasList;
	    }
}
