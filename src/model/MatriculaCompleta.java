package model;

import java.time.LocalDate;

public class MatriculaCompleta {
    private String disciplina;
    private String nota;
    private int faltas;

    public MatriculaCompleta(String disciplina, String nota, int faltas) {
        this.disciplina = disciplina;
        this.nota = nota;
        this.faltas = faltas;
    }
    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public int getFalta() {
        return faltas;
    }

    public void setFalta(int faltas) {
        this.faltas = faltas;
    }
}
