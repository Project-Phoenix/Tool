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
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class SWT_App {
    private MyHandler myhandler;
    

    public SWT_App(){
        
       myhandler= new MyHandler();
       
    }

    public static void main(String[] args) {

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

    public Shell createShell(final Display display) {
        // Shell is in the foreground. Better usability for DND
        final Shell shell = new Shell(display, SWT.ON_TOP | SWT.CLOSE);
        shell.setText("Phoenix");
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;
        shell.setLayout(gridLayout);

        // Add little programicon

        Image mainicon = new Image(display, this.getClass().getResourceAsStream("/icon_50x50.png"));
        shell.setImage(mainicon);

        // placeholder for the user statistic
        Button placeHolder = new Button(shell, SWT.PUSH);
        placeHolder.setText("PLACEHOLDER");
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 3;
        gridData.verticalSpan = 2;
        gridData.verticalIndent = 10;

        placeHolder.setLayoutData(gridData);

        // DROPTarget for the GUI // Transfer to handler class

       
        final Table control = new Table(shell, SWT.FILL);
        control.setHeaderVisible(true);

        final TableColumn tableColumnLeft = new TableColumn(control, SWT.NONE);
        tableColumnLeft.setWidth(427);
        tableColumnLeft.setText("Drag File To This Place");

        TableColumn tableColumnRight = new TableColumn(control, SWT.NONE);
        tableColumnRight.setWidth(100);

        // edit the right column of the table for buttons, which can delete a
        // line
        
        final FileTransfer fileTransfer = FileTransfer.getInstance();

        DropTarget targetShell = new DropTarget(control, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);

        targetShell.setTransfer(new Transfer[]{fileTransfer});

        targetShell.addDropListener(new DropTargetListener() {

            public void dropAccept(DropTargetEvent event) {

            }

            public void drop(DropTargetEvent event) {

                if (fileTransfer.isSupportedType(event.currentDataType)) {
                    final String[] files = (String[]) event.data;

                    new TableItem(control, SWT.NONE);
                    final TableItem[] items = control.getItems();

//Testing TableItem
                    System.out.println(items.length + "itemslength");

                    File f = new File(files[0]);
                    Text filename = new Text(control, SWT.NONE);
                    filename.setText(f.getName());
                    System.out.println(f.getName() + "name");

                    final TableEditor editor = new TableEditor(control);

                    editor.grabHorizontal = true;
                    editor.grabVertical = true;
                    editor.setEditor(filename, items[items.length - 1], 0);
                    editor.layout();
                 
                    // --------------------------------------------------------------------
                    // A delete button for each object dragged into the table
                    final TableEditor editor2 = new TableEditor(control);
                    
                    Button removeB = new Button(control, SWT.PUSH);
                    removeB.setText("Remove");
                    editor2.grabHorizontal = true;
                    editor2.grabVertical = true;
                    editor2.setEditor(removeB, items[items.length - 1], 1);
                    editor2.layout();
                    control.redraw();

                    // TODO remove row when klicked

                    removeB.addSelectionListener(new SelectionListener() {

                        // This function has some bugs when deleting row
                        // -------------------------------------------
                        public void widgetSelected(SelectionEvent e) {

                            control.remove(control.getSelectionIndex() + 1);

                            editor.getEditor().dispose();
                            editor2.getEditor().dispose();

                            editor.dispose();
                            editor2.dispose();

                            editor.layout();
                            editor2.layout();
                        }

                        public void widgetDefaultSelected(SelectionEvent e) {

                        }
                    });

                }

            }
            // -----------------------------------------------------------------------

            public void dragOver(DropTargetEvent event) {

                event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;

            }

            public void dragOperationChanged(DropTargetEvent event) {

            }

            public void dragLeave(DropTargetEvent event) {

            }

            public void dragEnter(DropTargetEvent event) {

                if (event.detail == DND.DROP_DEFAULT) {
                    if ((event.operations & DND.DROP_COPY) != 0)
                        event.detail = DND.DROP_COPY;
                    else {
                        event.detail = DND.DROP_NONE;
                    }
                }

                for (int i = 0; i < event.dataTypes.length; i++) {
                    if (fileTransfer.isSupportedType(event.dataTypes[i])) {
                        event.currentDataType = event.dataTypes[i];
                        if (event.detail != DND.DROP_COPY) {
                            event.detail = DND.DROP_NONE;
                        }
                        break;
                    }
                }
            }

        });
        
        // Setting the Drag and Drop Box as a new Dragsource
        
//        DragSource ddbox= new DragSource(control, DND.DROP_TARGET_MOVE | DND.DROP_COPY);
//        final FileTransfer fileTransferDL = FileTransfer.getInstance();
//        
//        ddbox.addDragListener(new DragSourceListener() {
//            
//            public void dragStart(DragSourceEvent arg0) {
//                // TODO Auto-generated method stub
//                
//            }
//            
//            public void dragSetData(DragSourceEvent event) {
//                if(fileTransfer.getInstance().isSupportedType(event.dataType));
//                event.data= control.getData();
//            }
//            
//            public void dragFinished(DragSourceEvent event) {
//              
//                
//            }
//        });

        GridData gridTable = new GridData();
        gridTable.horizontalSpan = 2;
        gridTable.verticalSpan = 5;
        gridTable.verticalIndent = 0;
        gridTable.heightHint = 240;
        gridTable.widthHint = 510;
        gridTable.horizontalIndent = 3;
        gridTable.grabExcessHorizontalSpace = true;
        gridTable.grabExcessVerticalSpace = true;

        control.setLayoutData(gridTable);

        // Buttons: Download, Upload, Option, Login
        GridData grid = new GridData(SWT.FILL);
        grid.horizontalSpan = 1;
        grid.verticalIndent = 10;
        grid.heightHint = 50;
        grid.widthHint = 80;

        Button downloadButton = new Button(shell, SWT.PUSH);
        downloadButton.setText("Download");
        downloadButton.setLayoutData(grid);
        
        downloadButton.addSelectionListener(new SelectionListener() {
            
            public void widgetSelected(SelectionEvent arg0) {
                // TODO Auto-generated method stub
                
            }
            
            public void widgetDefaultSelected(SelectionEvent arg0) {
                // TODO Auto-generated method stub
                
            }
        });

        Button uploadButton = new Button(shell, SWT.PUSH);
        uploadButton.setText("Upload");
        uploadButton.setLayoutData(grid);

        // uploading items in the DDBox
        uploadButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {

            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });

        Button optionButton = new Button(shell, SWT.PUSH);
        optionButton.setLayoutData(grid);
        optionButton.setText("Option");

        Button loginButton = new Button(shell, SWT.PUSH);
        loginButton.setText("Login");
        loginButton.setLayoutData(grid);
        loginButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                
                myhandler.createloginshell(shell,display);

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
