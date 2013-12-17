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



import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.win32.OS;
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
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class SWT_App  {

    public static void main( String[] args) {

        Display display = new Display();
        Shell shell = new SWT_App().createShell(display);
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
        //Shell is in the foreground. Better usability for DND
        final Shell shell= new Shell(display, SWT.ON_TOP| SWT.CLOSE);
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
        
        final FileTransfer fileTransfer = FileTransfer.getInstance();
        final Table control = new Table(shell, SWT.FILL);
        control.setHeaderVisible(true);
        
        TableColumn tableColumnLeft = new TableColumn(control, SWT.NONE);
        tableColumnLeft.setWidth(427);
        tableColumnLeft.setText("Drag File To This Place");

        TableColumn tableColumnRight = new TableColumn(control, SWT.NONE);
        tableColumnRight.setWidth(100);  
        
        
        //edit the right column of the table for buttons, which can delete a line

        
        DropTarget targetShell= new DropTarget(control,  DND.DROP_DEFAULT | DND.DROP_COPY |
                DND.DROP_LINK | DND.DROP_MOVE);
        
        targetShell.setTransfer(new Transfer[]{fileTransfer});
        
        targetShell.addDropListener(new DropTargetListener() {
                
            public void dropAccept(DropTargetEvent event) {
                  
            }
            
            public void drop(DropTargetEvent event) {
                
                if(fileTransfer.isSupportedType(event.currentDataType)){
                    String[] files=(String[])event.data;
                    
                    for (int i = 0; i < files.length; i++) {
                        
                        //TODO delete double input

                        new TableItem(control, SWT.NONE);
                        final TableItem[] items = control.getItems();
                        
                        for (int j = 0; j < items.length; j++) {
                            
                        final TableEditor editor= new TableEditor(control);
                        
                        File f=new File(files[i]);
                        Text filename= new Text(control,SWT.NONE);
                        filename.setText(f.getName());
                        editor.grabHorizontal=true;
                        editor.grabVertical= true;
                        editor.setEditor(filename, items[j], 0);
                        editor.layout();
            //--------------------------------------------------------------------
                        //A delete button for each object dragged into the table
                        final TableEditor editor2 = new TableEditor(control);
                        Button removeB= new Button(control, SWT.PUSH);
                        removeB.setText("Remove");                    
                        editor2.grabHorizontal=true;
                        editor2.grabVertical= true;
                        
                        editor2.setEditor(removeB, items[j], 1);
                        editor2.layout();
                        
                        //TODO remove row when klicked
                        removeB.addSelectionListener(new SelectionListener() {
                            
                            public void widgetSelected(SelectionEvent e) {
                                
                                int count= control.getItemCount();
                                System.out.println(count);
                                if(count>0){
                                    System.out.println("so bin drin, lösche jetzt count-1");
                                    
                                    
                                    editor2.getEditor().dispose();
                                    editor2.dispose();
                                    editor.getEditor().dispose();
                                    editor.dispose();
                                    control.remove(control.indexOf(items[count-1]));
                                    control.remove(control.indexOf(items[count-1]));
                                    control.redraw();
                                    int co=control.getItemCount();
                                    System.out.println(co +"thats the truth");
                                    
                                }
                                System.out.println("redraw");
                                
                                
                                
                             
                                    
                            }  
                                
                            
                            
                            public void widgetDefaultSelected(SelectionEvent e) {
                                
                            }
                        });
                        
                        
                        
                  
                        }                      
                        
                    }
                }                  
            }
            //-----------------------------------------------------------------------
            
            public void dragOver(DropTargetEvent event) {
                
                event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
                
            }
            
            public void dragOperationChanged(DropTargetEvent event) {
              
            }
            
            public void dragLeave(DropTargetEvent event) {
                
            }
            
            public void dragEnter(DropTargetEvent event) {
                
                if(event.detail == DND.DROP_DEFAULT){
                    if((event.operations & DND.DROP_LINK) !=0)
                        event.detail=DND.DROP_LINK;
                    else {event.detail=DND.DROP_NONE;}
                }
                
                for (int i = 0; i < event.dataTypes.length; i++) {
                    if(fileTransfer.isSupportedType(event.dataTypes[i])){
                        event.currentDataType= event.dataTypes[i];
                        if(event.detail != DND.DROP_LINK){
                            event.detail = DND.DROP_NONE;
                        }
                        break;
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
}
