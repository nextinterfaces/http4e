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
package org.roussev.http4e.httpclient.core.client.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.roussev.http4e.httpclient.core.CoreConstants;
import org.roussev.http4e.httpclient.core.misc.Styles;
import org.roussev.http4e.httpclient.core.util.BaseUtils;
import org.roussev.http4e.httpclient.core.util.ResourceUtils;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
class ParamTable {

   static final String           NAME  = "parameter";
   static final String           VALUE = "value";
   static final String           CLOSE = "Close";
   private static final String[] PROPS = { NAME, VALUE, CLOSE };

   // The data model
   private java.util.List        rowList;


   /**
    * Creates the main window's contents
    */
   Control createContents( Composite parent){
      rowList = new ArrayList();
      Composite composite = new Composite(parent, SWT.NONE);
      GridLayout gridLayout = new GridLayout(1, false);
      gridLayout.marginHeight = 0;
      gridLayout.marginWidth = 0;
      gridLayout.horizontalSpacing = 0;
      gridLayout.verticalSpacing = 0;
      composite.setLayout(gridLayout);

      // Add the TableViewer
      final TableViewer tv = new TableViewer(composite, SWT.FULL_SELECTION);
      tv.setContentProvider(new ParamContentProvider());
      tv.setLabelProvider(new ParamLabelProvider());
      tv.setInput(rowList);

      // Set up the table
      final Table table = tv.getTable();
      table.setLayoutData(new GridData(GridData.FILL_BOTH));

      final TableColumn column1 = new TableColumn(table, SWT.NONE);
      final TableColumn column2 = new TableColumn(table, SWT.NONE);
      final TableColumn column3 = new TableColumn(table, SWT.NONE);

      for (int i = 0, n = table.getColumnCount(); i < n; i++) {
         table.getColumn(i).pack();
      }

      table.setHeaderVisible(false);
      table.setLinesVisible(true);

      composite.addControlListener(new ControlAdapter() {

         public void controlResized( ControlEvent e){
            Table table = tv.getTable();
            Point areaSize = ((Composite) e.getSource()).getSize();
            Rectangle area = new Rectangle(0, 0, areaSize.x, areaSize.y); // table.getClientArea();
            Point preferredSize = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            int width = area.width - 2 * table.getBorderWidth();
            if (preferredSize.y > area.height + table.getHeaderHeight()) {
               // Subtract the scrollbar width from the total column width
               // if a vertical scrollbar will be required
               Point vBarSize = table.getVerticalBar().getSize();
               width -= vBarSize.x;
            }
            Point oldSize = table.getSize();
            if (oldSize.x > area.width) {
               // table is getting smaller so make the columns
               // smaller first and then resize the table to
               // match the client area width
               column1.setWidth(width / 3);
               column3.setWidth(width / 9);
               column2.setWidth(width - column1.getWidth() - column3.getWidth() /*- column4.getWidth()*/- 2);
               table.setSize(area.width, area.height);
            } else {
               // table is getting bigger so make the table
               // bigger first and then make the columns wider
               // to match the client area width
               // System.out.printf("\nw=%s, w/3=%s, w/4=%s, w/6=%s", width,
               // width/2, width/3, width/6);
               table.setSize(area.width, area.height);
               column1.setWidth(width / 3);
               column3.setWidth(width / 9);
               column2.setWidth(width - column1.getWidth() - column3.getWidth() /*- column4.getWidth()*/- 2);
            }
         }
      });

      // Create the cell editors
      CellEditor[] editors = new CellEditor[PROPS.length];
      editors[0] = new TextCellEditor(table);
      editors[1] = new TextCellEditor(table, SWT.MULTI);
      editors[2] = new TextCellEditor(table, SWT.READ_ONLY);

      table.addListener(SWT.MouseDown, new Listener() {

         public void handleEvent( Event event){
            Rectangle clientArea = table.getClientArea();
            Point pt = new Point(event.x, event.y);
            for (int k = table.getTopIndex(); k < table.getItemCount();) {
               boolean visible = false;
               TableItem item = table.getItem(k);
               for (int m = 0; m < PROPS.length; m++) {
                  Rectangle rect = item.getBounds(m);
                  if (rect.contains(pt)) {
                     // item.setText(".");
                     // item.setForeground(ResourceUtils.getColor(new
                     // RGB(123,21,123)));
                  }
                  if (!visible && rect.intersects(clientArea)) {
                     visible = true;
                  }
               }
               if (!visible)
                  return;
               k++;
            }
         }
      });

      // Set the editors, cell modifier, and column properties
      tv.setColumnProperties(PROPS);
      tv.setCellModifier(new ParamCellModifier(tv));
      tv.setCellEditors(editors);

      return composite;
   }
}

