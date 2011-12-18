/*
 * JYzer - A Java Bytecode Analyzer.
 * Copyright (C) 2005 Legendi Richard Oliver
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package jyzer;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;

import jyzer.gui.*;
import jyzer.gui.guihelpers.*;
import jyzer.items.exceptions.ParsingException;

// Java Help System
import javax.help.*;

// My own lib :-)
import edu.lro.gui.*;

/**
 * This is the main frame of the application. Contains the default GUI,
 * and also takes care of the properties file.
 *
 * @author Legendi Richard Oliver
 * @version 1.0
 *
 * Created on 2005.01.23 -> 31.
 */
public final class JYzer extends JFrame {

	/** The version number of the application. */
	public static final double VERSION = 0.18;

	/** The name of the classfile we're processing. */
	private String classFileName = "<N/A>";
	/** The classfile object*/
	private ClassFile cf;

	// program properties
	Properties props = new Properties();
	private String lastOpenPath = ".";
	private String lastSavePath = ".";
	private static final int RECENT_FILES_MAX_SIZE;
	private static Vector<String> recentFileNames;

	// state variables
	private boolean startup = true;

	// help
	private HelpSet    hs;
	private HelpBroker hb;

	static { // Creating the recent files vector.
		RECENT_FILES_MAX_SIZE = 5;
		recentFileNames    = new Vector<String>();
	}

	{ // Class initializer for loading the application properties.
		loadProperties();
	}

	/**
	 * The constructor. Creates the GUI, menubar & sets the default program properties.
	 */
	public JYzer() {
		super();

		initHelp();
		buildUpMenus();
		buildUpGUI();
		setDefaultValues();
	}// constr.JYzer()

	// --- Properties processing ----------------------------------------------------

	/**
	 * Loading the program properties (eg. the directory of the last succesfull parsing, saving, etc.).
	 * It there's no properties file given, then a default one is also created here.
	 */
	private void loadProperties() {

		File f = new File("defaultProperties");
		if ( ! f.exists() ) {
			System.err.println("Properties file was not found, creating a default one.");
			saveProperties();
		}

		FileInputStream in = null;

		try {
			in = new FileInputStream("defaultProperties");
			props.load(in);
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(
				this,
				"Warning:",
				"An unexpected exception was thrown while processing default properties!",
				JOptionPane.ERROR_MESSAGE
			);

			if ( in != null) {
				try { in.close(); } catch (Exception e) { e.printStackTrace(); }
			}
			return;
		}

		// update
		lastOpenPath = props.getProperty("path.lastopen");
		lastSavePath = props.getProperty("path.lastsave");

		int recentSize = Integer.parseInt( props.getProperty("recent.size") );
		for (int i=0; i<recentSize; ++i) {
			recentFileNames.add( props.getProperty("recent.item." + i) );
		}

	}// loadProperties

	/**
	 * Saving the actual program properties.
	 */
	private void saveProperties() {

		FileOutputStream out = null;

		try {
			out = new FileOutputStream("defaultProperties");

			props.put("path.lastopen", lastOpenPath);
			props.put("path.lastsave", lastSavePath);
			props.put("recent.size", String.valueOf( recentFileNames.size() ) );

			for (int i=0; i<recentFileNames.size(); ++i) {
				props.put("recent.item." + i, recentFileNames.get(i) );
			}

			props.store(out, "--- Program properties ---");
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(
				this,
				"Warning:",
				"An unexpected exception was thrown while processing default properties!",
				JOptionPane.ERROR_MESSAGE
			);

			if ( out != null) {
				try { out.close(); } catch (Exception e) { e.printStackTrace(); }
			}
		}

	}// saveProperties

	// --- Building the GUI ---------------------------------------------------------

	/**
	 * Initializing the help system.
	 */
	private void initHelp() {
		// Find the HelpSet file and create the HelpSet object:
		String      helpHS = "doc/help/jyzer.hs";
		ClassLoader cl     = getClass().getClassLoader();

		try {
			URL hsURL = HelpSet.findHelpSet(cl, helpHS);
			hs = new HelpSet(null, hsURL);
	 	} catch (Exception e) {
			// Say what the exception really is
			System.err.println( "HelpSet " + e.getMessage() );
			System.err.println( "HelpSet "+ helpHS +" not found" );

			e.printStackTrace();
			return;
	 	}

		// Create a HelpBroker object:
	 	hb = hs.createHelpBroker();
	}// initHelp


