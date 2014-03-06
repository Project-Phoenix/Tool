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

import java.io.File;
import java.sql.Array;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class MyHandler {


    public void getLoginData(){

    }

    public void createloginshell(Shell shell, Display display){
        Login loginWindow = new Login(shell,0, this);
        Shell loginshell= new Shell(SWT.ON_TOP | SWT.CLOSE);
        loginWindow.loginShell(display,loginshell);
       
    }

    public void createTableItem(int counter, Table control, Item item , Button removeB, TableItem[] items, TableEditor editor){
        

        
        removeB.setText("remove");
        editor.grabHorizontal = true;
        editor.grabVertical = true;
        editor.setEditor(removeB, items[counter], 1);
        editor.layout();

        control.redraw();
    }

    public void deleteRow(Table control, TableEditor editor, Button removeB, TableItem item){

        editor.getEditor().dispose();
        editor.dispose();
        item.dispose();
        
//        editor.layout();
//
//        control.layout();
//        
        
    }
    public void getUpload(){

    }
    

    public void getDownloadData(){

    }

    public void centerWindow(Shell shell){
        Rectangle bounds = shell.getDisplay().getBounds();

        Point p = shell.getSize();

        int sWidth = (bounds.width - p.x) / 2;
        int sHeight = (bounds.height - p.y) / 2;

        shell.setBounds(sWidth, sHeight, p.x, p.y);
    }
    
    public void closeWindow(Shell shell){
        shell.getShell().close();
        
    }
}
