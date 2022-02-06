import models.Aluno;
import models.Turma;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

public class Principal {

    private static List<Aluno> listaAlunos = new ArrayList<>();

    public static void main(String[] args) {

        Map<String, Turma> dados = new HashMap<>();

        String inputFileJava = "src/main/resources/turmaJava.txt";
        String inputFileBD = "src/main/resources/turmaBD.txt";

        dados.put(inputFileJava, Turma.JAVA);
        dados.put(inputFileBD, Turma.BANCO_DE_DADOS);

        String OutputFileSimple = "src/main/resources/alunos.txt";
        String OutputFileComplete = "src/main/resources/alunos_completa.txt";
        String OutputFileFormat = "src/main/resources/alunos_nao_formatado.txt";


        for (Map.Entry<String, Turma> valores : dados.entrySet()) {
            List<String> leituras = lerArquivoEntrada(valores.getKey());
            adicionarListaAlunos(leituras, valores.getValue());
        }

        if (CollectionUtils.isNotEmpty(listaAlunos)) {
            relatorioAlunosSimplificado(OutputFileSimple);
            relatorioAlunosCompleto(OutputFileComplete);
            relatorioAlunosNaoFormatado(OutputFileFormat);
            imprimirTelaListaAlunos();
        } else {
            System.out.println("Nada para imprimir");

        }
    }

    private static void adicionarListaAlunos(List<String> lista, Turma turma) {
        if (CollectionUtils.isNotEmpty(lista)) {
            lista.stream().forEach(aluno -> {
                        if (StringUtils.isNotEmpty(aluno)) {
                            if (!estaListaAluno(aluno)) {
                                Aluno novoAluno = new Aluno(aluno);
                                novoAluno.inserirTurma(turma);
                                listaAlunos.add(novoAluno);
                            } else adicionarAlunoExistente(aluno, turma);
                        }
                    }
            );
        }
    }

    public static void adicionarAlunoExistente(String aluno, Turma turma) {
        listaAlunos
                .stream()
                .filter(alunoNovo -> alunoNovo.getNome().equals(aluno))
                .forEach(alunoNovo -> alunoNovo.inserirTurma(turma));
    }

    public static boolean estaListaAluno(String aluno) {
        return listaAlunos.stream().anyMatch(alunoNovo -> alunoNovo.getNome().equals(aluno));
    }

    public static void imprimirTelaListaAlunos() {
        listaAlunos.forEach(System.out::println);
        System.out.println("----");
    }

    public static void relatorioAlunosSimplificado(String url) {
        Set<Aluno> listaOrdenada = new TreeSet<>(listaAlunos);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(url))) {
            listaOrdenada.forEach(aluno -> {
                try {
                    bufferedWriter.append(aluno.getNome() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void relatorioAlunosNaoFormatado(String url) {
        Set<Aluno> listaOrdenada = new TreeSet<>(listaAlunos);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(url))) {
            listaOrdenada.forEach(aluno -> {
                try {
                    bufferedWriter.append(aluno + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void relatorioAlunosCompleto(String url) {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(url))) {
            Set<Aluno> listaOrdenada = new TreeSet<>(listaAlunos);
            List<Turma> turmas = Arrays.asList(Turma.values());
            int quantidadeAlunos = 0;

            bufferedWriter.append("Quantidade total de alunos: " + listaOrdenada.size() + "\n\n");

            for (Turma turma : turmas) {
                quantidadeAlunos = (int) listaOrdenada
                        .stream()
                        .filter(aluno -> aluno.getTurmas().contains(turma))
                        .count();

                bufferedWriter.append("Quantidade total de alunos " + turma + " : " + quantidadeAlunos + "\n");

                listaOrdenada
                        .stream()
                        .filter(aluno -> aluno.getTurmas().contains(turma))
                        .forEach(aluno -> {
                            try {
                                bufferedWriter.append(aluno.getNome() + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                bufferedWriter.append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> lerArquivoEntrada(String inputPath) {
        List<String> listaAlunos = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inputPath));
            String linha = bufferedReader.readLine();
            while (linha != null) {
                listaAlunos.add(linha);
                linha = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaAlunos;
    }

}
