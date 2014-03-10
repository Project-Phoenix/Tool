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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Sir Lui
 * 
 */

public class Login extends Composite {
    private MyHandler myhandler;
    private LoginHandler loginhandler;
    private LongRunningOperation thread;
    private Boolean ithread;

    public Login(Composite parent, int style, MyHandler myhandler, LoginHandler loginhandler) {
        super(parent, 0);
        this.myhandler = myhandler;
        this.loginhandler = loginhandler;

    }

    public Shell loginShell(final Display display, final Shell shell) {

        Image loginicon = new Image(display, this.getClass().getResourceAsStream("/loginkey_50x50.png"));
        shell.setImage(loginicon);

        GridLayout gridlayout = new GridLayout();
        gridlayout.numColumns = 3;

        shell.setLayout(gridlayout);
        shell.setSize(185, 245);
        shell.setText("Login");
        myhandler.centerWindow(shell);

        GridData gridDataS1 = new GridData();
        gridDataS1.horizontalAlignment = GridData.BEGINNING;
        gridDataS1.horizontalSpan = 1;

        GridData gridDataS2 = new GridData();
        gridDataS2.horizontalAlignment = GridData.CENTER;
        gridDataS2.horizontalSpan = 2;

        GridData gridDataS3 = new GridData();
        gridDataS3.horizontalAlignment = GridData.FILL;
        gridDataS3.horizontalSpan = 3;

        GridData gridDataButton = new GridData();
        gridDataButton.horizontalAlignment = GridData.FILL;
        gridDataButton.verticalSpan = 2;
        gridDataButton.horizontalSpan = 3;

        // 1.Row
        Label progressBarTitle = new Label(shell, SWT.NULL);
        progressBarTitle.setText("Loading");
        progressBarTitle.setBackground(getDisplay().getSystemColor(SWT.COLOR_CYAN));
        progressBarTitle.setLayoutData(gridDataS1);

        final Label messageAns = new Label(shell, SWT.NULL);
        messageAns.setText("                     ");
        messageAns.setLayoutData(gridDataS2);

        // 2.Row
        final ProgressBar loadbar = new ProgressBar(shell, SWT.NULL);
        loadbar.setLayoutData(gridDataS3);

        // 3.Row
        Label label_username = new Label(shell, SWT.NULL);
        label_username.setText("Username");
        label_username.setLayoutData(gridDataS3);

        // 4.Row
        Text username = new Text(shell, SWT.NULL);
        username.setText("");
        username.setLayoutData(gridDataS3);

        // 5.Row
        Label label_password = new Label(shell, SWT.NULL);
        label_password.setText("Password");
        label_password.setLayoutData(gridDataS3);

        // 6.Row
        Text password = new Text(shell, SWT.PASSWORD);
        password.setLayoutData(gridDataS3);

        // 7.Row
        Button checkBox_pw = new Button(shell, SWT.CHECK);
        checkBox_pw.setText("Save Password");
        checkBox_pw.setLayoutData(gridDataS3);

        final Button button_login_submit = new Button(shell, SWT.NULL);
        button_login_submit.setText("Login");
        button_login_submit.setLayoutData(gridDataButton);
        button_login_submit.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
     
                loadbar.setSelection(0);
                button_login_submit.setEnabled(false);
                thread = new LongRunningOperation(display, loadbar, button_login_submit, messageAns);
                thread.start();
                
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // TODO Auto-generated method stub

            }
        });

        Button button_login_cancel = new Button(shell, SWT.NULL);
        button_login_cancel.setText("Cancel");

        button_login_cancel.setLayoutData(gridDataButton);
        button_login_cancel.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                System.out.println(ithread.toString());
                thread.kthread();
                myhandler.closeWindow(shell);


            }

            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        shell.open();

        return shell;

    }
}
