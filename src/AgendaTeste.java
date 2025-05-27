public class AgendaTeste {

    public static void main(String[] args) {
        AgendaTelefonica agenda = new AgendaTelefonica();
        

        // Adicionando contatos
        agenda.adicionarContato(new Contato("Alice", "alice@gmail.com", "1234-5678"));
        agenda.adicionarContato(new Contato("Bob", "bob@gmail.com", "8765-4321"));
        agenda.adicionarContato(new Contato("Charlie", "Charlie@gmail.com", "5555-5555"));

        agenda.removerContato("Alice");
        agenda.buscarContato("Bob");
        agenda.buscarContato("Charlie");
        agenda.listarContatos();

    }
}