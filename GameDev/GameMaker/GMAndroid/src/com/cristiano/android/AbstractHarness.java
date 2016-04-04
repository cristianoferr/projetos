package com.cristiano.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

import com.cristiano.utils.IReadWrite;
import com.cristiano.utils.Log;
import com.google.common.io.ByteStreams;
import com.jme3.app.AndroidHarness;

public class AbstractHarness extends AndroidHarness implements IReadWrite {

	public InputStreamReader getStreamReader(String fileName) {
		// File file1 = new File (fileName);
		// FileInputStream fis1 = new FileInputStream (file1);
		// File file = getFileStreamPath(fileName);

		// File fileStreamPath = getFileStreamPath(fileName);
		// FileInputStream fis = new FileInputStream(fileStreamPath);
		// getFileStreamPath("log4j.properties");

		// in = openFileInput(file);
		// openF
		// InputStreamReader inputStreamReader = new InputStreamReader(in);
		// InputStreamReader inputStreamReader = new InputStreamReader(fis);
		InputStreamReader inputStreamReader = new InputStreamReader(
				getInputStream(fileName));

		return inputStreamReader;
	}

	@Override
	public boolean assetExists(String file) {
		// TODO otimizar esse metodo... pode ficar lento quando tiver muitos
		// arquivos...
		InputStream inputStream = getInputStream(file);

		boolean exists = inputStream != null;
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Log.debug("Exists? "+exists);
		/*
		 * String path = file.substring(0, file.lastIndexOf("/")); String
		 * fileName = file.substring(file.lastIndexOf("/") + 1); try { String[]
		 * list = getAssets().list(path); for (String f : list) { if
		 * (f.equals(fileName)) { return true; } } } catch (IOException e) {
		 * Log.error("Error Listing path: " + path); return false; } return
		 * false;
		 */
		return exists;

	}

	@Override
	public boolean assetEmpty(String file) {
		// Returns false by default...
		return false;
	}

	@Override
	public InputStream getInputStream(String fileName) {
		try {
			return getAssets().open(fileName);
		} catch (IOException e) {
			Log.error("Error opening inputStream for " + fileName);
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<ByteBuffer> readByteBuffer(String fullFilePath) {
		try {
			//AssetFileDescriptor file = getAssets().openFd(fullFilePath);
			AssetFileDescriptor file = openAssetFile(fullFilePath);
			ArrayList<ByteBuffer> list = new ArrayList<ByteBuffer>();
			// InputStream inputStream = getInputStream(fullFilePath);

			FileInputStream fis = new FileInputStream(file.getFileDescriptor());
			FileChannel channel = fis.getChannel();
			ByteBuffer buffer = ByteBuffer.allocateDirect((int) channel.size());

			channel.read(buffer);
			buffer.rewind();
			// buffer.flip();
			list.add(buffer);
			channel.close();
			fis.close();
			return list;

		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
	}

	@Override
	public File getFile(String assetFilePath) {
		// TODO: not working...
		File file1 = new File(Environment.getDataDirectory(), assetFilePath);
		Log.debug("File1:" + file1.length());
		File file2 = new File(Environment.getDownloadCacheDirectory(),
				assetFilePath);
		Log.debug("File2:" + file2.length());
		File file3 = new File(Environment.getRootDirectory(), assetFilePath);
		Log.debug("File3:" + file3.length());
		return file1;
	}

	@Override
	public long fileSize(String assetFilePath) {
		AssetFileDescriptor file;
		try {
			file = getAssets().openFd(assetFilePath);
			long size = file.getLength();
			file.close();
			return size;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public AssetFileDescriptor openAssetFile(String assetPath) throws FileNotFoundException
	{
	    try
	    {
	        final boolean canBeReadDirectlyFromAssets = false; // if your asset going to be compressed?
	        if (canBeReadDirectlyFromAssets)
	        {
	            return getAssets().openFd(assetPath);
	        }
	        else
	        {
	            final File cacheFile = new File(getCacheDir(), assetPath);
	            cacheFile.getParentFile().mkdirs();
	            copyToCacheFile(assetPath, cacheFile);
	            return new AssetFileDescriptor(ParcelFileDescriptor.open(cacheFile, ParcelFileDescriptor.MODE_READ_ONLY), 0, -1);
	        }
	    }
	    catch (FileNotFoundException ex)
	    {
	        throw ex;
	    }
	    catch (IOException ex)
	    {
	        throw new FileNotFoundException(ex.getMessage());
	    }
	}

	private void copyToCacheFile(final String assetPath, final File cacheFile)
			throws IOException {
		final InputStream inputStream = getAssets().open(assetPath,AssetManager.ACCESS_BUFFER);
		try {
			final FileOutputStream fileOutputStream = new FileOutputStream(
					cacheFile, false);
			try {
				// using Guava IO lib to copy the streams, but could also do it
				// manually
				ByteStreams.copy(inputStream, fileOutputStream);
			} finally {
				fileOutputStream.close();
			}
		} finally {
			inputStream.close();
		}
	}
}
