package view;

import dao.CursoDAO;
import model.Aluno;

import model.Curso;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;

import dao.AlunoDAO;

import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InterfacePrincipal extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField txtRa, txtNome, txtEmail, txtEndereco, txtMunicipio, txtNomeAluno;
    private JFormattedTextField txtDataNascimento, txtCpf, txtCelular, txtRaAluno;
    private JComboBox<String> comboUf, comboCurso, comboCampus, comboSemestre, comboDisciplinas;
    private JRadioButton rbMatutino, rbVespertino, rbNoturno;
    private JTextField txtNota, txtFaltas;
    private JButton btnCadastrar, btnAdicionarNotas, btnConsultar, btnAlterar, btnExcluir, btnSalvar, btnCancelar;

    private Dimension textFieldDimension = new Dimension(150, 20);
    private Dimension smallComboBoxDimension = new Dimension(150, 20);
    private Dimension largeComboBoxDimension = new Dimension(300, 20);

    public InterfacePrincipal() {
        setTitle("Sistema de Cadastro de Alunos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Criando o painel inicial
        JPanel panelMenu = new JPanel();
        btnCadastrar = new JButton("Cadastrar Aluno");
        btnAdicionarNotas = new JButton("Adicionar Notas");
        btnConsultar = new JButton("Consultar Aluno");
        btnAlterar = new JButton("Alterar Aluno");
        btnExcluir = new JButton("Excluir Aluno");

        btnCadastrar.addActionListener(e -> cardLayout.show(mainPanel, "Dados Pessoais"));
        btnAdicionarNotas.addActionListener(e -> cardLayout.show(mainPanel, "Notas e Faltas"));

        panelMenu.add(btnCadastrar);
        panelMenu.add(btnAdicionarNotas);
        panelMenu.add(btnConsultar);
        panelMenu.add(btnAlterar);
        panelMenu.add(btnExcluir);

        mainPanel.add(panelMenu, "Menu");
        mainPanel.add(createDadosPessoaisPanel(), "Dados Pessoais");
        mainPanel.add(createCursoPanel(), "Curso");
        mainPanel.add(createNotasFaltasPanel(), "Notas e Faltas");

        add(mainPanel);
    }

    private JPanel createDadosPessoaisPanel() {
        JPanel panelDadosPessoais = new JPanel();
        panelDadosPessoais.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializando os campos de entrada
        txtRa = createFormattedTextField("###.###.###");
        txtNome = new JTextField(15);
        txtDataNascimento = createFormattedTextField("##/##/####");
        txtCpf = createFormattedTextField("###.###.###-##");
        txtCelular = createFormattedTextField("(##) #####-####");
        txtEmail = new JTextField(15);
        txtEndereco = new JTextField(15);
        txtMunicipio = new JTextField(15);

        // Definindo tamanho preferido
        txtDataNascimento.setPreferredSize(textFieldDimension);
        txtCpf.setPreferredSize(textFieldDimension);
        txtCelular.setPreferredSize(textFieldDimension);

        String[] ufs = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"};
        comboUf = createComboBox(ufs, smallComboBoxDimension);
        comboUf.setSelectedItem("SP");

        // Adicionando os componentes ao painel
        gbc.gridx = 0; gbc.gridy = 0;
        panelDadosPessoais.add(new JLabel("RA:"), gbc);
        gbc.gridx = 1;
        panelDadosPessoais.add(txtRa, gbc);

        gbc.gridx = 2;
        panelDadosPessoais.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 3;
        panelDadosPessoais.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelDadosPessoais.add(new JLabel("Data de Nascimento:"), gbc);
        gbc.gridx = 1;
        panelDadosPessoais.add(txtDataNascimento, gbc);

        gbc.gridx = 2;
        panelDadosPessoais.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 3;
        panelDadosPessoais.add(txtCpf, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelDadosPessoais.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panelDadosPessoais.add(txtEmail, gbc);

        gbc.gridx = 2;
        panelDadosPessoais.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 3;
        panelDadosPessoais.add(txtEndereco, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelDadosPessoais.add(new JLabel("Município:"), gbc);
        gbc.gridx = 1;
        panelDadosPessoais.add(txtMunicipio, gbc);

        gbc.gridx = 2;
        panelDadosPessoais.add(new JLabel("Celular:"), gbc);
        gbc.gridx = 3;
        panelDadosPessoais.add(txtCelular, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelDadosPessoais.add(new JLabel("UF:"), gbc);
        gbc.gridx = 1;
        panelDadosPessoais.add(comboUf, gbc);

        JButton btnAvancar = new JButton("Avançar");
        btnAvancar.addActionListener(e -> {
            if (confirmarAvanco("Dados Pessoais")) {
                cardLayout.show(mainPanel, "Curso");
            }
        });
        gbc.gridx = 2; gbc.gridy = 5;
        panelDadosPessoais.add(btnAvancar, gbc);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        gbc.gridx = 3;
        panelDadosPessoais.add(btnCancelar, gbc);

        return panelDadosPessoais;
    }

    private JPanel createCursoPanel() {
        JPanel panelCurso = new JPanel();
        panelCurso.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializando os combo boxes
        String[] cursos = {"Análise e Desenvolvimento de Sistemas", "Gestão de Negócios e Inovação", "Mecatrônica Industrial"};
        comboCurso = createComboBox(cursos, largeComboBoxDimension);
        comboCurso.setSelectedItem("Análise e Desenvolvimento de Sistemas");

        String[] campus = {"Fatec Guarulhos", "Fatec São Paulo", "Fatec São Bernardo do Campo"};
        comboCampus = createComboBox(campus, largeComboBoxDimension);
        comboCampus.setSelectedItem("Fatec Guarulhos");

        // ComboBox para o semestre
        String[] semestres = {"1º Semestre", "2º Semestre", "3º Semestre", "4º Semestre", "5º Semestre", "6º Semestre"};
        comboSemestre = createComboBox(semestres, smallComboBoxDimension);

        // Inicializando os botões de período
        rbMatutino = new JRadioButton("Matutino");
        rbVespertino = new JRadioButton("Vespertino");
        rbNoturno = new JRadioButton("Noturno");
        rbVespertino.setSelected(true);

        ButtonGroup periodoGroup = new ButtonGroup();
        periodoGroup.add(rbMatutino);
        periodoGroup.add(rbVespertino);
        periodoGroup.add(rbNoturno);

        // Adicionando os componentes ao painel
        gbc.gridx = 0; gbc.gridy = 0;
        panelCurso.add(new JLabel("Curso:"), gbc);
        gbc.gridx = 1;
        panelCurso.add(comboCurso, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelCurso.add(new JLabel("Campus:"), gbc);
        gbc.gridx = 1;
        panelCurso.add(comboCampus, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelCurso.add(new JLabel("Semestre:"), gbc);
        gbc.gridx = 1;
        panelCurso.add(comboSemestre, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelCurso.add(new JLabel("Período:"), gbc);
        gbc.gridx = 1;
        panelCurso.add(rbMatutino, gbc);
        gbc.gridx = 2;
        panelCurso.add(rbVespertino, gbc);
        gbc.gridx = 3;
        panelCurso.add(rbNoturno, gbc);

        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> {
            if (confirmarAvanco("Curso")) {
                // Capturando dados do aluno
                Aluno aluno = new Aluno();
                aluno.setRa(txtRa.getText());
                aluno.setNome(txtNome.getText());
                aluno.setCpf(txtCpf.getText());
                aluno.setCelular(txtCelular.getText());

                // Captura a data de nascimento e converte para LocalDate
                try {
                    // Formato esperado: DD/MM/YYYY
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dataNascimento = LocalDate.parse(txtDataNascimento.getText(), formatter); // Converte a String para LocalDate
                    aluno.setDataNascimento(dataNascimento); // Configura o LocalDate
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Data de nascimento inválida. Formato esperado: DD/MM/YYYY");
                    return; // Sai do método se a data for inválida
                }

                aluno.setEmail(txtEmail.getText());
                aluno.setEndereco(txtEndereco.getText());
                aluno.setMunicipio(txtMunicipio.getText());
                aluno.setUf((String) comboUf.getSelectedItem());

                // Capturando dados do curso
                Curso curso = new Curso();
                curso.setNomeCurso((String) comboCurso.getSelectedItem());
                curso.setCampus((String) comboCampus.getSelectedItem());
                curso.setPeriodo((String) comboSemestre.getSelectedItem());

                // Instâncias dos DAOs
                AlunoDAO alunoDAO = new AlunoDAO();
                CursoDAO cursoDAO = new CursoDAO();

                // Salvando dados do aluno e do curso
                boolean alunoSalvo = alunoDAO.adicionarAluno(aluno); // Método ajustado para não passar data como String
                boolean cursoSalvo = cursoDAO.adicionarCurso(curso);

                // Verificando se ambos foram salvos com sucesso
                if (alunoSalvo && cursoSalvo) {
                    JOptionPane.showMessageDialog(this, "Dados salvos com sucesso!");
                    // Limpar campos após salvar
                    limparCampos(); // Método que você pode implementar para limpar os campos
                    cardLayout.show(mainPanel, "Menu"); // Retornar para o menu
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar dados. Verifique as informações.");
                }
            }
        });
        gbc.gridx = 0; gbc.gridy = 4;
        panelCurso.add(btnSalvar, gbc);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        gbc.gridx = 1;
        panelCurso.add(btnCancelar, gbc);

        return panelCurso;
    }

    private void limparCampos() {
        txtRa.setText("");
        txtNome.setText("");
        txtCpf.setText("");
        txtCelular.setText("");
        txtDataNascimento.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
        txtMunicipio.setText("");
        comboUf.setSelectedIndex(0);
        comboCurso.setSelectedIndex(0);
        comboCampus.setSelectedIndex(0);
        comboSemestre.setSelectedIndex(0);
    }

    private JPanel createNotasFaltasPanel() {
        JPanel panelNotasFaltas = new JPanel();
        panelNotasFaltas.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializando os campos de entrada
        txtRaAluno = createFormattedTextField("###.###.###");
        txtNomeAluno = new JTextField(15);
        txtNomeAluno.setEditable(false); // Bloqueia a edição
        txtNomeAluno.setBackground(Color.LIGHT_GRAY); // Muda a cor de fundo para indicar que é apenas visualização
        txtNota = new JTextField(10);
        txtFaltas = new JTextField(10);

        // Adicionando a JComboBox para disciplinas
        JComboBox<String> comboDisciplinas = new JComboBox<>();
        carregarDisciplinas(comboDisciplinas); // Método para carregar as disciplinas

        // JTextField para curso e semestre
        JTextField txtCurso = new JTextField(15);
        txtCurso.setEditable(false);
        txtCurso.setBackground(Color.LIGHT_GRAY);

        JTextField txtSemestre = new JTextField(10);
        txtSemestre.setEditable(false);
        txtSemestre.setBackground(Color.LIGHT_GRAY);

        // Adicionando um DocumentListener ao campo RA
        txtRaAluno.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                verificarAluno();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                verificarAluno();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                verificarAluno();
            }

            private void verificarAluno() {
                String ra = txtRaAluno.getText().trim();
                if (ra.length() == 11) {
                    AlunoDAO alunoDAO = new AlunoDAO();
                    Aluno aluno = alunoDAO.buscarAlunoPorRA(ra);

                    if (aluno != null) {
                        txtNomeAluno.setText(aluno.getNome());

                        // Buscando o curso diretamente pela RA do aluno
                        CursoDAO cursoDAO = new CursoDAO();
                        Curso curso = cursoDAO.buscarCursoPorRA(ra);

                        if (curso != null) {
                            txtCurso.setText(curso.getNomeCurso());
                            txtSemestre.setText(curso.getPeriodo());
                        } else {
                            txtCurso.setText("");
                            txtSemestre.setText("");
                        }
                    } else {
                        txtNomeAluno.setText("");
                        txtCurso.setText("");
                        txtSemestre.setText("");
                    }
                } else {
                    txtNomeAluno.setText("");
                    txtCurso.setText("");
                    txtSemestre.setText("");
                }
            }
        });

        // Adicionando os componentes ao painel
        gbc.gridx = 0; gbc.gridy = 0;
        panelNotasFaltas.add(new JLabel("RA do Aluno:"), gbc);
        gbc.gridx = 1;
        panelNotasFaltas.add(txtRaAluno, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelNotasFaltas.add(new JLabel("Nome do Aluno:"), gbc);
        gbc.gridx = 1;
        panelNotasFaltas.add(txtNomeAluno, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelNotasFaltas.add(new JLabel("Disciplina:"), gbc);
        gbc.gridx = 1;
        panelNotasFaltas.add(comboDisciplinas, gbc); // Adicionando a JComboBox de disciplinas

        gbc.gridx = 0; gbc.gridy = 3;
        panelNotasFaltas.add(new JLabel("Curso:"), gbc);
        gbc.gridx = 1;
        panelNotasFaltas.add(txtCurso, gbc); // Adicionando o JTextField do curso

        gbc.gridx = 0; gbc.gridy = 4;
        panelNotasFaltas.add(new JLabel("Semestre:"), gbc);
        gbc.gridx = 1;
        panelNotasFaltas.add(txtSemestre, gbc); // Adicionando o JTextField do semestre

        gbc.gridx = 0; gbc.gridy = 5;
        panelNotasFaltas.add(new JLabel("Nota:"), gbc);
        gbc.gridx = 1;
        panelNotasFaltas.add(txtNota, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panelNotasFaltas.add(new JLabel("Faltas:"), gbc);
        gbc.gridx = 1;
        panelNotasFaltas.add(txtFaltas, gbc);

        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Notas e Faltas salvas com sucesso!");
            cardLayout.show(mainPanel, "Menu"); // Retornar para o menu
        });
        gbc.gridx = 0; gbc.gridy = 7;
        panelNotasFaltas.add(btnAdicionar, gbc);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        gbc.gridx = 1;
        panelNotasFaltas.add(btnCancelar, gbc);

        return panelNotasFaltas;
    }
    private void carregarDisciplinas(JComboBox<String> comboDisciplinas) {
        String[] disciplinas = {"Matemática", "Física", "Química", "História", "Biologia"};

        for (String disciplina : disciplinas) {
            comboDisciplinas.addItem(disciplina);
        }
    }
    private JFormattedTextField createFormattedTextField(String mask) {
        MaskFormatter formatter;
        try {
            formatter = new MaskFormatter(mask);
            formatter.setPlaceholderCharacter('_');
            return new JFormattedTextField(formatter);
        } catch (ParseException e) {
            e.printStackTrace();
            return new JFormattedTextField();
        }
    }

    private JComboBox<String> createComboBox(String[] items, Dimension dimension) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setPreferredSize(dimension);
        return comboBox;
    }

    private boolean confirmarAvanco(String telaAtual) {
        int resposta = JOptionPane.showConfirmDialog(this, "Deseja continuar para a próxima tela?", telaAtual, JOptionPane.YES_NO_OPTION);
        return resposta == JOptionPane.YES_OPTION;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfacePrincipal interfacePrincipal = new InterfacePrincipal();
            interfacePrincipal.setVisible(true);
        });
    }
}