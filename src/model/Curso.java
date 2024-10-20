package model;

public class Curso {
	private String nomeCurso;
    private String campus;
    private String periodo;

    public Curso(String nomeCurso, String campus, String periodo) {
        this.nomeCurso = nomeCurso;
        this.campus = campus;
        this.periodo = periodo;
    }

    public Curso() {

    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    // Método para representar o curso como uma String
    @Override
    public String toString() {
        return String.format("Curso: %s, Campus: %s, Período: %s", nomeCurso, campus, periodo);
    }

}