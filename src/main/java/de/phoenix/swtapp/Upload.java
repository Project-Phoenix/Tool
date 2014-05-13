/*
 * Copyright (C) 2013 Project-Phoenix
 * 
 * This file is part of Tool.
 * 
 * Tool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * Tool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Tool.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.phoenix.swtapp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.phoenix.util.Configuration;

public class Upload extends Composite {

    private MyHandler myhandler;
    private UploadHandler uploadHandler;
    private Configuration config;
    private String path;

    public Upload(Composite parent, int style, MyHandler myhandler, Configuration config) {
        super(parent, 0);
        this.myhandler = myhandler;
        this.config = config;
        uploadHandler = new UploadHandler();
    }

    public Shell uploadShell(final Display display, final Shell shell) {

        // Constructing a new shell for the upload window
        //
        // ---------------
        // | 1.Tasksheet |
        // | ----- Task1 |
        // | ----- Task2 |
        // | ----- Task3 |
        // | ----- Task4 |
        // | 2.Tasksheet |
        // | ----- Task1 |
        // | ----- Task2 |
        // |-------------|

        path = config.getString("downloadpath");

        Image downloadicon = new Image(display, this.getClass().getResourceAsStream("/uploadicon_50x50.png"));
        shell.setImage(downloadicon);

        GridLayout gridlayout = new GridLayout();
        gridlayout.numColumns = 2;

        shell.setSize(300, 300);
        shell.setText("Upload");
        myhandler.centerWindow(shell);

        GridData gridData_fill = new GridData();
        gridData_fill.horizontalAlignment = GridData.FILL;
        gridData_fill.horizontalSpan = 2;

        // Setting the visualized tree of the data, which the user can select
        // for uploading

        final Tree tree = new Tree(shell, SWT.VIRTUAL | SWT.BORDER | SWT.V_SCROLL);

        tree.setLayout(gridlayout);
        tree.setSize(300, 300);
        myhandler.centerWindow(shell);

        final FileTransfer fileTransfer = FileTransfer.getInstance();
        final TextTransfer textTransfer = TextTransfer.getInstance();



        shell.pack();
        shell.open();

        return shell;

    }
}
