/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 */
package ui.net.java.dev.colorchooser;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/**
 * Default UI delegate for color choosers. CCBorder handles minor per-look and
 * feel differences so we can use one delegate. Note this delegate is stateless
 * - there is one instance system-wide. State is kept by the listener - see
 * inner class L.
 * 
 * @author Tim Boudreau
 */
final class DefaultColorChooserUI extends ColorChooserUI {
	DefaultColorChooserUI() {
	}

	private static DefaultColorChooserUI INSTANCE = null;

	public static ComponentUI createUI(final JComponent jc) {
		// assert jc instanceof ColorChooser;
		return getDefault();
	}

	static DefaultColorChooserUI getDefault() {
		if (INSTANCE == null) {
			INSTANCE = new DefaultColorChooserUI();
		}
		return INSTANCE;
	}

	@Override
	protected void init(final ColorChooser c) {
		c.setToolTipText(getDefaultTooltip());
		c.setBorder(new CCBorder());
	}

	@Override
	protected void uninit(final ColorChooser c) {
		if (c.getBorder() instanceof CCBorder) {
			c.setBorder(null);
		}
		if (getDefaultTooltip().equals(c.getToolTipText())) {
			c.setToolTipText(null);
		}
	}

	private static String getDefaultTooltip() {
		return MAC ? ColorChooser.getString("tip.mac") : // NOI18N
			ColorChooser.getString("tip"); // NOI18N
	}

	@Override
	public void paint(final Graphics g, final JComponent c) {
		final ColorChooser chooser = (ColorChooser) c;
		final Color col = chooser.transientColor() != null ? chooser
				.transientColor() : chooser.getColor();

				g.setColor(col);
				g.fillRect(0, 0, chooser.getWidth() - 1, chooser.getHeight() - 1);
				if (chooser.hasFocus()) {
					g.setColor(invertColor(col));
					g.drawRect(4, 4, chooser.getWidth() - 8, chooser.getHeight() - 8);
				}
	}

	// *****************Some utility methods for manipulating colors***********
	/** Finds a color that will visually contrast with the selected color */
	private static final Color invertColor(final Color c) {
		final int r = checkRange(255 - c.getRed());
		final int g = checkRange(255 - c.getGreen());
		final int b = checkRange(255 - c.getBlue());
		return new Color(r, g, b);
	}

	/**
	 * Checks to make sure the color component passed is not too close to
	 * middle-of-the-road, and if so, returns its difference with 128. Used by
	 * invertColor to make sure that it doesn't, for example, return 129,129,129
	 * as a color to contrast with 127,127,127.
	 */
	private static final int checkRange(final int i) {
		int result = i;
		if (Math.abs(128 - i) < 24) {
			result = Math.abs(128 - i);
		}
		return result;
	}
}
