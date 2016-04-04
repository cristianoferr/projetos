package com.cristiano.java.bpM.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.cristiano.consts.Extras;
import com.cristiano.data.ISerializeJSON;
import com.cristiano.java.bpM.ElementManager;
import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.bpM.entidade.GenericElement;
import com.cristiano.java.bpM.entidade.blueprint.Blueprint;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.BPUtils;
import com.cristiano.utils.Log;

public class EMLoader {

	private ElementManager em;

	public EMLoader(ElementManager elementManager) {
		this.em = elementManager;
	}

	String headerFile = "";
	public static int lastElement = 0;
	public static int saveElementPosition = 0;
	private String line;
	private Blueprint bp;

	HashMap<String, BufferedWriter> writers = new HashMap<String, BufferedWriter>();
	String lastWriter = null;

	public BufferedWriter getWriter(IGameElement el) {
		String fileOut = BPUtils.clear(el.getParamAsText(Extras.LIST_OUTPUT,
				Extras.PROPERTY_FILE));
		if (fileOut.equals("")) {
			fileOut = lastWriter;
		} else {
			lastWriter = fileOut;
		}
		if (fileOut == null) {
			Log.fatal("No writer definer");
		}
		BufferedWriter writer = writers.get(fileOut);
		if (writer == null) {
			File file = new File(Extras.BLUEPRINTS_PATH +fileOut);
			FileWriter fw;
			try {
				fw = new FileWriter(file);
				writer = new BufferedWriter(fw);
				writers.put(fileOut, writer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return writer;
	}

	public void saveBlueprints() throws IOException {
		for (int i = 0; i < saveElementPosition; i++) {
			IGameElement el = em.getElementAt(i);
			BufferedWriter bw = getWriter(el);
			if (!((GenericElement) el).isMastered()) {
				bw.write(((AbstractElement) el).export() + "\n\n");
			}
		}

		Iterator<Entry<String, BufferedWriter>> it = writers.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<String, BufferedWriter> pairs = it.next();
			String key = pairs.getKey();
			BufferedWriter writer = writers.get(key);
			writer.close();
		}

		writers.clear();
	}

	public void loadBlueprintsFrom(String fileName) throws IOException {

		BufferedReader br;
		File file = new File(fileName);
		FileReader fr = new FileReader(file);

		br = new BufferedReader(fr);

		boolean isHeader = true;
		try {
			line = br.readLine();

			bp = null;
			boolean comentario = false;
			while (line != null) {
				line = line.trim();
				if (isHeader) {
					headerFile += line + "\n";
					if (line.contains("//Blueprints")) {
						isHeader = false;
					}
				}
				if (line.startsWith("//")) {
					line = line.substring(0, line.indexOf("//"));
				}

				// System.out.println("linha:"+line);

				if (bp == null) {
					loadLineInclude(file);
					loadLineDefine();
					loadLineFunction();
					loadElement();
				} else {
					loadLineBlueprint();
				}
				lastElement = em.size();
				line = br.readLine();
			}
		} finally {
			br.close();
		}

	}

	private String loadLineDefine() {
		if (line.startsWith("@define ")) {
			line = line.replace("@define ", "");
			String var = line.substring(0, line.indexOf("="));
			String val = line.substring(line.indexOf("=") + 1);
			em.setVar(var, val);
			line = "";
		}
		return line;
	}

	private String loadLineFunction() {
		if (line.startsWith("@function ")) {
			line = line.replace("@function ", "");
			String function = line.substring(0, line.indexOf("("));
			line = line.substring(function.length() + 1);
			String params = line.substring(0, line.indexOf(")"));
			line = line.substring(params.length() + 1);
			String val = line.substring(line.indexOf("=") + 1);
			em.addUserFunction(function, params, val);
			line = "";
		}
		return line;
	}

	private void loadLineInclude(File file) throws IOException {
		if (line.startsWith("@include ")) {
			line = line.replace("@include ", "");
			loadBlueprintsFrom(file.getParent() + File.separator + line);
			line = "";
		}
		if (line.startsWith("@import ")) {
			line = line.replace("@import ", "");
			loadBlueprintsFrom(file.getParent() + File.separator + line);
			line = "";
		}
	}

	private void loadElement() {
		IGameElement estende = null;
		estende = findEstende(estende);
		if (line.startsWith("@mod")) {
			line = line.replace("@mod ", "");
			bp = em.createMod(estende);
			bp.setProperty("identifier='" + line + "'");
			line = "";
		} else if (line.startsWith("@factory")) {
			line = line.replace("@factory ", "");
			bp = em.createFactory(estende);
			bp.setProperty("identifier='" + line + "'");
			line = "";
		} else if (line.startsWith("@blueprint")) {
			line = line.replace("@blueprint ", "");
			bp = em.createBlueprint(estende);
			bp.setProperty("identifier='" + line + "'");
			line = "";
		}
		if (estende != null) {
			((GenericElement) estende).addLeaf(bp);
		}
	}

	private IGameElement findEstende(IGameElement estende) {
		if (line.contains(":")) {
			String nameEstende = line.substring(line.indexOf(":") + 1);
			estende = em.getElementByIdentifier(nameEstende);
			line = line.substring(0, line.indexOf(":"));
		}
		return estende;
	}

	private void loadLineBlueprint() throws IOException {

		if (line.equals("@end")) {
			bp = finalizaInclusaoBlueprint(bp);
			line = "";
			bp = null;
		}

		if (!line.equals("")) {
			if (bp == null)
				throw new IOException(
						"Defining information without a defined Blueprint:"
								+ line);
			bp.setParam(line);
			line = "";
		}

	}

	private Blueprint finalizaInclusaoBlueprint(Blueprint bp) {
		String domainType = bp.getParamH("domain", "type", true);
		domainType = domainType.replace("+", "");
		domainType = domainType.replace("  ", " ");
		bp.addTag(domainType);
		// System.out.println("BP Final:"+bp);
		bp = null;
		return bp;
	}

	public void markSaving() {
		saveElementPosition = em.size();
	}

	public void markEnd() {
		lastElement = em.size();

	}

}
