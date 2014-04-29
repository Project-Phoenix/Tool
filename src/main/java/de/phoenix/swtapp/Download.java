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
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import sun.security.krb5.Config;

import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.util.Configuration;

public class Download extends Composite {

    private MyHandler myhandler;
    private DownloadHandler downloadHandler;
    private Configuration config;
    private String path;

    public Download(Composite parent, int style, MyHandler myhandler, Configuration config) {
        super(parent, 0);
        this.myhandler = myhandler;
        this.config = config;
        downloadHandler = new DownloadHandler();

    }

    public Shell downloadShell(final Display display, final Shell shell) {

        // Constructing a new shell for the download window
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

        // Setting the visualized tree of the data, which the user can select
        // and download

        final Tree tree = new Tree(shell, SWT.VIRTUAL | SWT.BORDER | SWT.V_SCROLL);

        tree.setLayout(gridlayout);
        tree.setSize(300, 300);
        myhandler.centerWindow(shell);

        final List<PhoenixTaskSheet> taskSheets = downloadHandler.showAllTaskSheets();

//        int k = 0;
        if (taskSheets.isEmpty()) {
            MessageBox msg = new MessageBox(shell);
            msg.setMessage("Sorry, there are no tasksheets available");
            msg.open();

            return null;
        } else {
            for (int i = 0; i < taskSheets.size(); i++) {
                TreeItem parent = new TreeItem(tree, SWT.CHECK);
                parent.setText(taskSheets.get(i).getTitle());
//                k++;
                for (int j = 0; j < taskSheets.get(i).getTasks().size(); j++) {

                    TreeItem subitem = new TreeItem(parent, SWT.CHECK);
                    subitem.setText(taskSheets.get(i).getTasks().get(j).getTitle());
//                    k++;
                }
            }
            
            //TODO: Bug beheben
            tree.addSelectionListener(new SelectionListener() {
                
                public void widgetSelected(SelectionEvent e) {
                    // TODO Auto-generated method stub
                    
                }
                
                public void widgetDefaultSelected(SelectionEvent e) {
                    // TODO Auto-generated method stub
                    
                }
            });

            DragSource downloadW = new DragSource(tree, DND.DROP_TARGET_MOVE | DND.DROP_COPY);
            downloadW.setTransfer(new Transfer[]{TextTransfer.getInstance(), FileTransfer.getInstance()});
            
            downloadW.addDragListener(new DragSourceAdapter() {
                @Override
                public void dragSetData(DragSourceEvent event) {
                    if (FileTransfer.getInstance().isSupportedType(event.dataType)) {
                        TreeItem[] selection = tree.getSelection();

                        // if parent or subitem
                        File file = new File(path);
                        

                        int tempTaskSheet = 0;
                        for (int i = 0; i < taskSheets.size(); i++) {
                            
                            for (int j = 0; j < taskSheets.get(i).getTasks().size(); j++) {

                                if (taskSheets.get(i).getTasks().get(j).getTitle().equals(selection[0].getText())) {
                                    System.out.println(taskSheets.get(i).getTasks().get(j).getTitle());
                                    System.out.println(selection[0].getText());
                                    tempTaskSheet = i;
                                }

//                                k++;
                            }
                        }

                        try {
                            downloadHandler.createTaskOnComputer(file, taskSheets.get(tempTaskSheet), path, selection[0].getText());
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        
                        event.data = new String[]{file.getAbsolutePath()};
                        
                    System.out.println("nach event");
                    }
                    if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
                        event.data = "";
                    }
                }
            });

            shell.pack();
            shell.open();

            return shell;

        }
    }
}