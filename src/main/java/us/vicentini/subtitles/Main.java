package us.vicentini.subtitles;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

import us.vicentini.subtitles.loader.SubRip;
import us.vicentini.subtitles.model.Subtitle;
import us.vicentini.subtitles.sync.BasicSyncSubtitles;
import us.vicentini.subtitles.util.FileUtils;
import us.vicentini.subtitles.util.SubtitlesUtil;


/**
 * @author Shulander
 */
public class Main {

    private final static Log log = LogFactory.getLog(us.vicentini.subtitles.Main.class);
    public static final int ITERATIONS = 8;

    static {
        if ((new File("config/log4j.xml")).exists()) {
            DOMConfigurator.configure("config/log4j.xml");
        } else if ((new File("config/log4j.properties")).exists()) {
            PropertyConfigurator.configure("config/log4j.properties");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        log.info("started");
        String inputSyncPathFile = "input/The.Beekeeper.2024.iTA-ENG.PROPER.Bluray.1080p.x264-CYBER.ita.srt";
        String inputUnsyncPathFile = "input/The.Beekeeper.2024.iTA-ENG.PROPER.Bluray.1080p.x264-CYBER.eng.srt";
//        String inputUnsyncPathFile = "output\\legenda.eng.srt";
        SubRip syncSubFormat = new SubRip();
        Subtitle synchronizedSubtitle = syncSubFormat.parse(FileUtils.fileRead(inputSyncPathFile));


        SubRip unsyncSubFormat = new SubRip();
        Subtitle unsynchronizedSubtitle = unsyncSubFormat.parse(FileUtils.fileRead(inputUnsyncPathFile));

        double original = SubtitlesUtil.compareSubtitles(synchronizedSubtitle, unsynchronizedSubtitle);

        BasicSyncSubtitles syncTool = new BasicSyncSubtitles(synchronizedSubtitle, unsynchronizedSubtitle);

        double compareValue = original;
        Subtitle newSub = null;

        for (int i = 0; i < ITERATIONS; i++) {
            for (int j = 1; j <= ITERATIONS; j++) {
                log.info(String.format("Run %d of %d", (i * ITERATIONS + j), ITERATIONS*ITERATIONS));
                for (int k = 0; k < ITERATIONS; k++) {
                    for (int l = 1; l <= ITERATIONS; l++) {
                        syncTool.setSynccedA(synchronizedSubtitle.elementAt(i).getStartTime());
                        syncTool.setSynccedB(
                                synchronizedSubtitle.elementAt(synchronizedSubtitle.getRowCount() - j).getStartTime());

                        syncTool.setDessynccedA(unsynchronizedSubtitle.elementAt(k).getStartTime());
                        syncTool.setDessynccedB(
                                unsynchronizedSubtitle.elementAt(unsynchronizedSubtitle.getRowCount() - l)
                                        .getStartTime());

                        Subtitle tmpSub = syncTool.synchronize();
                        double tmpValue = SubtitlesUtil.compareSubtitles(synchronizedSubtitle, tmpSub);
                        if (tmpValue > compareValue) {
                            log.info("new improvement compared with started value: "
                                     + String.format("%.2f", tmpValue * 100) + " > "
                                     + String.format("%.2f", compareValue * 100) +
                                     " with indexes " + String.format("i:%d, j:%d, k:%d, l:%d", i, j, k, l));
                            compareValue = tmpValue;
                            newSub = tmpSub;
                        }
                    }
                }
            }
        }

        if (newSub != null) {
            unsyncSubFormat.produce(newSub, new File("output\\output.srt"));
        }

        log.info("compareTo: " + SubtitlesUtil.compareSubtitles(synchronizedSubtitle, unsynchronizedSubtitle));
//        synchronizedSub.split(new SubEntry());
//        engSub.applyCorrection(1, 1000);
//        System.out.println(engSub.toString());
        syncSubFormat.produce(unsynchronizedSubtitle, new File("output\\legenda.eng.srt"));
        log.info("finished");
    }
}
