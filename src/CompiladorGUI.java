import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class CompiladorGUI extends JFrame {

    private JTextArea editorTextArea;
    private JTextArea messageArea;
    private JLabel statusBar;
    private String caminhoArquivoAtual;
    private String nomeArquivoAtual;

    public CompiladorGUI() {
        setTitle("Compilador");
        setSize(910, 600);
        setMinimumSize(new Dimension(910, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // BARRA DE FERRAMENTAS
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setPreferredSize(new Dimension(900, 70));
        add(toolBar, BorderLayout.NORTH);

        JButton novoButton = new JButton("Novo [Ctrl+N]", new ImageIcon(getClass().getResource("/icons/novo.png")));
        novoButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        novoButton.setHorizontalTextPosition(SwingConstants.CENTER);

        JButton abrirButton = new JButton("Abrir [Ctrl+O]", new ImageIcon(getClass().getResource("/icons/abrir.png")));
        abrirButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        abrirButton.setHorizontalTextPosition(SwingConstants.CENTER);

        JButton salvarButton = new JButton("Salvar [Ctrl+S]", new ImageIcon(getClass().getResource("/icons/salvar.png")));
        salvarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        salvarButton.setHorizontalTextPosition(SwingConstants.CENTER);

        JButton copiarButton = new JButton("Copiar [Ctrl+C]", new ImageIcon(getClass().getResource("/icons/copiar.png")));
        copiarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        copiarButton.setHorizontalTextPosition(SwingConstants.CENTER);

        JButton colarButton = new JButton("Colar [Ctrl+V]", new ImageIcon(getClass().getResource("/icons/colar.png")));
        colarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        colarButton.setHorizontalTextPosition(SwingConstants.CENTER);

        JButton recortarButton = new JButton("Recortar [Ctrl+X]", new ImageIcon(getClass().getResource("/icons/recortar.png")));
        recortarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        recortarButton.setHorizontalTextPosition(SwingConstants.CENTER);

        JButton compilarButton = new JButton("Compilar [F7]", new ImageIcon(getClass().getResource("/icons/compilar.png")));
        compilarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        compilarButton.setHorizontalTextPosition(SwingConstants.CENTER);

        JButton equipeButton = new JButton("Equipe [F1]", new ImageIcon(getClass().getResource("/icons/equipe.png")));
        equipeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        equipeButton.setHorizontalTextPosition(SwingConstants.CENTER);

        // ATALHOS DO TECLADO
        setupButtonWithShortcut(novoButton, "Novo", KeyStroke.getKeyStroke("control N"));
        setupButtonWithShortcut(abrirButton, "Abrir", KeyStroke.getKeyStroke("control O"));
        setupButtonWithShortcut(salvarButton, "Salvar", KeyStroke.getKeyStroke("control S"));
        setupButtonWithShortcut(copiarButton, "Copiar", KeyStroke.getKeyStroke("control C"));
        setupButtonWithShortcut(colarButton, "Colar", KeyStroke.getKeyStroke("control V"));
        setupButtonWithShortcut(recortarButton, "Recortar", KeyStroke.getKeyStroke("control X"));
        setupButtonWithShortcut(compilarButton, "Compilar", KeyStroke.getKeyStroke("F7"));
        setupButtonWithShortcut(equipeButton, "Equipe", KeyStroke.getKeyStroke("F1"));

        toolBar.add(novoButton);
        toolBar.add(abrirButton);
        toolBar.add(salvarButton);
        toolBar.add(copiarButton);
        toolBar.add(colarButton);
        toolBar.add(recortarButton);
        toolBar.add(compilarButton);
        toolBar.add(equipeButton);

        // EDITOR DE TEXTO
        editorTextArea = new JTextArea();
        editorTextArea.setLineWrap(true);
        editorTextArea.setWrapStyleWord(true);
        editorTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        editorTextArea.setBorder(new NumberedBorder());
        JScrollPane editorScrollPane = new JScrollPane(editorTextArea);
        editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        // ÁREA PARA MENSAGENS
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        // BARRA DE STATUS
        statusBar = new JLabel("Barra de status");
        statusBar.setPreferredSize(new Dimension(900, 25));
        add(statusBar, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorScrollPane, messageScrollPane);
        splitPane.setResizeWeight(0.8);
        add(splitPane, BorderLayout.CENTER);

        // AÇÕES DOS BOTÕES
        novoButton.addActionListener(e -> novoArquivo());
        abrirButton.addActionListener(e -> abrirArquivo());
        salvarButton.addActionListener(e -> salvarArquivo());
        copiarButton.addActionListener(e -> editorTextArea.copy());
        colarButton.addActionListener(e -> editorTextArea.paste());
        recortarButton.addActionListener(e -> editorTextArea.cut());
        compilarButton.addActionListener(e -> compilar());
        equipeButton.addActionListener(e -> messageArea.setText("Isabella Von Paraski Da Luz e Marcelli Rita Harbs"));

        setVisible(true);
    }

    private void setupButtonWithShortcut(JButton button, String actionKey, KeyStroke keyStroke) {
        InputMap inputMap = button.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = button.getActionMap();

        inputMap.put(keyStroke, actionKey);
        actionMap.put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(actionKey + " button clicked or shortcut used");
                button.doClick();
            }
        });
    }

    private void novoArquivo() {
        editorTextArea.setText("");
        messageArea.setText("");
        statusBar.setText("Novo arquivo criado");
        caminhoArquivoAtual = null;
        nomeArquivoAtual = null;
    }

    private void abrirArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                editorTextArea.read(reader, null);
                messageArea.setText("");
                caminhoArquivoAtual = selectedFile.getAbsolutePath();
                nomeArquivoAtual = selectedFile.getName();
                statusBar.setText("Arquivo: " + selectedFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void salvarArquivo() {
        if (caminhoArquivoAtual != null && !caminhoArquivoAtual.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivoAtual))) {
                editorTextArea.write(writer);
                messageArea.setText("");
                statusBar.setText("Arquivo salvo em: " + caminhoArquivoAtual);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            JFileChooser fileChooser = new JFileChooser();

            FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos de Texto (*.txt)", "txt");
            fileChooser.setFileFilter(filter);

            int returnValue = fileChooser.showSaveDialog(this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                String nomeArquivo = selectedFile.getName();
                if (!nomeArquivo.endsWith(".txt")) {
                    selectedFile = new File(selectedFile.getParent(), nomeArquivo + ".txt");
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                    editorTextArea.write(writer);
                    messageArea.setText("");
                    caminhoArquivoAtual = selectedFile.getAbsolutePath();
                    nomeArquivoAtual = selectedFile.getName();
                    statusBar.setText("Arquivo salvo em: " + selectedFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void compilar() {
        Lexico lexico = new Lexico();
        Sintatico sintatico = new Sintatico();
        Semantico semantico = new Semantico();
        lexico.setInput(editorTextArea.getText());

        if (!(editorTextArea.getText().isBlank() && editorTextArea.getText().isEmpty())) {
            String resultado = "";
            try {
                sintatico.parse(lexico, semantico); // tradução dirigida pela sintaxe

                if (caminhoArquivoAtual == null || caminhoArquivoAtual.isEmpty()) {
                    resultado = "o programa fonte deve estar salvo para gerar o código objeto";
                } else {
                    String nomeArquivoIL = gerarNomeArquivoIL();
                    salvarCodigoObjeto(semantico.getCodigoObjeto(), nomeArquivoIL);
                    resultado = "programa compilado com sucesso.";
                }

            }
            catch (LexicalError e) {
                //Trata erros léxicos conforme especificação da parte 2 do compilador
                String linhaErro = String.valueOf(getLinha(e.getPosition()));
                String lexema = e.getLexeme();
                String mensagemErro = e.getMessage();

                if (lexema != null) {
                    if (((lexema.startsWith("\"")) && !(mensagemErro.equals("simbolo invalido"))) || (lexema.startsWith(">@"))) {
                        resultado = String.format("Erro na linha %s: %s", linhaErro, mensagemErro);
                    } else {
                        resultado = String.format("Erro na linha %s: %s %s", linhaErro, lexema, mensagemErro);
                    }
                } else {
                    resultado = String.format("Erro na linha %s: %s", linhaErro, mensagemErro);
                }

            }
            catch (SyntaticError e) {
                //Trata erros sintáticos
                String linhaErro = String.valueOf(getLinha(e.getPosition()));
                String simboloEncontrado = sintatico.getToken();
                String mensagemErro = e.getMessage();

                if (simboloEncontrado.equals("$")) {
                    simboloEncontrado = "EOF";
                } else if (simboloEncontrado.startsWith("\"") && simboloEncontrado.endsWith("\"")) {
                    simboloEncontrado = "constante_string";
                }

                resultado = String.format("Erro na linha %s: encontrado %s  %s", linhaErro, simboloEncontrado, mensagemErro);
            }
            catch (SemanticError e) {
                //Trata erros semânticos
                int linhaErro = getLinha(e.getPosition());

                resultado = String.format("Erro na linha %d: %s", linhaErro, e.getMessage());
            }
            messageArea.setText(resultado);
        } else {
            messageArea.setText("Não é possível compilar");
        }
    }

    private String gerarNomeArquivoIL() {
        return nomeArquivoAtual.replace(".txt", ".il");
    }

    private void salvarCodigoObjeto(String codigo, String nomeArquivo) {
        try {
            // Salva na mesma pasta do arquivo-fonte ou editor
            File arquivo = new File(nomeArquivo);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
                writer.write(codigo);
            }
            System.out.println("Arquivo gerado: " + arquivo.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    private int getLinha(int posicao) {
        return editorTextArea.getDocument().getDefaultRootElement().getElementIndex(posicao) + 1;
    }

    private String getClassePorExtenso(int classe) {
        return switch (classe) {
            case 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 -> "palavra reservada";
            case 16 -> "identificador";
            case 17 -> "constante_int";
            case 18 -> "constante_float";
            case 19 -> "constante_string";
            case 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35 -> "símbolo especial";
            default -> "inválido";
        };
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(CompiladorGUI::new);
    }
}