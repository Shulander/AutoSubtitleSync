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
                "(?s)(\\d+)" + sp + nl + "(\\d\\d):(\\d\\d):(\\d\\d),(\\d\\d\\d)" + sp + "-->"
                + sp + "(\\d\\d):(\\d\\d):(\\d\\d),(\\d\\d\\d)" + sp + "(X1:\\d.*?)??" + nl + "(.*?)" + nl + nl);

    }

    @Override
    protected Pattern getPattern() {
        return pat;
    }

    @Override
    protected SubEntry getSubEntry(Matcher m) {
        Time start = new Time(m.group(2), m.group(3), m.group(4), m.group(5));
        Time finish = new Time(m.group(6), m.group(7), m.group(8), m.group(9));
        SubEntry entry = new SubEntry(start, finish, m.group(11));
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
