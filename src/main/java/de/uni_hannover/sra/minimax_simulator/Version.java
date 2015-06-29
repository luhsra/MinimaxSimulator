package de.uni_hannover.sra.minimax_simulator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;

/*
 * According to http://stackoverflow.com/questions/1272648/reading-my-own-jars-manifest
 * this is probably possible with less complexity
 */
public class Version
{
	private boolean _isJar = false;
	private String _moduleName = "";
	private String _revisionNumber = "";
	private String _versionNumber = "";
	private String _buildJdk = "";
	private String _buildTime = "";
	private String _authorName = "";
	private String _companyName = "";

	private int _jvmMajor;
	private int _jvmFeature;
	private int _jvmUpdate;
	private int _jvmBuild;

	public Version(Class<?> c)
	{
		setJavaVersion();

		if (c == null)
			return;

		File jarName = null;
		try
		{
			jarName = getResourceJar(c);
			if (jarName == null)
				return;

			JarFile jarFile = null;
			try
			{
				jarFile = new JarFile(jarName);
				
				Attributes attrs = jarFile.getManifest().getMainAttributes();

				setBuildTime(attrs);
				setBuildJdk(attrs);
				setRevisionNumber(attrs);
				setVersionNumber(attrs);
				setModuleName(attrs);
				setAuthor(attrs);
				setCompany(attrs);

				setIsJar(true);
			}
			finally
			{
				IOUtils.closeQuietly(jarFile);
			}
		}
		catch (IOException e)
		{
			// No jar file
		}
	}

	/*
	 * Convert the given class name to URL to get
	 * the file path of the JAR that contains the class.
	 */
	private static File getResourceJar(Class<?> c)
	{
		String resource = c.getName().replace('.', '/') + ".class";

		URL url = ClassLoader.getSystemResource(resource);
		if (url != null)
		{
			String u = url.toString();
			if (u.startsWith("jar:file:"))
			{
				int idx = u.indexOf("!");
				String jarName = u.substring(4, idx);
				try
				{
					return new File(new URI(jarName));
				}
				catch (URISyntaxException e)
				{
					return null;
				}
			}
		}
		return null;
	}

	private void setVersionNumber(Attributes attrs)
	{
		String versionNumber = attrs.getValue("Implementation-Version");
		if (versionNumber != null)
		{
			_versionNumber = versionNumber;
		}
	}

	private void setRevisionNumber(Attributes attrs)
	{
		String revisionNumber = attrs.getValue("Implementation-Build");
		if (revisionNumber != null)
		{
			_revisionNumber = revisionNumber;
		}
	}

	private void setBuildJdk(Attributes attrs)
	{
		String buildJdk = attrs.getValue("Build-Jdk");
		if (buildJdk != null)
		{
			_buildJdk = buildJdk;
		}
/*		else
		{
			buildJdk = attrs.getValue("Created-By");
			if (buildJdk != null)
			{
				_buildJdk = buildJdk;
			}
		}	*/
	}

	private void setBuildTime(Attributes attrs)
	{
		String buildTime = attrs.getValue("Build-Time");
		if (buildTime != null)
		{
			_buildTime = buildTime;
		}
	}

	private void setModuleName(Attributes attrs)
	{
		String moduleName = attrs.getValue("Implementation-Name");
		if (moduleName != null)
		{
			_moduleName = moduleName;
		}
	}

	private void setAuthor(Attributes attrs)
	{
		String authorName = attrs.getValue("Additional-Author");
		if (authorName != null)
		{
			_authorName = authorName;
		}
	}

	private void setCompany(Attributes attrs)
	{
		String companyName = attrs.getValue("Additional-Company");
		if (companyName != null)
		{
			_companyName = companyName;
		}
	}

