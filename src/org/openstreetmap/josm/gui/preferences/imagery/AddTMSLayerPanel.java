// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.preferences.imagery;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.View;

import org.openstreetmap.josm.data.imagery.ImageryInfo;
import org.openstreetmap.josm.tools.GBC;
import org.openstreetmap.josm.tools.Utils;

public class AddTMSLayerPanel extends AddImageryPanel {

    private final JTextField tmsZoom = new JTextField();
    private final JTextArea tmsUrl = new JTextArea(3, 40);
    private final KeyAdapter keyAdapter = new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            tmsUrl.setText(buildTMSUrl());
        }
    };

    public AddTMSLayerPanel() {

        add(new JLabel(tr("1. Enter URL")), GBC.eol());
        add(new JLabel("<html>" + Utils.joinAsHtmlUnorderedList(Arrays.asList(
                tr("{0} is replaced by tile zoom level, also supported:<br>" +
                        "offsets to the zoom level: {1} or {2}<br>" +
                        "reversed zoom level: {3}", "{zoom}", "{zoom+1}", "{zoom-1}", "{19-zoom}"),
                tr("{0} is replaced by X-coordinate of the tile", "{x}"),
                tr("{0} is replaced by Y-coordinate of the tile", "{y}"),
                tr("{0} is replaced by {1} (Yahoo style Y coordinate)", "{!y}", "2<sup>zoom–1</sup> – 1 – Y"),
                tr("{0} is replaced by {1} (OSGeo Tile Map Service Specification style Y coordinate)", "{-y}", "2<sup>zoom</sup> – 1 – Y"),
                tr("{0} is replaced by a random selection from the given comma separated list, e.g. {1}", "{switch:...}", "{switch:a,b,c}")
        )) + "</html>"), GBC.eol().fill());

        add(rawUrl, GBC.eop().fill());
        rawUrl.setLineWrap(true);
        rawUrl.addKeyListener(keyAdapter);

        add(new JLabel(tr("2. Enter maximum zoom (optional)")), GBC.eol());
        tmsZoom.addKeyListener(keyAdapter);
        add(tmsZoom, GBC.eop().fill());

        add(new JLabel(tr("3. Verify generated TMS URL")), GBC.eol());
        add(tmsUrl, GBC.eop().fill());
        tmsUrl.setLineWrap(true);

        add(new JLabel(tr("4. Enter name for this layer")), GBC.eol());
        add(name, GBC.eop().fill());

        registerValidableComponent(tmsUrl);
    }

    private String buildTMSUrl() {
        StringBuilder a = new StringBuilder("tms");
        String z = sanitize(tmsZoom.getText());
        if (!z.isEmpty()) {
            a.append("[").append(z).append("]");
        }
        a.append(":");
        a.append(getImageryRawUrl());
        return a.toString();
    }

    @Override
    public ImageryInfo getImageryInfo() {
        return new ImageryInfo(getImageryName(), getTmsUrl());
    }

    public static Dimension getPreferredSize(JLabel label, boolean width, int prefSize) {

        View view = (View) label.getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey);
        view.setSize(width ? prefSize : 0, width ? 0 : prefSize);

        return new java.awt.Dimension(
                (int) Math.ceil(view.getPreferredSpan(View.X_AXIS)),
                (int) Math.ceil(view.getPreferredSpan(View.Y_AXIS)));
    }
    
    protected final String getTmsUrl() {
        return sanitize(tmsUrl.getText());
    }

    protected boolean isImageryValid() {
        return !getImageryName().isEmpty() && !getTmsUrl().isEmpty();
    }
}
