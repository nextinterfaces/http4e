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

/**
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class XMLTree {

   private XMLElement rootElement;
   private List<XMLElement>       allElements   = new ArrayList<XMLElement>();
   private List<String>       allAttributes = new ArrayList<String>();


   public XMLTree() {

      super();
      rootElement = new XMLElement("world");
      XMLElement continent = newDTDElement("continent");
      rootElement.addChildElement(continent);

      continent.addChildAttribute(new XMLAttribute("name")).addChildAttribute(new XMLAttribute("population"));
      addAttribute("name");
      addAttribute("population");

      XMLElement continentDescription = newDTDElement("description");
      continent.addChildElement(continentDescription);

      XMLElement country = newDTDElement("country");
      country.addChildAttribute(new XMLAttribute("name")).addChildAttribute(new XMLAttribute("population"));
      continent.addChildElement(country);

      XMLElement countryDescription = newDTDElement("description");
      country.addChildElement(countryDescription);
      XMLElement countryAttraction = newDTDElement("attraction");
      country.addChildElement(countryAttraction);
      countryAttraction.addChildAttribute(new XMLAttribute("name"));

      XMLElement city = newDTDElement("city");
      city.addChildAttribute(new XMLAttribute("name")).addChildAttribute(new XMLAttribute("population"));
      country.addChildElement(city);

      XMLElement cityDescription = newDTDElement("description");
      city.addChildElement(cityDescription);
      XMLElement cityAttraction = newDTDElement("attraction");
      cityAttraction.addChildAttribute(new XMLAttribute("name"));
      cityAttraction.addChildAttribute(new XMLAttribute("cost"));
      city.addChildElement(cityAttraction);
      addAttribute("cost");

      XMLElement ocean = newDTDElement("ocean");
      continent.addChildElement(ocean);
      ocean.addChildAttribute(new XMLAttribute("name"));
      ocean.addChildAttribute(new XMLAttribute("depth"));
      addAttribute("depth");

   }


   private XMLElement newDTDElement( String elementName){
      XMLElement element = new XMLElement(elementName);
      allElements.add(element);
      return element;
   }


   private void addAttribute( String attributeName){
      if (!allAttributes.contains(attributeName)) {
         allAttributes.add(attributeName);
      }
   }


   public List<XMLElement> getAllElements(){
      return allElements;
   }


   public List<String> getAllAttributes(){
      return allAttributes;
   }


   public XMLElement getRootElement(){
      return rootElement;
   }


   public void setRootElement( XMLElement rootElement){
      this.rootElement = rootElement;
   }
}