package us.vicentini.subtitles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import us.vicentini.subtitles.loader.SubRip;
import us.vicentini.subtitles.model.Subtitle;

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
        String inputEngPathFile = "input\\legenda.eng.srt";
        String inputPorPathFile = "input\\legenda.eng.srt";
        SubRip engSubFormat = new SubRip();
        Subtitle engSub = engSubFormat.parse(fileRead(inputEngPathFile));
        
        SubRip porSubFormat = new SubRip();
        Subtitle porSub = porSubFormat.parse(fileRead(inputPorPathFile));
//        synchronizedSub.split(new SubEntry());
        engSub.applyCorrection(1, 1);
        System.out.println(engSub.toString());
        engSubFormat.produce(engSub, new File("output\\legenda.eng.srt") );
    }
}
