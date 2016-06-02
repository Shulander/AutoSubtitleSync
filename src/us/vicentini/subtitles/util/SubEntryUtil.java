package us.vicentini.subtitles.util;

import us.vicentini.subtitles.model.SubEntry;

/**
 * SubEntry utility class.
 *
 * <p>
 * This class has utility static methods to use for SybEntry objects, it shouldn't be instantiated </p>
 *
 * @author Shulander
 */
public class SubEntryUtil {

    /**
     * Calculate the <b>union</b> time between two SubEntry objects.
     * 
     * @param entryA SubEntry object
     * @param entryB SubEntry object
     * @return return the union time between the SubEntry objects in milliseconds
     */
    public static long calculateUnionTime(SubEntry entryA, SubEntry entryB) {
        long start = entryA.getStartTime().getMsecs();
        long end = entryA.getFinishTime().getMsecs();

        if (end < entryB.getFinishTime().getMsecs()) {
            end = entryB.getFinishTime().getMsecs();
        }

        return end - start;
    }

    /**
     * Calculate the <b>intersection</b> time between two SubEntry objects.
     * 
     * @param entryA SubEntry object
     * @param entryB SubEntry object
     * @return return the intersection time between the SubEntry objects in milliseconds
     */
    public static long calculateIntersectionTime(SubEntry entryA, SubEntry entryB) {
        long start = entryB.getStartTime().getMsecs();
        long end = entryA.getFinishTime().getMsecs();

        if (end > entryB.getFinishTime().getMsecs()) {
            end = entryB.getFinishTime().getMsecs();
        }

        return end - start;
    }
}
