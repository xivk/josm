// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.preferences;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openstreetmap.josm.tools.Utils.equal;

/**
 * A source entry primarily used to save the user's selection of mappaint styles, 
 * but also for preset sources.
 */
public class SourceEntry {

    /**
     *  A URL can be anything that MirroredInputStream understands, i.e.
     *  a local file, http://, or a file from the current jar
     */
    public String url;

    /**
     *  Name is used as a namespace for color preferences and (currently) only
     *  one file with a name can be loaded at a time. Additional styles must
     *  either have the same name as the main style or no name at all.
     *  If no name is provided, it will be set to the default value "standard".
     *  The name can also be given in the xml file as attribute for the rules tag.
     *  (This overrides the name given in the preferences, otherwise both
     *  methods are equivalent.)
     */
    public String name;

    /**
     * A title that can be used as menu entry.
     */
    public String title;

    /**
     * active is a boolean flag that can be used to turn the style on or off
     * at runtime.
     */
    public boolean active;

    public SourceEntry(String url, String name, String title, Boolean active) {
        this.url = url;
        this.name = equal(name, "") ? null : name;
        this.title = equal(title, "") ? null : title;
        this.active = active;
    }

    public SourceEntry(SourceEntry e) {
        this.url = e.url;
        this.name = e.name;
        this.title = e.title;
        this.active = e.active;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        final SourceEntry other = (SourceEntry) obj;
        return equal(other.url, url) && 
                equal(other.name, name) &&
                equal(other.title, title) &&
                other.active == active;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.url != null ? this.url.hashCode() : 0);
        hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 89 * hash + (this.title != null ? this.title.hashCode() : 0);
        hash = 89 * hash + (this.active ? 1 : 0);
        return hash;
    }

    @Override
    public String toString() {
        return title != null ? title : url;
    }

    /**
     * String to show in menus and error messages.
     * @return Usually the shortdescription, but can be the file name
     * if no shortdescription is available.
     */
    public String getDisplayString() {
        if (title != null)
            return title;
        else
            return getFileNamePart();
    }

    /**
     * extract file part from url, e.g.:
     * http://www.test.com/file.xml?format=text  --> file.xml
     */
    public String getFileNamePart() {
        Pattern p = Pattern.compile("([^/\\\\]*?)([?].*)?$");
        Matcher m = p.matcher(url);
        if (m.find()) {
            return m.group(1);
        } else {
            System.err.println("Warning: Unexpected URL format: "+url);
            return url;
        }
    }

    /**
     * the name / identifier that should be used to save custom color values
     * and similar stuff to the preference file
     * @return the identifier; never null. Usually the result is "standard"
     */
    public String getPrefName() {
        return name == null ? "standard" : name;
    }

    public boolean isLocal() {
        if (url.startsWith("http://") || url.startsWith("resource://"))
            return false;
        return true;
    }

    public String getLocalSourceDir() {
        if (!isLocal())
            return null;
        File f = new File(url);
        File dir = f.getParentFile();
        if (dir == null)
            return null;
        return dir.getPath();
    }
}
