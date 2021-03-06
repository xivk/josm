// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.mappaint;

import static org.openstreetmap.josm.tools.Utils.equal;

import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.data.osm.visitor.paint.MapPaintSettings;
import org.openstreetmap.josm.data.osm.visitor.paint.StyledMapRenderer;
import org.openstreetmap.josm.gui.mappaint.MapPaintStyles.IconReference;
import org.openstreetmap.josm.tools.CheckParameterUtil;

public class RepeatImageElemStyle extends ElemStyle implements StyleKeys {

    public enum LineImageAlignment { TOP, CENTER, BOTTOM }

    public MapImage pattern;
    public float offset;
    public float spacing;
    public LineImageAlignment align;

    public RepeatImageElemStyle(Cascade c, MapImage pattern, float offset, float spacing, LineImageAlignment align) {
        super(c, 2.9f);
        CheckParameterUtil.ensureParameterNotNull(pattern);
        CheckParameterUtil.ensureParameterNotNull(align);
        this.pattern = pattern;
        this.offset = offset;
        this.spacing = spacing;
        this.align = align;
    }

    public static RepeatImageElemStyle create(Environment env) {
        // FIXME: make use of NodeElemStyle.createIcon, in order to have
        // -width, -height and -opacity properties.
        Cascade c = env.mc.getCascade(env.layer);

        IconReference iconRef = c.get(REPEAT_IMAGE, null, IconReference.class);
        if (iconRef == null)
            return null;
        MapImage pattern = new MapImage(iconRef.iconName, iconRef.source);
        Float offset = c.get(REPEAT_IMAGE_OFFSET, 0f, Float.class);
        Float spacing = c.get(REPEAT_IMAGE_SPACING, 0f, Float.class);

        LineImageAlignment align = LineImageAlignment.CENTER;
        Keyword alignKW = c.get(REPEAT_IMAGE_ALIGN, Keyword.CENTER, Keyword.class);
        if (equal(alignKW.val, "top")) {
            align = LineImageAlignment.TOP;
        } else if (equal(alignKW.val, "bottom")) {
            align = LineImageAlignment.BOTTOM;
        }

        return new RepeatImageElemStyle(c, pattern, offset, spacing, align);
    }

    @Override
    public void paintPrimitive(OsmPrimitive primitive, MapPaintSettings paintSettings, StyledMapRenderer painter, boolean selected, boolean member) {
        Way w = (Way)primitive;
        painter.drawRepeatImage(w, pattern.getImage(), offset, spacing, align);
    }

    @Override
    public boolean isProperLineStyle() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        if (!super.equals(obj))
            return false;
        final RepeatImageElemStyle other = (RepeatImageElemStyle) obj;
        if (!this.pattern.equals(other.pattern)) return false;
        if (this.offset != other.offset) return false;
        if (this.spacing != other.spacing) return false;
        if (this.align != other.align) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.pattern.hashCode();
        hash = 83 * hash + Float.floatToIntBits(this.offset);
        hash = 83 * hash + Float.floatToIntBits(this.spacing);
        hash = 83 * hash + this.align.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return "RepeatImageStyle{" + super.toString() + "pattern=[" + pattern +
                "], offset=" + offset + ", spacing=" + spacing + ", align=" + align + "}";
    }
}
