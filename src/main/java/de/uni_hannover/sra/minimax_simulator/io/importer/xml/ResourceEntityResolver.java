package de.uni_hannover.sra.minimax_simulator.io.importer.xml;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ResourceEntityResolver implements EntityResolver
{
	private final String	_path;

	public ResourceEntityResolver()
	{
		this("");
	}

	public ResourceEntityResolver(String path)
	{
		_path = path;
	}

	@Override
	public InputSource resolveEntity(final String publicId, final String systemId)
			throws SAXException, IOException
	{
		if (systemId.startsWith("file://"))
		{
			String name = _path + systemId.substring(7);
			InputStream is = getClass().getResourceAsStream(name);
			return new InputSource(is);
		}
		return null;
	}
}