package us.vicentini.sync;

import us.vicentini.model.Subtitle;

/**
 *
 * @author Shulander
 */
public abstract class SyncSubtitles {
    
    protected Subtitle subSincced;
    protected Subtitle subDesSinc;
    
    public SyncSubtitles (Subtitle subSincced, Subtitle subDesSinc) {
        this.subDesSinc = subDesSinc;
        this.subSincced = subSincced;
    }
    
    public abstract Subtitle synchronize ();
   
}
