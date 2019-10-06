package Vuelos;

import quick.dbtable.DBTable;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class AdminPanel extends MainPanel {
	private DBTable table;
	private DataBaseConnection dataConnection;
	
	
	private JLabel lblDescription;
	private JButton btnConsult;
	private JTextArea textCommand;
	private JList<String> listTables;
	private JList<String> listAttributes;   

	/*
	 * Falta acomodar la tabla donde muestra las distintas tablas en la gui
	 */
	

	/**
	 * Create the panel.
	 */
	public AdminPanel(DBTable table) {
		this.table = table;
		initGUI();
		dataConnection = DataBaseConnection.getInstance();
		llenarListaTablas();
	}

	private void initGUI() {
		setBounds(100,100,1024,600);
		setLayout(null);
		
		table.setBounds(12, 160, 1000, 384);
		this.add(table);
		table.setVisible(true);

		lblDescription = new JLabel("Ingrese la consulta");
		lblDescription.setBounds(10, 11, 150, 25);
		add(lblDescription);
		lblDescription.setVisible(true);

		btnConsult = new JButton("Consultar");
		btnConsult.setBounds(882, 48, 130, 25);
		add(btnConsult);
		
		textCommand = new JTextArea();
		textCommand.setBounds(12, 48, 858, 100);
		add(textCommand);
		btnConsult.setVisible(true);
		
		listTables = new JList<String>();
		
		btnConsult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refrescarTabla();
			}                 
		});
		
		listTables.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				String nombreTabla = listTables.getSelectedValue().toString();
				llenarListaAtributos(nombreTabla);
			}
		});
				
	}
	
	private void llenarListaTablas() {
		String message = "show tables;";
		dataConnection.refrescarExecute(message, listTables, this);		
	}
	
	
	private void llenarListaAtributos(String tabla) {
		String message = "Describe " + tabla + ";";
		dataConnection.refrescarExecute(message, listAttributes, this);		
	}
	
	
	public DBTable getDBTable() {
		return table;
	}
	
	
	private void refrescarTabla(){	   
		String consult = this.textCommand.getText().trim();
		dataConnection.refrescarTabla(table, consult);	    	 	    
	}
	
	
}
