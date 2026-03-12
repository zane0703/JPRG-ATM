
/**
 * @author Ang Yun, Zane
 * Student ID:P1949955
 * class: DIT/FT/1B/01
 */
/*ATMUser is not in the ATM package because I do not want ATMUser to have 
direct access to BankAccount class*/
import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.UIManager;
public class ATMUser extends java.awt.Frame{

 public static ATMUser atmUser;

 public ATMUser() {
 this.setIconImage(java.awt.Toolkit.getDefaultToolkit().getImage(ATMUser.class.getResource("favicon.png")));
	this.setLayout(new java.awt.GridLayout(2, 1));
	this.setTitle("Automated Teller Machine");
	JPanel numPag = new JPanel();
	numPag.setPreferredSize(new java.awt.Dimension(400, 200));
	numPag.setLayout(new java.awt.GridLayout(0, 4));
	JButton[] number = new JButton[11];
	JButton[] spacialKey = {new JButton("Cancel"), new JButton("Clear"), new JButton("Enter")};
	spacialKey[0].setBackground(new Color(200, 0, 0));
	spacialKey[1].setBackground(new Color(200, 200, 0));
	spacialKey[2].setBackground(new Color(0, 200, 0));
	// setDefaultCloseOperation(0);
	setResizable(false);
	spacialKey[0].setEnabled(false);
	ATM.ATM atm = new ATM.ATM() {
	 @Override
	 public void setEnablePad(boolean enable) {
		for (JButton i : number) {
		 i.setEnabled(enable);
		}
		spacialKey[1].setEnabled(enable);
	 }

	 @Override
	 public void setEnableCancel(boolean enable) {
		spacialKey[0].setEnabled(enable);
	 }
	};
	this.add(atm);
	this.addWindowListener(new java.awt.event.WindowAdapter() {
	 @Override
	 public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		if (javax.swing.JOptionPane.showConfirmDialog(atmUser,
			"Are you sure you want to close this window?", "Close ATM?", 0, 3) == 0) {
		 System.exit(0);
		}
	 }
	});
	number[9] = new JButton(".");
	number[9].setActionCommand("10");
	number[10] = new JButton("0");
	this.setVisible(true);
	ActionListener numActIon = a -> atm.setInput(Integer.parseInt(a.getActionCommand()));
	for (byte i = 0; i < number.length; i++) {
	 if (i < 9) {
		number[i] = new JButton(Integer.toString(i + 1));
		numPag.add(number[i]);
		if (i % 3 == 2) {
		 numPag.add(spacialKey[(i + 1) / 3 - 1]);
		}
	 } else {
		numPag.add(number[i]);
	 }
	 number[i].addActionListener(numActIon);
	}

	spacialKey[0].addActionListener( e -> atm.exitATM(false));
	spacialKey[2].addActionListener(atm.doEnter);
	spacialKey[1].addActionListener( e -> atm.setInput(11));
	this.add(numPag);
	this.pack();
	atm.findAccount();

 }

 public static void main(String[] args) {
	String[][] styleKey = {{"OptionPane.background", "InternalFrame.background", "Panel.background"},
	 {"TextField.background", "Label.background", "PasswordField.background"}};
	String[] whiteStyle = {"RadioButton.foreground", "TextArea.foreground", "OptionPane.messageForeground", 
	 "Label.foreground", "TextField.foreground", "Button.foreground", "TextField.caretForeground", 
	 "PasswordField.foreground", "PasswordField.caretForeground"};
	for (int i = 0; i < 3; i++) {
	 UIManager.put(styleKey[0][i], new Color(10, 10, 10));
	 UIManager.put(styleKey[1][i], new Color(50, 50, 50));
	}
	for (String i : whiteStyle) {
	 UIManager.put(i, new Color(255, 255, 255));
	}
	UIManager.put("OptionPane.cancelButtonText", "Back");
	UIManager.put("Button.background", new Color(60, 60, 60));
	UIManager.put("RadioButton.background", new Color(10, 10, 10));
	UIManager.put("Button.select", new Color(100, 100, 100));
	UIManager.put("TextField.border", BorderFactory.createLineBorder(new Color(50, 50, 50)));
	UIManager.put("Button.border", BorderFactory.createLineBorder(new Color(10, 10, 10)));
	UIManager.put("PasswordField.border", BorderFactory.createLineBorder(new Color(50, 50, 50)));
	atmUser = new ATMUser();

 }
}
