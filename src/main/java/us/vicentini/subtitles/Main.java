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
 *
 * @author Shulander
 */
public class Main {

    private final static Log log = LogFactory.getLog(us.vicentini.subtitles.Main.class);

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
        String inputEngPathFile = "input\\legenda.eng.srt";
        String inputPorPathFile = "input\\legenda.pob.srt";
//        String inputPorPathFile = "output\\legenda.eng.srt";
        SubRip engSubFormat = new SubRip();
        Subtitle engSub = engSubFormat.parse(FileUtils.fileRead(inputEngPathFile));

        SubRip porSubFormat = new SubRip();
        Subtitle porSub = porSubFormat.parse(FileUtils.fileRead(inputPorPathFile));

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
                                log.info("new improovment compared with started value: "
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

        log.info("compareTo: " + SubtitlesUtil.compareSubtitles(engSub, porSub));
//        synchronizedSub.split(new SubEntry());
//        engSub.applyCorrection(1, 1000);
//        System.out.println(engSub.toString());
        engSubFormat.produce(engSub, new File("output\\legenda.eng.srt"));
        log.info("finished");
    }
}
