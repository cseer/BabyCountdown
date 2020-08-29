package countdown.babycountdown;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.ini4j.Wini;

import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class ConfDialog extends JDialog {

	public String getPhrase() {
		return tPhrase;
	}

	public void setPrase(String countDesc) {
		txtPhrase.setText(countDesc); 		
		this.tPhrase = countDesc;
	}

	
	public String getDueDate() {
		return tDueDate;
	}

	public void setDueDate(String sDueDate) {	
		//txtDueDate.setText(sDueDate);
		LocalDate dueDate = LocalDate.parse(sDueDate);
		txtYear.setText(Integer.toString(dueDate.getYear())) ;
		txtDay.setText(Integer.toString(dueDate.getDayOfMonth()));
		int iMonth= dueDate.getMonthValue() - 1;
		comboBox.setSelectedIndex(iMonth);
		this.tDueDate = sDueDate;
	}



	public boolean isShowDueDate() {
		return showDueDate;
	}

	public void setShowDueDate(boolean showDueDate) {
		ckbxShowDueDate.setSelected(showDueDate);
		this.showDueDate = showDueDate;		
	}

	public boolean isForceSave() {
		return forceSave;
	}

	public void setForceSave(boolean forceSave) {
		this.forceSave = forceSave;
	}


	String tPhrase = ""; 
	String tDueDate = "";
	boolean showDueDate = false;
	boolean forceSave = false;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField txtPhrase;
	private JCheckBox ckbxShowDueDate;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBox;
	private JTextField txtDay;
	String[] months = {" January", " February", " March", " April", " May", " June", 
					   " July", " August" , " September", " October", " November", " December"};
	private JTextField txtYear;

	private String buildDueDateString() {
    	int iMonth = comboBox.getSelectedIndex()+1;
    	String sMonth = Integer.toString(iMonth);
    	if (iMonth < 10) {
    		sMonth = "0"+sMonth;
    	}
    	String sDay = txtDay.getText();
    	if ( Integer.valueOf(sDay) < 10) {
    		sDay = "0"+sDay;
    	}
    	
    	String sDueDate = txtYear.getText() + "-" + sMonth + "-" + sDay ;  
    	return sDueDate;
	}
	
	private boolean isDueDateValid() {
		String sDueDate = buildDueDateString();
		boolean validDate = true;
		try {
			@SuppressWarnings("unused")
			LocalDate tDueDate = LocalDate.parse(sDueDate);
		}catch(Exception e) {
			validDate = false;
		}	
		return validDate;
	}
	
	void saveIni() {
		try{
			File f = new File("countdown.ini");
			if (!f.exists()) {
				System.out.println("ERROR in ConfDialog: countdown.ini do not exist");
				System.out.println("Creating the file");
				f.createNewFile();
		   	}
        	Wini ini = new Wini(f);
        	
        	String sDueDate = LocalDate.now().plusMonths(9).toString();
        	if (isDueDateValid()) {
        		sDueDate = buildDueDateString() ;   
        	} 
        	
        	ini.put("config", "duedate", sDueDate);
        	ini.put("config", "phrase", txtPhrase.getText());
        	if (showDueDate) {
        		ini.put("config", "showduedate", "true");
        	} else {
        		ini.put("config", "showduedate", "false");
        	};
            ini.store();
            f = null;
        // To catch basically any error related to writing to the file
        // (The system cannot find the file specified)
        }catch(Exception e){
            System.err.println(e.getStackTrace());
        }
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ConfDialog dialog = new ConfDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ConfDialog() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (forceSave) {
					saveIni();				
				} 				
			}
		});
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ConfDialog.class.getResource("/SmallIcons/Settings24.png")));
		setFont(new Font("DejaVu Serif", Font.PLAIN, 12));
		setTitle("Baby's Information");
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setBounds(100, 100, 450, 246);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblPhrase = new JLabel("Phrase:");
		lblPhrase.setBounds(54, 83, 60, 15);
		contentPanel.add(lblPhrase);	
	
		txtPhrase = new JTextField();
		txtPhrase.setBounds(165, 77, 222, 27);
		txtPhrase.setColumns(10);
		txtPhrase.setText(tPhrase); 				
		contentPanel.add(txtPhrase);

		
		JLabel lblDueDate = new JLabel("Due Date:");
		lblDueDate.setBounds(54, 44, 99, 15);		
		contentPanel.add(lblDueDate);
		
		ckbxShowDueDate = new JCheckBox("Show Due Date");
		ckbxShowDueDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showDueDate = ckbxShowDueDate.isSelected();				
			}
		});
		ckbxShowDueDate.setBounds(54, 122, 200, 23);
		contentPanel.add(ckbxShowDueDate);
		
		txtDay = new JTextField();
		txtDay.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtDay.setHorizontalAlignment(SwingConstants.CENTER);
		txtDay.setText("1");
		txtDay.setBounds(277, 42, 27, 23);
		contentPanel.add(txtDay);
		txtDay.setColumns(10);
		
		comboBox = new JComboBox(months);
		comboBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
		comboBox.setBounds(165, 41, 101, 24);
		contentPanel.add(comboBox);
		
		txtYear = new JTextField();
		txtYear.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txtYear.setBounds(316, 42, 71, 23);
		contentPanel.add(txtYear);
		txtYear.setColumns(10);

			
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setFont(new Font("SansSerif", Font.PLAIN, 12));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton btnCancel = new JButton("Cancel");
				btnCancel.setIcon(new ImageIcon(ConfDialog.class.getResource("/SmallIcons/Cancel.png")));
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {										
						if (forceSave) {
							saveIni();
						} 
						dispose();
						
					}
				});
				btnCancel.setActionCommand("Cancel");
				buttonPane.add(btnCancel);
			}
			{
				JButton btnOk = new JButton("OK");
				btnOk.setIcon(new ImageIcon(ConfDialog.class.getResource("/SmallIcons/Check.png")));
				btnOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.out.println("Click Ok (Save)");
						if (isDueDateValid()) {
							saveIni();
							dispose();
						} else {
							JOptionPane.showMessageDialog(
									null,
								    "Invalid Date",
								    "ERROR",
								    JOptionPane.ERROR_MESSAGE);

						}
							
					}
				});
				btnOk.setActionCommand("Ok");
				buttonPane.add(btnOk);
				getRootPane().setDefaultButton(btnOk);
			}
		}
	}
}
