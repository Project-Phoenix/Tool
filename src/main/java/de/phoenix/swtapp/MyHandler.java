/*
 * Copyright (C) 2014 Project-Phoenix
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import de.phoenix.util.Configuration;

public class MyHandler {
    //
    private LoginHandler loginhandler;
    @SuppressWarnings("unused")
    private String path;

    // This handlerclass sepperate the GUI components from the logical
    // functions.
    public MyHandler() {
//        this.loginhandler=loginhandler;
    }

    public void getLoginData() {

    }

    // This will create the window, where the user can log in. This is
    // necessary, because the user requires permissions for downloading and
    // uploading files.
    public void createloginshell(Shell shell, Display display) {
        Login loginWindow = new Login(shell, 0, this, loginhandler);
        Shell loginshell = new Shell(SWT.ON_TOP | SWT.CLOSE);
        loginWindow.loginShell(display, loginshell);

    }

    // This will create the window, which will show the downloadable files
    public void creatdownloadshell(Shell shell, Display display, Configuration config) {
        Download downloadWindow = new Download(shell, 0, this, config);
        Shell downloadShell = new Shell(SWT.ON_TOP | SWT.CLOSE);
        downloadWindow.downloadShell(display, downloadShell);
    }

    // Each file dropped into the DnD Box will create a new tableitem into the
    // table
    public void createTableItem(Table control, Item item, Button removeB, TableItem[] items, TableEditor editor) {

        removeB.setText("Remove");
        editor.grabHorizontal = true;
        editor.grabVertical = true;
        editor.setEditor(removeB, items[items.length - 1], 1);
    }

    // The user can delete specific rows by clicking on the "remove" button.
    public void deleteRow(Table control, TableEditor editor, Button removeB, TableItem item, TableItem[] items) {

        removeB.dispose();
        editor.getEditor().dispose();
        editor.getItem().dispose();
        editor.dispose();
        // Refreshing right column for updating the data in the table
        control.getColumn(1).pack();
        control.getColumn(1).setWidth(100);

    }

    public void getUpload() {

    }

    public void getDownloadData() {

    }

// This method set the window to the center of the display
    public void centerWindow(Shell shell) {
        Rectangle bounds = shell.getDisplay().getBounds();

        Point p = shell.getSize();

        int sWidth = (bounds.width - p.x) / 2;
        int sHeight = (bounds.height - p.y) / 2;

        shell.setBounds(sWidth, sHeight, p.x, p.y);
    }

    public void closeWindow(Shell shell) {
        shell.getShell().close();

    }

    public void createdirectionaryshell() {

    }

    // Saving the pathfile, which the user chose. If no path was selected the
    // window will not close.
    public void checkpath(Text text, Shell shell) {
        if (!text.getText().isEmpty() && !text.getText().matches("Please enter your downloadpath")) {
            // We could need this function when we start adding logical
            // functions
            path = text.getText().replace('\\', '/');
            closeWindow(shell);
        }

    }

    public void createuploadshell(Shell shell, Display display, Configuration config) {
        Upload uploadWindow = new Upload(shell, 0, this, config);
        Shell uploadShell = new Shell(SWT.ON_TOP|SWT.CLOSE);
        uploadWindow.uploadShell(display, uploadShell);
        
    }

}
