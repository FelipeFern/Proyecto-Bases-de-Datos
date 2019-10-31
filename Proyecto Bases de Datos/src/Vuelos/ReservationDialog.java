package Vuelos;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import java.awt.TextField;

public class ReservationDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ReservationDialog dialog = new ReservationDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ReservationDialog() {
		setBounds(100, 100, 400, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblVueloDeIda = new JLabel("Vuelo de ida");
			lblVueloDeIda.setBounds(12, 12, 115, 25);
			contentPanel.add(lblVueloDeIda);
		}
		{
			JLabel lblNewLabel = new JLabel("New label");
			lblNewLabel.setBounds(12, 47, 115, 25);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Vuelo de vuelta");
			lblNewLabel_1.setBounds(137, 12, 115, 25);
			contentPanel.add(lblNewLabel_1);
		}
		{
			JLabel lblNewLabel_2 = new JLabel("New label");
			lblNewLabel_2.setBounds(139, 47, 113, 25);
			contentPanel.add(lblNewLabel_2);
		}
		{
			JLabel lblNewLabel_3 = new JLabel("Tipo DNI");
			lblNewLabel_3.setBounds(12, 139, 150, 25);
			contentPanel.add(lblNewLabel_3);
		}
		
		JSeparator separator = new JSeparator();
		separator.setBounds(12, 84, 776, 2);
		contentPanel.add(separator);
		
		JLabel lblDatosDelCliente = new JLabel("Datos del cliente");
		lblDatosDelCliente.setBounds(12, 98, 150, 29);
		contentPanel.add(lblDatosDelCliente);
		
		TextField textField = new TextField();
		textField.setBounds(12, 170, 150, 25);
		contentPanel.add(textField);
		
		JLabel lblNewLabel_4 = new JLabel("Numero DNI");
		lblNewLabel_4.setBounds(208, 139, 180, 25);
		contentPanel.add(lblNewLabel_4);
		
		TextField textField_1 = new TextField();
		textField_1.setBounds(200, 170, 180, 25);
		contentPanel.add(textField_1);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
