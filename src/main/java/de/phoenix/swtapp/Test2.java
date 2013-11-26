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
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class Test2 extends ByteArrayTransfer {

    public static void main( String[] args) {

        Display display = new Display();
        Shell shell = new Test2().createShell(display);
        shell.setSize(650, 400);
        centerWindow(shell);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();

    }
    
    public Shell createShell(final Display display){
        final Shell shell= new Shell(display);
        shell.setText("Phoenix");
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns=3;
        shell.setLayout(gridLayout);
        
        // Hier noch ein Window Icon einfuegen


        //Platzhalter für die Statistik des Users
        Button placeHolder= new Button(shell, SWT.PUSH);
        placeHolder.setText("PLACEHOLDER");
        GridData gridData=new GridData();
        gridData.horizontalAlignment=GridData.FILL;
        gridData.verticalAlignment=GridData.FILL;
        gridData.grabExcessHorizontalSpace=true;
        gridData.grabExcessVerticalSpace=true;
        gridData.horizontalSpan=3;
        gridData.verticalSpan=2;  
        gridData.verticalIndent=10;
   
        placeHolder.setLayoutData(gridData);
             
        //DRAG and DROP
        Test _instance = new Test();
        final FileTransfer fileTransfer = FileTransfer.getInstance();
        final Table control = new Table(shell, SWT.FILL);
        
        TableItem item = new TableItem(control, SWT.NONE);
        item.setText("Drag data over this site to see the native transfer type.");
        DropTarget target = new DropTarget(control, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_LINK
            | DND.DROP_MOVE);
        target.setTransfer(new Transfer[] { Test.getInstance() });
        target.addDropListener(new DropTargetListener() {
            
            public void dropAccept(DropTargetEvent event) {
                // TODO Auto-generated method stub
                
            }
            
            public void drop(DropTargetEvent event) {
                // TODO Auto-generated method stub
                
            }
            
            public void dragOver(DropTargetEvent event) {
                // TODO Auto-generated method stub
                
            }
            
            public void dragOperationChanged(DropTargetEvent event) {
                // TODO Auto-generated method stub
                
            }
            
            public void dragLeave(DropTargetEvent event) {
                // TODO Auto-generated method stub
                
            }
            
         
          public void dragEnter(DropTargetEvent event) {
            String ops = "";
            if ((event.operations & DND.DROP_COPY) != 0)
              ops += "Copy;";
            if ((event.operations & DND.DROP_MOVE) != 0)
              ops += "Move;";
            if ((event.operations & DND.DROP_LINK) != 0)
              ops += "Link;";
            control.removeAll();
            TableItem item1 = new TableItem(control, SWT.NONE);
            item1.setText("Allowed Operations are " + ops);

            if (event.detail == DND.DROP_DEFAULT) {
              if ((event.operations & DND.DROP_COPY) != 0) {
                event.detail = DND.DROP_COPY;
              } else if ((event.operations & DND.DROP_LINK) != 0) {
                event.detail = DND.DROP_LINK;
              } else if ((event.operations & DND.DROP_MOVE) != 0) {
                event.detail = DND.DROP_MOVE;
              }
            }

          
          }
        });

        
        GridData gridTable= new GridData();
        gridTable.horizontalSpan=2;
        gridTable.verticalSpan=5;
        gridTable.verticalIndent=0;
        gridTable.heightHint=240;
        gridTable.widthHint=510;
        gridTable.horizontalIndent=3;
        gridTable.grabExcessHorizontalSpace=true;
        gridTable.grabExcessVerticalSpace=true;
       
        control.setLayoutData(gridTable);
        
        //Buttons: Download, Upload, Option, Login
        GridData grid= new GridData(SWT.FILL);
        grid.horizontalSpan=1;
        grid.verticalIndent=10;
        grid.heightHint=50;
        grid.widthHint=80;
        
        Button downloadButton = new Button(shell, SWT.PUSH);
        downloadButton.setText("Download");
        downloadButton.setLayoutData(grid);
        
        
        Button uploadButton = new Button(shell, SWT.PUSH);
        uploadButton.setText("Upload");
        uploadButton.setLayoutData(grid);
        
        
        Button optionButton = new Button(shell, SWT.PUSH);
        optionButton.setLayoutData(grid);
        optionButton.setText("Option");
        
        
        Button loginButton = new Button(shell, SWT.PUSH);       
        loginButton.setText("Login");
        loginButton.setLayoutData(grid);
        loginButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
              Login loginWindow= new Login(shell, 0);
              loginWindow.loginShell(display);
              
            }
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
     
        shell.pack();
        
        return shell;
    }


    private static void centerWindow(Shell shell) {

        Rectangle bounds = shell.getDisplay().getBounds();

        Point p = shell.getSize();

        int sWidth = (bounds.width - p.x) / 2;
        int sHeight = (bounds.height - p.y) / 2;

        shell.setBounds(sWidth, sHeight, p.x, p.y);

    }

    @Override
    protected int[] getTypeIds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String[] getTypeNames() {
        // TODO Auto-generated method stub
        return null;
    }
}
