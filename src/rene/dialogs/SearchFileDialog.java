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
 
 
 package rene.dialogs;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.Enumeration;

import eric.JEricPanel;

import rene.gui.ButtonAction;
import rene.gui.CheckboxAction;
import rene.gui.CloseDialog;
import rene.gui.Global;
import rene.gui.HistoryTextField;
import rene.gui.MyLabel;
import rene.gui.MyList;
import rene.gui.MyPanel;
import rene.gui.Panel3D;
import rene.util.FileList;

class FileListFinder extends FileList {
	String Res;

	public FileListFinder(final String dir, final String pattern,
			final boolean recurse) {
		super(dir, pattern, recurse);
	}

	@Override
	public boolean file(final File file) {
		try {
			Res = file.getCanonicalPath();
		} catch (final Exception e) {
		}
		return false;
	}

	public String getResult() {
		return Res;
	}
}

class SearchFileThread extends Thread {
	SearchFileDialog D;
	MyList L;
	String Dir, Pattern;
	boolean Recurse;

	public SearchFileThread(final SearchFileDialog dialog, final MyList l,
			final String d, final String p, final boolean r) {
		D = dialog;
		L = l;
		Dir = d;
		Pattern = p;
		Recurse = r;
	}

	@Override
	public void run() {
		D.enableButtons(false);
		L.removeAll();
		L.setEnabled(false);
		final FileList f = new FileList(Dir, Pattern, Recurse);
		D.F = f;
		f.search();
		f.sort();
		final Enumeration e = f.files();
		while (e.hasMoreElements()) {
			try {
				L.add(((File) e.nextElement()).getCanonicalPath());
			} catch (final Exception ex) {
			}
		}
		L.setEnabled(true);
		D.enableButtons(true);
	}
}

/**
 * This is a dialog to search a subtree for a specific file. The user can enter
 * a directory and a file pattern containing * and ?. He can choose between
 * immediate search and open, or search/select/open. Abort will result in an
 * empty string. The calling routine checks the result file name with
 * getResult().
 * <p>
 * You need to specify the following properties
 * 
 * <pre>
 * searchfile.title=Search File
 * searchfile.directory=Directory
 * searchfile.pattern=Pattern
 * searchfile.search=Search
 * searchfile.searchrek=Search Subdirectories
 * </pre>
 */

