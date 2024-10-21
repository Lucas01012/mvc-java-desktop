package view;

import dao.CursoDAO;
import dao.MatriculasDAO;
import model.Aluno;

import model.Curso;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import dao.AlunoDAO;
import model.MatriculaCompleta;
import java.util.List;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class InterfacePrincipal extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField txtRa, txtNome, txtEmail, txtEndereco, txtMunicipio, txtNomeAluno;
    private JFormattedTextField txtDataNascimento, txtCpf, txtCelular, txtRaAluno;
    private JComboBox<String> comboUf, comboCurso, comboCampus, comboSemestre, comboDisciplinas;
    private JRadioButton rbMatutino, rbVespertino, rbNoturno;
    private JTextField txtNota, txtFaltas;
    private JButton btnCadastrar, btnBoletim, btnAdicionarNotas, btnConsultar, btnAlterar, btnExcluir, btnSalvar, btnCancelar;
    private AlunoDAO alunoDAO;

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
        this.alunoDAO = new AlunoDAO();

        JPanel panelMenu = new JPanel();
        btnCadastrar = new JButton("Cadastrar Aluno");
        btnAdicionarNotas = new JButton("Adicionar Notas");
        btnConsultar = new JButton("Boletim");
        btnAlterar = new JButton("Consultar/Editar");

        btnCadastrar.addActionListener(e -> cardLayout.show(mainPanel, "Dados Pessoais"));
        btnAdicionarNotas.addActionListener(e -> cardLayout.show(mainPanel, "Notas e Faltas"));
        btnConsultar.addActionListener(e -> cardLayout.show(mainPanel, "Boletim"));
        btnAlterar.addActionListener(e -> cardLayout.show(mainPanel, "Consultar/Editar"));

        panelMenu.add(btnCadastrar);
        panelMenu.add(btnAdicionarNotas);
        panelMenu.add(btnConsultar);
        panelMenu.add(btnAlterar);

        mainPanel.add(panelMenu, "Menu");
        mainPanel.add(createDadosPessoaisPanel(), "Dados Pessoais");
        mainPanel.add(createCursoPanel(), "Curso");
        mainPanel.add(createNotasFaltasPanel(), "Notas e Faltas");
        mainPanel.add(createBoletimPanel(), "Boletim");
        mainPanel.add(createConsultaCursoPanel(), "Consultar/Editar");

        add(mainPanel);
    }

    private JPanel createBoletimPanel() {
        JPanel panelBoletim = new JPanel();
        panelBoletim.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JFormattedTextField txtRa = createFormattedTextField("###.###.###");
        txtRa.setColumns(15);

        JTextField txtNome = new JTextField(15);
        txtNome.setEditable(false);
        txtNome.setBackground(Color.LIGHT_GRAY);

        JTextField txtCurso = new JTextField(15);
        txtCurso.setEditable(false);
        txtCurso.setBackground(Color.LIGHT_GRAY);

        DefaultTableModel model = new DefaultTableModel(new String[]{"RA", "Nome", "Curso", "Disciplina", "Nota", "Faltas"}, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPaneTable = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        scrollPaneTable.setPreferredSize(new Dimension(500, 200)); // Definindo um tamanho preferido para a tabela

        JButton btnCarregarBoletim = new JButton("Carregar Boletim");
        btnCarregarBoletim.addActionListener(e -> {
            String ra = txtRa.getText().trim();
            if (ra.isEmpty() || !ra.matches("\\d{3}\\.\\d{3}\\.\\d{3}")) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um RA válido no formato XXX.XXX.XXX.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            AlunoDAO alunoDAO = new AlunoDAO();
            Aluno aluno = alunoDAO.buscarAlunoPorRA(ra);

            if (aluno != null) {
                txtNome.setText(aluno.getNome());

                String nomeCurso = alunoDAO.buscarNomeCursoPorIdCurso(aluno.getId());
                if (nomeCurso != null) {
                    txtCurso.setText(nomeCurso);
                } else {
                    txtCurso.setText("Curso não encontrado");
                }

                MatriculasDAO matriculasDAO = new MatriculasDAO();

                List<MatriculaCompleta> notasEFaltas = matriculasDAO.buscarNotasEFaltas(aluno.getId());

                model.setRowCount(0);

                String disciplina = "";
                String nota = "";
                String falta = "";

                if (!notasEFaltas.isEmpty()) {
                    MatriculaCompleta primeiroItem = notasEFaltas.get(0);
                    disciplina = primeiroItem.getDisciplina();
                    nota = primeiroItem.getNota();
                    falta = String.valueOf(primeiroItem.getFalta());
                }

                model.addRow(new Object[]{ra, aluno.getNome(), txtCurso.getText(), disciplina, nota, falta});

                for (int i = 1; i < notasEFaltas.size(); i++) {
                    MatriculaCompleta item = notasEFaltas.get(i);
                    model.addRow(new Object[]{"", "", "", item.getDisciplina(), item.getNota(), item.getFalta()});
                }
            } else {
                JOptionPane.showMessageDialog(this, "Aluno não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> {
            limparCampos(txtRa, txtNome, txtCurso, model);

            cardLayout.show(mainPanel, "Menu");
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelBoletim.add(new JLabel("RA do Aluno:"), gbc);

        gbc.gridx = 1;
        panelBoletim.add(txtRa, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelBoletim.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        panelBoletim.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panelBoletim.add(new JLabel("Curso:"), gbc);

        gbc.gridx = 1;
        panelBoletim.add(txtCurso, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelBoletim.add(scrollPaneTable, gbc); // Adiciona a tabela

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelBoletim.add(btnCarregarBoletim, gbc);

        gbc.gridy = 5;
        panelBoletim.add(btnCancelar, gbc);

        return panelBoletim;
    }

    private void limparCampos(JFormattedTextField txtRa, JTextField txtNome, JTextField txtCurso, DefaultTableModel model) {
        txtRa.setText("");
        txtNome.setText("");
        txtCurso.setText("");
        model.setRowCount(0); // Limpa a tabela
    }

    private void carregarCursos(JComboBox<Curso> combo) {
        CursoDAO cursoDAO = new CursoDAO();
        List<Curso> cursos = cursoDAO.listarCursos(); // Carrega os cursos do banco

        combo.removeAllItems();

        for (Curso curso : cursos) {
            combo.addItem(curso);
        }
    }

    private JPanel createDadosPessoaisPanel() {
        JPanel panelDadosPessoais = new JPanel();
        panelDadosPessoais.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        txtRa = createFormattedTextField("###.###.###");
        txtNome = new JTextField(15);
        txtDataNascimento = createFormattedTextField("##/##/####");
        txtCpf = createFormattedTextField("###.###.###-##");
        txtCelular = createFormattedTextField("(##) #####-####");
        txtEmail = new JTextField(15);
        txtEndereco = new JTextField(15);
        txtMunicipio = new JTextField(15);

        txtDataNascimento.setPreferredSize(textFieldDimension);
        txtCpf.setPreferredSize(textFieldDimension);
        txtCelular.setPreferredSize(textFieldDimension);

        String[] ufs = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"};
        comboUf = createComboBox(ufs, smallComboBoxDimension);
        comboUf.setSelectedItem("SP");

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
            if (txtRa.getText().trim().isEmpty() ||
                    txtNome.getText().trim().isEmpty() ||
                    txtDataNascimento.getText().trim().isEmpty() ||
                    txtCpf.getText().trim().isEmpty() ||
                    txtCelular.getText().trim().isEmpty() ||
                    txtEmail.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(panelDadosPessoais, "Por favor, preencha todos os campos obrigatórios.", "Campos Obrigatórios", JOptionPane.ERROR_MESSAGE);
            } else {
                if (confirmarAvanco("Dados Pessoais")) {
                    cardLayout.show(mainPanel, "Curso");
                }
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

        String[] semestres = {"1º Semestre", "2º Semestre", "3º Semestre", "4º Semestre", "5º Semestre", "6º Semestre"};
        comboSemestre = createComboBox(semestres, smallComboBoxDimension);

        rbMatutino = new JRadioButton("Matutino");
        rbVespertino = new JRadioButton("Vespertino");
        rbNoturno = new JRadioButton("Noturno");
        rbVespertino.setSelected(true);

        ButtonGroup periodoGroup = new ButtonGroup();
        periodoGroup.add(rbMatutino);
        periodoGroup.add(rbVespertino);
        periodoGroup.add(rbNoturno);

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
                Aluno aluno = new Aluno();
                aluno.setRa(txtRa.getText());
                aluno.setNome(txtNome.getText());
                aluno.setCpf(txtCpf.getText());
                aluno.setCelular(txtCelular.getText());

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dataNascimento = LocalDate.parse(txtDataNascimento.getText(), formatter);
                    aluno.setDataNascimento(dataNascimento);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this, "Data de nascimento inválida. Formato esperado: DD/MM/YYYY");
                    return;
                }

                aluno.setEmail(txtEmail.getText());
                aluno.setEndereco(txtEndereco.getText());
                aluno.setMunicipio(txtMunicipio.getText());
                aluno.setUf((String) comboUf.getSelectedItem());

                Curso curso = new Curso();
                curso.setNomeCurso((String) comboCurso.getSelectedItem());
                curso.setCampus((String) comboCampus.getSelectedItem());
                curso.setPeriodo((String) comboSemestre.getSelectedItem());

                AlunoDAO alunoDAO = new AlunoDAO();
                CursoDAO cursoDAO = new CursoDAO();

                boolean alunoSalvo = alunoDAO.adicionarAluno(aluno);
                boolean cursoSalvo = cursoDAO.adicionarCurso(curso);

                if (alunoSalvo && cursoSalvo) {
                    JOptionPane.showMessageDialog(this, "Dados salvos com sucesso!");
                    limparCampos();
                    cardLayout.show(mainPanel, "Menu");
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

    private JPanel createConsultaCursoPanel() {
        JPanel panelConsultaCurso = new JPanel();
        panelConsultaCurso.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JFormattedTextField txtRa = createFormattedTextField("###.###.###");
        JButton btnConsultar = new JButton("Consultar");

        String[] columnNames = {"RA", "Nome", "CPF", "Celular", "Data de Nascimento", "Email", "Município", "UF", "Nome do Curso", "Campus", "Período"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 200));

        btnConsultar.addActionListener(e -> {
            String ra = txtRa.getText().trim();
            if (ra.isEmpty() || !ra.matches("\\d{3}\\.\\d{3}\\.\\d{3}")) {
                JOptionPane.showMessageDialog(this, "Por favor, insira um RA válido no formato XXX.XXX.XXX.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            AlunoDAO alunoDAO = new AlunoDAO();
            Aluno aluno = alunoDAO.buscarAlunoPorRA(ra);

            if (aluno != null) {
                txtNome.setText(aluno.getNome());
                txtCpf.setText(aluno.getCpf());
                txtCelular.setText(aluno.getCelular());
                LocalDate dataNascimento = aluno.getDataNascimento();
                if (dataNascimento != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    txtDataNascimento.setText(dataNascimento.format(formatter));
                } else {
                    txtDataNascimento.setText("");
                }
                txtEmail.setText(aluno.getEmail());
                txtMunicipio.setText(aluno.getMunicipio());

                Curso curso = aluno.getCurso();
                if (curso != null) {
                    comboCurso.setSelectedItem(curso.getNomeCurso());
                    comboCampus.setSelectedItem(curso.getCampus());
                    comboSemestre.setSelectedItem(curso.getPeriodo());
                    comboUf.setSelectedItem(aluno.getUf());
                } else {
                    JOptionPane.showMessageDialog(this, "Curso não encontrado para o aluno.", "Erro", JOptionPane.ERROR_MESSAGE);
                }

                tableModel.setRowCount(0);
                Object[] rowData = {
                        aluno.getRa(),
                        aluno.getNome(),
                        aluno.getCpf(),
                        aluno.getCelular(),
                        dataNascimento != null ? dataNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "", // Formatação da data
                        aluno.getEmail(),
                        aluno.getMunicipio(),
                        aluno.getUf(),
                        curso != null ? curso.getNomeCurso() : "Curso não encontrado",
                        curso != null ? curso.getCampus() : "",
                        curso != null ? curso.getPeriodo() : ""
                };
                tableModel.addRow(rowData);
            } else {
                JOptionPane.showMessageDialog(this, "Aluno não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnCancelar = new JButton("Cancelar");

        btnEditar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Função Editar ainda não implementada.", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        btnExcluir.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Função Excluir ainda não implementada.", "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        btnCancelar.addActionListener(e -> {
            txtRa.setText("");
            txtNome.setText("");
            txtCpf.setText("");
            txtCelular.setText("");
            txtDataNascimento.setText("");
            txtEmail.setText("");
            txtMunicipio.setText("");
            comboCurso.setSelectedIndex(0);
            comboCampus.setSelectedIndex(0);
            comboSemestre.setSelectedIndex(0);
            comboUf.setSelectedIndex(0);
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelConsultaCurso.add(new JLabel("RA:"), gbc);

        gbc.gridx = 1;
        panelConsultaCurso.add(txtRa, gbc);

        gbc.gridx = 2;
        panelConsultaCurso.add(btnConsultar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panelConsultaCurso.add(scrollPane, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelConsultaCurso.add(btnEditar, gbc);

        gbc.gridx = 1;
        panelConsultaCurso.add(btnExcluir, gbc);

        gbc.gridx = 2;
        panelConsultaCurso.add(btnCancelar, gbc);

        return panelConsultaCurso;
    }
    private void limparCampos(JTextField txtNomeCurso, JTextField txtCursoInfo) {

    }


    private void limparCampos(JFormattedTextField txtRa, JTextField txtNome, JTextField txtCurso) {
        txtRa.setText("");
        txtNome.setText("");
        txtCurso.setText("");
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

        txtRaAluno = createFormattedTextField("###.###.###");
        txtNomeAluno = new JTextField(15);
        txtNomeAluno.setEditable(false);
        txtNomeAluno.setBackground(Color.LIGHT_GRAY);
        txtNota = new JTextField(10);
        txtFaltas = new JTextField(10);

        JTextField txtDataNascimento = new JTextField(15);
        txtDataNascimento.setEditable(false);
        txtDataNascimento.setBackground(Color.LIGHT_GRAY);

        JComboBox<String> comboDisciplinas = new JComboBox<>();
        carregarDisciplinas(comboDisciplinas);

        JTextField txtCurso = new JTextField(15);
        txtCurso.setEditable(false);
        txtCurso.setBackground(Color.LIGHT_GRAY);

        JTextField txtSemestre = new JTextField(10);
        txtSemestre.setEditable(false);
        txtSemestre.setBackground(Color.LIGHT_GRAY);

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
                    String nome = alunoDAO.buscarNomeAlunoPorRA(ra);

                    if (nome != null) {
                        txtNomeAluno.setText(nome);

                    } else {
                        txtNomeAluno.setText("");

                    }
                } else {
                    txtNomeAluno.setText("");
                }
            }
        });
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
        panelNotasFaltas.add(comboDisciplinas, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelNotasFaltas.add(new JLabel("Nota:"), gbc);
        gbc.gridx = 1;
        panelNotasFaltas.add(txtNota, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelNotasFaltas.add(new JLabel("Faltas:"), gbc);
        gbc.gridx = 1;
        panelNotasFaltas.add(txtFaltas, gbc);

        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener(e -> {
            String ra = txtRaAluno.getText().trim();
            AlunoDAO alunoDAO = new AlunoDAO();
            Aluno aluno = alunoDAO.buscarAlunoPorRA(ra);

            if (aluno != null) {
                String disciplina = (String) comboDisciplinas.getSelectedItem();
                String nota = txtNota.getText().trim();
                int faltas = Integer.parseInt(txtFaltas.getText().trim());
                MatriculasDAO matriculaDAO = new MatriculasDAO();
                boolean sucesso = matriculaDAO.salvarNotasEFaltas(aluno.getId(), disciplina, nota, faltas);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Notas e Faltas salvas com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar Notas e Faltas.");
                }
                cardLayout.show(mainPanel, "Menu");
            } else {
                JOptionPane.showMessageDialog(this, "Aluno não encontrado.");
            }
        });
        gbc.gridx = 0; gbc.gridy = 5;
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