package us.vicentini.model;


import java.util.LinkedList;


/**
 *
 * @author teras
 */
public class Subtitle {
    
    /** List of subtitles */
    private LinkedList <SubEntry> sublist;

    public Subtitle() {
        sublist = new LinkedList<SubEntry>();
    }
    
    public Subtitle(Subtitle old) {
        
        sublist = new LinkedList<SubEntry>();
        SubEntry newentry, oldentry;
        for (int i = 0 ; i < old.size() ; i++ ) {
            oldentry = old.elementAt(i);
            newentry = new SubEntry(oldentry);
            sublist.add(newentry);
        }
    }
    
    
    
    private double getMaxTime() {
        double max, cur;
        
        max = 0;
        for (int i = 0 ; i < sublist.size() ; i++) {
            cur = sublist.get(i).getFinishTime().toSeconds();
            if ( cur > max ) max = cur;
        }
        return max;
    }
    
    public int addSorted(SubEntry sub) {
        double time = sub.getStartTime().toSeconds();
        int pos = 0;
        while ( sublist.size() > pos && sublist.get(pos).getStartTime().toSeconds() < time )
            pos++;
        sublist.add(pos, sub);
        return pos;
    }
    
    public void add( SubEntry sub) {
        sublist.add(sub);
    }
    
    public void remove(int i) {
        sublist.remove(i);
    }
    public void remove(SubEntry sub) {
        sublist.remove(sub);
    }
    
    public SubEntry elementAt(int i) {
        return sublist.get(i);
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
    
    public int findSubEntry(double time, boolean fuzzyMatch) {
        /* If not an exact match is found, return the closest previous index in respect to the start time */
        int fuzzyresult = -1;
        double fuzzyDiff = Double.MAX_VALUE;
        
        SubEntry entry;
        double cdiff;
        for ( int i = 0 ; i < sublist.size() ; i++ ) {
            entry = sublist.get(i);
            if ( entry.isInTime(time)) return i;
            if (fuzzyMatch ) {
                cdiff = Math.abs(time - entry.getStartTime().toSeconds());
                if ( cdiff > 0 && cdiff < fuzzyDiff) {
                    fuzzyDiff = cdiff;
                    fuzzyresult = i;
                }
            }
        }
        return fuzzyresult;
    }
    
    public int getRowCount(){
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
}