package us.vicentini;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import us.vicentini.loader.SubRip;
import us.vicentini.model.SubEntry;
import us.vicentini.model.Subtitle;

/**
 *
 * @author Shulander
 */
public class Main {

    public static String fileRead(String filePath) throws java.io.IOException {
        FileInputStream stream = new FileInputStream(new File(filePath));
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String inputSyncPathFile = "input\\legenda.eng.srt";
        SubRip syncSub = new SubRip();
        Subtitle synchronizedSub = syncSub.parse(fileRead(inputSyncPathFile));
//        synchronizedSub.split(new SubEntry());
        synchronizedSub.applyCorrection(1, 1);
        System.out.println(synchronizedSub.toString());
        syncSub.produce(synchronizedSub, new File("output\\legenda.eng.srt") );
    }
}
