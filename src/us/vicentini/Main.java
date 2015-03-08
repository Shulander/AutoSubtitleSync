package us.vicentini;

import java.io.File;
import java.io.IOException;
import us.vicentini.loader.SubRip;
import us.vicentini.model.Subtitle;
import us.vicentini.utils.FileUtils;

/**
 *
 * @author Shulander
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String inputSyncPathFile = "input\\legenda.eng.srt";
        SubRip syncSub = new SubRip();
        Subtitle synchronizedSub = syncSub.parse(FileUtils.fileRead(inputSyncPathFile));
//        synchronizedSub.split(new SubEntry());
        System.out.println(synchronizedSub.toString());
        syncSub.produce(synchronizedSub, new File("output\\legenda.eng.srt") );
    }
}
