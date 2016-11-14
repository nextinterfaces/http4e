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

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.roussev.http4e.httpclient.core.CoreImages;


/**
 * This class demonstrates JFace's IconAndMessageDialog class
 */
public class DumbUser extends ApplicationWindow {
  /**
   * DumbUser constructor
   */
  public DumbUser() {
    super(null);
  }

  /**
   * Runs the application
   */
  public void run() {
    // Don't return from open() until window closes
    setBlockOnOpen(true);

    // Open the main window
    open();

    // Dispose the display
    Display.getCurrent().dispose();
  }

  /**
   * Creates the main window's contents
   * 
   * @param parent the main window
   * @return Control
   */
  protected Control createContents(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout(1, true));

    // Create the button
    Button show = new Button(composite, SWT.NONE);
    show.setText("Show");

    final Shell shell = parent.getShell();

    // Display the dialog
    show.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        // Create and show the dialog
        DumbMessageDialog dlg = new DumbMessageDialog(shell);
        dlg.open();
      }
    });

    parent.pack();
    return composite;
  }

  /**
   * The application entry point
   * 
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    new DumbUser().run();
  }
}

/**
 * This class demonstrates the IconAndMessageDialog class
 */
class DumbMessageDialog extends IconAndMessageDialog {
  public static final int I_DUNNO_ID = IDialogConstants.CLIENT_ID;
  public static final String I_DUNNO_LABEL = "I Dunno";

  // The image
  private Image image;

  // The label for the "hidden" message
  private Label label;

  /**
   * DumbMessageDialog constructor
   * 
   * @param parent the parent shell
   */
  public DumbMessageDialog(Shell parent) {
    super(parent);

    // Create the image
    try {
      image = new Image(parent.getDisplay(), new FileInputStream(CoreImages.LOGO_DIALOG));
    } catch (FileNotFoundException e) {}

    // Set the default message
    message = "Are you sure you want to do something that dumb?";
  }

  /**
   * Sets the message
   * 
   * @param message the message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Closes the dialog
   * 
   * @return boolean
   */
  public boolean close() {
    if (image != null) image.dispose();
    return super.close();
  }

  /**
   * Creates the dialog area
   * 
   * @param parent the parent composite
   * @return Control
   */
  protected Control createDialogArea(Composite parent) {
    createMessageArea(parent);

    // Create a composite to hold the label
    Composite composite = new Composite(parent, SWT.NONE);
    GridData data = new GridData(GridData.FILL_BOTH);
    data.horizontalSpan = 2;
    composite.setLayoutData(data);
    composite.setLayout(new FillLayout());

    // Create the label for the "hidden" message
    label = new Label(composite, SWT.LEFT);

    return composite;
  }

  /**
   * Creates the buttons
   * 
   * @param parent the parent composite
   */
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.YES_ID, IDialogConstants.YES_LABEL,
        true);
    createButton(parent, IDialogConstants.NO_ID, IDialogConstants.NO_LABEL, false);
    createButton(parent, I_DUNNO_ID, I_DUNNO_LABEL, false);
  }

  /**
   * Handles a button press
   * 
   * @param buttonId the ID of the pressed button
   */
  protected void buttonPressed(int buttonId) {
    // If they press I Dunno, close the dialog
    if (buttonId == I_DUNNO_ID) {
      setReturnCode(buttonId);
      close();
    } else {
      // Otherwise, have some fun
      label.setText("Yeah, right. You know nothing.");
    }
  }

  /**
   * Gets the image to use
   */
  protected Image getImage() {
    return image;
  }
}
           
       
