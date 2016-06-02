package us.vicentini.model;

/**
 *
 * @author teras
 */
public class SubEntry implements Comparable<SubEntry> {

    private Time start, finish;
    private String subtext;
    private int mark;

    public SubEntry(double start, double finish, String line) {
        this.start = new Time(start);
        this.finish = new Time(finish);
        this.subtext = line;
        mark = 0;
    }

    public SubEntry(Time start, Time finish, String line) {
        this.start = new Time(start);
        this.finish = new Time(finish);
        this.subtext = line;
        mark = 0;
    }

    public SubEntry(SubEntry old) {
        this(old.getStartTime(), old.getFinishTime(), old.getText());
        mark = old.mark;
    }

    public SubEntry() {
        this(0, 0, "");
    }

    public int compareTo(SubEntry other) {
        return start.compareTo(other.start);
    }

    public void setMark(int i) {
        mark = i;
    }

    public int getMark() {
        return mark;
    }

    public void setText(String text) {
        subtext = text;
    }

    public void setStartTime(Time t) {
        start.setTime(t);
    }

    public void setFinishTime(Time t) {
        finish.setTime(t);
    }

    String getData(int row, int col) {
        switch (col) {
            case 0:
                return Integer.toString(row + 1);
            case 1:
                return start.toString();
            case 2:
                return finish.toString();
            case 3:
                return "0";
            case 5:
                return subtext.replace('\n', '|');
        }
        return null;
    }

    public Time getStartTime() {
        return start;
    }

    public Time getFinishTime() {
        return finish;
    }

    public String getText() {
        return subtext;
    }

    void setData(int col, Object data) {
        if (col == 3) {
            subtext = data.toString();
        }
    }

    public boolean isInTime(double t) {
        if (t >= start.toSeconds() && t <= finish.toSeconds()) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return start.toString() + "->" + finish.toString() + " " + subtext;
    }
    
    public void applyCorrection(double angular, double linear) {
        this.start.applyCorrection(angular, linear);
        this.finish.applyCorrection(angular, linear);
    }
}
