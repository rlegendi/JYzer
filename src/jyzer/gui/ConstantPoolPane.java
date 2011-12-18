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

import jyzer.gui.guihelpers.ConstantPoolTableModel;
import jyzer.items.ConstantPool;

import edu.lro.gui.NumberTextField;

/**
 * This class is the representative of the <code>constant pool</code>. Contains a search panel (you can find a string
 * among the constant pool entries, but searches only in the values), a goto panel (you can go to a given entry in the pool),
 * and a filter panel (you can filter the displayed data).
 *
 * <p>TODO: there's a bug with repainting the table after filtering a data.</p>
 *
 * @author Legendi Richard Oliver
 * @version 1.0
 */
public class ConstantPoolPane extends JComponent {

	// --- Variables -------------------------------------------------------------------------------

	/** Filter options. */
	private static final String FILTER_ELEMENTS[] = {
		"ALL", "Unused", "Unknown","Class", "Fieldref", "Methodref", "InterfaceMethodref", "String",
		"Integer", "Float", "Long", "Double", "NameAndType", "Utf8"
	};

	private int actIndex = 0;
	private boolean matchCase = false;

	// -- Constructors ------------------------------------------------------------------------------

	/**
	 * Creating the pane.
	 */
	public ConstantPoolPane() {
		buildUpGUI();
		setDefaultValues();
	}

	// --- Building the GUI -------------------------------------------------------------------------

