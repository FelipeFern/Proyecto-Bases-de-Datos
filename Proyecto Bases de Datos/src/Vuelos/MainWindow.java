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
		setBounds(100, 100, 1024, 600);
		setResizable(false);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);

		JMenuItem mntmLogin = new JMenuItem("Ingresar");
		mntmLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setAllToVisible();
			}
		});
		mnArchivo.add(mntmLogin);

		JMenuItem mntmExit = new JMenuItem("Salir");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exitAction();
			}
		});

		mntmLogout = new JMenuItem("Desconectarse");
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DBTable table = contentPane.getDBTable();
				if (table != null) {
					connection.disconnectDataBase(table);
				}
				initContentPane();
				contentPane.setVisible(true);
				mntmLogout.setEnabled(false);
				getFrame().repaint();
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

		btnLogin = new JButton("Ingresar");
		btnLogin.setBounds(12, 12, 114, 25);
		contentPane.add(btnLogin);
		btnLogin.setEnabled(false);
		btnLogin.setVisible(false);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DBTable table = new DBTable();
				String username = tFieldUser.getText();
				String password = new String(passwordField.getPassword());
				if (connection.connectToDatabase(table, username, password)) {
					if (username.equals("admin")) {
						contentPane = new AdminPanel(table);
					} else {
						contentPane = new EmployeePanel(table);
					}
					setContentPane(contentPane);
					setVisible(true);
					mntmLogout.setEnabled(true);
				}
			}
		});

		lblUser = new JLabel("Usuario");
		lblUser.setHorizontalAlignment(SwingConstants.CENTER);
		lblUser.setBounds(12, 90, 85, 25);
		lblUser.setBorder(BorderFactory.createLineBorder(Color.black));
		lblUser.setVisible(false);
		lblUser.setEnabled(false);
		contentPane.add(lblUser);

		lblPassword = new JLabel("Contraseña");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(12, 141, 85, 25);
		lblPassword.setBorder(BorderFactory.createLineBorder(Color.black));
		lblPassword.setVisible(false);
		lblPassword.setEnabled(false);
		contentPane.add(lblPassword);

		tFieldUser = new JTextField();
		tFieldUser.setHorizontalAlignment(SwingConstants.CENTER);
		tFieldUser.setToolTipText("");
		tFieldUser.setColumns(10);
		tFieldUser.setBounds(115, 90, 200, 25);
		tFieldUser.setVisible(false);
		tFieldUser.setEnabled(false);
		contentPane.add(tFieldUser);

		passwordField = new JPasswordField();
		passwordField.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField.setBounds(115, 141, 200, 25);
		passwordField.setVisible(false);
		passwordField.setEnabled(false);
		contentPane.add(passwordField);

		btnCancel = new JButton("Cancelar");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setAllToNotVisible();
				btnCancel.setVisible(false);
				btnCancel.setEnabled(false);
			}
		});
		btnCancel.setBounds(12, 49, 114, 25);
		btnCancel.setEnabled(false);
		btnCancel.setVisible(false);
		contentPane.add(btnCancel);

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

	private void setAllToNotVisible() {
		passwordField.setVisible(false);
		passwordField.setEnabled(false);
		passwordField.setText("");
		tFieldUser.setVisible(false);
		tFieldUser.setEnabled(false);
		tFieldUser.setText("");
		lblUser.setVisible(false);
		lblUser.setEnabled(false);
		lblPassword.setVisible(false);
		lblPassword.setEnabled(false);
		btnLogin.setEnabled(false);
		btnLogin.setVisible(false);
	}

	private void setAllToVisible() {
		passwordField.setVisible(true);
		passwordField.setEnabled(true);
		tFieldUser.setVisible(true);
		tFieldUser.setEnabled(true);
		lblUser.setVisible(true);
		lblUser.setEnabled(true);
		lblPassword.setVisible(true);
		lblPassword.setEnabled(true);
		btnCancel.setEnabled(true);
		btnCancel.setVisible(true);
		btnLogin.setEnabled(true);
		btnLogin.setVisible(true);
	}
}
