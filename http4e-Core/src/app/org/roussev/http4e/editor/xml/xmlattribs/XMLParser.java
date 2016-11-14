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


/**
 * Performs DTD validation on supplied XML document
 * 
 * @deprecated
 * @author Atanas Roussev (http://nextinterfaces.com)
 */
public class XMLParser
{

//	private ErrorHandler errorHandler;
//	private ContentHandler contentHandler;
//
//	public void setErrorHandler(ErrorHandler errorHandler)
//	{
//		this.errorHandler = errorHandler;
//	}
//
//	public void setContentHandler(ContentHandler contentHandler)
//	{
//		this.contentHandler = contentHandler;
//	}
//
//	public static void main(String[] args)
//	{
//		try
//		{
//			XMLParser parser = new XMLParser();
//			parser.setErrorHandler(new XMLValidationErrorHandler());
//			parser.doParse(new File(args[0]));
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			System.exit(-1);
//		}
//	}
//
//	public static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
//
//	/**
//	 * Does DTD-based validation on File
//	 */
//	public void doParse(File xmlFilePath) throws RuntimeException
//	{
//
//		InputSource inputSource = null;
//		try
//		{
//			inputSource = new InputSource(new FileReader(xmlFilePath));
//		}
//		catch (FileNotFoundException e)
//		{
//			throw new RuntimeException(e);
//		}
//		doParse(inputSource);
//
//	}
//
//	/**
//	 * Does DTD-based validation on text
//	 */
//	public void doParse(String xmlText) throws RuntimeException
//	{
//
//		InputSource inputSource = new InputSource(new StringReader(xmlText));
//		doParse(inputSource);
//
//	}
//
//	/**
//	 * Does DTD-based validation on inputSource
//	 */
//	public void doParse(InputSource inputSource) throws RuntimeException
//	{
//
//		try
//		{
//			XMLReader reader = new SAXParser();
//			reader.setErrorHandler(errorHandler);
//			reader.setContentHandler(contentHandler);
//			reader.setFeature(VALIDATION_FEATURE, true);
//			reader.parse(inputSource);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}

}

