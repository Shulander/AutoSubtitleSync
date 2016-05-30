package us.vicentini.subtitles.sync;

import us.vicentini.subtitles.model.Subtitle;
import us.vicentini.subtitles.model.Time;

/**
 *
 * @author Shulander
 */
public class BasicSyncSubtitles extends SyncSubtitles {

    private Time synccedA;
    private Time synccedB;
    private Time dessynccedA;
    private Time dessynccedB;

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

        float angular = (synccedB.getMsecs() - synccedA.getMsecs())
            / (float) (dessynccedB.getMsecs() - dessynccedA.getMsecs());
        float linear = synccedB.getMsecs() - angular * dessynccedB.getMsecs();

        newSubtitle.applyCorrection(angular, linear);

        return newSubtitle;
    }
}
