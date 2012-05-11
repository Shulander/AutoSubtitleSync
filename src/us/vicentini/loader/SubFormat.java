package us.vicentini.loader;

import java.io.File;
import java.io.IOException;
import us.vicentini.model.Subtitle;

/**
 *
 * @author teras
 */
public abstract class SubFormat {
    
    protected float FPS;
    private String ENCODING;

    public abstract String getExtension();
    public abstract String getName();
            
    public boolean produce(Subtitle subs, File outfile) throws IOException {
        FPS = 25f;
        ENCODING = "UTF-8";
        return true;
    }
    
    public String getExtendedName() {
        return getName();
    }
    
    public String getDescription() {
        return getExtendedName() + "  (*." + getExtension() + ")";
    }

    public String getEncoding() {
        return ENCODING;
    }
    
    /* convert a string into Subtitle */
    public abstract Subtitle parse(String input);
    
}
