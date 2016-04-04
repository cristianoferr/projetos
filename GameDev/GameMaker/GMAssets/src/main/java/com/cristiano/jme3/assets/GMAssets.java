package com.cristiano.jme3.assets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.json.simple.JSONObject;

import com.cristiano.data.CRJsonUtils;
import com.cristiano.jme3.consts.JMEConsts;
import com.cristiano.utils.CRJavaUtils;
import com.cristiano.utils.Log;

public class GMAssets {

	//cant compress...
	public static final String SAVABLE_EXTENSION = ".sav";
	
	//can compress...
	public static final String ASSET_EXTENSION = ".dat";
	public static final String GEN_FOLDER = "gen/";

	private static String currentDir = null;
	private static Class<? extends GMAssets> myobj;

	private String rootPath;

	public GMAssets() {
		// try {
		File currentDirFile = new File(".");
		// String helper = currentDirFile.getAbsolutePath();
		myobj = this.getClass();
		URL resource = getClass().getResource(".");
		// Log.debug("resource:"+resource);
		
		//android...
		if (resource==null){
			currentDir ="";
		} else {
			currentDir = resource.getPath().substring(0, resource.getPath().lastIndexOf("/bin") + 1)+JMEConsts.assetsRoot + "/";
		}
		// Log.debug("CurDir:"+currentDir);
		/*
		 * } catch (IOException e) { Log.error("Error on GMAssets");
		 * e.printStackTrace(); }
		 */
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public static String getPathForSavableEntity(String entId, String type) {
		String folder = GEN_FOLDER + "savable/" + type + "/";
		if (!CRJavaUtils.isRelease()) {
			checkFolder(folder);
		}
		return folder + entId + SAVABLE_EXTENSION;
	}

	public static String getPathForSavableEntity(int entId, String type) {
		return getPathForSavableEntity(Integer.toString(entId), type);
	}

	public static void checkFolder(String folder) {
		File f = new File(getFullFilePath(folder));
		if (!f.exists()) {
			if (folder.contains("/")) {
				String subFolder = folder.substring(0, folder.lastIndexOf("/"));
				if (!subFolder.trim().equals("")) {
					checkFolder(subFolder);
				}
			}
			f.mkdir();
		}
	}

	public static String getCurrentDir() {
		if (currentDir == null) {
			new GMAssets();
		}
		return currentDir;
	}

	// filepath is a path as should be accessible from within jme
	public static String getFullFilePath(String filePath) {
		return getCurrentDir() + getJMEFilePath(filePath);
	}

	public static String getJMEFilePath(String filePath) {

		return  filePath;
	}

	public static OutputStream getOutputStream(String filePath) {
		String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
		checkFolder(dirPath);
		filePath = getFullFilePath(filePath);
		try {
			// URL resourceUrl =
			// classe.getClassLoader().getResource(saveFolder);
			// URL resourceUrl1 = classe.getResource(name);
			File file;
			file = new File(filePath);
			OutputStream output = new FileOutputStream(file);
			return output;
		} catch (Exception e) {
			Log.error("Error generating outputStream to " + filePath);
			e.printStackTrace();
		}
		return null;
	}

	public static InputStream getInputStream(String filePath) {
		return CRJavaUtils.context.getInputStream(getFullFilePath(filePath));
		
		
		
	}

	public static URL getReadingURL(String fileName) {
		String filePath = getJMEFilePath(fileName);
		URL resource = myobj.getClassLoader().getResource(filePath);
		// URL resource3 = myobj.getResource(filePath);
		// URL resource2 =
		// myobj.getClassLoader().getResource(getFullFilePath(fileName));
		return resource;
	}

	public static boolean exportHeightMap(String assetFolder, String id, float[] heightMap, boolean compressed) {
		Log.info("Exporting HeightMap:" + id + " size is: " + heightMap.length);
		DataOutputStream output;
		assetFolder = GEN_FOLDER + assetFolder;
		checkFolder(assetFolder);
		String assetFilePath = getAssetFilePath(assetFolder, id);
		try {
			output = new DataOutputStream(new FileOutputStream(assetFilePath));
			output.writeFloat(heightMap.length);
			for (int i = 0; i < heightMap.length; i++) {
				output.writeFloat(heightMap[i]);
			}
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (compressed) {
			exportHeightMapZip(assetFolder, id, heightMap);
			//TODO:descomentar essa linha
		//	deleteAsset(assetFolder + "/" + id + MAP_EXTENSION);
		}
		return true;

	}

	public static void exportHeightMapZip(String assetFolder, String id, float[] heightMap) {
		String _zipFile = getAssetFilePath(assetFolder, id);
		checkFolder(assetFolder);
		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(_zipFile + ".zip");

			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

			int BUFFER = 1024;
			byte data[] = new byte[BUFFER];

			FileInputStream fi = new FileInputStream(_zipFile);
			origin = new BufferedInputStream(fi, BUFFER);
			ZipEntry entry = new ZipEntry(_zipFile.substring(_zipFile.lastIndexOf("/") + 1));
			out.putNextEntry(entry);
			int count;

			while ((count = origin.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			origin.close();

			out.close();
			dest.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getAssetFilePath(String assetFolder, String id) {
		return getFullFilePath(assetFolder + "/" + id + ASSET_EXTENSION);
	}

	public static float[] importHeightMap(String assetFolder, String id, boolean compressed) {
		assetFolder = GEN_FOLDER + assetFolder;
		if (compressed) {
			return importHeightMapZip(assetFolder, id);
		}
		return importHeightMapRaw(assetFolder, id);
	}

	public static float[] importHeightMapRaw(String assetFolder, String id) {
		float[] ret;
		Log.info("Importing HeightMap:" + id);
		String assetFilePath = getAssetFilePath(assetFolder, id);
		
		int size = (int) (CRJavaUtils.context.fileSize(assetFilePath) / 4);
		Log.debug("Size is: " + size);
		ret = new float[size];
		try {
			DataInputStream input = new DataInputStream(CRJavaUtils.context.getInputStream(assetFilePath));
			int pos = 0;
			float c = input.readFloat();
			while (input.available() > 0) {
				// read character
				c = input.readFloat();
				// print
				ret[pos] = c;
				pos++;
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static float[] importHeightMapZip(String assetFolder, String id) {
		String _zipFile = getAssetFilePath(assetFolder, id)+".zip";
		InputStream in;
		float[] ret = null;
		try {
			in = CRJavaUtils.context.getInputStream(_zipFile);
			ZipInputStream zipInput = new ZipInputStream(in);
			zipInput.getNextEntry();
			byte bytes[] = new byte[4];
			byte buffer[] = new byte[1];
			int pos = -1;
			int size;
			// for (int c = zipInput.read(bytes,0,4); c != -1; c =
			// zipInput.read()) {
			int i = 0;
			while ((size = zipInput.read(buffer, 0, 1)) != -1) {
				bytes[i % 4] = buffer[0];
				if (i % 4 == 3) {
					ret = addFloat(ret, bytes, pos, size);
					pos++;
				}
				i++;
			}
			zipInput.closeEntry();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	private static float[] addFloat(float[] ret, byte[] bytes, int pos, int size) {
		int asInt = (bytes[3] & 0xFF) | ((bytes[2] & 0xFF) << 8) | ((bytes[1] & 0xFF) << 16) | ((bytes[0] & 0xFF) << 24);
		float asFloat = Float.intBitsToFloat(asInt);
		/*
		 * Log.debug(pos + ":" + asFloat + " (" + size + ") " + bytes[3] + ":" +
		 * bytes[2] + ":" + bytes[1] + ":" + bytes[0]);
		 */
		if (pos == -1) {
			ret = new float[(int) asFloat];
		} else {
			ret[pos] = asFloat;
		}
		return ret;
	}

	public static void deleteAsset(String file) {
		if (CRJavaUtils.isAndroid()){
			Log.warn("Trying do delete a file '"+file+"' on Android...");
			return;
		}
		String path = getFullFilePath(file);
		File f = new File(path);
		f.delete();
	}

	public static boolean assetExists(String file) {
		String fullFilePath = getFullFilePath(file);
		return CRJavaUtils.context.assetExists(fullFilePath);
	}

	public static JSONObject readJSON(String filePath) {
		JSONObject obj = CRJsonUtils.readJSON(getFullFilePath(filePath));
		return obj;
	}
	
	public static String getJSONPath(String type) {
		if (CRJavaUtils.IS_TEST){
			return  "test-"+type+".json";
		} else {
			return  "game-"+type+".json";
		}
	}

	/* Returns a path within the gen/ folder (copy when its the builder) */
	public static String requestAsset(String file) {
		String newFile = GEN_FOLDER + file;
		if (assetExists(newFile)){
			return newFile;
		}
		if (!assetExists(file)){
			Log.warn("Error requesting asset (maybe its internal):"+file);
			return file;
		}
			
		if (!CRJavaUtils.isRelease()) {
			if (!assetExists(newFile)) {
				copyAsset(file, newFile);
			}
		}
		return newFile;
	}

	private static void copyAsset(String file, String newFile) {
		String source = getFullFilePath(file);
		String dest = getFullFilePath(newFile);
		String folder = newFile.substring(0, newFile.lastIndexOf("/"));
		checkFolder(folder);
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(source);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
			input.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String writeByteBuffer(String id, String type, List<ByteBuffer> data) {
		try {
			String pathForSavableEntity = getPathForSavableEntity(id, type);
			String dirPath = pathForSavableEntity.substring(0, pathForSavableEntity.lastIndexOf("/"));
			checkFolder(dirPath);
			// Allocate a direct (memory-mapped) byte buffer with a byte
			// capacity equal to file's length
			// DO NOT use this approach for copying large files
			// ByteBuffer buf = ByteBuffer.allocateDirect((int)
			// inFile.length());
			// InputStream is = new FileInputStream(inFile);
			/*
			 * while ((b = is.read()) != -1) { buf.put((byte) b); }
			 */
			File file = new File(getFullFilePath(pathForSavableEntity));
			// append or overwrite the file
			boolean append = false;
			FileChannel channel = new FileOutputStream(file, append).getChannel();
			// Flips this buffer. The limit is set to the current position and
			// then
			// the position is set to zero. If the mark is defined then it is
			// discarded.
			// buf.flip();
			// Writes a sequence of bytes to this channel from the given buffer.
			for (ByteBuffer buf : data) {
				 buf.flip();
				channel.write(buf);
			}
			// close the channel
			long size = channel.size();
			channel.close();
			if (size==0){
				Log.fatal("Exported an empty buffer:"+pathForSavableEntity);
			}
			return pathForSavableEntity;
		} catch (IOException e) {
			System.out.println("I/O Error: " + e.getMessage());
		}
		return null;

	}

	public static ArrayList<ByteBuffer> readByteBuffer(String id, String type) {
		String pathForSavableEntity = getPathForSavableEntity(id, type);
		return CRJavaUtils.context.readByteBuffer(getFullFilePath(pathForSavableEntity));
		
		

	}

	public static boolean assetEmpty(String file) {
		String path = getFullFilePath(file);
		if (!assetExists(file)){
			return true;
		}
		
		return CRJavaUtils.context.assetEmpty(path);
	}

	public static String getPropertiesPath() {
		return "gen/game.properties";
	}
}
