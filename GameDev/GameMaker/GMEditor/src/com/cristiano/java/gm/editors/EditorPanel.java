package com.cristiano.java.gm.editors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.cristiano.java.bpM.entidade.AbstractElement;
import com.cristiano.java.gm.editors.controllers.EditorController;
import com.cristiano.java.product.IGameElement;
import com.cristiano.java.product.utils.StringHelper;

public class EditorPanel {
	private static final String WORD_LIST = "mainList";
	private static final String WORD_FUNCTIONS = "functions";
	private static final String WORD_REF = "ref";
	private static final String WORD_STRING = "string";
	private static final String WORD_TAG = "tag";
	private static final String WORD_NUMBER= "number";
	private static final String WORD_OPERATOR= "operator";
	private static final String WORD_IDENTIFIER= "identifier";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EditorController controller;
	private String identifier;
	private JTextPane elementEditor;
	private IGameElement currentElement;
	JPanel panel;
	private JButton btnUndo;
	private JButton btnConfirma;
	private String originalText;

	// styles
	final StyleContext cont = StyleContext.getDefaultStyleContext();
	HashMap<String, AttributeSet> styles = new HashMap<String, AttributeSet>();
	HashMap<String, String> reservedWords = new HashMap<String, String>();
	final AttributeSet defaultStyle = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
	private DefaultStyledDocument doc;

