package Vuelos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import quick.dbtable.DBTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

@SuppressWarnings("serial")
public class EmployeePanel extends MainPanel {
	private DBTable table, tableDescription;
	private DataBaseConnection dbConnection;
	private JComboBox<String> cbCityFrom, cbCityTo;
	private JFormattedTextField ftfDateFrom, ftfDateUp;
	private int legajo;
	private JLabel lblSelectedGoFlight, lblSelectedGoClass, lblSelectedBackFlight, lblSelectedBackClass, lblGoFlight,
			lblGoClass, lblBackFlight, lblBackClass;

	/**
	 * Create the panel.
	 */
	public EmployeePanel(DBTable table, DBTable table2, String legajo) {
		this.legajo = Integer.parseInt(legajo);
		this.table = table;
		this.tableDescription = table2;
		setBounds(0, 0, 1350, 725);
		setLayout(null);
		dbConnection = DataBaseConnection.getInstance();
		initGUI();
		fillCityComboBox();
	}

	private void initGUI() {
		add(table, BorderLayout.CENTER);
		table.setBounds(12, 120, 1170, 200);
		table.setEditable(false);
		tableDescription.setBounds(12, 340, 1170, 200);
		add(tableDescription, BorderLayout.CENTER);
		tableDescription.setEditable(false);

		table.addMouseListener(createMouseListener(table, true));
		tableDescription.addMouseListener(createMouseListener(tableDescription, false));

		lblGoFlight = new JLabel("Vuelo seleccionado");
		lblGoFlight.setBounds(1194, 120, 130, 25);
		add(lblGoFlight);

		lblSelectedGoFlight = new JLabel();
		lblSelectedGoFlight.setBounds(1194, 150, 130, 25);
		lblSelectedGoFlight.setBorder(new LineBorder(Color.black));
		add(lblSelectedGoFlight);

		lblGoClass = new JLabel("Clase Seleccionada");
		lblGoClass.setBounds(1194, 180, 130, 25);
		add(lblGoClass);

		lblSelectedGoClass = new JLabel();
		lblSelectedGoClass.setBounds(1194, 210, 130, 25);
		lblSelectedGoClass.setBorder(new LineBorder(Color.black));
		add(lblSelectedGoClass);

		lblBackFlight = new JLabel("Vuelo seleccionado");
		lblBackFlight.setBounds(1194, 340, 130, 25);
		add(lblBackFlight);

		lblSelectedBackFlight = new JLabel();
		lblSelectedBackFlight.setBounds(1194, 370, 130, 25);
		lblSelectedBackFlight.setBorder(new LineBorder(Color.black));
		add(lblSelectedBackFlight);

		lblBackClass = new JLabel("Clase seleccionada");
		lblBackClass.setBounds(1194, 400, 130, 25);
		add(lblBackClass);

		lblSelectedBackClass = new JLabel();
		lblSelectedBackClass.setBounds(1194, 430, 130, 25);
		lblSelectedBackClass.setBorder(new LineBorder(Color.black));
		add(lblSelectedBackClass);

		setVisibleTable(false);
		setVisibleTableDescription(false);

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
				setVisibleTable(true);
				setVisibleTableDescription(false);
				refrescarTablaIda();
				if (rdbtnRoundTrip.isSelected()) {
					setVisibleTableDescription(true);
					refrescarTablaVuelta();
				}
			}
		});

		JSeparator separator = new JSeparator();
		separator.setBounds(12, 106, 1326, 2);
		add(separator);

		initJFormattedTextFields();
	}

	private void setVisibleTable(boolean b) {
		table.setVisible(b);
		lblGoClass.setVisible(b);
		lblGoFlight.setVisible(b);
		lblSelectedGoClass.setVisible(b);
		lblSelectedGoFlight.setVisible(b);
	}

	private void setVisibleTableDescription(boolean b) {
		tableDescription.setVisible(b);
		lblBackFlight.setVisible(b);
		lblBackClass.setVisible(b);
		lblSelectedBackClass.setVisible(b);
		lblSelectedBackFlight.setVisible(b);
	}

	private MouseListener createMouseListener(DBTable tabla, boolean esVueloIda) {
		return new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				int row = tabla.getSelectedRow();
				Date fechaSalida;
				String nroVuelo = tabla.getValueAt(row, 0).toString();
				if (esVueloIda) {
					fechaSalida = Fechas.convertirStringADateSQL(ftfDateFrom.getText());
					lblSelectedGoFlight.setText(nroVuelo);
					lblSelectedGoClass.setText("");
				} else {
					fechaSalida = Fechas.convertirStringADateSQL(ftfDateUp.getText());
					lblSelectedBackFlight.setText(nroVuelo);
					lblSelectedBackClass.setText("");
				}
				String query = "SELECT Numero AS 'Numero Vuelo', NombreClase AS Clase, \n"
						+ "asientos_disponibles AS 'Asientos disponibles', Precio \n"
						+ "FROM vuelos_disponibles WHERE Numero = '" + nroVuelo + "' AND Fecha = '" + fechaSalida
						+ "' ORDER BY Numero, Fecha;";
				JTable showTable = dbConnection.getFilledUpTable(query);
				showTable.addMouseListener(new MouseListener() {
					public void mouseReleased(MouseEvent arg0) {}
					public void mousePressed(MouseEvent arg0) {}
					public void mouseExited(MouseEvent arg0) {}
					public void mouseEntered(MouseEvent arg0) {}
					public void mouseClicked(MouseEvent arg0) {	
						String clase = showTable.getValueAt(showTable.getSelectedRow(), 1).toString();
						if (esVueloIda) {
							lblSelectedGoClass.setText(clase);
						} else {
							lblSelectedBackClass.setText(clase);
						}
					}
				});
				JOptionPane.showMessageDialog(null, showTable);
			}
		};
	}

	private void initJFormattedTextFields() {

		MaskFormatter dateMask = null;
		try {
			dateMask = new MaskFormatter("##/##/####");
			dateMask.setPlaceholderCharacter('/');
			dateMask.setValidCharacters("0123456789");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		ftfDateFrom = new JFormattedTextField(dateMask);
		ftfDateFrom.setBounds(404, 45, 125, 25);
		ftfDateFrom.setHorizontalAlignment(JTextField.CENTER);
		add(ftfDateFrom);

		ftfDateUp = new JFormattedTextField(dateMask);
		ftfDateUp.setBounds(541, 45, 125, 25);
		ftfDateUp.setHorizontalAlignment(JTextField.CENTER);
		add(ftfDateUp);

		cbCityFrom = new JComboBox<String>();
		cbCityFrom.setBounds(12, 45, 125, 25);
		add(cbCityFrom);

		cbCityTo = new JComboBox<String>();
		cbCityTo.setBounds(149, 45, 125, 25);
		add(cbCityTo);

		JButton btnFlightReservation = new JButton("Reservar vuelo");
		btnFlightReservation.setBounds(900, 28, 150, 30);
		btnFlightReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String tipoDNI = JOptionPane.showInputDialog("Ingrese el tipo de documento del cliente");
					String dni = JOptionPane.showInputDialog("Ingrese el numero de documento del cliente");
					int i = 0;
					ArrayList<Object> parameters = new ArrayList<Object>();
					parameters.add(i++, lblSelectedGoFlight.getText());
					parameters.add(i++, lblSelectedGoClass.getText());
					parameters.add(i++, getFromDate());
					String query;
					if (tableDescription.isVisible()) {
						query = "CALL reservavueloidavuelta(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
						parameters.add(i++, lblBackFlight.getText());
						parameters.add(i++, lblBackClass.getText());
						parameters.add(i++, getUpDate());
					} else {
						query = "CALL reservavueloida(?, ?, ?, ?, ?, ?, ?);";
					}
					parameters.add(i++, tipoDNI);
					parameters.add(i++, Integer.parseInt(dni));
					parameters.add(i++, legajo);
					parameters.add(i++, new String());
					String finishStatus = dbConnection.excecuteProcedure(query, parameters);
					if (finishStatus != null && finishStatus.contains("correctamente")) {
						JOptionPane.showMessageDialog(MainWindow.getInstance(), "La reserva se realizo� con exito!", "Exito en la operacion", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(MainWindow.getInstance(), finishStatus, "Fallo en la operacion", JOptionPane.ERROR_MESSAGE);
					}				
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error en excepcion");
				}
			}
		});
		add(btnFlightReservation);
	}

	private void fillCityComboBox() {
		String message = "SELECT DISTINCT ALlegada_ciudad FROM vuelos_disponibles;";
		dbConnection.refreshExcecute(message, cbCityTo, this);
		message = "SELECT DISTINCT ASalida_ciudad FROM vuelos_disponibles;";
		dbConnection.refreshExcecute(message, cbCityFrom, this);
	}

	public DBTable getDBTable() {
		return table;
	}

	private void refrescarTablaIda() {
		try {
			String cityFrom = cbCityFrom.getSelectedItem().toString();
			String cityTo = cbCityTo.getSelectedItem().toString();
			Date fechaSalida = getFromDate();
			String query = "SELECT vp.Numero as Vuelo, vp.ASalida_nombre as 'Aeropuerto salida',\n"
					+ "vp.HoraSalida as 'Hora de salida', vp.ALlegada_nombre as 'Aeropuerto llegada',\n "
					+ "vp.HoraLlegada as 'Hora de llegada', vp.Modelo as 'Mod. Avion', vp.duracion as 'Duracion' \n"
					+ "FROM vuelos_disponibles as vp WHERE vp.ASalida_ciudad = '" + cityFrom + "' AND "
					+ "vp.ALlegada_ciudad = '" + cityTo + "' AND vp.Fecha = '" + fechaSalida
					+ "' ORDER BY vp.Fecha, vp.ASalida_ciudad, vp.ALlegada_ciudad, vp.NombreClase;";
			dbConnection.refreshTable(table, query);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	private Date getFromDate() throws Exception {
		if (Fechas.validar(ftfDateFrom.getText()))
			return Fechas.convertirStringADateSQL(ftfDateFrom.getText());
		else
			throw new Exception("Alguna de las fechas ingresadas no es correcta, por favor reviselas nuevamente");
	}

	private Date getUpDate() throws Exception {
		if (Fechas.validar(ftfDateFrom.getText()))
			return Fechas.convertirStringADateSQL(ftfDateUp.getText());
		else
			throw new Exception("Alguna de las fechas ingresadas no es correcta, por favor reviselas nuevamente");
	}

	private void refrescarTablaVuelta() {
		try {
			String cityTo = cbCityFrom.getSelectedItem().toString();
			String cityFrom = cbCityTo.getSelectedItem().toString();
			Date fechaVuelta = getUpDate();
			String query = "SELECT vp.Numero as Vuelo, vp.ASalida_nombre as 'Aeropuerto salida',\n"
					+ "vp.HoraSalida as 'Hora de salida', vp.ALlegada_nombre as 'Aeropuerto llegada',\n "
					+ "vp.HoraLlegada as 'Hora de llegada', vp.Modelo as 'Mod. Avion', vp.duracion as 'Duracion' \n"
					+ "FROM vuelos_disponibles as vp WHERE vp.ASalida_ciudad = '" + cityFrom + "' AND \n"
					+ "vp.ALlegada_ciudad = '" + cityTo + "' AND vp.Fecha = '" + fechaVuelta + "'"
					+ " ORDER BY vp.Fecha, vp.ASalida_ciudad, vp.ALlegada_ciudad, vp.NombreClase;";
			dbConnection.refreshTable(tableDescription, query);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}
}