	/**
	 * This method writes a letter to Santa for Xmas :-) Ok, that was just a joke.
	 * Building the menu.
	 */
	private void buildUpMenus() {
		setJMenuBar(menuBar);

		menuBar.add(fileMenu);
		/*
		menuBar.add(validateMenu);
		*/
		menuBar.add(optionsMenu);
		menuBar.add(helpMenu);

		fileMenu.addSeparator();
		recentUpdate("");

		/*
		This will be needed in future if I implement editing. Will be helpful, but yet it is unnecessary.

		validateMenu.add(validationMenuItem);
		validationMenuItem.setState(true);
		*/

		optionsMenu.add(preferencesMenuItem);
		optionsMenu.addSeparator();
		optionsMenu.add(clearRecentFilesMenuItem);

		helpMenu.add(helpMenuItem);
		helpMenu.addSeparator();
		helpMenu.add(hexCtrMenuItem);
		helpMenu.addSeparator();
		helpMenu.add(licenseMenuItem);
		helpMenu.add(aboutMenuItem);
		helpMenu.addSeparator();
		helpMenu.add(openJavaWebMenuItem);

		fileMenu.setMnemonic('f');
		validateMenu.setMnemonic('v');
		optionsMenu.setMnemonic('o');
		helpMenu.setMnemonic('h');

		openMenuItem.setMnemonic('o');
		saveSummaryMenuItem.setMnemonic('s');
		saveSourceMenuItem.setMnemonic('d');
		closeMenuItem.setMnemonic('c');
		previewMenuItem.setMnemonic('v');
		printMenuItem.setMnemonic('p');
		exitMenuItem.setMnemonic('e');
		validationMenuItem.setMnemonic('f');
		preferencesMenuItem.setMnemonic('p');
		clearRecentFilesMenuItem.setMnemonic('c');
		helpMenuItem.setMnemonic('h');
		hexCtrMenuItem.setMnemonic('x');
		licenseMenuItem.setMnemonic('l');
		aboutMenuItem.setMnemonic('a');
		openJavaWebMenuItem.setMnemonic('w');

		// giving keystrokes
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		saveSummaryMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		saveSourceMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
		previewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		printMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		//preferencesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_, InputEvent.CTRL_MASK)); ???
		clearRecentFilesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
		validationMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		hexCtrMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));

		// adding listeners
		openMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				openMenuItemActionPerformed();
			}
		});

		saveSummaryMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				saveSummaryMenuItemActionPerformed();
			}
		});

		saveSourceMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				saveSourceMenuItemActionPerformed();
			}
		});

		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				clear();
			}
		});

		previewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				previewMenuItemActionPerformed();
			}
		});

		printMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				printMenuItemActionPerformed();
			}
		});

		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				exitMenuItemActionPerformed();
			}
		});

		preferencesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				preferencesMenuItemActionPerformed();
			}
		});

		clearRecentFilesMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				clearRecentFilesMenuItemActionPerformed();
			}
		});

		helpMenuItem.addActionListener( new CSH.DisplayHelpFromSource(hb) );

		hexCtrMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (null == hexCtr || ! hexCtr.isShowing() ) {
					hexCtr = new HexCtr(JYzer.this);
				} else {
					hexCtr.toFront();
				}
			}
		});

		licenseMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				licenseMenuItemActionPerformed();
			}
		});

		aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				aboutMenuItemActionPerformed();
			}
		});

		openJavaWebMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				openJavaWebMenuItemActionPerformed();
			}
		});

	}// buildUpMenus

	/**
	 * Building up the GUI.
	 */
	private void buildUpGUI() {
		getContentPane().add(mainPanel);

		mainPanel.setLayout( new BorderLayout() );
		mainPanel.add(tabbedPane);

		JPanel statePanel = new JPanel( new BorderLayout() );
		mainPanel.add(statePanel, BorderLayout.SOUTH);
		statePanel.setBorder( BorderFactory.createEmptyBorder(3, 3, 3, 3) );
		statePanel.add(stateLabel, BorderLayout.CENTER);
		statePanel.add(progressBar, BorderLayout.EAST);

		tabbedPane.addTab("Summary", new ImageIcon( getClass().getResource("/data/pix/sum.jpg") ), summaryPane);
		tabbedPane.addTab("Hexa View", new ImageIcon( getClass().getResource("/data/pix/hexaView.jpg") ), hexaViewPane );
		tabbedPane.addTab("Disassembled Source", new ImageIcon( getClass().getResource("/data/pix/dis.gif") ), disassemblePane);
		tabbedPane.addTab("Constant Pool", new ImageIcon( getClass().getResource("/data/pix/cp.gif") ), constantPoolPane );
		tabbedPane.addTab("Fields", new ImageIcon( getClass().getResource("/data/pix/field.gif") ), fieldsPane );
		tabbedPane.addTab("Methods", new ImageIcon( getClass().getResource("/data/pix/method.gif") ), methodsPane );
	}// buildUpGUI

	/**
	 * Setting the default GUI values.
	 */
	private void setDefaultValues() {
		setTitleString();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		progressBar.setIndeterminate(false);

		final JFrame tmp = this;
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				int back = JOptionPane.showConfirmDialog(tmp, "Are you sure wish to quit?", "Quitting:", JOptionPane.YES_NO_OPTION);

				if ( JOptionPane.YES_OPTION == back ) {
					saveProperties();
					System.exit(0);
				}
			}
		});

		// --- Unimplemented methods -----------------
		previewMenuItem.setEnabled(false);
		printMenuItem.setEnabled(false);

		String unimp_text = "<html><font size=+1><b>UNIMPLEMENTED YET!</b></font></html>";
		previewMenuItem.setToolTipText(unimp_text);
		printMenuItem.setToolTipText(unimp_text);
		// -------------------------------------------

		Utils.setSize( this, new Dimension(800, 600) );
		Utils.putToMiddle(this);
		setVisible(true);
	}// setDefaultValues

	// --- GUI helper methods ------------------------------------------------------

	/**
	 * Sets the label of this frame to a usable string.
	 */
	private void setTitleString() {
		setTitle("JYzer v" + VERSION + " - [" + classFileName + "]");
	}// setTitleString

	/**
	 * This method sets the string of the state label that is visible for a few seconds.
	 *
	 * @param message the message that has to be set.
	 */
	private void setStateLabelString(final String message) {
		stateLabel.setText(message);

		Thread thread = new Thread() {
			int delay = 8000; // delay

			public void run() {
				try {
					sleep(delay);
				} catch (InterruptedException ie) {
					// log it!
					ie.printStackTrace();
				}

				stateLabel.setText(" ");
			}
		};

		thread.start();
	}// setStateLabelString

	/**
	 * Updating the list of the last opened files that can be accessed from the file menu easily.
	 * The <code>with</code> string can be the empty srting (<code>""</code>), and that means just an update,
	 * otherwise it is placed to the first place of the recent files.
	 *
	 * @param with could be the empty string, or an absolute path & filename.
	 */
	private void recentUpdate(final String with) {
		// needed 'cause cannot remove separetors ...
		fileMenu.removeAll();

		if ( recentFileNames.contains(with) ) {
			int i = recentFileNames.indexOf(with);
			String obj = recentFileNames.get(i);
			recentFileNames.remove(i);
			recentFileNames.add(0, obj);
		} else if ( ! with.equals("") && ! recentFileNames.contains(with) ) {
			recentFileNames.add(0, with);

			if ( RECENT_FILES_MAX_SIZE < recentFileNames.size() ) {
				recentFileNames.remove(recentFileNames.size() - 1);
			}

		}

		fileMenu.add(openMenuItem);
		fileMenu.add(saveSummaryMenuItem);
		fileMenu.add(saveSourceMenuItem);
		fileMenu.add(closeMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(previewMenuItem);
		fileMenu.add(printMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		if ( ! recentFileNames.isEmpty() ) {
			// ... like this :-(
			fileMenu.addSeparator();

			for (int i=0; i<recentFileNames.size(); ++i) {
				recentMenuItems[i] = new JMenuItem( recentFileNames.get(i) );
				fileMenu.add( recentMenuItems[i] );
				recentMenuItems[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						String path = ( (JMenuItem) ae.getSource() ).getText();
						recentFileNames.remove(path);
						recentFileNames.add(0, path);
						doOpen(path);
					}
				});
			}

		}
	}// recentUpdate

	/**
	 * Execute opening.
	 *
	 * @param path the absolute path to the file that has to be opened.
	 */
	private void doOpen(final String path) {
		final String newClassFileName = path.substring( path.lastIndexOf('\\') + 1 );

		if (newClassFileName.equals( classFileName ) ) {
			return;
		}

		classFileName = newClassFileName;
		cf = new ClassFile( path, JYzer.this );

		parse( path );
	}// doOpen

	/**
	 * Closing the opened classfile, and clearing every information from each pane.
	 */
	private void clear() {
		summaryPane.clear();
		hexaViewPane.clear();
		disassemblePane.clear();
		constantPoolPane.clear();
		fieldsPane.clear();
		methodsPane.clear();

		classFileName = "<N/A>";
		setTitleString();
	}// clear

	// --- Implementation of the menu item actions ------------------------------------------

	/**
	 * Implementation of the open menu item.
	 */
	private void openMenuItemActionPerformed() {
		JFileChooser fc = new JFileChooser(lastOpenPath);
		fc.setFileFilter( new UniversalFileFilter("class", "Compiled Java Bytecodes") );

		int back = fc.showOpenDialog(this);
		if (back != JFileChooser.APPROVE_OPTION) {
			return;
		}

		doOpen( fc.getSelectedFile().getAbsolutePath() );
	}// openMenuItemActionPerformed

	/**
	 * Implementation of the save summary menu item. Saving the text that is visible on the Summary pane.
	 * The file gets <code>html</code> extension whatever happens.
	 */
	private void saveSummaryMenuItemActionPerformed() {
		if (startup) {
			return;
		}

		JFileChooser fc = new JFileChooser(lastSavePath);
		fc.setFileFilter( new UniversalFileFilter("html", "HTML files") );
		fc.setSelectedFile( new File (lastSavePath + classFileName.substring( 0, classFileName.lastIndexOf(".class") ) + ".html" ) );

		int back = fc.showSaveDialog(this);
		if (back != JFileChooser.APPROVE_OPTION) {
			return;
		}

		String saveFileName = fc.getSelectedFile().getAbsolutePath();

		File file = new File(saveFileName);
		if ( file.exists() ) {
			int sure = JOptionPane.showConfirmDialog(this,
					"A file already exists with this name.\n Are you sure wish to use this name?",
					"Warning!",
					JOptionPane.YES_NO_OPTION);

			if (sure != JOptionPane.YES_OPTION) {
				return;
			}
		}

		try {

			if ( ! saveFileName.toLowerCase().endsWith(".html") ) {
				saveFileName += ".html";
			}

			PrintWriter pw = new PrintWriter( new FileWriter(saveFileName) );
			pw.print( summaryPane.getText() );
			pw.close();

		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, "Save Failed!\nIOException was risen: " + ioe.getMessage(), "Exception:", JOptionPane.ERROR_MESSAGE);
			return;
		}

		setStateLabelString("File: " + saveFileName + " was successfully saved.");
		lastSavePath = fc.getSelectedFile().getAbsolutePath();
	}// saveSummaryMenuItemActionPerformed

	/**
	 * Implementation of the save source menu item. Saves the disassembled source that is visible on the Disassemble pane.
	 * The file gets <code>java</code> extension whatever happens.
	 */
	private void saveSourceMenuItemActionPerformed() {
		if (startup) {
			return;
		}

		JFileChooser fc = new JFileChooser(lastSavePath);
		fc.setFileFilter( new UniversalFileFilter("java", "Java sourcecodes") );
		fc.setSelectedFile( new File (lastSavePath + classFileName.substring( 0, classFileName.lastIndexOf(".class") ) + ".java" ) );

		int back = fc.showSaveDialog(this);
		if (back != JFileChooser.APPROVE_OPTION) {
			return;
		}

		String saveFileName = fc.getSelectedFile().getAbsolutePath();

		File file = new File(saveFileName);
		if ( file.exists() ) {
			int sure = JOptionPane.showConfirmDialog(this,
					"A file already exists with this name.\n Are you sure wish to use this name?",
					"Warning!",
					JOptionPane.YES_NO_OPTION);

			if (sure != JOptionPane.YES_OPTION) {
				return;
			}
		}

		try {

			if ( ! saveFileName.toLowerCase().endsWith(".java") ) {
				saveFileName += ".java";
			}

			PrintWriter pw = new PrintWriter( new FileWriter(saveFileName) );
			pw.print( disassemblePane.getText() );
			pw.close();

		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, "Save Failed!\nIOException was risen: " + ioe.getMessage(), "Exception:", JOptionPane.ERROR_MESSAGE);
			return;
		}

		setStateLabelString("File: " + saveFileName + " was successfully saved.");
		lastSavePath = fc.getSelectedFile().getAbsolutePath();
	}// saveSourceMenuItemActionPerformed

	/**
	 * Implementation of the preview menu item.
	 * <b>UNIMPLEMENTED yet.</b>
	 */
	private void previewMenuItemActionPerformed() {
		if (startup) {
			return;
		}

		/*
		// Will like something like this:
		// Get a PrinterJob
		PrinterJob job = PrinterJob.getPrinterJob();
		// Ask user for page format (e.g., portrait/landscape)
		PageFormat pf = job.pageDialog(job.defaultPage());
		*/
	}// previewMenuItemActionPerformed

	/**
	 * Implementation of the print menu item.
	 * <b>UNIMPLEMENTED yet.</b>
	 */
	private void printMenuItemActionPerformed() {
		if (startup) {
			return;
		}

		/*
		// Will like something like this:
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        */
	}// printMenuItemActionPerformed

	/**
	 * Implementation of the exit menu item.
	 */
	private void exitMenuItemActionPerformed() {
		saveProperties();
		System.exit(0);
	}// exitMenuItemActionPerformed

	/*
	private void validationMenuItemActionPerformed() {
	}
	*/

	/**
	 * Implementation of the preferences menu item.
	 */
	private void preferencesMenuItemActionPerformed() {
	}// preferencesMenuItemActionPerformed

	/**
	 * Implementation of the clear recent files menu item.
	 */
	private void clearRecentFilesMenuItemActionPerformed() {
		recentFileNames.clear();
		recentUpdate("");
	}// clearRecentFilesMenuItemActionPerformed

	/**
	 * Implementation of the license menu item.
	 */
	private void licenseMenuItemActionPerformed() {
		TextDisplayer textDisplayer = new TextDisplayer();
	}// licenseMenuItemActionPerformed

	/**
	 * Implementation of the about menu item.
	 */
	private void aboutMenuItemActionPerformed() {
		AboutDialog aboutDialog = new AboutDialog( this, true, "JYzer", String.valueOf( VERSION ),
				"Software Engineering Project in summer 2005.\nA minimal Java disassembler.", "2005");
	}// aboutMenuItemActionPerformed

	/**
	 * Implementation of the "visit my page" menu item.
	 */
	private void openJavaWebMenuItemActionPerformed() {
	}// openJavaWebMenuItemActionPerformed

	// --- Working methods ----------------------------------------------------------------------------

	/**
	 * Performing the parsing. Works with an other thread to don't burden the swing thread.
	 *
	 * @param path the path of the file that has to be parsed.
	 */
	private void parse(final String path) {
		Thread parser = new Thread() {
			public void run() {
				// TODO: find the reason for npe in the awt event queue ! Serious

				/*
				If the system is under serious usage, the awt event queue throws a NullPointerException
				for some reason, if I want to set the state of the progressbar. Don't know why ...
				*/

				progressBar.setIndeterminate(true);

				try {

					// make the parsing
					cf.parse();

					// make the validation if needed ...
					/*
					if ( validationMenuItem.getState() ) {
						cf.validate();
					}
					*/

					// show the result
					summaryPane.setText(  cf.getHTMLDescription() );
					hexaViewPane.setData( new File(path) );
					disassemblePane.setClassFile( cf );
					constantPoolPane.setData( cf.getConstantPoolItem().getConstantPoolData() );
					fieldsPane.setData( cf.getFieldItem().getFields(), cf.getFieldItem().getFieldData( fieldsPane.isShowingRealModifiersOnly() ) );
					methodsPane.setData( cf.getMethodItem().getMethods(), cf.getMethodItem().getMethodData( methodsPane.isShowingRealModifiersOnly() ) );

					// update state
					setTitleString();
					setStateLabelString("<HTML><FONT color=\"green\">File: " + classFileName + " was successfully opened & disassembled, " +
							"and it seems it is a valid classfile.</FONT></HTML>");
					recentUpdate(path);
					lastOpenPath = path.substring(0, path.lastIndexOf("\\") );
					startup = false;
				} catch (ParsingException pe) { // if not a classfile
					progressBar.setIndeterminate(false);
					Toolkit.getDefaultToolkit().beep();
					clear();

					JOptionPane.showMessageDialog(JYzer.this, pe.getMessage(), "Non-valid classfile!", JOptionPane.ERROR_MESSAGE);
					return;
				} catch (IOException ioe) {
					progressBar.setIndeterminate(false);
					Toolkit.getDefaultToolkit().beep();
					clear();

					JOptionPane.showMessageDialog(JYzer.this, "I/O Exception!\n" + ioe.getMessage(), "Parsing stopped:", JOptionPane.ERROR_MESSAGE);
					ioe.printStackTrace();
				} catch (Exception e) {
					progressBar.setIndeterminate(false);
					Toolkit.getDefaultToolkit().beep();
					clear();

					JOptionPane.showMessageDialog(JYzer.this, "Unknown exception! Please, if you can, send me the stack trace dump!!",
							"Parsing stopped:", JOptionPane.ERROR_MESSAGE);
					System.err.println( "Error: " + e.getMessage() );
					e.printStackTrace();
				}

				// TODO: find the reason for npe in the awt event queue !!! Serious
				progressBar.setIndeterminate(false);
				Toolkit.getDefaultToolkit().beep();
			}
		};

		parser.start();
	}// parse

	/**
	 * To start the application. Sets some static values & kicks the program.
	 *
	 * @param args the commandline arguments.
	 */
	public static void main(final String args[]) {
		// to make a fancy style
        JFrame.setDefaultLookAndFeelDecorated(true);
		UIManager.put("swing.boldMetal", Boolean.FALSE);
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame.setDefaultLookAndFeelDecorated(true);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        System.setProperty("sun.awt.noerasebackground","true");

		JYzer jyzer = new JYzer();
	}// main

	// --- GUI variables -----------------------------------------------------------------------------------------------------------------------------------

	// menu things
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");
	private JMenu validateMenu = new JMenu("Validate");
	private JMenu optionsMenu = new JMenu("Options"); {
		optionsMenu.setIcon( new ImageIcon( getClass().getResource("/data/pix/options.gif") ) );
	}
	private JMenu helpMenu = new JMenu("Help"); {
		helpMenu.setIcon( new ImageIcon( getClass().getResource("/data/pix/info.gif") ) );
	}
	private JMenuItem openMenuItem = new JMenuItem("Open class", new ImageIcon( getClass().getResource("/data/pix/open.gif") ) );
	private JMenuItem saveSummaryMenuItem = new JMenuItem("Save summary", new ImageIcon( getClass().getResource("/data/pix/save.gif") ) );
	private JMenuItem saveSourceMenuItem = new JMenuItem("Save disassembled source", new ImageIcon( getClass().getResource("/data/pix/save.gif") ) );
	private JMenuItem closeMenuItem = new JMenuItem("Close file", new ImageIcon( getClass().getResource("/data/pix/close.gif") ) );
	private JMenuItem previewMenuItem = new JMenuItem("Print Preview", new ImageIcon( getClass().getResource("/data/pix/preview.gif") ) );
	private JMenuItem printMenuItem = new JMenuItem("Print", new ImageIcon( getClass().getResource("/data/pix/print.gif") ) );
	private JMenuItem exitMenuItem = new JMenuItem("Exit");
	private JCheckBoxMenuItem validationMenuItem = new JCheckBoxMenuItem("Full validation");
	private JMenuItem preferencesMenuItem = new JMenuItem("Preferences", new ImageIcon( getClass().getResource("/data/pix/preferences.gif") ) );
	private JMenuItem clearRecentFilesMenuItem = new JMenuItem("Clear recent files", new ImageIcon( getClass().getResource("/data/pix/clearRecent.gif") ) );
	private JMenuItem helpMenuItem = new JMenuItem("Help", new ImageIcon( getClass().getResource("/data/pix/help.gif") ) );
	private JMenuItem hexCtrMenuItem = new JMenuItem("HexCtr");
	private JMenuItem licenseMenuItem = new JMenuItem("License", new ImageIcon( getClass().getResource("/data/pix/license.gif") ) );
	private JMenuItem aboutMenuItem = new JMenuItem("About...", new ImageIcon( getClass().getResource("/data/pix/about.gif") ) );
	private JMenuItem openJavaWebMenuItem = new JMenuItem("OpenJava Web");
	private JMenuItem recentMenuItems[] = new JMenuItem[RECENT_FILES_MAX_SIZE];

	// GUI components
	private JPanel mainPanel = new JPanel();
	private JLabel stateLabel = new JLabel(" ");
	private JTabbedPane tabbedPane = new JTabbedPane();
	private JProgressBar progressBar = new JProgressBar();
	private SummaryPane summaryPane = new SummaryPane();
	private DisassemblePane disassemblePane = new DisassemblePane();
	private ConstantPoolPane constantPoolPane = new ConstantPoolPane();
	private FieldsPane fieldsPane = new FieldsPane(this);
	private MethodsPane methodsPane = new MethodsPane(this);
	private HexaViewPane hexaViewPane = new HexaViewPane();
	private HexCtr hexCtr;

}// class.JYzer
