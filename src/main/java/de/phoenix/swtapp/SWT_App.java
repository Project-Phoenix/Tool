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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.util.Configuration;
import de.phoenix.util.JSONConfiguration;

public class SWT_App {
    private static MyHandler myhandler;
    private static Display display;
    private static Shell shell;
    private static CdirectionThread thread;
    private static CdirectionThread2 thread2;
    private static boolean dWindow;
    private static boolean showPathInOpt;
    private static Configuration config;
    private DownloadHandler downloadHandler;
    private UploadHandler uploadHandler;

    public SWT_App() {

        myhandler = new MyHandler();
        downloadHandler = new DownloadHandler();
        uploadHandler = new UploadHandler();
        display = new Display();
        shell = new Shell(SWT.ON_TOP | SWT.CLOSE);
        shell = createShell(display, shell);
        shell.setSize(650, 400);
        myhandler.centerWindow(shell);
    }

    public static void main(String[] args) {
        new SWT_App();

        try {

            config = new JSONConfiguration("config.json");
            // If a config does not exists the user have to go through "the
            // first time using GUI"progress, where the user should select a
            // downloadpath. After that the user will be redirect to the main
            // window.
            if (!config.exists("downloadpath")) {
                dWindow = true;
                thread = new CdirectionThread(showPathInOpt, config);
                thread.start();

                while (true) {
                    if (display.isDisposed()) {
                        break;
                    }
                    if (!thread.isAlive()) {

                        shell.open();
                        while (!shell.isDisposed()) {
                            if (!display.readAndDispatch())
                                display.sleep();

                        }

                        display.dispose();
                    }
                }
            }

            else {
                @SuppressWarnings("unused")
                String path = config.getString("downloadpath");
                showPathInOpt = true;
            }
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!dWindow) {
            shell.open();
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch())
                    display.sleep();
            }
            display.dispose();
        }
    }

    public Shell createShell(final Display display, final Shell shell) {

        // Constructing a new shell for the main window
        //
        // -----------------------------------------------
        // | Userstatistic |
        // |---------------------------------------------|
        // | |
        // |---------------------------------------------|
        // | |Download |
        // | |-------------|
        // | |Upload |
        // | DNDBox |-------------|
        // | |Option |
        // | |-------------|
        // | |Login |
        // |-------------------------------|-------------|

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 3;

        shell.setText("Phoenix");
        shell.setLayout(gridLayout);

        // Setting our teamlogo as icon
        Image mainicon = new Image(display, this.getClass().getResourceAsStream("/icon_50x50.png"));
        shell.setImage(mainicon);

        // Placeholderbutton for the userstatistic
        Label placeHolder = new Label(shell, SWT.BORDER);

        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 2;
        gridData.verticalSpan = 2;
        gridData.verticalIndent = 10;

//        placeHolder.setText("Phoenixtool 2014." + "\n" + "Created by Phoenix in Association with Fakultät für Informatik.");
        placeHolder.setLayoutData(gridData);
        Image bannericon = new Image(display, this.getClass().getResourceAsStream("/phoenixbanner.png"));
        
        placeHolder.setImage(bannericon);

        GridData gridDataUpload = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalSpan = 2;
        gridData.verticalSpan = 2;
        gridData.verticalIndent = 10;

        Label chooseTask = new Label(shell, SWT.BORDER_SOLID);
        chooseTask.setText("Upload homework for");
        chooseTask.setLayoutData(gridDataUpload);

        final List<PhoenixTaskSheet> taskSheets = downloadHandler.showAllTaskSheets();
        final List<String> chooseTSItems = new ArrayList<String>();
        final Combo combo = new Combo(shell, SWT.DROP_DOWN);
        combo.setLayoutData(gridDataUpload);
        

        if (taskSheets.isEmpty()) {
            MessageBox msg = new MessageBox(shell);
            msg.setMessage("Sorry, there are no tasksheets available");
            msg.open();

            return null;
        } else {
            for (int i = 0; i < taskSheets.size(); i++) {

                for (int j = 0; j < taskSheets.get(i).getTasks().size(); j++) {
                    chooseTSItems.add(j, (((taskSheets.get(i).getTasks().get(j).getTitle()))));
                }
            }
            combo.setItems((String[]) chooseTSItems.toArray(new String[chooseTSItems.size()]));
        }

        // DROPtarget for the GUI
        final Table control = new Table(shell, SWT.FILL);
        control.setHeaderVisible(true);
        control.setLinesVisible(true);

        // Dividing the table into two columns. On the left part the table
        // "control" contains the dropped filenames. On the right part the
        // related button, which will delete on click the row in the table (or
        // tableitem)
        final TableColumn tableColumnLeft = new TableColumn(control, SWT.NONE);
        tableColumnLeft.setWidth(310);
        tableColumnLeft.setText("Drag File To This Place");

        final TableColumn tableColumnRight = new TableColumn(control, SWT.NONE);
        tableColumnRight.setWidth(85);

        final FileTransfer fileTransfer = FileTransfer.getInstance();

        // Creating a DropTarget, where files can be dropped in that area.
        DropTarget targetShell = new DropTarget(control, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE);

        targetShell.setTransfer(new Transfer[]{fileTransfer});

        targetShell.addDropListener(new DropTargetListener() {

            public void dropAccept(DropTargetEvent event) {

            }

            public void drop(DropTargetEvent event) {

                if (fileTransfer.isSupportedType(event.currentDataType)) {

                    final String[] files = (String[]) event.data;

                    final Button removeB = new Button(control, SWT.PUSH);
                    final TableEditor editor = new TableEditor(control);

                    final TableItem item = new TableItem(control, SWT.NONE);
                    final TableItem[] items = control.getItems();

//                    File f = new File(files[0]);
//                    item.setText(f.getName());
                    item.setData(files[0]);
                    item.setText(files[0]);
                    removeB.setData(item);

                    myhandler.createTableItem(control, item, removeB, items, editor);

                    // Remove row when button klicked
                    removeB.addSelectionListener(new SelectionListener() {

                        public void widgetSelected(SelectionEvent arg0) {
                            myhandler.deleteRow(control, editor, removeB, item, items);

                        }

                        public void widgetDefaultSelected(SelectionEvent arg0) {
                            // TODO Auto-generated method stub

                        }
                    });
                }
            }

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

        DragSource source = new DragSource(control, DND.DROP_TARGET_MOVE | DND.DROP_COPY);
        source.setTransfer(new Transfer[]{TextTransfer.getInstance(), FileTransfer.getInstance()});
        source.addDragListener(new DragSourceAdapter() {
            @Override
            public void dragSetData(DragSourceEvent event) {
                if (FileTransfer.getInstance().isSupportedType(event.dataType)) {
                    TableItem[] selection = control.getSelection();
                    File file = new File(selection[0].getText());
                    event.data = new String[]{file.getAbsolutePath()};
                }
                if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
                    event.data = "";
                }
            }
        });

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

        // Buttons: download, upload, option, login
        GridData grid = new GridData(SWT.FILL);
        grid.horizontalSpan = 1;
        grid.verticalIndent = 10;
        grid.heightHint = 50;
        grid.widthHint = 230;

        Button downloadButton = new Button(shell, SWT.PUSH);
        downloadButton.setText("Download");
        downloadButton.setLayoutData(grid);
        downloadButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent arg0) {
                myhandler.creatdownloadshell(shell, display, config);

            }

            public void widgetDefaultSelected(SelectionEvent arg0) {
                // TODO Auto-generated method stub

            }
        });

        Button uploadButton = new Button(shell, SWT.PUSH);
        uploadButton.setText("Upload");
        uploadButton.setLayoutData(grid);
        uploadButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                uploadHandler.prepare4Upload(control,combo);              
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });

        Button optionButton = new Button(shell, SWT.PUSH);
        optionButton.setLayoutData(grid);
        optionButton.setText("Option");
        optionButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                thread2 = new CdirectionThread2(showPathInOpt, config);
                thread2.start();

            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });

        Button loginButton = new Button(shell, SWT.PUSH);
        loginButton.setText("Login");
        loginButton.setLayoutData(grid);
        loginButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {

                myhandler.createloginshell(shell, display);

            }
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });

        shell.pack();

        return shell;
    }

}
