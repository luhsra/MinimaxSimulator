package de.uni_hannover.sra.minimax_simulator.io.importer.xml;

import static com.google.common.base.Preconditions.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

import de.uni_hannover.sra.minimax_simulator.Main;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.EntityResolver;

import de.uni_hannover.sra.minimax_simulator.io.ProjectImportException;

@Deprecated
class Importer
{
	/**
	 * Create and parse a JDOM document from the given {@link InputStream}. <br>
	 * <br>
	 * Parsing from input streams instead of {@link Reader}s allows the parser to determine the file
	 * encoding from the xml preamble.
	 * 
	 * @param inputStream
	 *            the input stream containing an XML document
	 * @return the parsed Document
	 * @throws ProjectImportException
	 *             if there was a parsing error
	 */
	static Document parse(InputStream inputStream) throws ProjectImportException
	{
		EntityResolver resolver = new ResourceEntityResolver();

		Document doc;
		try
		{
			SAXBuilder sax = new SAXBuilder(XMLReaders.NONVALIDATING);
			sax.setEntityResolver(resolver);
			doc = sax.build(inputStream, "/resources/xml/");
		}
		catch (JDOMException e)
		{
			throw new ProjectImportException("XML parsing error", e);
		}
		catch (IOException e)
		{
			throw new ProjectImportException("I/O error", e);
		}
		return doc;
	}

	/**
	 * Create and parse a JDOM document from the given {@link InputStream}. <br>
	 * <br>
	 * Parsing from input streams instead of {@link Reader}s allows the parser to determine the file
	 * encoding from the xml preamble.
	 * 
	 * @param inputStream
	 *            the input stream containing an XML document
	 * @return the parsed Document
	 * @throws ProjectImportException
	 *             if there was a parsing error
	 */
	static Document parseAndValidate(InputStream inputStream) throws ProjectImportException
	{
		EntityResolver resolver = new ResourceEntityResolver();

		Document doc;
		try
		{
			SAXBuilder sax = new SAXBuilder(XMLReaders.DTDVALIDATING);
			sax.setEntityResolver(resolver);

			doc = sax.build(inputStream, "/resources/xml/");
		}
		catch (JDOMException e)
		{
			throw new ProjectImportException("XML parsing error", e);
		}
		catch (IOException e)
		{
			throw new ProjectImportException("I/O error", e);
		}
		return doc;
	}

	/**
	 * Fetches the root element with the given name from the document, or throws an exception if the
	 * name of the root element is not (case-sensitively) matching.
	 * 
	 * @param doc
	 * @param name
	 * @return
	 * @throws ProjectImportException
	 */
	static Element rootOf(Document doc, String name) throws ProjectImportException
	{
		Element elem = doc.getRootElement();
		if (elem.getName().equals(name))
			return elem;
		throw new ProjectImportException("Root element <" + name
			+ "> is missing in document " + doc.getBaseURI());
	}

	static <T extends Enum<T>> T get(Class<T> enumClass, Element element, String attribute)
			throws ProjectImportException
	{
		checkNotNull(enumClass);

		String value = element.getAttributeValue(attribute);

		if (value == null)
			throw new ProjectImportException("Attribute " + attribute + " of "
				+ element.getName() + " is null");

		try
		{
			return Enum.valueOf(enumClass, value);
		}
		catch (IllegalArgumentException e)
		{
			throw new ProjectImportException("Attribute " + attribute
				+ " is not a valid member of enum " + enumClass.getSimpleName() + ": "
				+ value, e);
		}
	}

	static <T extends Enum<T>> T get(Class<T> enumClass, Element element)
			throws ProjectImportException
	{
		String value = element.getText();
		if (value == null)
			throw new ProjectImportException("Content of " + element.getName()
				+ " is null");

		try
		{
			return Enum.valueOf(enumClass, value);
		}
		catch (IllegalArgumentException e)
		{
			throw new ProjectImportException("Content of " + element.getName()
				+ " is not a valid member of enum " + enumClass.getSimpleName() + ": "
				+ value, e);
		}
	}

	static int getInt(Element element, String attribute) throws ProjectImportException
	{
		String value = element.getAttributeValue(attribute);
		try
		{
			return Integer.parseInt(value);
		}
		catch (IllegalArgumentException e)
		{
			throw new ProjectImportException("Attribute " + attribute + " of "
				+ element.getName() + " cannot be parsed to integer: " + value, e);
		}
	}

	static int getInt(Element element) throws ProjectImportException
	{
		String value = element.getText();
		try
		{
			return Integer.parseInt(value);
		}
		catch (IllegalArgumentException e)
		{
			throw new ProjectImportException("Content of " + element.getName()
				+ " cannot be parsed to integer: " + value, e);
		}
	}

	static String getNonEmptyString(Element element, String attribute)
			throws ProjectImportException
	{
		String value = element.getAttributeValue(attribute);
		if (value == null || value.isEmpty())
			throw new ProjectImportException("Attribute " + attribute + " of "
				+ element.getName() + " must be a non-empty string");
		return value;
	}

	static String getNonEmptyString(Element element) throws ProjectImportException
	{
		String value = element.getText();
		if (value == null || value.isEmpty())
			throw new ProjectImportException("Content of " + element.getName()
				+ " must be a non-empty string");
		return value;
	}
}