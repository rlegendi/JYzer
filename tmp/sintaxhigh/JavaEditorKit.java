package jyzer.gui.guihelpers;

import javax.swing.Action;
import javax.swing.text.*;

public class JavaEditorKit extends DefaultEditorKit {

	private JavaContext preferences;

	public JavaEditorKit() {
		super();
		preferences = new JavaContext();
	}

	private JavaEditorKit(JavaContext preferences) {
		super();
		this.preferences = preferences;
	}

	public JavaContext getStylePreferences() {
		return preferences;
	}

	public void setStylePreferences(JavaContext preferences) {
		this.preferences = preferences;
	}

	// --- super methods -------------------------------------

	public Caret createCaret() {
		return null;
	}

	public Document createDefaultDocument() {
		return new JavaDocument();
	}

	public Action[] getActions() {
		return null;
	}

	public String getContentType() {
		return "text/java";
	}

	public ViewFactory getViewFactory() {
		return getStylePreferences();
	}

	public Object clone() {
		return new JavaEditorKit(preferences);
	}

}