class ParamContentProvider implements IStructuredContentProvider {

   public Object[] getElements( Object inputElement){
      return ((List) inputElement).toArray();
   }


   public void dispose(){
   }


   public void inputChanged( Viewer viewer, Object oldInput, Object newInput){
   }

}

/**
 * This class represents the cell modifier for the PersonEditor program
 */
class ParamCellModifier implements ICellModifier {

   private Viewer viewer;


   public ParamCellModifier( Viewer viewer) {
      this.viewer = viewer;
   }


   /**
    * Returns whether the property can be modified
    */
   public boolean canModify( Object element, String property){
      // Allow editing of all values
      return true;
   }


   /**
    * Returns the value for the property
    */
   public Object getValue( Object element, String property){
      Param p = (Param) element;
      if (ParamTable.NAME.equals(property))
         return p.getName();
      else if (ParamTable.VALUE.equals(property))
         return p.getValue();
      // else if (ParamTable.INPUT.equals(property))
      // return p.getType();
      else if (ParamTable.CLOSE.equals(property))
         return CoreConstants.CROSS_TEXT;
      else
         return null;
   }


   /**
    * Modifies the element
    */
   public void modify( Object element, String property, Object value){
      if (element instanceof Item)
         element = ((Item) element).getData();

      Param p = (Param) element;
      if (ParamTable.NAME.equals(property))
         p.setName((String) value);
      else if (ParamTable.VALUE.equals(property))
         p.setValue((String) value);
      // else if (ParamTable.INPUT.equals(property))
      // p.setType((Integer) value);

      viewer.refresh();
   }
}

/**
 * This class provides the labels for the table
 */
class ParamLabelProvider implements ITableLabelProvider, ITableColorProvider, ITableFontProvider {

   public Image getColumnImage( Object element, int column){
      // if(column == 3){
      // return ResourceUtils.getImage(CoreImages.CLOSE_ROW);
      // }
      return null;
   }


   public String getColumnText( Object element, int column){
      Param param = (Param) element;
      switch (column) {
         case 0:
            return param.getName();
         case 1:
            return param.getValue();
            // case 2:
            // return InputType.INSTANCES[param.getType().intValue()];
         case 3:
            return CoreConstants.CROSS_TEXT;
      }
      return null;
   }


   public void addListener( ILabelProviderListener listener){
   }


   public void dispose(){
   }


   public boolean isLabelProperty( Object element, String property){
      return false;
   }


   public void removeListener( ILabelProviderListener listener){
   }


   public Color getBackground( Object element, int column){
      // Param p = (Param) element;
      // if (Param.DEFAULT_INSTANCE.equals(p)) {
      // return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
      // }
      return null;
   }


   public Color getForeground( Object element, int column){
      Param p = (Param) element;
      if (Param.DEFAULT_INSTANCE.equals(p)) {
         return ResourceUtils.getColor(Styles.LIGHT_RGB_TEXT);
      }
      return null;
   }


   public Font getFont( Object element, int column){
//      if (column == 3) {
//         return ResourceUtils.getFont(Styles.getInstance(parent.getShell()).getFontMonospaced());
//      }
      return null;
   }
}

// ////////////////////
class Param {

   private String     name;
   private String     value;
   private int        type;
   final static Param DEFAULT_INSTANCE = new Param();
   static {
      DEFAULT_INSTANCE.setName(ParamTable.NAME);
      DEFAULT_INSTANCE.setValue(ParamTable.VALUE);
      DEFAULT_INSTANCE.setType(0);
   }


   public int getType(){
      return type;
   }


   public void setType( int type){
      this.type = type;
   }


   public String getValue(){
      return value;
   }


   public void setValue( String value){
      this.value = value;
   }


   public String getName(){
      return name;
   }


   public void setName( String name){
      this.name = name;
   }


   public boolean equals( Object obj){
      if (obj != null && obj instanceof Param) {
         Param p2 = (Param) obj;
         return (BaseUtils.noNull(p2.getName()).equals(this.getName()) && BaseUtils.noNull(p2.getValue()).equals(this.getValue()));
      }
      return super.equals(obj);
   }
}
// ///////////////////
