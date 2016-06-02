/*
 * SubRip.java
 *
 * Created on 26 Αύγουστος 2005, 11:08 πμ
 *
 * This file is part of Jubler.
 *
 * Jubler is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2.
 *
 *
 * Jubler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jubler; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package us.vicentini.subtitles.loader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import us.vicentini.subtitles.model.SubEntry;
import us.vicentini.subtitles.model.Time;

/**
 *
 * @author teras
 */
public class SubRip extends StyledTextSubFormat {

    private static final Pattern pat;
    private int counter = 1;

    static {
        pat = Pattern.compile(
            "(?s)(\\d+)" + SPACE_PATTERN + NEW_LINE_PATTERN + "(\\d\\d):(\\d\\d):(\\d\\d),(\\d\\d\\d)" + SPACE_PATTERN + "-->"
            + SPACE_PATTERN + "(\\d\\d):(\\d\\d):(\\d\\d),(\\d\\d\\d)" + SPACE_PATTERN + "(X1:\\d.*?)??" + NEW_LINE_PATTERN + "(.*?)" + NEW_LINE_PATTERN + NEW_LINE_PATTERN);

    }

    @Override
    protected Pattern getPattern() {
        return pat;
    }

    @Override
    protected SubEntry getSubEntry(Matcher match) {
        Time.TimeBuilder startTimeBuilder = new Time.TimeBuilder();
        startTimeBuilder.setHour(match.group(2)).setMinutes(match.group(3))
            .setSeconds(match.group(4)).setMilliseconds(match.group(5));
        Time start = startTimeBuilder.build();

        Time.TimeBuilder finishTimeBuilder = new Time.TimeBuilder();
        finishTimeBuilder.setHour(match.group(6)).setMinutes(match.group(7))
            .setSeconds(match.group(8)).setMilliseconds(match.group(9));
        Time finish = finishTimeBuilder.build();
        SubEntry entry = new SubEntry(start, finish, match.group(11));
        return entry;
    }

    @Override
    public String getExtension() {
        return "srt";
    }

    @Override
    public String getName() {
        return "SubRip";
    }

    @Override
    protected void appendSubEntry(SubEntry sub, StringBuffer str) {
        str.append(Integer.toString(counter++));
        str.append("\n");
        str.append(sub.getStartTime().getSeconds());
        str.append(" --> ");
        str.append(sub.getFinishTime().getSeconds());
        str.append("\n");
        str.append(sub.getText());
        str.append("\n\n");
    }

}
