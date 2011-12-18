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
package jyzer.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import jyzer.gui.guihelpers.AttribDialog;
import jyzer.gui.guihelpers.MethodsTableModel;
import jyzer.items.methods.MethodInfo;

/**
 * This class is the representative of <code>Methods</code> in the <code>ClassFile</code>. You can search a String
 * in the displayed data, and view the attributes of a method.
 *
 * @author Legendi Richard Oliver
 * @version 1.0
 */
public class MethodsPane extends JComponent {

	// --- Own variables -----------------------------------------------------------------

	private boolean showRealModifiersOnly = false;
	private int actIndex = -1;
	private boolean matchCase = false;
	private JFrame parentFrame;

	// --- Constructors ------------------------------------------------------------------

	/**
	 * Constructor, creates a new instance of MethodsPane. The parent is needed to be able
	 * to make the AttribDialog a modal Dialog.
	 *
	 * @param parentFrame the frame that contains this component in it.
	 */
	public MethodsPane(final JFrame parentFrame) {
		super();
		this.parentFrame = parentFrame;

		builUpGUI();
		setDefaultValues();
	}

	// --- Building up the GUI ------------------------------------------------------------

	/**
	 * Building up the core GUI.
	 */
	private void builUpGUI() {
		setLayout( new BorderLayout() );
		add(mainPanel, BorderLayout.CENTER);

		mainPanel.add(buttonsPanel, BorderLayout.NORTH);
		mainPanel.add(tablePanel,   BorderLayout.CENTER);

		tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
		tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

		buttonsPanel.setLayout( new GridLayout(0, 2) );
		buttonsPanel.add(findPanel, BorderLayout.CENTER);
		buttonsPanel.add(attributesPanel, BorderLayout.EAST);

		findPanel.setBorder( BorderFactory.createTitledBorder("Search String:") );
		attributesPanel.setBorder( BorderFactory.createTitledBorder("Attributes:") );

		findPanel.add(findTextField);
		findPanel.add(findNextButton);
		findPanel.add(findPrevButton);

		attributesPanel.add(attributesStringLabel);
		attributesPanel.add(attributesLabel);
		attributesPanel.add(attributesButton);

		// adding listeners
		findTextField.addKeyListener( new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				if ( KeyEvent.VK_ENTER == ke.getKeyChar() ) {
					findNext();
				}
			}
		});

		findNextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				findNext();
			}
		});

		findPrevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				findPrev();
			}
		});

		attributesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				displayAttribs();
			}
		});

		ListSelectionModel rowSM = table.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//Ignore extra messages.
				if ( e.getValueIsAdjusting() ) return;

				ListSelectionModel lsm = (ListSelectionModel)e.getSource();
				if ( lsm.isSelectionEmpty() ) {
					return; //no rows are selected
				} else {
					int selectedRow = lsm.getMinSelectionIndex();
					MethodsTableModel model = ( (MethodsTableModel) table.getModel() );
					MethodInfo minfo = model.getMethod(selectedRow);
					if ( minfo.hasAttributes() ) {
						attributesButton.setEnabled(true);
						attributesLabel.setText( "" + minfo.getAttributesCount() );
					} else {
						attributesButton.setEnabled(false);
						attributesLabel.setText("0");
					}
				}
			}
		});

	}// builUpGUI

	/**
	 * Setting the default values of the components.
	 */
	private void setDefaultValues() {
		refresh();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		findPanel.setToolTipText("<HTML><B>Search String:</B><BR>To find a string in the method entries.</HTML>");
		attributesPanel.setToolTipText("<HTML><B>Attributes:</B><BR>To display the attributes of the selected method.</HTML>");
	}

	// --- Other methods ------------------------------------------------------------------------------------------------------

	/**
	 * Returns if the real modifiers are displayed only.
	 *
	 * @see "Access flags - modifier groups."
	 */
	public boolean isShowingRealModifiersOnly() {
		return showRealModifiersOnly;
	}

	/**
	 * Clears the displayed data on the pane.
	 */
	public void clear() {
		setData(null, null);
		attributesButton.setEnabled(false);
		attributesLabel.setText("");
		findTextField.setText("");
	}

	/**
	 * Sets the table column widths after refreshing the table contents.
	 */
	private void refresh() {
		table.getColumnModel().getColumn(0).setPreferredWidth(200);
		table.getColumnModel().getColumn(0).setMaxWidth(300);

		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(1).setMaxWidth(300);

		table.getColumnModel().getColumn(2).setPreferredWidth(500);
		table.getColumnModel().getColumn(2).setMaxWidth(750);
		attributesButton.setEnabled(false);
	}

	/**
	 * Setting the information displayed in the table.
	 *
	 * @param methods references to the method infos (to know the attributes).
	 * @param data the array of strings to be displayed in the table.
	 */
	public void setData(MethodInfo[] methods, Object[][] data) {
		findTextField.setText("");
		table.setModel( new MethodsTableModel(methods, data) );
		actIndex = -1;
		refresh();
	}

	/**
	 * Finds the next occurance of the given string in the 2nd (name) and the 3rd (value) column of the table.
	 * Starts the search from the 0st entry.
	 */
	private void findNext() {
		String infix = findTextField.getText();

		if ( infix.equals("") ) {
			return;
		}

		if ( ! matchCase ) {
			infix = infix.toLowerCase();
		}

		MethodsTableModel model = ( (MethodsTableModel) table.getModel() );

		if ( model.isEmpty() ) {
			return;
		}

		String desc[][] = model.getDescriptions();

		if ( table.getSelectedRowCount() != 0 ) {
			actIndex = table.getSelectedRow();
		}

		int rows = model.getRowCount();
		boolean found = false, end = false;
		int start = actIndex;

		while ( ! end && ! found ) {
			if ( actIndex < (rows-1) ) {
				actIndex++;
			} else {
				actIndex = 0;
			}

			end   = ( start == actIndex);
			found = ! end && ( desc[actIndex][0].toLowerCase().contains(infix) || desc[actIndex][1].toLowerCase().contains(infix) );
		}

		if (found) {
			table.changeSelection(actIndex, 0, false, false);
		} else if (end) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(
				this,
				"String " + infix + " was not found.",
				"Alert:",
				JOptionPane.ERROR_MESSAGE
			);
		}
	}// findNext

	/**
	 * Finds the next occurance of the given string in the 2nd (name) and the 3rd (value) column of the table.
	 */
	private void findPrev() {
		String infix = findTextField.getText();
		if ( infix.equals("") ) {
			return;
		}

		if ( ! matchCase ) {
			infix = infix.toLowerCase();
		}

		MethodsTableModel model = ( (MethodsTableModel) table.getModel() );

		if ( model.isEmpty() ) {
			return;
		}

		String desc[][] = model.getDescriptions();

		if ( table.getSelectedRowCount() != 0 ) {
			actIndex = table.getSelectedRow();
		}

		int rows = model.getRowCount();
		boolean found = false, end = false;
		int start = actIndex;

		while ( ! end && ! found ) {
			if ( 0 < actIndex ) {
				actIndex--;
			} else {
				actIndex = (rows-1);
			}

			end   = ( start == actIndex);
			found = ! end && ( desc[actIndex][0].toLowerCase().contains(infix) || desc[actIndex][1].toLowerCase().contains(infix) );
		}

		if (found) {
			table.changeSelection(actIndex, 0, false, false);
		} else if (end) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(
				this,
				"String " + infix + " was not found.",
				"Alert:",
				JOptionPane.ERROR_MESSAGE
			);
		}
	}// findPrev

	/**
	 * If the selected field has any attributes this method creates a new <code>AttribDialog</code> to display them.
	 */
	private void displayAttribs() {
		MethodsTableModel model = ( (MethodsTableModel) table.getModel() );
		StringBuilder sb = new StringBuilder();
		model.getMethod( table.getSelectedRow() ).getAttributes().getHTMLDescription(sb);

		AttribDialog attribDialog = new AttribDialog(parentFrame, sb.toString(), "Attributes:");
	}

	// --- GUI Variables ------------------------------------------------------------------------------------------------------------

	private JPanel mainPanel    = new JPanel( new BorderLayout() );
	private JTable table        = new JTable( new MethodsTableModel() );
	private JPanel buttonsPanel = new JPanel();
	private JPanel tablePanel   = new JPanel( new BorderLayout() );

	private JPanel findPanel       = new JPanel();
	private JPanel attributesPanel = new JPanel();

	private JTextField findTextField = new JTextField(20);
	private JPanel findButtonPanel   = new JPanel();
	private JButton findNextButton   = new JButton("Next");
	private JButton findPrevButton   = new JButton("Prev");

	private JLabel attributesStringLabel = new JLabel( "Number of Attributes:" );
	private JLabel attributesLabel       = new JLabel("0");
	private JButton attributesButton     = new JButton("Show attributes");

}// class.MethodsPane
