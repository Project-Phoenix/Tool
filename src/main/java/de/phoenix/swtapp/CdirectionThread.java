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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class CdirectionThread extends Thread {

    public void run() {
        final Display display = new Display();
        final Shell shell = new Shell(SWT.APPLICATION_MODAL | SWT.CLOSE);
        shell.setSize(350, 100);

        display.syncExec(new Runnable() {

            public void run() {

                MessageBox message = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES);
                message.setMessage("It seems to be your first start. Please enter where you want to save your files");
                message.setText("Starting for the first time");
                message.open();
            }
        });

        display.syncExec(new Runnable() {

            public void run() {
                if (display.isDisposed())
                    return;

                Cdirection directionWindow = new Cdirection();
                directionWindow.createPathDirectionShell(shell);

                while (!shell.isDisposed()) {
                    if (!display.readAndDispatch())
                        display.sleep();

                }
                display.dispose();
            }
        });

    }

}
