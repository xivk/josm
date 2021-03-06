// License: GPL. Copyright 2007 by Immanuel Scholz and others
package org.openstreetmap.josm.gui.layer.markerlayer;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Collections;

import javax.swing.JOptionPane;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.gpx.GpxConstants;
import org.openstreetmap.josm.data.gpx.GpxLink;
import org.openstreetmap.josm.data.gpx.WayPoint;
import org.openstreetmap.josm.tools.OpenBrowser;

/**
 * Marker class with Web URL activation.
 *
 * @author Frederik Ramm <frederik@remote.org>
 *
 */
public class WebMarker extends ButtonMarker {

    public final URL webUrl;

    public WebMarker(LatLon ll, URL webUrl, MarkerLayer parentLayer, double time, double offset) {
        super(ll, "web.png", parentLayer, time, offset);
        this.webUrl = webUrl;
    }

    @Override public void actionPerformed(ActionEvent ev) {
        String error = OpenBrowser.displayUrl(webUrl.toString());
        if (error != null) {
            JOptionPane.showMessageDialog(Main.parent,
                    "<html><b>" +
                            tr("There was an error while trying to display the URL for this marker") +
                            "</b><br>" + tr("(URL was: ") + webUrl.toString() + ")" + "<br>" + error,
                            tr("Error displaying URL"), JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public WayPoint convertToWayPoint() {
        WayPoint wpt = super.convertToWayPoint();
        GpxLink link = new GpxLink(webUrl.toString());
        link.type = "web";
        wpt.attr.put(GpxConstants.META_LINKS, Collections.singleton(link));
        return wpt;
    }
}
