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
package org.roussev.http4e.editor.xml.xmlattribs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.Position;

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class XMLElement {

   private List<XMLElement>       elementChildren   = new ArrayList<XMLElement>();
   private List<XMLAttribute>     attributeChildren = new ArrayList<XMLAttribute>();

   private String     name;
   private XMLElement parent;
   private Position   position;


   public XMLElement( String name) {
      super();
      this.name = name;
   }


   public List<XMLElement> getChildrenDTDElements(){
      return elementChildren;
   }


   public XMLElement addChildElement( XMLElement element){
      elementChildren.add(element);
      element.setParent(this);
      return this;
   }


   public void setParent( XMLElement element){
      this.parent = element;
   }


   public XMLElement getParent(){
      return parent;
   }


   public XMLElement addChildAttribute( XMLAttribute attribute){
      attributeChildren.add(attribute);
      return this;
   }


   public String getName(){
      return name;
   }


   public String getAttributeValue( String localName){
      for (XMLAttribute attribute : attributeChildren) {
         if (attribute.getName().equals(localName))
            return attribute.getValue();
      }
      return null;
   }


   public void clear(){
      elementChildren.clear();
      attributeChildren.clear();
   }


   public void setPosition( Position position){
      this.position = position;
   }


   public Position getPosition(){
      return position;
   }
}