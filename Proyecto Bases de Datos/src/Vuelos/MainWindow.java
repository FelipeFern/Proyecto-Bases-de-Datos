package Vuelos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import quick.dbtable.DBTable;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	private MainPanel contentPane;
	private JTextField tFieldUser;
	private JLabel lblUser, lblPassword;
	private JPasswordField passwordField;
	private JButton btnLogin, btnCancel;
	private DataBaseConnection connection;
	private JMenuItem mntmLogout;
	private static JFrame INSTANCE;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static JFrame getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MainWindow();
		}
		return INSTANCE;
	}

	/**
	 * Create the frame.
	 */
	private MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		connection = DataBaseConnection.getInstance();
		initGUI();
		initContentPane();
	}

	private void initGUI() {
		setBounds(0, 0, 1350, 725);
		setResizable(false);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		JMenuItem mntmLogin = new JMenuItem("Ingresar");
		mntmLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionLogin();
			}
		});
		mnArchivo.add(mntmLogin);

		JMenuItem mntmExit = new JMenuItem("Salir");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (mntmLogout.isEnabled()) {
					actionDisconnect();
				}
				exitAction();
			}
		});

		mntmLogout = new JMenuItem("Desconectarse");
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionDisconnect();
			}
		});
		mnArchivo.add(mntmLogout);
		mnArchivo.add(mntmExit);
		mntmLogout.setEnabled(false);
	}

	private void initContentPane() {
		contentPane = new MainPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setVisible(true);

		btnLogin = new JButton("Ingresar");
		btnLogin.setBounds(529, 292, 114, 25);
		contentPane.add(btnLogin);
		btnLogin.setVisible(true);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionLogin();
			}
		});

		lblUser = new JLabel("Usuario");
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblUser.setBounds(505, 220, 85, 25);
		lblUser.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(lblUser);
		lblUser.setVisible(true);

		lblPassword = new JLabel("Contrase�a");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(505, 256, 85, 25);
		lblPassword.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(lblPassword);
		lblPassword.setVisible(true);

		tFieldUser = new JTextField();
		tFieldUser.setHorizontalAlignment(SwingConstants.CENTER);
		tFieldUser.setToolTipText("");
		tFieldUser.setColumns(10);
		tFieldUser.setBounds(600, 220, 200, 25);
		contentPane.add(tFieldUser);
		tFieldUser.setVisible(true);

		passwordField = new JPasswordField();
		passwordField.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField.setBounds(600, 257, 200, 25);
		contentPane.add(passwordField);
		passwordField.setVisible(true);

		btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				passwordField.setText("");
				tFieldUser.setText("");
			}
		});
		btnCancel.setBounds(669, 292, 114, 25);
		contentPane.add(btnCancel);
		btnCancel.setVisible(true);
	}

	private void actionDisconnect() {
		DBTable table = contentPane.getDBTable();
		if (table != null) {
			connection.disconnectDataBase(table);
		}
		initContentPane();
		mntmLogout.setEnabled(false);
		contentPane.revalidate();
		contentPane.repaint();
	}

	private void actionLogin() {
		DBTable table = new DBTable();
		String username = tFieldUser.getText();
		String password = new String(passwordField.getPassword());
		if (connection.connectToDatabase(table, username, password)) {
			if (username.equals("admin")) {
				contentPane = new AdminPanel(table);
			} else {
				DBTable table2 = new DBTable();
				DBTable table3 = new DBTable();
				connection.connectToDatabase(table2, username, password);
				connection.connectToDatabase(table3, username, password);
				contentPane = new EmployeePanel(table, table2, table3);
			}
			setContentPane(contentPane);
			setVisible(true);
			mntmLogout.setEnabled(true);
		}
	}

	private JFrame getFrame() {
		return this;
	}

	private void exitAction() {
		Object[] options = { "Si", "No" };
		int n = JOptionPane.showOptionDialog(getFrame(), "¿Realmente desea salir de la aplicación?", "Cerrar",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (n == 0) {
			Frame f = getFrame();
			f.dispose();
		}
	}
}
