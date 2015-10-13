package lilsmile;


/**
 * Created by Smile on 07.10.15.
 */
public abstract class Connector {

    protected RunCommand controller;



    Connector()
    {}


    abstract boolean connect();
    abstract boolean disconnect();
    abstract boolean getCommand();
    abstract boolean sendResult(String[] data);
    abstract boolean sendError(String error);
    abstract boolean run();
    abstract boolean unConnect();


    void setController(RunCommand controller)
    {
        this.controller=controller;
    }

}
