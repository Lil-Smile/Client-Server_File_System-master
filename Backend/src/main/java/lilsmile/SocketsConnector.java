package lilsmile;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Smile on 08.10.15.
 */
public class SocketsConnector extends  Connector implements Constants{

    private ServerSocket serverSocket;
    private Socket client=null;
    private BufferedReader in=null;
    private PrintWriter out=null;
    private int PORT = 3200;

    SocketsConnector()
    {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println("Couldn't listen to port"+PORT);
            System.exit(1);
        }
    }

    @Override
    boolean connect() {
        try {
            client = null;
            client = serverSocket.accept();
            if (client!=null)
            {
                System.out.println("client accepted");
                in = new BufferedReader(new InputStreamReader(client.getInputStream())); //todo:add execute runCommand
                out = new PrintWriter(client.getOutputStream(),true);
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    } //on server

    @Override
    boolean disconnect() {
        if (in!=null)
        {
            try {
                in.close();
            } catch (IOException e) {
                return false;
            }
        }
        if (out!=null)
        {
            out.close();
        }
        if (client!=null)
        {
            try {
                client.close();
            } catch (IOException e) {
                return false;
            }
        }
        if (serverSocket!=null)
        {
            try {
                serverSocket.close();
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    } //off server

    @Override
    boolean getCommand() {
        try {
            String readed=null;
            while ((!client.isClosed())&&(readed=in.readLine())==null){
                if (client.isClosed())
                {
                    ((Controller)controller).startServer();
                    return true;
                }
            }
            System.out.println(readed);
            String[] result = parseCommand(readed);
            if (result==null)
            {
                controller.run(ERROR,WRONG_COMMAND);
                return true;
            }
            controller.run(result[0],result[1]);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    } //do not know what for did i need id

    @Override
    boolean sendResult(String[] data) { //send data to client
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<data.length; i++)
        {
           sb.append(data[i]+"\n");
        }
        out.println(sb.toString());
        out.flush();
        return true;
    }

    @Override
    boolean sendError(String error) { //send error to client
        out.println(error);
        out.flush();
        return false;
    }

    @Override
    boolean run() {//hmm.. it should start execute server's code
        return false;
    }

    @Override
    boolean unConnect() {
        try {
            client.close();
            in=null;
            out=null;
            client=null;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String[] parseCommand(String readed)
    {
        String[] result = new String[2];
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (readed.charAt(i)==' '){i++;}
        sb.append(readed.substring(i));
        readed = sb.toString();
        if ((readed.length()>=2)&&(readed.substring(0,2).equals(LS)))
        {
            if (readed.length()>2){return null;}
            result[0]=LS;
            result[1]=null;
        } else
        if ((readed.length()>=5)&&(readed.substring(0,5).equals(MKDIR)))
        {
            if (readed.length()<7){return null;}
            result[0]=MKDIR;
            result[1]=readed.substring(6);
        } else
        if ((readed.length()>=5)&&(readed.substring(0,5).equals(TOUCH)))
        {
            if (readed.length()<7){return null;}
            result[0]=TOUCH;
            result[1]=readed.substring(6);
        } else
        if ((readed.length()>=2)&&(readed.substring(0,2).equals(CD)))
        {
            if (readed.length()<4){return null;}
            result[0]=CD;
            result[1]=readed.substring(3);
        } else if((readed.length()>=4)&&(readed.substring(0,4).equals(EXIT)))
        {
            if (readed.length()<4){return null;}
            result[0]=EXIT;
            result[1]=null;
        } else
        {
            return null;
        }
        return result;

    }



}
