package model;

public class NotasFaltas {
	 private String raAluno;
	    private String nomeCurso;
	    private int semestre;
	    private double nota;
	    private int faltas;

	    public NotasFaltas(String raAluno, String nomeCurso, int semestre, double nota, int faltas) {
	        this.raAluno = raAluno;
	        this.nomeCurso = nomeCurso;
	        this.semestre = semestre;
	        this.nota = nota;
	        this.faltas = faltas;
	    }

	    public String getRaAluno() {
	        return raAluno;
	    }

	    public void setRaAluno(String raAluno) {
	        this.raAluno = raAluno;
	    }

	    public String getNomeCurso() {
	        return nomeCurso;
	    }

	    public void setNomeCurso(String nomeCurso) {
	        this.nomeCurso = nomeCurso;
	    }

	    public int getSemestre() {
	        return semestre;
	    }

	    public void setSemestre(int semestre) {
	        this.semestre = semestre;
	    }

	    public double getNota() {
	        return nota;
	    }

	    public void setNota(double nota) {
	        this.nota = nota;
	    }

	    public int getFaltas() {
	        return faltas;
	    }

	    public void setFaltas(int faltas) {
	        this.faltas = faltas;
	    }

	    // Método para representar as notas e faltas como uma String
	    @Override
	    public String toString() {
	        return String.format("RA: %s, Curso: %s, Semestre: %d, Nota: %.2f, Faltas: %d", raAluno, nomeCurso, semestre, nota, faltas);
	    }
}
