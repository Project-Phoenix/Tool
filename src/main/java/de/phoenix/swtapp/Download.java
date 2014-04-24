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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.phoenix.rs.entity.PhoenixTaskSheet;

public class Download extends Composite {

    private MyHandler myhandler;
    private DownloadHandler downloadHandler;

    public Download(Composite parent, int style, MyHandler myhandler) {
        super(parent, 0);
        this.myhandler = myhandler;
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

        System.out.println("hole sheets");

        List<PhoenixTaskSheet> taskSheets = downloadHandler.showAllTaskSheets();
        
        int k = 0;
        if (taskSheets.isEmpty()) {
            System.out.println("Sorry, there are no tasksheets available");
            return null;
        } else {
            for (int i = 0; i < taskSheets.size(); i++) {
                TreeItem parent = new TreeItem(tree, SWT.CHECK);
                parent.setText(taskSheets.get(i).getTitle());
                k++;
                for (int j = 0; j < taskSheets.get(i).getTasks().size(); j++) {
                    
                    TreeItem subitem = new TreeItem(parent, SWT.CHECK);
                    subitem.setText(taskSheets.get(i).getTasks().get(j).getTitle());
                    k++;
                }
            }
            
            DragSource downloadW = new DragSource(tree, DND.DROP_TARGET_MOVE | DND.DROP_COPY);
            downloadW.setTransfer(new Transfer[]{TextTransfer.getInstance(), FileTransfer.getInstance()});
            downloadW.addDragListener(new DragSourceAdapter() {
                @Override
                public void dragSetData(DragSourceEvent event) {
                    if (FileTransfer.getInstance().isSupportedType(event.dataType)) {
                        TreeItem[] selection= tree.getSelection();     
                        
                        //if parent or subitem
                        File file = new File(selection[0].getText());
                        
                        
                        event.data = new String[]{file.getAbsolutePath()};
                    }
                    if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
                        event.data = "once upon a time";
                    }
                }
            });
            

//        for (int i = 0; i < taskSheets.size(); i++) {
//
//            TreeItem parent = new TreeItem(tree, SWT.CHECK);
//            parent.setText(taskSheets.get(i));
//
////                parent.setImage(ts);
//
//            for (int j = 0; j < ; j++) {
//                TreeItem subitem = new TreeItem(parent, SWT.CHECK);
//                subitem.setText("Task_" + (j + 1));
//                if ((j * i + 1) % 3 >= 1) {
////                        subitem.setImage(abg);
//
//                }
//
//            }
        }
//        Image ts = new Image(display, this.getClass().getResourceAsStream("/tasksheet.png"));
//        Image neg = new Image(display, this.getClass().getResourceAsStream("/nichtabgegeben.png"));
//        Image abg = new Image(display, this.getClass().getResourceAsStream("/abgegeben.png"));
//
//        for (int i = 0; i < 3; i++) {
//
//            TreeItem parent = new TreeItem(tree, SWT.CHECK);
//            parent.setText("Tasksheet_" + (i + 1));
//
//            parent.setImage(ts);
//
//            for (int j = 0; j < 5; j++) {
//                TreeItem subitem = new TreeItem(parent, SWT.CHECK);
//                subitem.setText("Task_" + (j + 1));
//                if ((j * i + 1) % 3 >= 1) {
//                    subitem.setImage(abg);
//
//                } else
//                    subitem.setImage(neg);
//            }
//        }

        shell.pack();
        shell.open();

        return shell;

    }
}