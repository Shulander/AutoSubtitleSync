package us.vicentini.subtitles.util;

import us.vicentini.subtitles.model.Subtitle;

/**
 * Subtitle utilitary class.
 * 
 * <p>This class has utility static methods to use with Subtitle objects, it shouldn't be instantiated </p>
 * @author Shulander
 */
public class SubtitlesUtil {
    
    /**
     * Compare two subtitle and return the intersection time between a and b.
     * 
     * <p>It can be used to check how similar the entries in the subtitles are synchronized.</p>
     * 
     * @param subtitleA base Subtitle object
     * @param subtitleB Subtitle instance to compare against.
     * @return double value containing the percentual similarity betweeen a and b
     */
    public static double compareSubtitles(Subtitle subtitleA, Subtitle subtitleB) {
        return 0.0;
    }
}
