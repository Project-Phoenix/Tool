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

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

public class UploadFilesThread extends Thread {
    private Display display;
    private ProgressBar loadbar;
    private Button uploadButton;
    private Boolean isactive = true;

    /**
     * @param args
     */
    public UploadFilesThread(Display display, ProgressBar loadbar, Button uploadButton) {
        this.display = display;
        this.loadbar = loadbar;
        this.uploadButton = uploadButton;
        
    }

    public void run() {

        for (int i = 0; i <= 100; i++) {

            if (!this.isactive) {
//               System.out.println("closing thread");
                return;
            }

            try {
                Thread.sleep(1);

            } catch (InterruptedException e) {

            }

            display.asyncExec(new Runnable() {
                public void run() {
                    if (loadbar.isDisposed())
                        return;

                    loadbar.setSelection(loadbar.getSelection() + 1);
                }
            });

        }
        display.asyncExec(new Runnable() {
            public void run() {
                if (uploadButton.isDisposed())
                    return;

                uploadButton.setEnabled(true);
            }
        });

    }

    public void kthread() {

        this.isactive = false;

    }

}
