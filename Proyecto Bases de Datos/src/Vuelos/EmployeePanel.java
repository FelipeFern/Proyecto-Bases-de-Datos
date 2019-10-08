package Vuelos;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;

import quick.dbtable.DBTable;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;
import javax.swing.JRadioButton;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class EmployeePanel extends MainPanel {
	private DBTable table, tableDescription;
	private DataBaseConnection dbConnection;
	private JComboBox<String> cbCityFrom, cbCityTo,cbDateFrom, cbDateUp;

	/**
	 * Create the panel.
	 * @param table2 
	 */
	public EmployeePanel(DBTable table, DBTable table2) {
		this.table = table;
		this.tableDescription = table2;
		setBounds(100, 100, 1024, 600);
		setLayout(null);
		dbConnection = DataBaseConnection.getInstance();
		initGUI();
		fillCityFrom();
		fillCityTo();
		fillDateFrom();
		fillDateUp();
	}

	private void initGUI() {
		add(table, BorderLayout.CENTER);
		table.setBounds(12, 119, 512, 415);
		table.setEditable(false);
		table.addMouseListener(createMouseListener());
		
		tableDescription.setBounds(528, 119, 484, 415);
		add(tableDescription);		

		JLabel lblCityFrom = new JLabel("Ciudad origen");
		lblCityFrom.setBounds(12, 12, 125, 25);
		lblCityFrom.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblCityFrom);

		JLabel lblCityTo = new JLabel("Ciudad destino");
		lblCityTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblCityTo.setBounds(149, 12, 125, 25);
		add(lblCityTo);

		JRadioButton rdbtnGoFlights = new JRadioButton("Ida");
		rdbtnGoFlights.setBounds(282, 45, 110, 25);
		add(rdbtnGoFlights);

		JRadioButton rdbtnRoundTrip = new JRadioButton("Ida y Vuelta");
		rdbtnRoundTrip.setBounds(282, 73, 110, 25);
		add(rdbtnRoundTrip);

		ButtonGroup bGroup = new ButtonGroup();
		bGroup.add(rdbtnGoFlights);
		bGroup.add(rdbtnRoundTrip);

		JLabel lblOption = new JLabel("Opciones vuelo");
		lblOption.setHorizontalAlignment(SwingConstants.CENTER);
		lblOption.setBounds(284, 12, 108, 25);
		add(lblOption);

		JLabel lblDateSince = new JLabel("Fecha desde");
		lblDateSince.setHorizontalAlignment(SwingConstants.LEFT);
		lblDateSince.setBounds(404, 12, 125, 25);
		add(lblDateSince);

		JLabel lblDateTo = new JLabel("Fecha hasta");
		lblDateTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblDateTo.setBounds(541, 12, 125, 25);
		add(lblDateTo);

		JButton btnShowFlights = new JButton("Mostrar vuelos");
		btnShowFlights.setBounds(714, 28, 150, 30);
		add(btnShowFlights);
		btnShowFlights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (rdbtnRoundTrip.isSelected()) {
					refrescarTablaIdaYVuelta();
				} else {
					refrescarTablaIda();
				}
			}
		});

		JSeparator separator = new JSeparator();
		separator.setBounds(12, 106, 1000, 2);
		add(separator);

		initJFormattedTextFields();
	}
	
	private MouseListener createMouseListener() {
		return new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				String nroVuelo = table.getValueAt(row, 0).toString();
				String query = "SELECT Numero AS 'Numero Vuelo', NombreClase AS Clase, \n"
						+ "asientos_disponibles AS 'Asientos disponibles', Precio \n" 
						+ "FROM vuelos_disponibles WHERE Numero = '" + nroVuelo + "' ORDER BY Numero, Fecha;";
				dbConnection.refreshTable(tableDescription, query);
			}
		};
	}

	private void initJFormattedTextFields() {
		MaskFormatter dateMask = null;
		try {
			dateMask = new MaskFormatter("##/##/####");
			dateMask.setPlaceholderCharacter('-');
			dateMask.setValidCharacters("0123456789");
		} catch (Exception e) {
			e.printStackTrace();
		}

		cbDateFrom = new JComboBox<String>();
		cbDateFrom.setBounds(404, 45, 125, 25);
		add(cbDateFrom);

		cbDateUp = new JComboBox<String>();
		cbDateUp.setBounds(541, 45, 125, 24);
		add(cbDateUp);

		cbCityFrom = new JComboBox<String>();
		cbCityFrom.setBounds(12, 49, 125, 24);
		add(cbCityFrom);

		cbCityTo = new JComboBox<String>();
		cbCityTo.setBounds(149, 49, 125, 24);
		add(cbCityTo);
	}

	private void fillCityTo() {
		String message = "SELECT DISTINCT ALlegada_ciudad FROM vuelos_disponibles;";
		dbConnection.refreshExcecute(message, cbCityTo, this);
	}

	private void fillCityFrom() {
		String message = "SELECT DISTINCT ASalida_ciudad FROM vuelos_disponibles;";
		dbConnection.refreshExcecute(message, cbCityFrom, this);
	}

	private void fillDateFrom() {
		String message = "SELECT DISTINCT Fecha FROM vuelos_disponibles;";
		dbConnection.refreshExcecute(message, cbDateFrom, this);
	}

	private void fillDateUp() {
		String message = "SELECT DISTINCT Fecha FROM vuelos_disponibles ;";
		dbConnection.refreshExcecute(message, cbDateUp, this);
	}

	public DBTable getDBTable() {
		return table;
	}

	private void refrescarTablaIda() {
		String cityFrom = cbCityFrom.getSelectedItem().toString();
		String cityTo = cbCityTo.getSelectedItem().toString();
		Date fechaSalida = Fechas.convertirStringADateSQL(cbDateFrom.getSelectedItem().toString());
		
		String query = "SELECT vp.Numero as Vuelo, vp.ASalida_nombre as 'Aeropuerto salida',\n" 
				+ "vp.HoraSalida as 'Hora de salida', vp.ALlegada_nombre as 'Aeropuerto llegada',\n "
				+ "vp.HoraLlegada as 'Hora de llegada', vp.Modelo as 'Mod. Avion', vp.duracion as 'Duracion' \n"
				+ "FROM vuelos_disponibles as vp WHERE vp.ASalida_ciudad = '" + cityFrom + "' AND "
				+ "vp.ALlegada_ciudad = '" + cityTo + "' AND vp.Fecha = '" + fechaSalida
				+ "' ORDER BY vp.Fecha, vp.ASalida_ciudad, vp.ALlegada_ciudad;";
		dbConnection.refreshTable(table, query);
	}

	private void refrescarTablaIdaYVuelta() {
		String cityFrom = cbCityFrom.getSelectedItem().toString();
		String cityTo = cbCityTo.getSelectedItem().toString();
		Date fechaSalida = Fechas.convertirStringADateSQL(cbDateFrom.getSelectedItem().toString());
		Date fechaVuelta = Fechas.convertirStringADateSQL(cbDateUp.getSelectedItem().toString());
		String query = "SELECT vp.Numero as Vuelo, vp.ASalida_nombre as 'Aeropuerto salida',\n" 
				+ "vp.HoraSalida as 'Hora de salida', vp.ALlegada_nombre as 'Aeropuerto llegada',\n "
				+ "vp.HoraLlegada as 'Hora de llegada', vp.Modelo as 'Mod. Avion', vp.duracion as 'Duracion' \n"
				+ "FROM vuelos_disponibles as vp WHERE (vp.ASalida_ciudad = '" + cityFrom + "' AND \n"
				+ "vp.ALlegada_ciudad = '" + cityTo + "' AND vp.Fecha = '" + fechaSalida + "') OR \n"
				+ "(vp.ASalida_ciudad = '" + cityTo + "' AND vp.ALlegada_ciudad = '" + cityFrom + "' AND \n" 
				+ "vp.Fecha = '" + fechaVuelta + "') ORDER BY vp.Fecha, vp.ASalida_ciudad, vp.ALlegada_ciudad;";
		dbConnection.refreshTable(table, query);
	}
}
