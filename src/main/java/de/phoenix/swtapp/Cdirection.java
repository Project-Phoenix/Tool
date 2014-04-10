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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.phoenix.util.Configuration;

public class Cdirection {

    private MyHandler myhandler;
    private boolean iswText = true;
    private boolean cancelClicked;
    private boolean acceptClicked;
    private boolean redXClicked;
    private String path;

    public Cdirection() {
        myhandler = new MyHandler();
    }

    public Shell createPathDirectionShell(final Shell shell, boolean showPathInOpt, final Configuration config) {

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

        final Shell shell1 = shell;
        final DirectoryDialog directoryDialog = new DirectoryDialog(shell1);
        final String textHint = "Please enter your downloadpath";
        final Text text = new Text(shell, SWT.BORDER | SWT.WRAP);
        text.setLayoutData(pathT);
       
        if (config.exists("downloadpath")) {
            System.out.println("SETTING TEXT");
            text.setText(config.getString("downloadpath"));

        } else {

            text.setText(textHint);
            text.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
        }

        // After clicking on the Button the user is capable of selecting a
        // directionpath in the directorydiaolog
        GridData brow = new GridData();
        brow.horizontalAlignment = GridData.FILL;
        brow.horizontalSpan = 1;

        Button browseB = new Button(shell, SWT.PUSH);
        browseB.setText("Browse");
        browseB.setLayoutData(brow);

        // By clickong on the "browser" button the user can select a download
        // file
        browseB.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {

                directoryDialog.setFilterPath(text.getText());
                directoryDialog.setText("Browser");
                directoryDialog.setMessage("Select a directory");

                String direction = directoryDialog.open();

                if (direction != null) {
                    // Set the text box to the new selection
                    path = direction;
                    text.setText(direction);
                    text.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));

                }
            }
        });

        // This two listeners will refresh the standard text
        // "please enter your downloadpath"
        // when the user let the textfield empty
        text.addMouseMoveListener(new MouseMoveListener() {

            public void mouseMove(MouseEvent e) {

                if (!iswText && text.getText().isEmpty()) {
                    text.setText(textHint);
                    text.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
                }

            }
        });

        text.addKeyListener(new KeyListener() {

            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            public void keyPressed(KeyEvent e) {
                if (iswText && text.getText().isEmpty()) {
                    iswText = false;
                    text.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
                    text.setText(textHint);

                }

                if (text.getText().equals(textHint)) {
                    text.setText("");
                    iswText = false;
                    text.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
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

            // If a downloadpath is chosen and it shows up in the textfield the
            // user will be able to dl or ul files after clicking on accept,
            // else the program will ignore it until there is a path in the
            // textfield or the user close the window. Closing the window will
            // shut down the program, when no path was selected
            public void widgetSelected(SelectionEvent e) {
                System.out.println("bin im acceptor");
                if (!text.getText().isEmpty() && !text.getText().matches("Please enter your downloadpath")) {
                    acceptClicked = true;
//                    if(config.getString("downloadpath") != path)
                    config.setString("downloadpath", path);
                }

                myhandler.checkpath(text, shell);

            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });

        final Button cancel = new Button(shell, SWT.PUSH);
        cancel.setText("Cancel");
        cancel.setLayoutData(botLine);
        // Selecting a pathfile and then try to close the window will show up a
        // message, which will ensure the userdecision, else the GUI will shut
        // down
        cancel.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {

                if (config.exists("downloadpath")) {
                    myhandler.closeWindow(shell1);
                } else {
                    if (!text.getText().isEmpty() && !text.getText().equals(textHint)) {

                        MessageBox message = new MessageBox(shell1, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
                        message.setText("Information");
                        message.setMessage("Do you want to close the programm?");

                        if (message.open() == SWT.YES) {
                            myhandler.closeWindow(shell1);
                            System.exit(0);

                        }
                    } else {
                        System.exit(0);
                    }
                }

                cancelClicked = true;
            }
            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });

        myhandler.centerWindow(shell);

        shell.addListener(SWT.Close, new Listener() {

            public void handleEvent(Event event) {
//                if (!cancelClicked && !acceptClicked && !redXClicked) {
                    if (!config.exists("downloadpath")) {
                       
                        System.exit(0);
                        
//                    }

                }
            }
        });

        shell.open();

        return shell;

    }
}
