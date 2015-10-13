package lilsmile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by Smile on 07.10.15.
 */
public class FileSystem implements Constants{

    private GetResult controller;

    private File root;
    private File currentDir;
    private String[] dataToReturn;
    private String error;




    FileSystem()
    {
        String path = Paths.get("").toAbsolutePath().toString(); //get path
        System.out.println(path);
        path+="/"+ROOT_NAME;
        System.out.println(path);
        root = new File(path);
        if (!root.exists()) //create root
        {
            root.mkdir();
        }
        currentDir = root;
    }


    public void startNewClient()
    {
        currentDir=root;
    }


    public void setController(GetResult controller) {
        this.controller = controller;
    }


    boolean doSomething(String command, String arg){

        boolean result=false;
        dataToReturn=null;
	error=null;
        dataToReturn= new String[]{""};
        if (command.equals(ERROR))
        {
            error=arg;
            result=false;
        }else
        {
            switch (command) {
                case CD: {
                    result = cd(arg);
                    break;
                }
                case LS: {
                    result = ls();
                    break;
                }
                case MKDIR: {
                    result = mkdir(arg);
                    break;
                }
                case TOUCH: {
                    result = touch(arg);
                    break;
                }
                case EXIT:
                {
                    ((Controller)controller).disconnectClient();
                    dataToReturn=null;
                    error=null;
                    result =  true;
                    break;
                }
            }
        }
        if (result)
        {
            controller.getResult(dataToReturn,null);
        } else
        {
            controller.getResult(null,error);
        }
        return result;
    }
    private boolean cd(String dir){
        File tmpDir = new File(currentDir.getAbsolutePath()+SLASH+dir);
	
        if ((tmpDir.exists())&&(!tmpDir.isFile()))
        {
            String tmp = tmpDir.getAbsolutePath();
            String[] temps = tmp.split("/");
            StringBuilder sb = new StringBuilder();
	    int i = 0;
            boolean trigger = false;
            for (String direct:temps)
            {
                if(direct.equals(ROOT_NAME))
                {
		    if(i==1) 
                    {
			trigger=true;
		    } else {i++;}
                }
                if(trigger)
                {
                    sb.append(direct+"/");
                }
            }
	    currentDir = tmpDir;
            dataToReturn = new String[]{sb.toString()};
            return true;
        }
        error = NO_DIR_ERROR;
        return false;
    }
    private boolean mkdir(String dir){
        File tmpDir = new File(currentDir.getAbsolutePath()+SLASH+dir);
        if (!tmpDir.exists())
        {
            dataToReturn = new String[]{""};
            tmpDir.mkdir();
            return true;
        }
        error = EXISTING_DIR_ERROR;
        return false;
    }
    private boolean touch(String fileName){
        File tmpFile = new File(currentDir.getAbsolutePath()+SLASH+fileName);
        System.out.println(tmpFile.getAbsolutePath());
        if (tmpFile.exists())
        {
            error = EXISTING_FILE_ERROR;
            return false;
        } else
        {
            try {
                tmpFile.createNewFile();
                if (tmpFile.exists())
                {
                    dataToReturn = new String[]{""};
                    return true;
                }
            } catch (IOException e) {
                //e.printStackTrace();
                error = SMTH_WENT_WRONG_ERROR;
                return false;
            }
        }
        error = SMTH_WENT_WRONG_ERROR;
        return false;
    }
    private boolean ls(){
        //dataToReturn = currentDir.list();

        File[] tmp = currentDir.listFiles();
        dataToReturn = new String[tmp.length];
        for (int i = 0; i<tmp.length; i++)
        {
            if (tmp[i].isDirectory())
            {
                dataToReturn[i]=tmp[i].getName()+"/";
            } else
            {
                dataToReturn[i]=tmp[i].getName();
            }
        }
        if (dataToReturn.equals(null))
        {
            error = SMTH_WENT_WRONG_ERROR;
            return false;
        }
        if (dataToReturn.length==0)
        {
            dataToReturn = new String[]{""};
        }
        return true;
    }


}
