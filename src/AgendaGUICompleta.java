import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AgendaGUICompleta extends JFrame {

    // Componentes da Interface Gráfica
    private JTextField campoNome;
    private JTextField campoEmail;
    private JTextField campoTelefone;
    private JTextField campoBusca;
    private JTextField campoNomeAntigo;
    
    // Botões para todas as funcionalidades
    private JButton botaoAdicionar;
    private JButton botaoListar;
    private JButton botaoBuscar;
    private JButton botaoRemover;
    private JButton botaoAtualizar;
    private JButton botaoLimparCampos;
    private JButton botaoLimparTodos;
    
    private JTextArea areaMensagens;
    private JTabbedPane abas;
    
    // Referência para a nossa classe de lógica de negócios
    private AgendaTelefonica agenda;

    public AgendaGUICompleta() {
        // Inicializa a agenda
        agenda = new AgendaTelefonica();

        // Configurações da Janela Principal
        setTitle("Agenda Telefônica - Interface Completa");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Criar as abas para organizar as funcionalidades
        abas = new JTabbedPane();
        
        // Aba 1: Adicionar/Listar Contatos
        JPanel abaAdicionar = criarAbaAdicionar();
        abas.addTab("Adicionar/Listar", abaAdicionar);
        
        // Aba 2: Buscar Contatos
        JPanel abaBuscar = criarAbaBuscar();
        abas.addTab("Buscar", abaBuscar);
        
        // Aba 3: Atualizar Contatos
        JPanel abaAtualizar = criarAbaAtualizar();
        abas.addTab("Atualizar", abaAtualizar);
        
        // Aba 4: Remover Contatos
        JPanel abaRemover = criarAbaRemover();
        abas.addTab("Remover", abaRemover);
        
        add(abas, BorderLayout.CENTER);

        // Área de Mensagens (compartilhada por todas as abas)
        areaMensagens = new JTextArea(8, 50);
        areaMensagens.setEditable(false);
        areaMensagens.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(areaMensagens);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Mensagens"));
        add(scrollPane, BorderLayout.SOUTH);

        // Torna a janela visível
        setVisible(true);
    }

    private JPanel criarAbaAdicionar() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de entrada
        JPanel painelEntrada = new JPanel(new GridBagLayout());
        painelEntrada.setBorder(BorderFactory.createTitledBorder("Dados do Contato"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campo Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        painelEntrada.add(new JLabel("Nome:"), gbc);
        campoNome = new JTextField(25);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelEntrada.add(campoNome, gbc);

        // Campo Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        painelEntrada.add(new JLabel("Email:"), gbc);
        campoEmail = new JTextField(25);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelEntrada.add(campoEmail, gbc);

        // Campo Telefone
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        painelEntrada.add(new JLabel("Telefone:"), gbc);
        campoTelefone = new JTextField(25);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelEntrada.add(campoTelefone, gbc);

        painel.add(painelEntrada, BorderLayout.NORTH);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout());
        botaoAdicionar = new JButton("Adicionar Contato");
        botaoListar = new JButton("Listar Todos");
        botaoLimparCampos = new JButton("Limpar Campos");
        botaoLimparTodos = new JButton("Limpar Todos os Contatos");
        
        botaoLimparTodos.setBackground(new Color(255, 100, 100));
        botaoLimparTodos.setForeground(Color.WHITE);

        painelBotoes.add(botaoAdicionar);
        painelBotoes.add(botaoListar);
        painelBotoes.add(botaoLimparCampos);
        painelBotoes.add(botaoLimparTodos);

        painel.add(painelBotoes, BorderLayout.CENTER);

        // Configurar ações dos botões
        configurarAcoesAbaAdicionar();

        return painel;
    }

    private JPanel criarAbaBuscar() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelBusca = new JPanel(new FlowLayout());
        painelBusca.setBorder(BorderFactory.createTitledBorder("Buscar Contato"));
        
        painelBusca.add(new JLabel("Nome:"));
        campoBusca = new JTextField(20);
        painelBusca.add(campoBusca);
        
        botaoBuscar = new JButton("Buscar");
        painelBusca.add(botaoBuscar);

        painel.add(painelBusca, BorderLayout.NORTH);

        // Configurar ação do botão buscar
        botaoBuscar.addActionListener(e -> {
            String nome = campoBusca.getText().trim();
            if (nome.isEmpty()) {
                exibirMensagem("Digite um nome para buscar.");
                return;
            }
            
            Contato contato = agenda.buscarContato(nome);
            if (contato != null) {
                exibirMensagem("CONTATO ENCONTRADO:\n" + contato.toString());
            } else {
                exibirMensagem("Contato '" + nome + "' não encontrado.");
            }
        });

        return painel;
    }

    private JPanel criarAbaAtualizar() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel para nome antigo
        JPanel painelNomeAntigo = new JPanel(new FlowLayout());
        painelNomeAntigo.setBorder(BorderFactory.createTitledBorder("Contato a Atualizar"));
        painelNomeAntigo.add(new JLabel("Nome Atual:"));
        campoNomeAntigo = new JTextField(20);
        painelNomeAntigo.add(campoNomeAntigo);

        painel.add(painelNomeAntigo, BorderLayout.NORTH);

        // Painel para novos dados
        JPanel painelNovosDados = new JPanel(new GridBagLayout());
        painelNovosDados.setBorder(BorderFactory.createTitledBorder("Novos Dados"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Campos para novos dados (reutilizando os componentes)
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        painelNovosDados.add(new JLabel("Novo Nome:"), gbc);
        JTextField campoNovoNome = new JTextField(25);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelNovosDados.add(campoNovoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        painelNovosDados.add(new JLabel("Novo Email:"), gbc);
        JTextField campoNovoEmail = new JTextField(25);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelNovosDados.add(campoNovoEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        painelNovosDados.add(new JLabel("Novo Telefone:"), gbc);
        JTextField campoNovoTelefone = new JTextField(25);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelNovosDados.add(campoNovoTelefone, gbc);

        painel.add(painelNovosDados, BorderLayout.CENTER);

        // Botão atualizar
        JPanel painelBotaoAtualizar = new JPanel(new FlowLayout());
        botaoAtualizar = new JButton("Atualizar Contato");
        painelBotaoAtualizar.add(botaoAtualizar);
        painel.add(painelBotaoAtualizar, BorderLayout.SOUTH);

        // Configurar ação do botão atualizar
        botaoAtualizar.addActionListener(e -> {
            String nomeAntigo = campoNomeAntigo.getText().trim();
            String novoNome = campoNovoNome.getText().trim();
            String novoEmail = campoNovoEmail.getText().trim();
            String novoTelefone = campoNovoTelefone.getText().trim();

            if (nomeAntigo.isEmpty()) {
                exibirMensagem("Digite o nome atual do contato.");
                return;
            }
            if (novoNome.isEmpty()) {
                exibirMensagem("Digite o novo nome do contato.");
                return;
            }

            Contato contatoAtualizado = new Contato(novoNome, novoEmail, novoTelefone);
            agenda.atualizarContato(nomeAntigo, contatoAtualizado);
            
            exibirMensagem("Operação de atualização executada. Verifique as mensagens do sistema.");
            
            // Limpar campos após atualização
            campoNomeAntigo.setText("");
            campoNovoNome.setText("");
            campoNovoEmail.setText("");
            campoNovoTelefone.setText("");
        });

        return painel;
    }

    private JPanel criarAbaRemover() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelRemover = new JPanel(new FlowLayout());
        painelRemover.setBorder(BorderFactory.createTitledBorder("Remover Contato"));
        
        painelRemover.add(new JLabel("Nome do Contato:"));
        JTextField campoRemover = new JTextField(20);
        painelRemover.add(campoRemover);
        
        botaoRemover = new JButton("Remover");
        botaoRemover.setBackground(new Color(255, 150, 150));
        painelRemover.add(botaoRemover);

        painel.add(painelRemover, BorderLayout.NORTH);

        // Configurar ação do botão remover
        botaoRemover.addActionListener(e -> {
            String nome = campoRemover.getText().trim();
            if (nome.isEmpty()) {
                exibirMensagem("Digite o nome do contato a ser removido.");
                return;
            }
            
            // Confirmação antes de remover
            int opcao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja remover o contato '" + nome + "'?",
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (opcao == JOptionPane.YES_OPTION) {
                agenda.removerContato(nome);
                exibirMensagem("Operação de remoção executada para '" + nome + "'.");
                campoRemover.setText("");
            }
        });

        return painel;
    }

    private void configurarAcoesAbaAdicionar() {
        // Ação para adicionar contato
        botaoAdicionar.addActionListener(e -> {
            String nome = campoNome.getText().trim();
            String email = campoEmail.getText().trim();
            String telefone = campoTelefone.getText().trim();

            if (nome.isEmpty()) {
                exibirMensagem("O campo 'Nome' é obrigatório.");
                return;
            }

            Contato novoContato = new Contato(nome, email, telefone);
            agenda.adicionarContato(novoContato);
            
            exibirMensagem("Tentativa de adicionar contato '" + nome + "' executada. Verifique as mensagens do sistema.");
            limparCamposEntrada();
        });

        // Ação para listar contatos
        botaoListar.addActionListener(e -> {
            List<Contato> contatos = agenda.listarContatos();
            if (contatos.isEmpty()) {
                exibirMensagem("Nenhum contato encontrado na agenda.");
            } else {
                StringBuilder lista = new StringBuilder("=== LISTA DE CONTATOS ===\n");
                for (int i = 0; i < contatos.size(); i++) {
                    lista.append((i + 1)).append(". ").append(contatos.get(i).toString()).append("\n");
                }
                exibirMensagem(lista.toString());
            }
        });

        // Ação para limpar campos
        botaoLimparCampos.addActionListener(e -> {
            limparCamposEntrada();
            exibirMensagem("Campos de entrada limpos.");
        });

        // Ação para limpar todos os contatos
        botaoLimparTodos.addActionListener(e -> {
            int opcao = JOptionPane.showConfirmDialog(
                this,
                "ATENÇÃO: Esta operação irá remover TODOS os contatos da agenda.\nTem certeza que deseja continuar?",
                "Confirmar Limpeza Total",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (opcao == JOptionPane.YES_OPTION) {
                agenda.limparTodosContatos();
                exibirMensagem("Operação de limpeza total executada. Todos os contatos foram removidos.");
            }
        });
    }

    private void limparCamposEntrada() {
        campoNome.setText("");
        campoEmail.setText("");
        campoTelefone.setText("");
    }

    private void exibirMensagem(String mensagem) {
        areaMensagens.setText(mensagem);
        areaMensagens.setCaretPosition(0); // Posiciona o cursor no início
    }

    // Método main para iniciar a interface gráfica
    public static void main(String[] args) {
        // Executar na Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AgendaGUICompleta();
            }
        });
    }
}