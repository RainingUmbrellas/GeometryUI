/* 

Copyright 2006 Rene Grothmann, modified by Eric Hakenholz

This file is part of C.a.R. software.

C.a.R. is a free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, version 3 of the License.

C.a.R. is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */
package rene.zirkel.tools;

// file: MetaMover.java

import java.util.ArrayList;
import java.util.Enumeration;

import eric.GUI.palette.PaletteManager;
import eric.bar.JPropertiesBar;
import java.awt.Cursor;
import java.awt.event.MouseEvent;

import rene.gui.Global;
import rene.zirkel.ZirkelCanvas;
import rene.zirkel.constructors.ObjectConstructor;
import rene.zirkel.objects.AreaObject;
import rene.zirkel.objects.ConstructionObject;
import rene.zirkel.objects.FixedAngleObject;
import rene.zirkel.objects.FixedCircleObject;
import rene.zirkel.objects.MoveableObject;
import rene.zirkel.objects.PointObject;

public class MetaMover extends MoverTool {

    ObjectConstructor OC;
    ConstructionObject PP;

    public MetaMover(final ObjectConstructor oc, final ZirkelCanvas zc,
            final ConstructionObject p, final MouseEvent e) {
        
        OC=oc;
        PP=P=p;
        oc.pause(true);
        if (P!=null) {
            P.setSelected(true);
            ShowsValue=P.showValue();
            ShowsName=P.showName();
            zc.repaint();
            showStatus(zc);
            zc.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            if (P instanceof MoveableObject) {
                ((MoveableObject) P).startDrag(zc.x(e.getX()), zc.y(e.getY()));
            }
            if (ZCG!=null) {
                ZCG.dispose();
                ZCG=null;
            }
            ZCG=zc.getGraphics();
            if ((P instanceof PointObject)&&(P instanceof MoveableObject)) {
                    zc.prepareDragActionScripts(P);
            }
        }
    }

    @Override
    public void mouseReleased(final MouseEvent e, final ZirkelCanvas zc) {
        if (P==null) {
            return;
        }
        if (ZCG!=null) {
            ZCG.dispose();
            ZCG=null;
        }
        zc.getConstruction().haveChanged();
        JPropertiesBar.RefreshBar();
        zc.setCursor(Cursor.getDefaultCursor());
        P.setSelected(false);
        P.setShowValue(ShowsValue);
        P.setShowName(ShowsName);
        zc.validate();
        if (Grab) {
            zc.grab(false);
            Grab=false;
        }
        if (ChangedDrawable) {
            if (P instanceof FixedCircleObject) {
                ((FixedCircleObject) P).setDragable(WasDrawable);
            } else if (P instanceof FixedAngleObject) {
                ((FixedAngleObject) P).setDragable(WasDrawable);
            }
        }
        zc.stopDragAction();
        zc.clearSelected();
        zc.repaint();
        P=null;
        V=null;
        Selected=false;
        zc.setTool(OC);
        OC.pause(false);
        zc.validate();
        zc.repaint();
    }
}
