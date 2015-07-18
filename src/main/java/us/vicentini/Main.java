package us.vicentini;

import java.io.File;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import us.vicentini.loader.SubRip;
import us.vicentini.model.SubEntry;
import us.vicentini.model.Subtitle;
import us.vicentini.utils.FileUtils;

/**
 *
 * @author Shulander
 */
public class Main {
    private final static Log log = LogFactory.getLog(Main.class);
    
    static {
        if((new File("config/log4j.xml")).exists()){
            DOMConfigurator.configure("config/log4j.xml");
        }else if((new File("config/log4j.properties")).exists()) {
            PropertyConfigurator.configure("config/log4j.properties");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        log.info("teste");
        String inputSyncPathFile = "input\\legenda.eng.srt";
        SubRip syncSub = new SubRip();
        Subtitle synchronizedSub = syncSub.parse(FileUtils.fileRead(inputSyncPathFile));
        SubEntry entry = synchronizedSub.elementAt(10);
        log.info(entry.toString());
//        synchronizedSub.split(new SubEntry());
//        System.out.println(synchronizedSub.toString());
        syncSub.produce(synchronizedSub, new File("output\\legenda.eng.srt") );
        
        log.info("finish");
    }
}
