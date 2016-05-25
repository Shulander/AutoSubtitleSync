package us.vicentini.subtitles.sync;

import us.vicentini.subtitles.model.Subtitle;
import us.vicentini.subtitles.model.Time;

/**
 *
 * @author Shulander
 */
public class BasicSyncSubtitles extends SyncSubtitles {

    Time synccedA, synccedB, dessynccedA, dessynccedB;

    public BasicSyncSubtitles(Subtitle subSincced, Subtitle subDesSinc) {
        super(subSincced, subDesSinc);
    }

    public void setDessynccedA(Time dessynccedA) {
        this.dessynccedA = dessynccedA;
    }

    public void setDessynccedB(Time dessynccedB) {
        this.dessynccedB = dessynccedB;
    }

    public void setSynccedA(Time synccedA) {
        this.synccedA = synccedA;
    }

    public void setSynccedB(Time synccedB) {
        this.synccedB = synccedB;
    }

    @Override
    public Subtitle synchronize() {
        Subtitle newSubtitle = new Subtitle(subDesSinc);
        
        int angular = (synccedB.getMsecs() - synccedA.getMsecs()) / (dessynccedB.getMsecs() - dessynccedA.getMsecs());
        int linear = synccedB.getMsecs() - angular * dessynccedB.getMsecs();

        newSubtitle.applyCorrection(angular, linear);

        return newSubtitle;
    }
}
