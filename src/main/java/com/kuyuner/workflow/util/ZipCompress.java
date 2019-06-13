package com.kuyuner.workflow.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class ZipCompress {
	private static final int BUFFER = 1024;
	private File zipFile;
	private ZipOutputStream out;

	public ZipCompress(File zipFile) {
		this.zipFile = zipFile;
	}
	
	public ZipCompress compress(byte[] buf, String fileName) throws IOException {
		compress(new ByteArrayInputStream(buf), fileName);
		return this;
	}

	public ZipCompress compress(InputStream inputStream, String fileName) throws IOException {
		if (out == null) {
			out = new ZipOutputStream(zipFile);
		}

		BufferedInputStream bis = new BufferedInputStream(inputStream);
		ZipEntry entry = new ZipEntry(fileName);
		out.putNextEntry(entry);
		int count;
		byte data[] = new byte[BUFFER];
		while ((count = bis.read(data, 0, BUFFER)) != -1) {
			out.write(data, 0, count);
		}
		bis.close();
		return this;
	}

	public void close() {
		IOUtils.closeQuietly(out);
	}
}
