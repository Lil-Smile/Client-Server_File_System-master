package lilsmile;

/**
 * Created by Smile on 07.10.15.
 */
public class Controller implements RunCommand, GetResult{
    Form form;
    FileSystem fileSystem;
    Connector connector;



    Controller(Form form, FileSystem fileSystem, Connector connector)
    {
        this.fileSystem=fileSystem;
        this.form=form;
        this.connector=connector;
    }

    void startServer()
    {
        connector.connect();
        connector.getCommand();
    }

    public void stopServer()
    {
        connector.disconnect();
    }

    public void disconnectClient(){
        connector.unConnect();
    }


    @Override
    public void getResult(String[] result, String error) {
            if ((result==null)&&(error==null))
            {
                connector.connect();
                fileSystem.startNewClient();
                connector.getCommand();
            }
            if (result==null)
            {
                System.out.println("send error:"+error);
                connector.sendError(error);
            }
            else
            {
                System.out.print("send result:");
                for (int i = 0; i<result.length; i++)
                {
                    System.out.println(result[i]);
                }
                System.out.println();
                connector.sendResult(result);
            }
        connector.getCommand();
    }

    @Override
    public void run(String command, String args) {
        fileSystem.doSomething(command,args);
    }
}