	public EditorPanel(String identifier, EditorController controller) {
		super();
		this.controller = controller;
		this.identifier = identifier;
		currentElement = controller.getElementForIdentifier(identifier);

		panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));

		initStyles();
		setupControles();
		controller.elementSelected(this, identifier);
	}

	private void initStyles() {
		//EditorKeyWordsEnum
		Color brown = new Color(0.3f,0.2f,0.1f);
		
		addStyle(WORD_LIST,Color.RED);
		addStyle(WORD_FUNCTIONS,Color.MAGENTA);
		addStyle(WORD_REF,Color.blue);
		addStyle(WORD_STRING,brown);
		addStyle(WORD_TAG,brown.darker());
		addStyle(WORD_NUMBER,new Color(0.4f,0.2f,0.3f));
		addStyle(WORD_OPERATOR,Color.orange.darker());
		addStyle(WORD_IDENTIFIER,Color.yellow.darker());
		
		
		doc = new DefaultStyledDocument() {
            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                    	
                    	wordR=checkReservedWord(text, wordL, wordR);
                        wordL = wordR;
                    }
                    wordR++;
                }
                checkLetters(text);
                checkStrings(text);
            }

			private void checkStrings(String text) {
              
                checkString(text,'\'','\'',WORD_STRING);
                checkString(text,'{','}',WORD_TAG);
                markWord(text,0,WORD_LIST);
                markWord(text,1,WORD_IDENTIFIER);
			}

			private void markWord(String text, int i, String wordList) {
				AttributeSet attributeSet = styles.get(wordList);
				for (int c=0;c<text.length();c++){
					if (text.charAt(c)=='@'){
						int posFim=StringHelper.pegaPosicaoFinalPalavra(text, c+1, false);
						if (i==0){
							setCharacterAttributes(c, posFim-c, attributeSet, false);
						} else {
							int posIni=posFim;
							posFim=StringHelper.pegaPosicaoFinalPalavra(text, posIni+1, true);
							String word=text.substring(posIni,posFim);
							setCharacterAttributes(posIni, posFim-posIni, attributeSet, false);
						}
					}
				}
				/*AttributeSet attributeSet = styles.get(wordList);
				int posIni = text.indexOf(" ",text.indexOf("@"));
				int posFim= text.indexOf(" ",posIni);
				setCharacterAttributes(posIni, posFim-posIni, attributeSet, false);*/
			}

			private void checkLetters(String text) {
				//setCharacterAttributes(0, text.length(), defaultStyle, false);
				checkLetter(text,'0',WORD_NUMBER);
                checkLetter(text,'1',WORD_NUMBER);
                checkLetter(text,'2',WORD_NUMBER);
                checkLetter(text,'3',WORD_NUMBER);
                checkLetter(text,'4',WORD_NUMBER);
                checkLetter(text,'.',WORD_NUMBER);
                checkLetter(text,'5',WORD_NUMBER);
                checkLetter(text,'6',WORD_NUMBER);
                checkLetter(text,'7',WORD_NUMBER);
                checkLetter(text,'8',WORD_NUMBER);
                checkLetter(text,'9',WORD_NUMBER);
                checkLetter(text,'(',WORD_NUMBER);
                checkLetter(text,')',WORD_NUMBER);
                checkLetter(text,'+',WORD_OPERATOR);
                checkLetter(text,'-',WORD_OPERATOR);
                checkLetter(text,'*',WORD_OPERATOR);
                checkLetter(text,'/',WORD_OPERATOR);
                checkLetter(text,'=',WORD_OPERATOR);
			}
			
			private void checkLetter(String text, char letter,String style) {
				AttributeSet attributeSet = styles.get(style);
				for (int c=0;c<text.length();c++){
					if (text.charAt(c)==letter){
						setCharacterAttributes(c, 1, attributeSet, false);
					}
				
			}}
			

			private void checkString(String text, char abre, char fecha, String style) {
				int pIni=-1;
				AttributeSet attributeSet = styles.get(style);
				for (int c=0;c<text.length();c++){
					if (pIni==-1){
						if (text.charAt(c)==abre){
							pIni=c;
						} 
					} else{
					if (text.charAt(c)==fecha){
						setCharacterAttributes(pIni, c - pIni+1, attributeSet, false);
						pIni=-1;
					}
					}
				}
				
			}

			private int checkReservedWord(String text, int wordL, int wordR) {
				boolean isReserved=false;
				Iterator<Entry<String, String>> it = reservedWords.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
					String key = pairs.getKey();
					String words=reservedWords.get(key);
					String substring = text.substring(wordL, wordR);
					if (substring.matches(words)){
						if (isOperator(substring)){
							wordL++;
						}
						
						if (text.charAt(wordR)=='.'){
							wordR=StringHelper.pegaPosicaoFinalPalavra(text, wordR, true);
						}
						setCharacterAttributes(wordL, wordR - wordL, styles.get(key), false);
						isReserved=true;
					}
				}
				
				if (!isReserved){
				   // setCharacterAttributes(wordL, wordR - wordL, defaultStyle, false);
				}
				return wordR;
			}

            private boolean isOperator(String substring) {
				return substring.startsWith("=")||substring.startsWith("+")||substring.startsWith("*")||substring.startsWith("-")||substring.startsWith("/");
			}

			public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offs);

                after=checkReservedWord(text, before, after);
                checkLetters(text);
                checkStrings(text);
            }
        };
	}

	private void addStyle(String wordList, Color cor) {
		IGameElement words = controller.em.pickAnyOne("editorKeyWords " + wordList);
		if (words != null) {
			String propertyAsGEList = StringHelper.clear(words.getProperty("value"));
			propertyAsGEList = "(\\W)*(" + propertyAsGEList.replace(" ", "|") + ")";
			reservedWords.put(wordList, propertyAsGEList);
		}
		// propertyAsGEList="("+propertyAsGEList.replace(" ", "|")+")";
		AttributeSet style = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, cor);
		style = cont.addAttribute(style, StyleConstants.Bold, true);
		
		styles.put(wordList, style);

	}

	private void setupControles() {

		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		panel.setPreferredSize(new Dimension(300, 200));
		// panel.setBounds(100, 100, 450, 300);

		JPanel panelParent = new JPanel();
		sl_panel.putConstraint(SpringLayout.NORTH, panelParent, 0, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, panelParent, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, panelParent, 0, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, panelParent, 0, SpringLayout.EAST, panel);
		SpringLayout sl_panelParent = new SpringLayout();
		panelParent.setLayout(sl_panelParent);

		JTextPane popOutEditor = createElementEditor();
		// popOutEditor.setLineWrap(true);
		// popOutEditor.setWrapStyleWord(true);

		JScrollPane localScrollEditor = new JScrollPane(popOutEditor);

		sl_panelParent.putConstraint(SpringLayout.WEST, localScrollEditor, 0, SpringLayout.WEST, panelParent);
		sl_panelParent.putConstraint(SpringLayout.SOUTH, localScrollEditor, 0, SpringLayout.SOUTH, panelParent);
		sl_panelParent.putConstraint(SpringLayout.EAST, localScrollEditor, 0, SpringLayout.EAST, panelParent);
		sl_panel.putConstraint(SpringLayout.WEST, localScrollEditor, 0, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, localScrollEditor, 0, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.NORTH, localScrollEditor, 0, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, localScrollEditor, 0, SpringLayout.EAST, panel);

		panelParent.add(localScrollEditor);

		JPanel panelTop = new JPanel();
		sl_panelParent.putConstraint(SpringLayout.NORTH, localScrollEditor, 0, SpringLayout.SOUTH, panelTop);
		sl_panelParent.putConstraint(SpringLayout.NORTH, panelTop, 0, SpringLayout.NORTH, panelParent);
		sl_panelParent.putConstraint(SpringLayout.WEST, panelTop, 0, SpringLayout.WEST, panelParent);
		sl_panelParent.putConstraint(SpringLayout.SOUTH, panelTop, 20, SpringLayout.NORTH, panelParent);
		sl_panelParent.putConstraint(SpringLayout.EAST, panelTop, 0, SpringLayout.EAST, panelParent);
		panelParent.add(panelTop);
		SpringLayout sl_panelTop = new SpringLayout();
		panelTop.setLayout(sl_panelTop);

		JButton btnVisualiza = new JButton("Visualiza");
		sl_panelTop.putConstraint(SpringLayout.EAST, btnVisualiza, 0, SpringLayout.EAST, panelTop);
		btnVisualiza.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				visualiza();
			}
		});
		sl_panelTop.putConstraint(SpringLayout.NORTH, btnVisualiza, 0, SpringLayout.NORTH, panelTop);
		sl_panelTop.putConstraint(SpringLayout.SOUTH, btnVisualiza, 0, SpringLayout.SOUTH, panelTop);
		panelTop.add(btnVisualiza);

		JLabel lblElementName = new JLabel("ElementName");
		sl_panelTop.putConstraint(SpringLayout.NORTH, lblElementName, 2, SpringLayout.NORTH, panelTop);
		sl_panelTop.putConstraint(SpringLayout.WEST, lblElementName, 5, SpringLayout.WEST, panelTop);
		lblElementName.setVerticalAlignment(SwingConstants.TOP);
		panelTop.add(lblElementName);
		lblElementName.setText(identifier);

		btnConfirma = new JButton("OK");
		btnConfirma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commitChanges();
			}
		});
		sl_panelTop.putConstraint(SpringLayout.SOUTH, btnConfirma, 0, SpringLayout.SOUTH, btnVisualiza);
		sl_panelTop.putConstraint(SpringLayout.EAST, btnConfirma, -4, SpringLayout.WEST, btnVisualiza);
		panelTop.add(btnConfirma);
		btnConfirma.setVisible(false);

		btnUndo = new JButton("Undo");
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undoChanges();
			}
		});
		sl_panelTop.putConstraint(SpringLayout.SOUTH, btnUndo, 0, SpringLayout.SOUTH, panelTop);
		sl_panelTop.putConstraint(SpringLayout.EAST, btnUndo, -6, SpringLayout.WEST, btnConfirma);
		panelTop.add(btnUndo);
		btnUndo.setVisible(false);

		if (currentElement != null) {
			JLabel lblEstende = new JLabel("<dynamic>");
			sl_panelTop.putConstraint(SpringLayout.NORTH, lblEstende, 2, SpringLayout.NORTH, btnVisualiza);
			sl_panelTop.putConstraint(SpringLayout.WEST, lblEstende, 6, SpringLayout.EAST, lblElementName);
			lblEstende.setVerticalAlignment(SwingConstants.TOP);
			panelTop.add(lblEstende);
			if (((AbstractElement) currentElement).getEstende() != null) {
				lblEstende.setText(((AbstractElement) currentElement).getEstende().getIdentifier());
			} else {
				lblEstende.setText("");
			}
		}

		panel.add(panelParent);
	}

	protected void visualiza() {
		controller.visualiza(currentElement);

	}

	private JTextPane createElementEditor() {
		final EditorPanel ep = this;
		elementEditor = new JTextPane(doc);
		elementEditor.addCaretListener(new CaretListener() {
			
			@Override
			public void caretUpdate(CaretEvent e) {
				selectText(Math.min(e.getDot(), e.getMark()),Math.max(e.getDot(), e.getMark()));
				
			}
		});
		elementEditor.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				textChanged(elementEditor.getText());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				textChanged(elementEditor.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				textChanged(elementEditor.getText());
			}
		});
		return elementEditor;
	}

	protected void selectText(int min, int max) {
		String selectedText=elementEditor.getText().substring(min,max);
		if (selectedText.trim().equals("")){
			return;
		}
		//if (selectedText.startsWith("{")){
			controller.filterBy(StringHelper.clear(selectedText));
		//}
	}

	private void checkChanges() {
		if (btnConfirma == null)
			return;
		if (originalText == null)
			return;
		if (elementEditor == null)
			return;
		btnConfirma.setVisible(!originalText.equals(elementEditor.getText()));
		btnUndo.setVisible(!originalText.equals(elementEditor.getText()));
		controller.saveIfChanged();
	}

	protected void undoChanges() {
		setText(originalText);
		checkChanges();
	}

	protected void commitChanges() {
		controller.elementChanged(this, elementEditor.getText());
		setText(elementEditor.getText());
		checkChanges();
	}

	protected void textChanged(String text) {
		checkChanges();

	}

	public void setCurrentElement(IGameElement currentElement2) {
		this.currentElement = currentElement2;
	}

	public IGameElement getCurrentElement() {
		return currentElement;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setText(String text) {
		elementEditor.setText(text);
		originalText = text;

	}

	public JPanel getPanel() {
		return panel;
	}

	public void resize(Dimension size, int qtd) {

		int width = size.width - 5;
		int height = size.height / qtd - 5;
		panel.setPreferredSize(new Dimension(width, height));
	}

	private int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	private int findFirstNonWordChar(String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}
}
