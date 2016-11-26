package eu.tankernn.gameEngine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class InternalFile {

	private static final String FILE_SEPARATOR = "/";

	private String path;
	private String name;

	public InternalFile(String path) throws FileNotFoundException {
		this.path = path;
		if (!path.startsWith("/") || path.isEmpty())
			this.path = FILE_SEPARATOR + path;

		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
		try {
			getInputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public InternalFile(String... paths) throws FileNotFoundException {
		this(String.join(FILE_SEPARATOR, paths));
	}

	public InternalFile(InternalFile file, String... subFiles) {
		this.path = file.path;
		for (String part : subFiles) {
			this.path += (FILE_SEPARATOR + part);
		}
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return getPath();
	}

	@SuppressWarnings("resource")
	public InputStream getInputStream() throws FileNotFoundException {
		InputStream in = null;
		try {
			in = new FileInputStream(new File("." + path));
		} catch (FileNotFoundException ex) {
			in = InternalFile.class.getResourceAsStream(path);
		}
		if (in == null)
			throw new FileNotFoundException("Cannot find file " + path + " on classpath.");
		return in;
	}

	public BufferedReader getReader() throws IOException {
		try {
			InputStreamReader isr = new InputStreamReader(getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			return reader;
		} catch (IOException e) {
			System.err.println("Couldn't get reader for " + path);
			throw e;
		}
	}
	
	public URL getURL() {
		return InternalFile.class.getResource(path);
	}
	
	public String readFile() throws IOException {
		return Resources.toString(getURL(), Charsets.UTF_8);
	}

	public String getName() {
		return name;
	}

	public static InternalFile[] fromFilenames(String dir, String[] filenames, String extension) {
		return Arrays.asList(filenames).stream().map(f -> {
			try {
				return new InternalFile(dir + FILE_SEPARATOR + f + "." + extension);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}).toArray(size -> new InternalFile[size]);
	}

	@Override
	public boolean equals(Object obj) {
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		} else {
			return this.getPath().equals(((InternalFile) obj).getPath());
		}
	}

}
