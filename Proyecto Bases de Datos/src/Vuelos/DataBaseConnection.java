package Vuelos;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import quick.dbtable.DBTable;

public class DataBaseConnection {
	private java.sql.Connection connection;
	private static DataBaseConnection INSTANCE;

	private DataBaseConnection() {
	}

	public static DataBaseConnection getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DataBaseConnection();
		}
		return INSTANCE;
	}

	public boolean connectToDatabase(DBTable table, String username, String password) {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String servidor = "localhost:3306";
			String baseDatos = "vuelos";
			String url = "jdbc:mysql://" + servidor + "/" + baseDatos;
			if (username.equals("admin")) {
				table.connectDatabase(driver, url, username, password);
				connection = DriverManager.getConnection(url, username, password);
			} else {
				if (!connectAsEmployee(table, driver, url, username, password)) {
					return false;
				}
			}
			return true;
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(MainWindow.getInstance(),
					"Se produjo un error al intentar conectarse a la base de datos.\nPor favor, revise el usuario y contraseña",
					"Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean connectAsEmployee(DBTable table, String driver, String url, String username, String password) {
		try {
			table.connectDatabase(driver, url, "empleado", "empleado");
			connection = DriverManager.getConnection(url, "empleado", "empleado");
			String query = "SELECT legajo, password FROM empleados WHERE '" + password + "' = password AND "
					+ username + " = legajo;";
			Statement s = (Statement) connection.createStatement();
			s.executeQuery(query);
			ResultSet rs = s.getResultSet();
			if (rs.next()) { 		// Contiene el legajo - password ingresados
				return true;
			}
			disconnectDataBase(table);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(MainWindow.getInstance(),
					"Se produjo un error al intentar conectarse a la base de datos.\nPor favor, revise el usuario y contraseña",
					"Error", JOptionPane.ERROR_MESSAGE);
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
		return false;
	}

	public void disconnectDataBase(DBTable table) {
		try {
			if (this.connection != null) {
				connection.close();
				connection = null;
			}
			table.close();
		} catch (SQLException ex) {
			System.out.println("SQLExcepcion " + ex.getMessage());
			System.out.println("SQLEstado: " + ex.getSQLState());
			System.out.println("CodigoError: " + ex.getErrorCode());
		}
	}

	public void refreshTable(DBTable table, String consult) {
		try {
			table.setSelectSql(consult);
			table.createColumnModelFromQuery();
			for (int i = 0; i < table.getColumnCount(); i++) {
				if (table.getColumn(i).getType() == Types.TIME) {
					table.getColumn(i).setType(Types.CHAR);
				}
				if (table.getColumn(i).getType() == Types.DATE) {
					table.getColumn(i).setDateFormat("dd/MM/YYYY");
				}
			}
			table.refresh();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			JOptionPane.showMessageDialog(MainWindow.getInstance(), ex.getMessage() + "\n",
					"Error al ejecutar la consulta.", JOptionPane.ERROR_MESSAGE);
		} catch (ClassCastException ex) { // Arreglar esto! Con valores nulos se clava.
			System.out.println("Class cast exeption");
			System.out.println("Consulta: " + consult);
		}
	}

	public void refreshExcecute(String message, JList<String> list, MainPanel panel) {
		try {
			DefaultListModel<String> model = new DefaultListModel<String>();
			Statement s = (Statement) connection.createStatement();
			s.executeQuery(message);
			ResultSet rs = s.getResultSet();
			while (rs.next()) {
				model.addElement(rs.getString(1));
			}
			list.setModel(model);
		} catch (SQLException ex) {
			System.out.println("SQLExcepcion: " + ex.getMessage());
			System.out.println("SQLEstado: " + ex.getSQLState());
			System.out.println("CodigoError: " + ex.getErrorCode());
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(panel), ex.getMessage() + "\n",
					"Error en el acceso.", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void refreshExcecute(String message, JComboBox<String> comboBox, EmployeePanel panel) {
		try {
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
			Statement s = (Statement) connection.createStatement();
			s.executeQuery(message);
			ResultSet rs = s.getResultSet();
			while (rs.next()) {
				String str;
				if (message.contains("Fecha")) {
					str = Fechas.convertirDateAString(rs.getDate(1));
				} else {
					str = rs.getString(1);
				}
				model.addElement(str);				
			}
			comboBox.setModel(model);
		} catch (SQLException ex) {
			System.out.println("SQLExcepcion: " + ex.getMessage());
			System.out.println("SQLEstado: " + ex.getSQLState());
			System.out.println("CodigoError: " + ex.getErrorCode());
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(panel), ex.getMessage() + "\n",
					"Error en el acceso.", JOptionPane.ERROR_MESSAGE);
		}
	}
}