public class SearchFileDialog extends CloseDialog implements Runnable,
Enumeration {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HistoryTextField Dir, Pattern;
	MyList L;
	static public int ListNumber = Global.getParameter("searchfile.number", 10);
	String Result = null;
	Button ActionButton, CloseButton, SearchButton, SearchrekButton;
	public FileList F = null;
	public boolean Abort = true;
	Checkbox Mod;

	public SearchFileDialog(final Frame f, final String action,
			final String modify, final boolean modifystate) {
		super(f, Global.name("searchfile.title"), true);
		setLayout(new BorderLayout());
		final JEricPanel north = new MyPanel();
		north.setLayout(new BorderLayout());
		final JEricPanel northa = new MyPanel();
		northa.setLayout(new BorderLayout());
		final JEricPanel north1 = new MyPanel();
		north1.setLayout(new GridLayout(0, 2));
		north1.add(new MyLabel(Global.name("searchfile.directory")));
		north1.add(Dir = new HistoryTextField(this, "Dir", 20));
		Dir.setText(".");
		north1.add(new MyLabel(Global.name("searchfile.pattern")));
		north1.add(Pattern = new HistoryTextField(this, "TextAction", 20));
		northa.add("Center", north1);
		final JEricPanel north2 = new MyPanel();
//		north2.add(SearchButton = new ButtonAction(this, Global
//				.name("searchfile.search"), "Search"));
//		north2.add(SearchrekButton = new ButtonAction(this, Global
//				.name("searchfile.searchrek"), "SearchRek"));
		northa.add("South", north2);
		north.add("North", northa);
		add("North", new Panel3D(north));
		add("Center", new Panel3D(L = new MyList(ListNumber)));
		L.addActionListener(this);
		L.setMultipleMode(true);
		final JEricPanel south = new MyPanel();
		south.setLayout(new FlowLayout(FlowLayout.RIGHT));
		if (!modify.equals("")) {
			south.add(Mod = new CheckboxAction(this, modify, ""));
			Mod.setState(modifystate);
		}
//		south.add(ActionButton = new ButtonAction(this, action, "Action"));
//		south.add(CloseButton = new ButtonAction(this, Global.name("abort"),
//		"Close"));
		add("South", new Panel3D(south));
		pack();
		Dir.loadHistory("searchfile.dir");
		Pattern.loadHistory("searchfile.pattern");

		// size
		setSize("searchfiledialog");
		addKeyListener(this);
		Dir.addKeyListener(this);
		Pattern.addKeyListener(this);
	}

	public SearchFileDialog(final Frame f, final String action) {
		this(f, action, "", false);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == L) {
			action();
		} else
			super.actionPerformed(e);
	}

	@Override
	public void doAction(final String o) {
		Result = null;
		if (o.equals("SearchRek"))
			search(true);
		else if (o.equals("Search"))
			search(false);
		else if (o.equals("TextAction")) {
			L.removeAll();
			action();
		} else if (o.equals("Action"))
			action();
		else if (o.equals("Help"))
			help();
		else if (o.equals("Close")) {
			Abort = true;
			doclose();
		}
	}

	public void help() {
	}

	Thread Run;

	public void search(final boolean recurse) {
		saveHistory();
		if (Run != null && Run.isAlive())
			return;
		Run = new SearchFileThread(this, L, Dir.getText(), Pattern.getText(),
				recurse);
		Run.start();
	}

	public void action() {
		saveHistory();
		if (Run != null && Run.isAlive())
			return;
		Run = new Thread(this);
		Run.start();
	}

	public void enableButtons(final boolean f) {
		Pattern.setEnabled(f);
		SearchButton.setEnabled(f);
		SearchrekButton.setEnabled(f);
		ActionButton.setEnabled(f);
	}

	public void run() {
		Result = null;
		enableButtons(false);
		if (L.getItemCount() > 0) {
			final int i = L.getSelectedIndex();
			if (i > 0)
				Result = L.getItem(i);
			else
				Result = L.getItem(0);
		} else {
			final FileListFinder f = new FileListFinder(Dir.getText(), Pattern
					.getText(), true);
			F = f;
			f.search();
			Result = f.getResult();
		}
		enableButtons(true);
		Abort = false;
		doclose();
	}

	public String getResult() {
		return Result;
	}

	@Override
	public void focusGained(final FocusEvent e) {
		Pattern.requestFocus();
	}

	@Override
	public void setVisible(final boolean flag) {
		if (flag)
			enableButtons(true);
		super.setVisible(flag);
	}

	@Override
	public boolean close() {
		Abort = true;
		return true;
	}

	@Override
	public void doclose() {
		if (F != null)
			F.stopIt();
		Dir.saveHistory("searchfile.dir");
		Pattern.saveHistory("searchfile.pattern");
		noteSize("searchfiledialog");
		super.doclose();
	}

	public void saveHistory() {
		Dir.remember();
		Pattern.remember();
	}

	public void setPattern(final String s) {
		Pattern.setText(s);
	}

	String S[];
	int Sn;

	/**
	 * Get an enumeration of selected files. Should check for an aborted dialog
	 * before.
	 */
	public Enumeration getFiles() {
		S = L.getSelectedItems();
		Sn = 0;
		return this;
	}

	public boolean hasMoreElements() {
		return Sn < S.length;
	}

	public Object nextElement() {
		if (Sn >= S.length)
			return null;
		final String s = S[Sn];
		Sn++;
		return s;
	}

	@Override
	public boolean isAborted() {
		return Abort;
	}

	public void deselectAll() {
		for (int i = L.getItemCount() - 1; i >= 0; i--) {
			L.deselect(i);
		}
	}

	public boolean isModified() {
		return Mod.getState();
	}
}
