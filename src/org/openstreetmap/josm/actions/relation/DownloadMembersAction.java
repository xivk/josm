// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.actions.relation;

import static org.openstreetmap.josm.gui.help.HelpUtil.ht;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.dialogs.relation.DownloadRelationTask;
import org.openstreetmap.josm.tools.ImageProvider;

/**
 * The action for downloading members of relations

 */
public class DownloadMembersAction extends AbstractRelationAction {

    /**
     * Constructs a new <code>DownloadMembersAction</code>.
     */
    public DownloadMembersAction() {
        putValue(SHORT_DESCRIPTION, tr("Download all members of the selected relations"));
        putValue(NAME, tr("Download members"));
        putValue(SMALL_ICON, ImageProvider.get("dialogs", "downloadincomplete"));
        putValue("help", ht("/Dialog/RelationList#DownloadMembers"));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isEnabled() || relations.isEmpty() || Main.map==null || Main.map.mapView==null) return;
        Main.worker.submit(new DownloadRelationTask(relations, Main.map.mapView.getEditLayer()));
    }
}
