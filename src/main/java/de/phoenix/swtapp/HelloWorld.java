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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class HelloWorld {
    

    public static void main(String[] args) {

        Display display = new Display();
        Shell shell = new Shell(display);
        GridLayout layout = new GridLayout(2, false);
        shell.setLayout(layout);
        shell.setText("Hello World");
        
        
        shell.setSize(500, 500);
        center(shell);

        Label label = new Label(shell, SWT.BORDER);
       

        label.setText("Test1");
       

        label.setToolTipText("Just for Testing");
        Button button = new Button(shell, SWT.PUSH);
        button.setText("Press Me");
        label = new Label(shell, SWT.BORDER);
        label.setText("Another One");
        label.setBackground(display.getSystemColor(SWT.COLOR_DARK_GREEN));

        Text text = new Text(shell, SWT.NONE);
        text.setText("Write Something here");
        text.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
        text.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
        
        Text text2 = new Text(shell, SWT.NONE);
        text2.setText("DONT REMOVE THIS TEXT!!!");
        text2.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
        text2.setForeground(display.getSystemColor(SWT.COLOR_BLACK));

        text.pack();
        label.pack();

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();

    }

    private static void center(Shell shell) {

        Rectangle bounds = shell.getDisplay().getBounds();

        Point p = shell.getSize();

        int nWid = (bounds.width - p.x) / 2;
        int nHei = (bounds.height - p.y) / 2;

        shell.setBounds(nWid, nHei, p.x, p.y);

    }
}

