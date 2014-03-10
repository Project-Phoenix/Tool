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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Download extends Composite {

    private MyHandler myhandler;

    public Download(Composite parent, int style, MyHandler myhandler) {
        super(parent, 0);
        this.myhandler = myhandler;

    }

    public Shell downloadShell(final Display display, final Shell shell) {

        Image downloadicon = new Image(display, this.getClass().getResourceAsStream("/downloadicon_50x50.png"));
        shell.setImage(downloadicon);

        GridLayout gridlayout = new GridLayout();
        gridlayout.numColumns = 2;

        shell.setSize(300, 300);
        shell.setText("Download");
        myhandler.centerWindow(shell);

        GridData gridData_fill = new GridData();
        gridData_fill.horizontalAlignment = GridData.FILL;
        gridData_fill.horizontalSpan = 2;

        final Tree tree = new Tree(shell, SWT.VIRTUAL | SWT.BORDER | SWT.V_SCROLL);

        tree.setLayout(gridlayout);
        tree.setSize(300, 300);
        myhandler.centerWindow(shell);

        Image ts = new Image(display, this.getClass().getResourceAsStream("/tasksheet.png"));
        Image neg = new Image(display, this.getClass().getResourceAsStream("/nichtabgegeben.png"));
        Image abg = new Image(display, this.getClass().getResourceAsStream("/abgegeben.png"));

        for (int i = 0; i < 3; i++) {

            TreeItem parent = new TreeItem(tree, SWT.CHECK);
            parent.setText("Tasksheet_" + (i + 1));

            parent.setImage(ts);

            for (int j = 0; j < 5; j++) {
                TreeItem subitem = new TreeItem(parent, SWT.CHECK);
                subitem.setText("Task_" + (j + 1));
                if ((j * i + 1) % 3 >= 1) {
                    subitem.setImage(abg);

                } else
                    subitem.setImage(neg);
            }
        }

        shell.pack();
        shell.open();

        return shell;

    }
}