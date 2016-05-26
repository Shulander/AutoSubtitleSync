package us.vicentini.subtitles.util;

import java.util.Iterator;

import us.vicentini.subtitles.model.SubEntry;
import us.vicentini.subtitles.model.Subtitle;

/**
 * Subtitle utility class.
 *
 * <p>
 * This class has utility static methods to use for Subtitle objects, it shouldn't be instantiated </p>
 *
 * @author Shulander
 */
public class SubtitlesUtil {

    /**
     * Compare two subtitle and return the intersection time between a and b.
     *
     * <p>
     * It can be used to check how similar the entries in the subtitles are synchronized.</p>
     *
     * @param subtitleA base Subtitle object
     * @param subtitleB Subtitle instance to compare against.
     * @return double value containing the percentual similarity betweeen a and b
     */
    public static double compareSubtitles(Subtitle subtitleA, Subtitle subtitleB) {
        long totalTime = totalTime(subtitleA);
        long intersectTime = intersectionTime(subtitleA, subtitleB);
        return intersectTime / (float) totalTime;
    }
    
    /**
     * Calculate the intersection time for all sub entries that compose the subtitles.
     * 
     * @param subtitleA subtitle object
     * @param subtitleB subtitle object
     * @return time in milliseconds representing the intersection time between subtitles
     */
    public static long intersectionTime(Subtitle subtitleA, Subtitle subtitleB) {
        Iterator<SubEntry> iteratorA = subtitleA.iterator();
        Iterator<SubEntry> iteratorB = subtitleB.iterator();
        SubEntry entryA = null;
        SubEntry entryB = null;
        if (iteratorA.hasNext() && iteratorB.hasNext()) {
            entryA = iteratorA.next();
            entryB = iteratorB.next();
        }

        long intersectTime = 0;
        while (entryA != null && entryB != null) {
            if (entryA.getStartTime().compareTo(entryB.getStartTime()) <= 0) {
                if (entryA.isInTime(entryB.getStartTime())) {
                    long intersect = SubEntryUtil.calculateIntersectionTime(entryA, entryB);
                    intersectTime += intersect;
                }
                entryA = iteratorA.hasNext() ? iteratorA.next() : null;
            } else {
                if (entryB.isInTime(entryA.getStartTime())) {
                    long intersect = SubEntryUtil.calculateIntersectionTime(entryB, entryA);
                    intersectTime += intersect;
                }
                entryB = iteratorB.hasNext() ? iteratorB.next() : null;
            }

        }
        return intersectTime;
    }

    /**
     * Return the sums for all subtitle entries.
     * 
     * @param subtitle subtitle object
     * @return time in milliseconds 
     */
    public static long totalTime(Subtitle subtitle) {
        Iterator<SubEntry> iterator = subtitle.iterator();
        long returnValue = 0;
        while (iterator.hasNext()) {
            SubEntry entry = iterator.next();
            returnValue += entry.deltaTime();
        }
        return returnValue;
    }

}
