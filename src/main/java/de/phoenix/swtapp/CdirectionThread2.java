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
import org.eclipse.swt.widgets.Shell;

import de.phoenix.util.Configuration;

public class CdirectionThread2 extends Thread {

    private boolean showPathInOpt;
    private Configuration config;
    // This thread is needed for the option window. It will replace
    // CdirectionThread when the user clicks on the option button for the second
    // time, but only if a config has been created.
    public CdirectionThread2(boolean showPathInOpt, Configuration config) {
        this.showPathInOpt = showPathInOpt;
        this.config = config;
    }

    public void run() {
        final Display display = new Display();
        final Shell shell = new Shell(SWT.ON_TOP | SWT.CLOSE);
        shell.setSize(350, 100);

        display.syncExec(new Runnable() {

            public void run() {
                if (display.isDisposed())
                    return;

                Cdirection directionWindow = new Cdirection();
                directionWindow.createPathDirectionShell(shell, showPathInOpt, config);

                while (!shell.isDisposed()) {
                    if (!display.readAndDispatch())
                        display.sleep();

                }
                display.dispose();
            }
        });
    }
}
