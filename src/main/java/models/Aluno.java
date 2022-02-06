package models;

import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Getter
@ToString
@EqualsAndHashCode
public class Aluno implements Comparable<Aluno>{

    private String nome;
    private Set<Turma> turmas;

    public Aluno(String nome) {
        this.nome = nome;
        this.turmas = new HashSet<>();
    }

    public void inserirTurma(Turma turma) {
        this.turmas.add(turma);
    }

    @Override
    public int compareTo(Aluno o) {
        return this.getNome().compareToIgnoreCase(o.getNome());
    }
}
