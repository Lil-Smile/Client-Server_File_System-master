package lilsmile;


public class Main {



    public static void main(String[] args) {

        Connector connector;
        Form form;
        Controller controller;
        FileSystem fileSystem;

        connector = new SocketsConnector();
        form = new Form();
        fileSystem = new FileSystem();
        controller = new Controller(form,fileSystem,connector);

        connector.setController(controller);
        fileSystem.setController(controller);
        form.setController(controller);
        controller.startServer();
    }
}
