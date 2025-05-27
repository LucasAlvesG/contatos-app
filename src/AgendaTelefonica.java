import java.util.List;
import java.util.ArrayList;

public class AgendaTelefonica {
    private List<Contato> contatos;

    public AgendaTelefonica() {
        contatos = new ArrayList<>();
    }

    public void adicionarContato(Contato contato) {
        if (contato != null && contato.getNome() != null && !contato.getNome().isEmpty()) {
            contatos.add(contato);
        } else {
            System.out.println("Contato inválido. O nome não pode ser nulo ou vazio.");
        }
    }

    public void removerContato(String nome) {
        if(nome != null || nome.isEmpty()) {
            for (Contato contato : contatos) {
                if (contato.getNome().equalsIgnoreCase(nome)) {
                    contatos.remove(contato);
                    System.out.println("Contato removido: " + nome);
                    return;
                }
            }
        }
        else {
            System.out.println("Contato não encontrado ou nome inválido.");
        }
    }

    public Contato buscarContato(String nome) {
        if (nome != null && !nome.isEmpty()) {
            for (Contato contato : contatos) {
                if (contato.getNome().equalsIgnoreCase(nome)) {
                    System.out.println("Contato encontrado:\n" + contato);
                    return contato;
                }
            }
            System.out.println("Contato não encontrado.");
            return null;
        } else {
            System.out.println("Nome inválido. O nome não pode ser nulo ou vazio.");
            return null;
        }
    }
    

    public void listarContatos() {
        if (contatos.isEmpty()) {
            System.out.println("A agenda está vazia.");
        } else {
            System.out.println("Lista de contatos:");
            for (Contato contato : contatos) {
                System.out.println(contato);
            }
        }
    }
}