package eu.tankernn.gameEngine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

	public BufferedReader getReader() throws Exception {
		try {
			InputStreamReader isr = new InputStreamReader(getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			return reader;
		} catch (Exception e) {
			System.err.println("Couldn't get reader for " + path);
			throw e;
		}
	}

	public String getName() {
		return name;
	}

}
