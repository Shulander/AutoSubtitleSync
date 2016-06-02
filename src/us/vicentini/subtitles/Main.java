package us.vicentini.subtitles;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import us.vicentini.subtitles.loader.SubRip;
import us.vicentini.subtitles.model.SubEntry;
import us.vicentini.subtitles.model.Subtitle;
import us.vicentini.subtitles.sync.BasicSyncSubtitles;
import us.vicentini.subtitles.sync.SyncSubtitles;
import us.vicentini.subtitles.util.SubtitlesUtil;

/**
 *
 * @author Shulander
 */
public class Main {

    public static String fileRead(String filePath) throws java.io.IOException {
        FileInputStream stream = new FileInputStream(new File(filePath));
        FileChannel fc = stream.getChannel();
        try {
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /*
             * Instead of using default, pass in a decoder.
             */
            String returnValue = Charset.defaultCharset().decode(bb).toString();
            returnValue = returnValue.replaceAll("\\\r\\\n", "\\\n");
            return returnValue;
        } finally {
            fc.close();
            stream.close();
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String inputEngPathFile = "input\\legenda.eng.srt";
        String inputPorPathFile = "input\\legenda.pob.srt";
//        String inputPorPathFile = "output\\legenda.eng.srt";
        SubRip engSubFormat = new SubRip();
        Subtitle engSub = engSubFormat.parse(fileRead(inputEngPathFile));

        SubRip porSubFormat = new SubRip();
        Subtitle porSub = porSubFormat.parse(fileRead(inputPorPathFile));

        double original = SubtitlesUtil.compareSubtitles(engSub, porSub);

        BasicSyncSubtitles syncTool = new BasicSyncSubtitles(engSub, porSub);

        double compareValue = 0.0;
        double tmpValue;
        Subtitle tmpSub;
        Subtitle newSub = null;

        for (int i = 0; i < 20; i++) {
            for (int j = 1; j <= 20; j++) {
                for (int k = 0; k < 20; k++) {
                    for (int l = 1; l <= 20; l++) {
                        syncTool.setSynccedA(engSub.elementAt(i).getStartTime());
                        syncTool.setSynccedB(engSub.elementAt(engSub.getRowCount() - j).getStartTime());

                        syncTool.setDessynccedA(porSub.elementAt(k).getStartTime());
                        syncTool.setDessynccedB(porSub.elementAt(porSub.getRowCount() - l).getStartTime());

                        tmpSub = syncTool.synchronize();
                        tmpValue = SubtitlesUtil.compareSubtitles(engSub, tmpSub);
                        if (tmpValue > compareValue) {
                            if (tmpValue > original) {
                                System.out.println("new improovment compared with started value: "
                                    + String.format("%.2f", tmpValue * 100) + " > "
                                    + String.format("%.2f", compareValue * 100));
                                compareValue = tmpValue;
                                newSub = tmpSub;
                            }
                        }

                    }
                }
            }
        }
        if (newSub != null) {
            porSubFormat.produce(newSub, new File("output\\legenda.por2.srt"));
        }

        System.out.println("compareTo: " + SubtitlesUtil.compareSubtitles(engSub, porSub));
//        synchronizedSub.split(new SubEntry());
//        engSub.applyCorrection(1, 1000);
//        System.out.println(engSub.toString());
        engSubFormat.produce(engSub, new File("output\\legenda.eng.srt"));
    }
}
