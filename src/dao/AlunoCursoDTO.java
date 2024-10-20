package dao;

public class AlunoCursoDTO {
    private String nomeAluno;
    private String nomeCurso;
    private String campus;
    private String semestre;
    public AlunoCursoDTO(String nomeAluno, String nomeCurso, String campus, String semestre) {
        this.nomeAluno = nomeAluno;
        this.nomeCurso = nomeCurso;
        this.campus = campus;
        this.semestre = semestre;
    }

    // Getters e Setters
    public String getNomeAluno() {
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
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

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }
}
