package de.uni_hannover.sra.minimax_simulator.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;

public class PropertyResourceControl extends Control
{
	private final String _basePath;

	public PropertyResourceControl(String basePath)
	{
		if (!basePath.endsWith("/"))
			basePath = basePath + "/";
		_basePath = basePath;
	}

	/*
	@Override
	public String toBundleName(String baseName, Locale locale)
	{
		StringBuilder sb = new StringBuilder(_basePath);
		sb.append("/");

		if (locale == Locale.ROOT)
		{
			sb.append("root");
			return sb.toString();
		}

		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();

		if (language.isEmpty() && country.isEmpty() && variant.isEmpty())
		{
			sb.append("root");
			return sb.toString();
		}

		if (!variant.isEmpty())
		{
			sb.append(language).append('_').append(country).append('_').append(variant);
		}
		else if (!country.isEmpty())
		{
			sb.append(language).append('_').append(country);
		}
		else
		{
			sb.append(language);
		}
		return sb.toString();
	}
	*/

	@Override
	public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader,
			boolean reload) throws IllegalAccessException, InstantiationException, IOException
	{
		if (baseName == null || locale == null || format == null || loader == null)
			throw new NullPointerException();

		String bundleName = toBundleName("", locale);
		if (bundleName.isEmpty())
			bundleName = "_base";

		String bundleFile = _basePath + "loc" + bundleName + "/" + baseName + ".properties";

		Map<String, Object> entries = new HashMap<String, Object>();
		
		InputStream is = null;
		try
		{
			is = this.getClass().getClassLoader().getResourceAsStream(bundleFile);
			if (is == null)
				throw new FileNotFoundException("Resource not found: " + (new File(bundleFile)).getAbsolutePath());
			parsePropertiesFile(is, entries);
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
		
		ResourceBundle bundle = new MapResourceBundle(entries);
		return bundle;
	}

	protected static void parsePropertiesFile(InputStream is, Map<String, Object> map) throws IOException
	{
		Properties prop = new Properties();
		prop.load(is);
		for (Entry<Object, Object> e : prop.entrySet())
		{
			map.put((String) e.getKey(), e.getValue());
		}
		prop.clear();
	}
}
