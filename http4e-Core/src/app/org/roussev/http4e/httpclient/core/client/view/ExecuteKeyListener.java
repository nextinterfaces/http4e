package org.roussev.http4e.httpclient.core.client.view;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

public class ExecuteKeyListener implements KeyListener {

   private static final int SHIFT = 1 << 17;

   private static final int CTRL  = 1 << 18;

   private ExecuteCommand  cmd;


   public ExecuteKeyListener( ExecuteCommand cmd) {
      this.cmd = cmd;
   }

   public void keyPressed( KeyEvent e){
      // pass through
   }

   public void keyReleased( KeyEvent e){
      // String string = "";// e.type == SWT.KeyDown ? "DOWN:" : "UP  :";
      // string += " stateMask=0x" + Integer.toHexString(e.stateMask) +
      // Snippet25.stateMask(e.stateMask) + ",";
      // string += " keyCode=0x" + Integer.toHexString(e.keyCode) + " " +
      // Snippet25.keyCode(e.keyCode) + ",";
      // string += " character=0x" + Integer.toHexString(e.character) + " " +
      // Snippet25.character(e.character);
      // if (e.keyLocation != 0) {
      // string += " location=";
      // if (e.keyLocation == SWT.LEFT)
      // string += "LEFT";
      // if (e.keyLocation == SWT.RIGHT)
      // string += "RIGHT";
      // if (e.keyLocation == SWT.KEYPAD)
      // string += "KEYPAD";
      //
      // }

      if ((e.stateMask & CTRL) != 0 && (e.stateMask & SHIFT) != 0 && e.keyCode == 'r') {
         // System.out.println(string);
         // model.fireExecute(new ModelEvent(ModelEvent.PARAMS_FOCUS_LOST,
         // model));
         // model.fireExecute(new ModelEvent(ModelEvent.REQUEST_START, model));
         cmd.execute();
      }
   }

}
