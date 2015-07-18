package us.vicentini.utils;

import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 *
 * @author Shulander
 */
public class FileUtils {
    public static String fileRead(String filePath) throws java.io.IOException {
        FileInputStream stream = new FileInputStream(new java.io.File(filePath));
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /*
             * Instead of using default, pass in a decoder.
             */
            String returnValue = Charset.defaultCharset().decode(bb).toString();
            returnValue = returnValue.replaceAll("\\\r\\\n", "\\\n");
            return returnValue;
        } finally {
            stream.close();
        }

    }
}
