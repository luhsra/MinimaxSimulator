package de.uni_hannover.sra.minimax_simulator.resources;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;

/**
 * The Icons singleton class provides ImageIcon objects to display inside the
 * application. When an icon is requested via the {@link #get(String)} method,
 * it is loaded from the application resources and stored in a cache.
 * 
 * When an icon is missing in the application resources,
 * no exception is thrown, instead a replacement graphic is shown.
 * 
 * @author Martin
 */
public class Icons
{
	private final static Icons _instance = new Icons();

	public final static Icons getInstance()
	{
		return _instance;
	}

	private final Map<String, ImageIcon> _icons;
	private final BufferedImage _default;

	private Icons()
	{
		_icons = new HashMap<String, ImageIcon>();
		
		_default = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = _default.createGraphics();
		g.setColor(Color.PINK);
		g.fillRect(0, 0, 32, 32);
		g.dispose();
	}

	/**
	 * Retrieves an icon from the cache. If the icon is not yet cached, it will be loaded
	 * from the application resources.
	 * 
	 * If a resource with the given name cannot be found, a replacement icon will be
	 * returned instead.
	 * 
	 * @param name the file name of the local image resource
	 * @return the loaded image icon
	 */
	public ImageIcon get(String name)
	{
		ImageIcon icon = _icons.get(name);
		if (icon == null)
		{
			icon = loadImage(name);
			_icons.put(name, icon);
		}
		return icon;
	}

	private ImageIcon loadImage(String name)
	{
		BufferedImage image;
		try
		{
			InputStream is = null;
			try
			{
				name = "/resources/images/" + name;
				is = Icons.class.getResourceAsStream(name);
				if (is == null)
					throw new FileNotFoundException("File not found: " + name);
				image = ImageIO.read(is);
			}
			finally
			{
				IOUtils.closeQuietly(is);
			}
			
			return new ImageIcon(image);
		}
		catch (IOException e)
		{
			// logging?
			e.printStackTrace();

			// use a replacement image
			return new ImageIcon(_default, "Missing file: " + name);
		}
	}
}