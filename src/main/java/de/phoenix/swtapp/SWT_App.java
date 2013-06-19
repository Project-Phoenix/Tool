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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SWT_App {

    public static void main(String[] args) {

        Display display = new Display();
        final Shell shell = new Shell(display);
        GridLayout layout = new GridLayout(2, false);
        
        // Hier noch ein Window Icon einfuegen
        
//        Image window_Icon = new Image(display,"Phoenix_Window_Icon");  
//        shell.setImage(window_Icon); 
        
        shell.setLayout(layout);
        shell.setText("Phoenix");

        shell.setSize(650, 400);
        centerWindow(shell);

        // First ROW

        Button closeButton = new Button(shell, SWT.PUSH);
        closeButton.setText("");
    
        GridData gridDataclsbutton = new GridData();
        gridDataclsbutton.horizontalAlignment = GridData.FILL;
        gridDataclsbutton.horizontalSpan = 1;
        closeButton.setLayoutData(gridDataclsbutton);

        closeButton.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                shell.getDisplay().dispose();
                System.exit(0);
            }

            public void widgetDefaultSelected(SelectionEvent e) {

            }
        });
        
               
        

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();

    }

    private static void centerWindow(Shell shell) {

        Rectangle bounds = shell.getDisplay().getBounds();

        Point p = shell.getSize();

        int sWidth = (bounds.width - p.x) / 2;
        int sHeight = (bounds.height - p.y) / 2;

        shell.setBounds(sWidth, sHeight, p.x, p.y);

    }
}