	private void setJavaVersion()
	{
		String jv = System.getProperty("java.version");
		try
		{
			String[] p0 = jv.split("_");
			String[] p1 = p0[0].split("\\.");

			_jvmMajor = Integer.parseInt(p1[0]);
			_jvmFeature = Integer.parseInt(p1[1]);
			_jvmUpdate = Integer.parseInt(p1[2]);

			try
			{
				_jvmBuild = Integer.parseInt(p0[1]);
			}
			catch (Exception e)
			{
				Pattern numberPattern = Pattern.compile("[0-9]+");
				Matcher m = numberPattern.matcher(p0[1]);
				if (m.find())
					_jvmBuild = Integer.parseInt(m.group(0));
			}
		}
		catch (Exception e)
		{
			// Ignore
		}
	}

	private void setIsJar(boolean isLoaded)
	{
		_isJar = isLoaded;
	}

	public String getRevisionNumber()
	{
		return _revisionNumber;
	}

	public String getVersionNumber()
	{
		return _versionNumber;
	}

	public String getBuildJdk()
	{
		return _buildJdk;
	}

	public String getBuildTime()
	{
		return _buildTime;
	}

	public String getModuleName()
	{
		return _moduleName;
	}

	public String getAuthorName()
	{
		return _authorName;
	}

	public String getCompanyName()
	{
		return _companyName;
	}

	public boolean isJar()
	{
		return _isJar;
	}

	public String[] getFullInfoStrings()
	{
		return new String[] {
				"[Module: " + getModuleName() + "]",
				"Version: " + getVersionNumber() + " - "
						+ "Revision: " + getRevisionNumber() + " - "
						+ "Time: " + getBuildTime() + " - "
						+ "Build JDK: " + getBuildJdk() };
	}

	public String[] getShortInfoStrings()
	{
		return new String[] {
				"[Module: " + getModuleName() + "]",
				"Version: " + getVersionNumber() + " - "
						+ "Revision: " + getRevisionNumber() };
	}

	public int getJvmMajor()
	{
		return _jvmMajor;
	}

	public int getJvmFeature()
	{
		return _jvmFeature;
	}

	public int getJvmUpdate()
	{
		return _jvmUpdate;
	}

	public int getJvmBuild()
	{
		return _jvmBuild;
	}

	public boolean isJvmEqual(int major, int feature, int update, int build)
	{
		if (major == -1)
			return true;
		if (_jvmMajor != major)
			return false;

		if (feature == -1)
			return true;
		if (_jvmFeature != feature)
			return false;

		if (update == -1)
			return true;
		if (_jvmUpdate != update)
			return false;

		if (build == -1)
			return true;
		if (_jvmBuild != build)
			return false;
		return true;
	}

	public boolean isJvmLower(int major, int feature, int update, int build)
	{
		if (major == -1)
			return true;
		if (_jvmMajor >= major)
			return false;

		if (feature == -1)
			return true;
		if (_jvmFeature >= feature)
			return false;

		if (update == -1)
			return true;
		if (_jvmUpdate >= update)
			return false;

		if (build == -1)
			return true;
		if (_jvmBuild >= build)
			return false;
		return true;
	}

	public boolean isJvmLowerOrEqual(int major, int feature, int update, int build)
	{
		if (major == -1)
			return true;
		if (_jvmMajor > major)
			return false;

		if (feature == -1)
			return true;
		if (_jvmFeature > feature)
			return false;

		if (update == -1)
			return true;
		if (_jvmUpdate > update)
			return false;

		if (build == -1)
			return true;
		if (_jvmBuild > build)
			return false;
		return true;
	}

	public boolean isJvmHigher(int major, int feature, int update, int build)
	{
		if (major == -1)
			return true;
		if (_jvmMajor <= major)
			return false;

		if (feature == -1)
			return true;
		if (_jvmFeature <= feature)
			return false;

		if (update == -1)
			return true;
		if (_jvmUpdate <= update)
			return false;

		if (build == -1)
			return true;
		if (_jvmBuild <= build)
			return false;
		return true;
	}

	public boolean isJvmHigherOrEqual(int major, int feature, int update, int build)
	{
		if (major == -1)
			return true;
		if (_jvmMajor < major)
			return false;

		if (feature == -1)
			return true;
		if (_jvmFeature < feature)
			return false;

		if (update == -1)
			return true;
		if (_jvmUpdate < update)
			return false;

		if (build == -1)
			return true;
		if (_jvmBuild < build)
			return false;
		return true;
	}
}
