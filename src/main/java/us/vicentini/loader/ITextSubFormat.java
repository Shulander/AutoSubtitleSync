/*
 * AbstractTextSubFormat.java
 *
 * Created on 22 Ιούνιος 2005, 3:17 πμ
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
package us.vicentini.loader;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import us.vicentini.model.SubEntry;
import us.vicentini.model.Subtitle;

/**
 *
 * @author teras
 */
public abstract class ITextSubFormat extends SubFormat {

    protected static final String nl = "\\\n";
    protected static final String sp = "[ \\t]*";
    protected Subtitle subtitle_list;

    /*
     * Initialization functions
     */
    private void setFPS(float FPS) {
        this.FPS = FPS;
    }

    /*
     * Loading functions
     */
    protected abstract SubEntry getSubEntry(Matcher m);

    protected abstract Pattern getPattern();

    /*
     * Saving functions
     */
    protected abstract void appendSubEntry(SubEntry sub, StringBuffer str);

    protected Pattern getTestPattern() {
        return getPattern();
    }

    @Override
    public Subtitle parse(String input) {
        try {
            if (!getTestPattern().matcher(input).find()) {
                return null;    // Not valid - test pattern does not match
            }
            subtitle_list = new Subtitle();
            setFPS(FPS);
            input = initLoader(input);

            Matcher m = getPattern().matcher(input);
            SubEntry entry;
            while (m.find()) {
                entry = getSubEntry(m);
                if (entry != null) {
                    subtitle_list.add(entry);
                }
            }
            if (subtitle_list.isEmpty()) {
                return null;
            }
            return subtitle_list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String initLoader(String input) {
        return input + "\n";
    }

    protected void cleanupLoader(Subtitle sub) {
    }

    @Override
    public boolean produce(Subtitle subs, File outfile) throws IOException {
        super.produce(subs, outfile);
        StringBuffer res = new StringBuffer();
        initSaver(subs, res);
        for (int i = 0; i < subs.size(); i++) {
            appendSubEntry(subs.elementAt(i), res);
        }
        cleanupSaver(res);

        /*
         * Clean up leading \n characters
         */
        while (res.charAt(res.length() - 1) == '\n' && res.charAt(res.length() - 2) == '\n') {
            res.setLength(res.length() - 1);
        }

        // encoder = Charset.forName(jub.prefs.getSaveEncoding()).newEncoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        CharsetEncoder encoder = Charset.forName(getEncoding()).newEncoder();

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile), encoder));
        out.write(res.toString().replace("\n", "\r\n"));
        out.close();
        return true;
    }

    protected void initSaver(Subtitle subs, StringBuffer header) {
    }

    protected void cleanupSaver(StringBuffer footer) {
    }
}
