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

public class MyHandler {
    private LoginHandler loginhandler;
    private String path;
    
    public MyHandler() {
//        this.loginhandler=loginhandler;
    }

    public void getLoginData() {

    }

    public void createloginshell(Shell shell, Display display) {
        Login loginWindow = new Login(shell, 0, this, loginhandler);
        Shell loginshell = new Shell(SWT.ON_TOP | SWT.CLOSE);
        loginWindow.loginShell(display, loginshell);

    }

    public void creatdownloadshell(Shell shell, Display display) {
        Download downloadWindow = new Download(shell, 0, this);
        Shell downloadShell = new Shell(SWT.ON_TOP | SWT.CLOSE);
        downloadWindow.downloadShell(display, downloadShell);
    }
    public void createTableItem(Table control, Item item, Button removeB, TableItem[] items, TableEditor editor) {

        removeB.setText("Remove");
        editor.grabHorizontal = true;
        editor.grabVertical = true;
        editor.setEditor(removeB, items[items.length - 1], 1);
    }

    public void deleteRow(Table control, TableEditor editor, Button removeB, TableItem item, TableItem[] items) {

        removeB.dispose();
        editor.getEditor().dispose();
        editor.getItem().dispose();
        editor.dispose();
        // refreshing right column
        control.getColumn(1).pack();
        control.getColumn(1).setWidth(100);

    }

    public void getUpload() {

    }

    public void getDownloadData() {

    }

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

    public void checkpath(Text text, Shell shell) {
        if(!text.getText().isEmpty()){
            path = text.getText().replace('\\', '/');
            System.out.println(path);
            
            closeWindow(shell);
        }
        
        
    }

    
}
