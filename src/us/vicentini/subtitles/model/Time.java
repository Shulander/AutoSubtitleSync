package us.vicentini.subtitles.model;

/**
 *
 * @author teras
 */
public class Time implements Comparable<Time>, Cloneable {

    protected double msecs = -1;
    public static final int MAX_TIME = 3600 * 24;   // in seconds
    public static final int MAX_MILLI_TIME = MAX_TIME * 1000;   // in seconds

    private Time() {

    }

    public long getMsecs() {
        return Math.round(msecs);
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

    private void setTime(long h, long m, long s, long f) {
        setMilliSeconds((h * 3600 + m * 60 + s) * 1000 + f);
    }

    private void setMilliSeconds(long msecs) {
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
        milli = (int) (getMsecs() % 1000);
        time = (int) (getMsecs() / 1000);

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
        milli = (int) (getMsecs() % 1000);

        time = (int) (getMsecs() / 1000);
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
        return String.format("%d", Math.round(toSeconds() * FPS));
    }

    public double toSeconds() {
        return msecs / 1000d;
    }

    @Override
    public String toString() {
        return getSeconds();
    }

    public void applyCorrection(double angular, double linear) {
        msecs = msecs * angular + linear;
    }

    @Override
    public Time clone() {
        Time returnValue;
        try {
            returnValue = (Time) super.clone();
        } catch (CloneNotSupportedException ex) {
            returnValue = new Time();
            returnValue.setMilliSeconds(this.getMsecs());
        }
        return returnValue;
    }

    public static class TimeBuilder {

        short hour;
        short min;
        short seconds;
        long milliseconds;

        /**
         * Basic Builder Constructor.
         */
        public TimeBuilder() {
            hour = 0;
            min = 0;
            seconds = 0;
            milliseconds = 0;
        }

        public TimeBuilder setHour(String strHour) {
            return setHour(Short.parseShort(strHour));
        }

        public TimeBuilder setHour(short hour) {
            this.hour = hour;
            return this;
        }

        public TimeBuilder setMinutes(String strMinutes) {
            return setMinutes(Short.parseShort(strMinutes));
        }

        public TimeBuilder setMinutes(short minutes) {
            this.min = minutes;
            return this;
        }

        public TimeBuilder setSeconds(String strMinutes) {
            return setSeconds(Short.parseShort(strMinutes));
        }

        public TimeBuilder setSeconds(short seconds) {
            this.seconds = seconds;
            return this;
        }

        public TimeBuilder setMilliseconds(final String strMilliseconds) {
            int flength = strMilliseconds.length();
            String localStrMilliseconds;
            if (flength < 3) {
                localStrMilliseconds = strMilliseconds + "000".substring(flength);
            } else {
                localStrMilliseconds = strMilliseconds;
            }
            return setMilliseconds(Short.parseShort(localStrMilliseconds));
        }

        public TimeBuilder setMilliseconds(long milliseconds) {
            this.milliseconds = milliseconds;
            return this;
        }

        public Time build() {
            Time returnValue = new Time();
            returnValue.setTime(hour, min, seconds, milliseconds);
            return returnValue;
        }

    }
}
