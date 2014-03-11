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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Cdirection {

    private MyHandler myhandler;
    private static Shell shell;
    private static Display display;

    public Cdirection() {
        myhandler = new MyHandler();
        display = new Display();
        shell = new Shell(SWT.ON_TOP | SWT.CLOSE);
        shell = createPathDirectionShell();
        shell.setSize(350, 100);
        myhandler.centerWindow(shell);

    }

    
    public static void main(String[] args) {

        new Cdirection();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();

        }
        display.dispose();

    }

    public Shell createPathDirectionShell() {

        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 6;
        gridLayout.makeColumnsEqualWidth = true;
        shell.setText("Browser");
        shell.setLayout(gridLayout);

        GridData dire = new GridData();
        dire.horizontalAlignment = GridData.FILL;
        dire.horizontalSpan = 1;

        Label leftLabel = new Label(shell, SWT.NONE);
        leftLabel.setText("Directory:");
        leftLabel.setLayoutData(dire);

        GridData pathT = new GridData();
        pathT.horizontalAlignment = GridData.FILL;
        pathT.horizontalSpan = 4;

        final Text text = new Text(shell, SWT.BORDER);
        text.setLayoutData(pathT);

        // After clicking on Button the user is capable of selecting a
        // directionpath in the directorydiaolog
        GridData brow = new GridData();
        brow.horizontalAlignment = GridData.FILL;
        brow.horizontalSpan = 1;

        Button browseB = new Button(shell, SWT.PUSH);
        browseB.setText("Browse");
        browseB.setLayoutData(brow);
        browseB.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                DirectoryDialog directoryDialog = new DirectoryDialog(shell);

//                // Set the initial filter path according
//                // to anything they've selected or typed in
                directoryDialog.setFilterPath(text.getText());

                // Change the title bar text
                directoryDialog.setText("Browser");

                // Customizable message displayed in the dialog
                directoryDialog.setMessage("Select a directory");

                // Calling open() will open and run the dialog.
                // It will return the selected directory, or
                // null if user cancels
                String direction = directoryDialog.open();
               
                if (direction != null) {
                    // Set the text box to the new selection
                    text.setText(direction);
                }
            }
        });

        GridData botLine = new GridData();
        botLine.horizontalAlignment = GridData.FILL;
        botLine.horizontalSpan = 2;

        Button accept = new Button(shell, SWT.PUSH);
        accept.setText("Accept");
        accept.setLayoutData(botLine);
        accept.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
               myhandler.checkpath(text);
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });

        Button cancel = new Button(shell, SWT.PUSH);
        cancel.setText("Cancel");
        cancel.setLayoutData(botLine);
        cancel.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                myhandler.closeWindow(shell);

            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });

        return shell;

    }

}
