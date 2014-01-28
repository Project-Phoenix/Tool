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

import java.awt.Graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 * @author Sir Lui
 *
 */

public class Login extends Composite {
    private MyHandler myhandler;

    public Login(Composite parent, int style, MyHandler myhandler) {
        super(parent, 0);
        this.myhandler=myhandler;
       
    }

    public Shell loginShell(final Display display, final Shell shell){

        Image loginicon = new Image(display, this.getClass().getResourceAsStream("/loginkey_50x50.png"));
        shell.setImage(loginicon);

        GridLayout gridlayout = new GridLayout();
        gridlayout.numColumns = 3;       

        shell.setLayout(gridlayout);
        shell.setSize(185, 180);
        shell.setText("Login");       
        myhandler.centerWindow(shell);

        GridData gridData_fill = new GridData();
        gridData_fill.horizontalAlignment = GridData.FILL;
        gridData_fill.horizontalSpan=3;


        //First Row             
        Label label_username = new Label(shell, SWT.NULL);
        label_username.setText("Username");
        label_username.setLayoutData(gridData_fill);

        //Second Row
        Text username= new Text(shell, SWT.FILL);
        username.setText("");
        username.setLayoutData(gridData_fill);

        //Third Row
        Label label_password = new Label(shell, SWT.NULL);
        label_password.setText("Password");

        //Fourth Row
        Text password= new Text(shell, SWT.PASSWORD);
        password.setLayoutData(gridData_fill);

        //Fifth Row
        Button checkBox_pw= new Button(shell, SWT.CHECK); 
        checkBox_pw.setText("Save Password");

        //Sixth Row         
        Button button_login_submit= new Button(shell, SWT.CENTER);   
        button_login_submit.setText("Login");

        Button button_login_cancel= new Button(shell, SWT.CENTER);
        button_login_cancel.setText("Cancel");        
        button_login_cancel.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                myhandler.closeWindow(shell);

            }

            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });

        shell.pack();
        shell.open();

        return shell;

    } 

}

