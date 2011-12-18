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
import javax.swing.*;

/**
 * A mini radix calculator.
 *
 * @author Legendi Richard Oliver
 * @version 1.0, 2005. sept. 17. (17:52)
 */
public class HexCtr extends JDialog {

    /** Creates new form HexCtr */
    public HexCtr(Window parent) {
        initComponents();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
		setVisible(true);
        setLocationRelativeTo(parent);
    }

	/** Performs the transforming. */
    private void convert() {
        int from = 0;
        try {
            if ( fromHexaRadioButton.isSelected() ) {
                from = Integer.valueOf( inputTextField.getText(), 16 );
            } else if ( fromDecimalRadioButton.isSelected() ) {
                from = Integer.valueOf( inputTextField.getText(), 10 );
            } else {
                from = Integer.valueOf( inputTextField.getText(), 2 );
            }

            if ( toHexaRadioButton.isSelected() ) {
                outputTextField.setText( Integer.toString(from, 16) );
            } else if ( toDecimalRadioButton.isSelected() ) {
                outputTextField.setText( Integer.toString(from, 10) );
            } else {
                outputTextField.setText( Integer.toString(from, 2) );
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Non-valid input");
            return;
        }
    }

    /**
     * This method is called from within the constructor to
     * initialize the dialog.
     */
    private void initComponents() {
        fromButtonGroup = new javax.swing.ButtonGroup();
        toButtonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        inputTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        fromBinaryRadioButton = new javax.swing.JRadioButton();
        fromDecimalRadioButton = new javax.swing.JRadioButton();
        fromHexaRadioButton = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        outputTextField = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        toBinaryRadioButton = new javax.swing.JRadioButton();
        toDecimalRadioButton = new javax.swing.JRadioButton();
        toHexaRadioButton = new javax.swing.JRadioButton();

        getContentPane().setLayout(new java.awt.GridLayout(0, 2));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HexCtr");
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(2, 2, 2, 2)));
        inputTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        inputTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                inputTextFieldKeyTyped(evt);
            }
        });

        jPanel1.add(inputTextField, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.GridLayout(3, 0));

        jPanel3.setBorder(new javax.swing.border.TitledBorder("Base:"));
        fromButtonGroup.add(fromBinaryRadioButton);
        fromBinaryRadioButton.setSelected(true);
        fromBinaryRadioButton.setText("Binary");
        jPanel3.add(fromBinaryRadioButton);

        fromButtonGroup.add(fromDecimalRadioButton);
        fromDecimalRadioButton.setText("Decimal");
        jPanel3.add(fromDecimalRadioButton);

        fromButtonGroup.add(fromHexaRadioButton);
        fromHexaRadioButton.setText("Hexa");
        jPanel3.add(fromHexaRadioButton);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(2, 2, 2, 2)));
        outputTextField.setBackground(java.awt.Color.orange);
        outputTextField.setEditable(false);
        outputTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel2.add(outputTextField, java.awt.BorderLayout.NORTH);

        jPanel4.setLayout(new java.awt.GridLayout(3, 0));

        jPanel4.setBorder(new javax.swing.border.TitledBorder("Base:"));
        toButtonGroup.add(toBinaryRadioButton);
        toBinaryRadioButton.setSelected(true);
        toBinaryRadioButton.setText("Binary");
        jPanel4.add(toBinaryRadioButton);

        toButtonGroup.add(toDecimalRadioButton);
        toDecimalRadioButton.setText("Decimal");
        jPanel4.add(toDecimalRadioButton);

        toButtonGroup.add(toHexaRadioButton);
        toHexaRadioButton.setText("Hexa");
        jPanel4.add(toHexaRadioButton);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2);
    }// initComponents

	/** Watching submission. */
    private void inputTextFieldKeyTyped(java.awt.event.KeyEvent evt) {
        if ( '\n' == evt.getKeyChar() ) {
            convert();
        }
    }

    // Start: Variables declaration
    private javax.swing.JRadioButton fromBinaryRadioButton;
    private javax.swing.ButtonGroup fromButtonGroup;
    private javax.swing.JRadioButton fromDecimalRadioButton;
    private javax.swing.JRadioButton fromHexaRadioButton;
    private javax.swing.JTextField inputTextField;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField outputTextField;
    private javax.swing.JRadioButton toBinaryRadioButton;
    private javax.swing.ButtonGroup toButtonGroup;
    private javax.swing.JRadioButton toDecimalRadioButton;
    private javax.swing.JRadioButton toHexaRadioButton;
    // EoF: variables declaration

}// class.HexCtr
