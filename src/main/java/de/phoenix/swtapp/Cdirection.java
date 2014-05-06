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

@SuppressWarnings("unused")
public class Cdirection {

    // The SWT_App will create Cdirection when the user clicks on the option
    // button.
    // This class uses CdirectionThread and CdirectionThread2. CdirectionThread
    // is only used for the first time. Then CdirectionThread2 will replace the
    // function of CdirectionThread.

    private MyHandler myhandler;
    private boolean iswText = true;
    private boolean okClicked;
    private String path = "";
    private boolean pathExist;
//  private boolean cancelClicked;

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

        // If it exists a downloadpath in the created config, the path will be
        // show in the textfield else a standard text will show up
        // Furthermore it will check the choosen path and wrap it, if it has mor
        // the 37 characters
        if (config.exists("downloadpath") && !pathExist) {

            String temp = config.getString("downloadpath");
            String subTemp = "";
            String[] pathComponents = null;
            if (temp.length() > 37) {
                pathComponents = temp.split("\\\\");
                subTemp = (pathComponents[0] + "\\" + pathComponents[1] + "\\...\\" + pathComponents[pathComponents.length - 1]);

                text.setText(subTemp);
            } else {
                text.setText(temp);
            }
        } else {
            text.setText(textHint);
            text.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
        }

        // After clicking on the button the user is capable of selecting a
        // directionpath in the directorydiaolog
        GridData brow = new GridData();
        brow.horizontalAlignment = GridData.FILL;
        brow.horizontalSpan = 1;

        Button browseB = new Button(shell, SWT.PUSH);
        browseB.setText("Browse");
        browseB.setLayoutData(brow);

        // By clickong on the "browser" button the user can select a download
        // file. Furthermore the config will be created when the user selects a
        // path
        browseB.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {

                directoryDialog.setFilterPath(text.getText());
                directoryDialog.setText("Browser");
                directoryDialog.setMessage("Select a directory");
                String direction = directoryDialog.open();

                if (direction != null) {
                    // Set the text box to the new selection
                    path = direction;
                    config.setString("downloadpath", path);
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
//        System.out.println("pathform before ok" + path);

        GridData botLine = new GridData();
        botLine.horizontalAlignment = GridData.FILL;
        botLine.horizontalSpan = 2;

        Button OK = new Button(shell, SWT.PUSH);
        OK.setText("OK");
        OK.setLayoutData(botLine);
        OK.addSelectionListener(new SelectionListener() {

            // If a downloadpath is chosen and it shows up in the textfield the
            // user will be able to dl or ul files after clicking on accept,
            // else the program will ignore it until there is a path in the
            // textfield or the user close the window. Closing the window will
            // shut down the program, when no path was selectedb
            public void widgetSelected(SelectionEvent e) {

                if (!text.getText().matches("Please enter your downloadpath")) {
//                    okClicked = true;
//                    System.out.println("pathform after ok" + path);
                }

                myhandler.checkpath(text, shell);

            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });

//        final Button cancel = new Button(shell, SWT.PUSH);
//        cancel.setText("Cancel");
//        cancel.setLayoutData(botLine);
//        // Selecting a pathfile and then try to close the window will show up a
//        // message, which will ensure the userdecision, else the GUI will shut
//        // down
//        cancel.addSelectionListener(new SelectionListener() {
//
//            public void widgetSelected(SelectionEvent e) {
//
//                if (config.exists("downloadpath")) {
//                    myhandler.closeWindow(shell1);
//                } else {
//                    if (!text.getText().isEmpty() && !text.getText().equals(textHint)) {
//
//                        MessageBox message = new MessageBox(shell1, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
//                        message.setText("Information");
//                        message.setMessage("Do you want to close the programm?");
//
//                        if (message.open() == SWT.YES) {
//                            myhandler.closeWindow(shell1);
//                            System.exit(0);
//
//                        }
//                    } else {
//                        System.exit(0);
//                    }
//                }
//
//                cancelClicked = true;
//            }
//            public void widgetDefaultSelected(SelectionEvent e) {
//                // TODO Auto-generated method stub
//
//            }
//        });

        myhandler.centerWindow(shell);

        shell.addListener(SWT.Close, new Listener() {

            public void handleEvent(Event event) {
                if (!config.exists("downloadpath")) {
                    System.exit(0);
                }
            }
        });

        shell.open();

        return shell;

    }
}