	/**
	 * Building up the core GUI.
	 */
	private void buildUpGUI() {
		table = new JTable( new ConstantPoolTableModel() );

		setLayout( new BorderLayout() );
		add(mainPanel);

		mainPanel.add( tablePanel,  BorderLayout.CENTER);
		mainPanel.add( searchPanel, BorderLayout.NORTH );

		tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
		tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

		searchPanel.setLayout( new GridLayout(0, 3) );
		searchPanel.add(findPanel);
		searchPanel.add(gotoPanel);
		searchPanel.add(filterPanel);

		findPanel.setBorder( BorderFactory.createTitledBorder("Search String:") );
		gotoPanel.setBorder( BorderFactory.createTitledBorder("Jump to index:") );
		filterPanel.setBorder( BorderFactory.createTitledBorder("Filtering:") );

		findPanel.setLayout( new BoxLayout(findPanel, BoxLayout.Y_AXIS) );
		findTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
		findPanel.add(findTextField);
		findButtonPanel.add(findNextButton);
		findButtonPanel.add(findPrevButton);

		findButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		findPanel.add(findButtonPanel);

		gotoPanel.setLayout( new BorderLayout() );
		JPanel tmp = new JPanel();
		gotoPanel.add(tmp, BorderLayout.CENTER);
		tmp.add(gotoTextField);
		tmp = new JPanel();
		gotoPanel.add(tmp,    BorderLayout.EAST);
		tmp.add(gotoButton);

		filterPanel.setLayout( new BorderLayout() );
		tmp = new JPanel();
		filterPanel.add(tmp, BorderLayout.CENTER);
		tmp.add(filterComboBox);

		tmp = new JPanel();
		filterPanel.add(tmp, BorderLayout.EAST);
		tmp.add(filterButton);

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

		gotoTextField.addKeyListener( new KeyAdapter() {
			public void keyTyped(KeyEvent ke) {
				if ( KeyEvent.VK_ENTER == ke.getKeyChar() ) {
					gotoLine();
				}
			}
		});

		gotoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				gotoLine();
			}
		});

		filterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				filterData();
			}
		});
	}// buildUpGUI

	/**
	 * Setting the default values of the components.
	 */
	private void setDefaultValues() {
		//tablePanel.setOpaque(true);
		//tablePanel.setBackground(Color.WHITE);
		refresh();
		gotoTextField.setColumns(8);

		findPanel.setToolTipText("<HTML><B>Search String:</B><BR>To find a string in the Constant Pool entries.</HTML>");
		gotoPanel.setToolTipText("<HTML><B>Jump to index:</B><BR>To jump to the given index of the Constant Pool.</HTML>");
		filterPanel.setToolTipText("<HTML><B>Filtering:</B><BR>To filter the displayed Constant Pool entries.</HTML>");
	}// setDefaultValues

	// --- GUI helper methods ---------------------------------------------------------------------------------------------

	/**
	 * Clears the displayed data on the pane.
	 */
	public void clear() {
		findTextField.setText("");
		gotoTextField.setText("");
		filterComboBox.setSelectedIndex(0);
		setData(null);
	}

	/**
	 * Sets the table column widths after refreshing the table contents.
	 */
	private void refresh() {
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(1).setMaxWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(600);
		table.getColumnModel().getColumn(2).setMaxWidth(1500);
	}

	/**
	 * Setting the information displayed in the table.
	 */
	public void setData(Object[][] data) {
		findTextField.setText("");
		gotoTextField.setText("");
		filterComboBox.setSelectedIndex(0);
		table.clearSelection();
		table.setModel( new ConstantPoolTableModel(data) );
		refresh();
	}

	/**
	 * Finds the next occurance of the given string in the 3rd column of the table.
	 * Starts the search from the 1st entry, cause the 0th is not used.
	 */
	private void findNext() {
		String infix = findTextField.getText();
		if ( infix.equals("") ) {
			return;
		}

		if ( ! matchCase ) {
			infix = infix.toLowerCase();
		}

		String desc[] = ( (ConstantPoolTableModel) table.getModel() ).getDescriptions();
		if ( null == desc ) {
			return;
		}

		if ( table.getSelectedRowCount() != 0 ) {
			actIndex = table.getSelectedRow();
		}

		int rows = ( (ConstantPoolTableModel) table.getModel() ).getRowCount();
		boolean found = false, end = false;
		int start = actIndex;

		while ( ! end && ! found ) {
			if ( actIndex < (rows-1) ) {
				actIndex++;
			} else {
				actIndex = 0;
			}

			found = desc[actIndex].toLowerCase().contains(infix);
			end = ( start == actIndex) && ! found;
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
	 * Finds the previous occurance of the given string in the 3rd column of the table.
	 */
	private void findPrev() {
		String infix = findTextField.getText();
		if ( infix.equals("") ) {
			return;
		}

		if ( ! matchCase ) {
			infix = infix.toLowerCase();
		}

		String desc[] = ( (ConstantPoolTableModel) table.getModel() ).getDescriptions();
		if ( null == desc ) {
			return;
		}

		if ( table.getSelectedRowCount() != 0 ) {
			actIndex = table.getSelectedRow();
		}

		int rows = ( (ConstantPoolTableModel) table.getModel() ).getRowCount();
		boolean found = false, end = false;
		int start = actIndex;

		while ( ! end && ! found ) {
			if ( 0 < actIndex ) {
				actIndex--;
			} else {
				actIndex = (rows-1);
			}

			found = desc[actIndex].toLowerCase().contains(infix);
			end = ( start == actIndex) && ! found;
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
	 * Make the selected line of the table to the given line, if it is displayed. Otherwise a warning message is displayed.
	 */
	private void gotoLine() {
		if ( gotoTextField.getText().equals("") ) {
			return;
		}

		int index = Integer.parseInt( gotoTextField.getText() );
		ConstantPoolTableModel model = (ConstantPoolTableModel) table.getModel();

		if ( model.isEmpty() ) {
			gotoTextField.setText("");
			return;
		}

		if ( index < 0 || (model.getFullRowCount()-1) < index ) {
			JOptionPane.showMessageDialog(
				this,
				gotoTextField.getText() + " is not in the allowed range [0.." + (model.getFullRowCount()-1) + "] !",
				"Warning:",
				JOptionPane.ERROR_MESSAGE
			);
			gotoTextField.setText("");
			return;
		}

		int indexes[] = model.getIndexes() ;
		boolean found = false;
		for (int i=0; i<indexes.length; ++i) {
			if ( found = ( indexes[i] == index ) ) {
				table.changeSelection(i, 0, false, false);
				break;
			}
		}

		if ( ! found ) {
			JOptionPane.showMessageDialog(
				this,
				"Index " + gotoTextField.getText() + " is not present in the filtered pool!",
				"Warning:",
				JOptionPane.ERROR_MESSAGE
			);
			gotoTextField.setText("");
			return;
		}

		gotoTextField.setText("");
	}// gotoLine

	/**
	 * Filters the displayed data of the table.
	 */
	private void filterData() {
		String desc = filterComboBox.getSelectedItem().toString();

		if ( desc.equals("ALL") ) {
			desc = "*";
		} else if ( desc.equals("Unused") ) {
			desc = "UNUSED";
		} else if ( desc.equals("Unknown") ) {
			desc = "UNKNOWN !!!";
		} else {
			desc = "CONSTANT_" + desc;
		}

		( (ConstantPoolTableModel) table.getModel() ).filterData(desc);

		repaint();
	}// filterData

	// --- GUI variables --------------------------------------------------------------------------------------------

	private JPanel mainPanel   = new JPanel( new BorderLayout() );
	private JPanel searchPanel = new JPanel();
	private JPanel findPanel   = new JPanel();
	private JPanel gotoPanel   = new JPanel();
	private JPanel filterPanel = new JPanel();
	private JTextField findTextField = new JTextField();
	private JButton findNextButton = new JButton("Next");
	private JButton findPrevButton = new JButton("Prev");
	private NumberTextField gotoTextField = new NumberTextField();
	private JButton gotoButton = new JButton("Goto", new ImageIcon( getClass().getResource("/data/pix/goto.gif") ) );
	private JComboBox filterComboBox = new JComboBox( FILTER_ELEMENTS );
	private JButton filterButton = new JButton("Apply");
	private JPanel findButtonPanel = new JPanel();
	private JPanel tablePanel = new JPanel( new BorderLayout() );
	private JTable table;

}// class.ConstantPoolPane
