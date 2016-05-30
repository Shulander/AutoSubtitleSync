package us.vicentini.subtitles.model;

/**
 *
 * @author teras
 */
public class Time implements Comparable<Time> {

    protected float msecs = -1;
    public static final int MAX_TIME = 3600 * 24;   // in seconds
    public static final int MAX_MILLI_TIME = MAX_TIME * 1000;   // in seconds

    /* Time in seconds */
    public Time(double time) {
        setTime(time);
    }

    public int getMsecs() {
        return Math.round(msecs);
    }

    /* Time in frames & FPS */
    public Time(String frame, float fps) {
        try {
            setTime(Double.parseDouble(frame) / fps);
        } catch (NumberFormatException e) {
            invalidate();
        }
    }

    /* Time in hours, minutes, seconds & milliseconds */
    public Time(String h, String m, String s, String f) {
        setTime(h, m, s, f);
    }

    /* Time in hours, minutes, seconds & frames */
    public Time(String h, String m, String s, String f, float fps) {
        setTime(h, m, s, f, fps);
    }

    public Time(Time time) {
        setTime(time);
    }

    public void addTime(double d) {
        if (!isValid()) {
            return;
        }
        setTime(toSeconds() + d);
    }

    public void recodeTime(double beg, double fac) {
        if (!isValid()) {
            return;
        }
        setTime((toSeconds() - beg) * fac + beg);
    }

    private void setTime(String h, String m, String s, String f, float fps) {
        short hour, min, sec, milli, fram;
        try {
            hour = Short.parseShort(h);
            min = Short.parseShort(m);
            sec = Short.parseShort(s);
            fram = Short.parseShort(f);
            milli = (short) Math.round(fram * 1000f / fps);
            setTime(hour, min, sec, milli);
        } catch (NumberFormatException e) {
            invalidate();
        }
    }

    private void setTime(String h, String m, String s, String f) {
        short hour, min, sec, milli;
        int flength;
        try {
            hour = Short.parseShort(h);
            min = Short.parseShort(m);
            sec = Short.parseShort(s);
            flength = f.length();
            if (flength < 3) {
                f = f + "000".substring(flength);
            }
            milli = Short.parseShort(f);
            setTime(hour, min, sec, milli);
        } catch (NumberFormatException e) {
            invalidate();
        }
    }

    public boolean isValid() {
        return getMsecs() >= 0;
    }

    private void invalidate() {
        msecs = -1;
    }

    public void setTime(double time) {
        setMilliSeconds((int) (time * 1000d + 0.5d));
    }

    public void setTime(Time time) {
        msecs = time.getMsecs();
    }

    private void setTime(short h, short m, short s, short f) {
        setMilliSeconds((h * 3600 + m * 60 + s) * 1000 + f);
    }

    private void setMilliSeconds(int msecs) {
        if (msecs < 0) {
            msecs = 0;
        }
        if (msecs > MAX_MILLI_TIME) {
            msecs = MAX_MILLI_TIME;
        }
        this.msecs = msecs;
    }

    @Override
    public int compareTo(Time t) {
        if (getMsecs() < t.getMsecs()) {
            return -1;
        }
        if (getMsecs() > t.getMsecs()) {
            return 1;
        }
        return 0;
    }

    public String getSeconds() {

        int time;
        int milli;
        milli = getMsecs() % 1000;
        time = getMsecs() / 1000;
        
        int sec;
        sec = time % 60;
        time /= 60;
        
        int min;
        min = time % 60;
        time /= 60;
        
        int hour = time;

        StringBuilder res = new StringBuilder();
        res.append(String.format("%02d", hour));
        res.append(":");

        res.append(String.format("%02d", min));
        res.append(":");

        res.append(String.format("%02d", sec));
        res.append(",");

        res.append(String.format("%03d", milli));
        return res.toString();
    }

    public String getSecondsFrames(float fps) {
        int time;
        int milli;
        milli = getMsecs() % 1000;

        time = getMsecs() / 1000;
        int sec;
        sec = time % 60;

        time /= 60;
        int min;
        min = time % 60;

        time /= 60;
        int hour = time;

        StringBuilder res = new StringBuilder();
        res.append(String.format("%02d", hour));
        res.append(":");

        res.append(String.format("%02d", min));
        res.append(":");

        res.append(String.format("%02d", sec));
        res.append(":");

        int frm = Math.round(milli * fps / 1000f);

        res.append(String.format("%02d", frm));
        return res.toString();
    }

    public String getFrames(float FPS) {
        return Integer.toString((int) Math.round(toSeconds() * FPS));
    }

    public double toSeconds() {
        return msecs / 1000d;
    }

    public String toString() {
        return getSeconds();
    }

    public void applyCorrection(double angular, double linear) {
        msecs = (int) Math.round(msecs * angular + linear);
    }
}
