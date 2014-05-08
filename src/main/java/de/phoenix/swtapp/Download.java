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

import java.io.IOException;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.FileTransfer;
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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.util.Configuration;

public class Download extends Composite {

    private MyHandler myhandler;
    private DownloadHandler downloadHandler;
    private Configuration config;
    private String path;
    static int counter;

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

        if (taskSheets.isEmpty()) {
            MessageBox msg = new MessageBox(shell);
            msg.setMessage("Sorry, there are no tasksheets available");
            msg.open();

            return null;
        } else {
            for (int i = 0; i < taskSheets.size(); i++) {
                TreeItem parent = new TreeItem(tree, SWT.CHECK);
                // The current tasksheet will be saved in data so it can be used
                // directly when it is selected
                parent.setData(taskSheets.get(i));
                parent.setText(taskSheets.get(i).getTitle());
                for (int j = 0; j < taskSheets.get(i).getTasks().size(); j++) {

                    TreeItem subitem = new TreeItem(parent, SWT.CHECK);
                    // The tasks will be saved too as data
                    subitem.setData(taskSheets.get(i).getTasks().get(j));
                    subitem.setText(taskSheets.get(i).getTasks().get(j).getTitle());
                }
            }

            DragSource downloadW = new DragSource(tree, DND.DROP_TARGET_MOVE | DND.DROP_COPY);
            downloadW.setTransfer(new Transfer[]{FileTransfer.getInstance()});
            // On Windows this event will be called a lot of times caused due to
            // the prefetch system of Windows.
            downloadW.addDragListener(new DragSourceAdapter() {

                @Override
                public void dragSetData(DragSourceEvent event) {

                    if (!FileTransfer.getInstance().isSupportedType(event.dataType))
                        return;
                    // Get selected tasksheet
                    if (tree.getSelection()[0].getData() instanceof PhoenixTaskSheet) {
                        PhoenixTaskSheet taskSheet = (PhoenixTaskSheet) tree.getSelection()[0].getData();
                        try {
                            // Write tasksheet in temp folder
                            String tmpDirPath = downloadHandler.writeTaskSheetTo(taskSheet);
                            // Commit folder to event
                            event.data = new String[]{tmpDirPath};
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (tree.getSelection()[0].getData() instanceof PhoenixTask) {
                        PhoenixTask task = (PhoenixTask) tree.getSelection()[0].getData();
                        try {
                            // Write the task in temp folder
                            String tmpDirPath = downloadHandler.writeTask(task);
                            // Commit task to event
                            event.data = new String[]{tmpDirPath};
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            shell.pack();
            shell.open();

            return shell;

        }
        // TODO: Import the task package
    }
}