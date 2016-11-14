/*
 *  Copyright 2017 Eclipse HttpClient (http4e) http://nextinterfaces.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.roussev.http4e.httpclient.ui.dialog.example;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class PrintDialogExample {
  Display d;

  Shell s;

  PrintDialogExample() {
    d = new Display();
    s = new Shell(d);
    s.setSize(400, 400);
    
    s.setText("A PrintDialog Example");
    s.setLayout(new FillLayout(SWT.VERTICAL));
    final Text t = new Text(s, SWT.BORDER | SWT.MULTI);
    final Button b = new Button(s, SWT.PUSH | SWT.BORDER);
    b.setText("Print");
    b.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        PrintDialog printDialog = new PrintDialog(s, SWT.NONE);
        printDialog.setText("Print");
        PrinterData printerData = printDialog.open();
        if (!(printerData == null)) {
          Printer p = new Printer(printerData);
          p.startJob("PrintJob");
          p.startPage();
          Rectangle trim = p.computeTrim(0, 0, 0, 0);
          Point dpi = p.getDPI();
          int leftMargin = dpi.x + trim.x;
          int topMargin = dpi.y / 2 + trim.y;
          GC gc = new GC(p);
          Font font = gc.getFont();
          String printText = t.getText();
          Point extent = gc.stringExtent(printText);
          gc.drawString(printText, leftMargin, topMargin
              + font.getFontData()[0].getHeight());
          p.endPage();
          gc.dispose();
          p.endJob();
          p.dispose();
        }
      }
    });
    s.open();

    while (!s.isDisposed()) {
      if (!d.readAndDispatch())
        d.sleep();
    }
    d.dispose();
  }

  public static void main(String[] argv) {
    new PrintDialogExample();
  }
}