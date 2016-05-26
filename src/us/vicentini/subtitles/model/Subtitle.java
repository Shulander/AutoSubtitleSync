package us.vicentini.subtitles.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * In memory subtitle representation.
 *
 * @author teras
 */
public class Subtitle implements Iterable<SubEntry> {

    // List of subtitles 
    private final LinkedList<SubEntry> sublist;

    /**
     * Empty Subtitle Constructor.
     */
    public Subtitle() {
        sublist = new LinkedList<SubEntry>();
    }

    /**
     * Subtitle Constructor and copy the entries from the old subtitle.
     *
     * @param old the original subtitle
     */
    public Subtitle(Subtitle old) {

        sublist = new LinkedList<SubEntry>();
        SubEntry newentry;
        SubEntry oldentry;
        for (int i = 0; i < old.size(); i++) {
            oldentry = old.elementAt(i);
            newentry = new SubEntry(oldentry);
            sublist.add(newentry);
        }
    }

    private double getMaxTime() {
        double max;
        double cur;

        max = 0;
        for (int i = 0; i < sublist.size(); i++) {
            cur = sublist.get(i).getFinishTime().toSeconds();
            if (cur > max) {
                max = cur;
            }
        }
        return max;
    }

    /**
     * Add new SubEntry sorted by start time.
     *
     * @param sub new subtitle entry
     * @return new subtitle position
     */
    public int addSorted(SubEntry sub) {
        double time = sub.getStartTime().toSeconds();
        int pos = 0;
        while (sublist.size() > pos && sublist.get(pos).getStartTime().toSeconds() < time) {
            pos++;
        }
        sublist.add(pos, sub);
        return pos;
    }

    public void add(SubEntry sub) {
        sublist.add(sub);
    }

    public void remove(int idx) {
        sublist.remove(idx);
    }

    public void remove(SubEntry sub) {
        sublist.remove(sub);
    }

    public SubEntry elementAt(int idx) {
        return sublist.get(idx);
    }

    public boolean isEmpty() {
        return sublist.isEmpty();
    }

    public int size() {
        return sublist.size();
    }

    public int indexOf(SubEntry entry) {
        return sublist.indexOf(entry);
    }

    /**
     * If not an exact match is found, return the closest previous index in respect to the start time.
     * 
     * @param time
     * @param fuzzyMatch
     * @return 
     */
    public int findSubEntry(Time time, boolean fuzzyMatch) {
        int fuzzyresult = -1;
        double fuzzyDiff = Double.MAX_VALUE;

        SubEntry entry;
        double cdiff;
        for (int i = 0; i < sublist.size(); i++) {
            entry = sublist.get(i);
            if (entry.isInTime(time)) {
                return i;
            }
            if (fuzzyMatch) {
                cdiff = Math.abs(time.getMsecs() - entry.getStartTime().getMsecs());
                if (cdiff > 0 && cdiff < fuzzyDiff) {
                    fuzzyDiff = cdiff;
                    fuzzyresult = i;
                }
            }
        }
        return fuzzyresult;
    }

    public int getRowCount() {
        return sublist.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (SubEntry subEntry : sublist) {
            sb.append(subEntry.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    public Subtitle split(SubEntry subEntry) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void applyCorrection(double angular, double linear) {
        for (SubEntry subEntry : sublist) {
            subEntry.applyCorrection(angular, linear);
        }
    }

    @Override
    public Iterator<SubEntry> iterator() {
        return Arrays.asList(sublist.toArray(new SubEntry[0])).iterator();
    }
}
