package br.com.microhobby.utils;

import totalcross.io.BufferedStream;
import totalcross.io.File;
import totalcross.io.LineReader;
import totalcross.io.Stream;

public class ExecUtils {

    public static boolean writeSysFsFile(String cmd, String path) {
        try {
            File sysfs = new File(path, File.READ_WRITE);
            BufferedStream output =
                new BufferedStream(sysfs, BufferedStream.WRITE);
            output.writeBytes(cmd);
            output.close();
            sysfs.close();

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public static String readSysFsFile(String path)
    {
        try {
            File file = new File(path, File.READ_ONLY);
            BufferedStream stream = new BufferedStream(file, BufferedStream.READ);
            String ret = stream.readLine();
            stream.close();

            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String executeProgram(String execPath)
    {
        Process process = null;
        byte[] output = new byte[1024];

        try {
            process = Runtime.getRuntime().exec(execPath);
            process.getOutputStream().write(output, 0, output.length);
            process.waitFor();
        } catch (Exception ex) {
            ex.printStackTrace();
        };

        String input = "";

        try {
            // Read line by line the buffered stream
            LineReader lineReader =
                new LineReader(Stream.asStream(process.getInputStream()));
            String lines = "";
            while ((lines = lineReader.readLine()) != null) {
                input += lines + "\n";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        };

        return input;
    }
}
