package eu.tankernn.gameEngine.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JOptionPane;

public class NativesExporter {
	public static void exportNatives() {
		File nativeDir = new File("natives");
		if (!nativeDir.isDirectory() || nativeDir.list().length == 0) {
			try {
				nativeDir.mkdir();

				File jarFile = null;
				try {
					jarFile = new File(
							NativesExporter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}

				if (jarFile != null && jarFile.isFile()) { // Run with JAR file
					final JarFile jar = new JarFile(jarFile);
					final Enumeration<JarEntry> entries = jar.entries(); // gives
																			// ALL
																			// entries
																			// in
																			// jar
					while (entries.hasMoreElements()) {
						final String name = entries.nextElement().getName();
						if (name.endsWith(".dll") || name.endsWith(".so") || name.endsWith(".dylib")
								|| name.endsWith(".jnilb")) { // filter
																// according to
																// the path
							System.out.println(name);
							try {
								exportFile(name, "natives");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					jar.close();
					System.setProperty("org.lwjgl.librarypath", nativeDir.getAbsolutePath());
				} else { // Run with IDE
					System.out.println("Running in IDE environment. Setting native path to target/natives.");
					System.setProperty("org.lwjgl.librarypath", new File("target/natives").getAbsolutePath());
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,
						"Could not export natives. Execute in terminal to see full error output.", "Export natives",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		} else {
			System.setProperty("org.lwjgl.librarypath", nativeDir.getAbsolutePath());
		}
	}

	private static String exportFile(String classPath, String targetDir) throws Exception {
		InputStream stream = null;
		OutputStream resStreamOut = null;
		try {
			stream = NativesExporter.class.getResourceAsStream("/" + classPath);// note
																			// that
																			// each
																			// /
																			// is
																			// a
																			// directory
																			// down
																			// in
																			// the
																			// "jar
																			// tree"
																			// been
																			// the
																			// jar
																			// the
																			// root
																			// of
																			// the
																			// tree
			if (stream == null) {
				throw new Exception("Cannot get resource \"" + classPath + "\" from Jar file.");
			}

			int readBytes;
			byte[] buffer = new byte[4096];
			File outFile = new File(targetDir + "/" + classPath);
			outFile.getParentFile().mkdirs();
			resStreamOut = new FileOutputStream(outFile);
			while ((readBytes = stream.read(buffer)) > 0) {
				resStreamOut.write(buffer, 0, readBytes);
			}
			stream.close();
			resStreamOut.close();
		} catch (Exception ex) {
			throw ex;
		}

		return classPath;
	}
}
