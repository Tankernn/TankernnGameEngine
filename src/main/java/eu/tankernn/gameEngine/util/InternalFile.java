package eu.tankernn.gameEngine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class InternalFile {

	private static final String FILE_SEPARATOR = "/";

	private String path;
	private String name;

	public InternalFile(String path) {
		this.path = FILE_SEPARATOR + path;
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}

	public InternalFile(String... paths) {
		this.path = "";
		for (String part : paths) {
			this.path += (FILE_SEPARATOR + part);
		}
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}

	public InternalFile(InternalFile file, String subFile) {
		this.path = file.path + FILE_SEPARATOR + subFile;
		this.name = subFile;
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

	public InputStream getInputStream() throws FileNotFoundException {
		InputStream in = Class.class.getResourceAsStream(path);
		if (in == null) {
			in = new FileInputStream(new File("." + path));
		}
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

	public String getName() {
		return name;
	}
	
	public static InternalFile[] fromFilenames(String dir, String[] filenames, String extension) {
		return Arrays.asList(filenames).stream().map(f -> new InternalFile(dir + FILE_SEPARATOR + f + "." + extension)).toArray(size -> new InternalFile[size]);
	}

}
