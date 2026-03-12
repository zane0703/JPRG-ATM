package ATM;

/*
Module by
Miller, D. 2015. jBCrypt. Australia.

Icon by 
key,tick. 2019. [extract from imageres.dll] microsoft corporation.

preloaders.net 2019. Windows8 loader loading image. [image]
Available at: https://icons8.com/preloaders/en/circular/windows8-loader/ 
[Accessed 13 Dec. 2019].

sound effects by

google translate. 2019. [online] Google LLC.
Available at: https://translate.google.com/ [Accessed 13 Dec. 2019].

Castiglione, C. 2011. newport-atm. [online] freesound. 
Available at: https://freesound.org/people/alienistcog/sounds/124982/
[Accessed 13 Dec. 2019].

Spelten, C. 2015. Cash Machine, Indoors, Full Transaction. [online] freesound.
Available at: https://freesound.org/people/Claudius/sounds/267153/ 
[Accessed 13 Dec. 2019].
 */
/**
 * @author Ang Yun, Zane Student ID:P1949955 class: DIT/FT/1B/01
 */
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.FileReader;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import org.mindrot.jbcrypt.BCrypt;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import javax.swing.JRadioButton;

public abstract class ATM extends javax.swing.JPanel implements ActionListener {

 private SoundEffect soundEffect = new SoundEffect();
 private BankAccount[] bankAccounts;
 private byte j = -1;
 private final java.text.DecimalFormat format = new java.text.DecimalFormat();
 private JTextField inputText;
 private byte numTry = 0;
 private JButton enter = new JButton("Enter");
 private final Insets insets = new Insets(5, 5, 5, 5);
 /* to play a beep sound effect when the user press any key
    some text field may not have this feature because in a real ATM it will be
    physical action like putting the ATM card into the reader
  */
 private final java.awt.event.KeyListener keyListener = new java.awt.event.KeyListener() {
	@Override
	public void keyTyped(KeyEvent e) {
	 final char keyChar = e.getKeyChar();
	 /*Check if the input is a number if not the input will be  ignored*/
	 if (!Character.isDigit(keyChar) && keyChar != '.') {
		e.consume();
	 }
	}

	@Override
	public void keyPressed(KeyEvent e) {
	 if (e.getKeyCode() == 10) {
		e.consume();
		enter.doClick();
	 }

	 soundEffect.playBeep();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
 };

 public abstract void setEnablePad(boolean enable);

 public abstract void setEnableCancel(boolean enable);

 public ATM() {
setPreferredSize(new Dimension(500, 40));
	/*create 3 BankAccount objects */
	this.setLayout(new GridBagLayout());
	try {
		java.io.ObjectInputStream objectInputStream =new java.io.ObjectInputStream(new java.io.FileInputStream("./banaccounta.dat"));
	    bankAccounts = (BankAccount[]) objectInputStream.readObject();
	    objectInputStream.close();
	} catch (FileNotFoundException e) {
		 /*split the data into an array*/
		 /*get the text file that info of the account as buffer*/
		e.printStackTrace();
		try {
			final BufferedReader br = new BufferedReader(new FileReader("./account.txt"));
			bankAccounts = new BankAccount[Integer.parseInt(br.readLine())];
			 for (int i = 0; i < bankAccounts.length; i++) {
				String[] accountInfo = br.readLine().split(";");
				switch (accountInfo[5]) {
				 case "Savings Account":
					bankAccounts[i] = new Saving(accountInfo[0], Integer.parseInt(accountInfo[1]), BCrypt.hashpw(accountInfo[2], BCrypt.gensalt()), new BigDecimal(accountInfo[3]), Boolean.parseBoolean(accountInfo[4]));
					break;
				 case "Fixed Deposit account":
					bankAccounts[i] = new FixedDeposit(accountInfo[0], Integer.parseInt(accountInfo[1]), BCrypt.hashpw(accountInfo[2], BCrypt.gensalt()), new BigDecimal(accountInfo[3]), Boolean.parseBoolean(accountInfo[4]));
					break;
				 default:
					 System.out.println("test2");
					throw new Exception("invalid account type");
				}
			 }
			 br.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}catch (Exception e2) {
			e.printStackTrace();
			System.exit(1);
		}
		 
	}
	catch (Exception e) {
	 e.printStackTrace();
	}
 }

 public void setInput(int num) {
	soundEffect.playBeep();
	inputText.requestFocus();
	switch (num){
		case 10:
			inputText.setText(inputText.getText() + ".");
			break;
		case 11:
			inputText.setText("");
			break;
		default:
			inputText.setText(inputText.getText() + num);
	}
 }

 /*add a loading screen */
 private void loading() {
	this.removeAll();
	this.add(new JLabel(new ImageIcon(this.getClass().getResource("ATM/loading.gif"))));
	this.add(new JLabel(" Please wait..."));
	this.revalidate();
	this.repaint();

 }

 private void showMessage(javax.swing.JComponent text) {
	for (ActionListener i : enter.getActionListeners()) {
	 enter.removeActionListener(i);
	}
	this.removeAll();
	this.add(text, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, insets, 0, 0));
	this.add(enter, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 0, insets, 50, 0));
	this.revalidate();
	this.repaint();
 }
 public final ActionListener doEnter = e -> enter.doClick();

 public void findAccount() {
	for (ActionListener i : enter.getActionListeners()) {
	 enter.removeActionListener(i);
	}
	setEnablePad(true);
	setEnableCancel(false);
	JTextField input2 = new JTextField(10);
	this.removeAll();
	input2.addKeyListener(keyListener);
	this.add(new JLabel("Please insert your ATM card \n or enter account number: "),
		new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, insets, 0, 0));
	this.add(input2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 0, insets, 0, 0));
	this.add(enter, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 10, 0, insets, 50, 0));
	input2.requestFocus();
	this.revalidate();
	this.repaint();
	inputText = input2;
	enter.addActionListener(e -> {
	 soundEffect.playBeep();
	 String input = input2.getText();
	 try {
		final int accNum = Integer.parseInt(input);
		soundEffect.setFile("cardIn.wav");
		soundEffect.play();
		loading();
		new Timer(1500, a -> {
		 for (int i = 0, length = bankAccounts.length ; length > i; i++) {
			if (bankAccounts[i].getAccNum() == accNum) {
			 j = (byte)i;
			 break;
			}
		 }

		 if (j == -1) {
			this.removeAll();
			showMessage(new JLabel("<html><font color='red'>Invalid account, please re-enter</font></html>"));
			enter.addActionListener(e2 -> {
			 soundEffect.playBeep();
			 for (ActionListener i : enter.getActionListeners()) {
				enter.removeActionListener(i);
			 }
			 returnCard();
			 enter.addActionListener(e3 -> {
				for (ActionListener i : enter.getActionListeners()) {
				 enter.removeActionListener(i);
				}
				findAccount();
			 });

			});

		 } else if (bankAccounts[j].isNotLock()) {
			askPassword(true);
		 } else {
			showMessage(new JLabel("<html><font color='red'>Account lock due to multiple failed "
				+ "login attempts,<br> please go to our nearest bank to unlock your Account</font></html>"));
			enter.addActionListener(e2 -> {
			 soundEffect.playBeep();
			 for (ActionListener i : enter.getActionListeners()) {
				enter.removeActionListener(i);
			 }

			 findAccount();

			});
		 }
		});

	 } catch (Exception a) {
		showMessage(new JLabel("<html><font color='red'>Invalid input, please re-enter"
			+ "</font></html>"));
		System.out.println(e);
		enter.addActionListener(e2 -> {
		 soundEffect.playBeep();
		 for (ActionListener i : enter.getActionListeners()) {
			enter.removeActionListener(i);
		 }
		 findAccount();

		});
	 }
	}
	);

 }

 public void askPassword(boolean notshow) {
	if (notshow) {
	 soundEffect.setFile("welcome.wav");
	 soundEffect.play();
	 numTry = 0;
	}
	setEnablePad(true);
	setEnableCancel(true);
	JPasswordField pass = new JPasswordField(10);
	this.removeAll();
	pass.addKeyListener(keyListener);
	this.add(new JLabel("Hello " + bankAccounts[j].getName() + ", Welcome to Zane's bank."),
		new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, insets, 0, 0));
	this.add(new JLabel(" Enter your password:"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
		10, 0, insets, 0, 0));
	this.add(pass, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 10, 0, insets, 0, 0));
	this.add(enter, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, 10, 0, insets, 50, 0));
	pass.requestFocus();
	this.revalidate();
	this.repaint();
	inputText = pass;
	for (ActionListener i : enter.getActionListeners()) {
	 enter.removeActionListener(i);
	}
	enter.addActionListener((e) -> {
	 soundEffect.playBeep();
	 char[] pin = pass.getPassword();
	 try {
		Integer.parseInt(new String(pin));
		/*BCrypt is hash the password so if someine get into the database they know the password*/
		if (BCrypt.checkpw(new String(pin), bankAccounts[j].getPassword())) {
		 menu();
		} else {
		 showMessage(new JLabel("<html><font color='red'>Invalid password, please re-enter</font></html>"));
		 enter.addActionListener(e2 -> {
			soundEffect.playBeep();
			for (ActionListener i : enter.getActionListeners()) {
			 enter.removeActionListener(i);
			}
			numTry++;
			if (numTry > 3) {
			 bankAccounts[j].lock();
			 saveTransaction();
			 showMessage(new JLabel(/*language=html*/ "<html><font color='red'>Account lock due to multiple failed login attempts,<br> please go to our nearest bank to unlock your Account</font></html>"));
			 enter.addActionListener((e3) -> {
				soundEffect.playBeep();
				for (ActionListener i : enter.getActionListeners()) {
				 enter.removeActionListener(i);
				}
				findAccount();
			 });

			} else {
			 askPassword(false);
			}

		 });
		}
	 } catch (NumberFormatException err) {
		showMessage(new JLabel(/*language=html*/ "<html><font color='red'>Invalid key, please re-enter.</font></html>"));
		enter.addActionListener((e2) -> {
		 soundEffect.playBeep();
		 for (ActionListener i : enter.getActionListeners()) {
			enter.removeActionListener(i);
		 }
		 askPassword(false);

		});
	 } catch (Exception err) {
		System.out.println(err);
	 }

	});

 }

 private void menu() {
	for (ActionListener i : enter.getActionListeners()) {
	 enter.removeActionListener(i);
	}
	setEnablePad(false);
	String[] btnName = {"Print Statement", "Withdraw", "Deposit cash", "Change Password", "Fund Transfer"};
	String[] btnQuick = {"$50", "$100", "$200", "$1,000"};
	Insets quIn = new Insets(5, 100, 5, 5);
	this.removeAll();
	this.add(new JLabel("Please select a transaction"),
		new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, 10, 0, insets, 0, 0));
	for (byte i = 0; i < 5; i++) {
	 JButton btn = new JButton(btnName[i]);
	 btn.setMinimumSize(new Dimension(1000, 20));
	 JButton quick;
	 btn.addActionListener(this);
	 this.add(btn, new GridBagConstraints(0, i + 1, 1, 1, 0.0, 0.0, 17, 1, insets, 100, 0));
	 if (i < 4) {
		quick = new JButton(btnQuick[i]);
		quick.addActionListener(this);
		this.add(quick, new GridBagConstraints(2, i + 1, 1, 1, 0.0, 0.0, 13, 1, quIn, 100, 0));
	 }
	}

	this.revalidate();
	this.repaint();

 }

 @Override
 public void actionPerformed(ActionEvent e) {
	switch (e.getActionCommand()) {
	 case "Print Statement":
		getBalance();
		break;
	 case "Withdraw":
		withdraw();
		break;
	 case "Deposit cash":
		depositCash();
		break;
	 case "Fund Transfer":
		fundTransfer();
		break;
	 case "Change Password":
		changePassword();
		break;
	 case "$50":
		withdraw(new BigDecimal(50));
		break;
	 case "$100":
		withdraw(new BigDecimal(100));
		break;
	 case "$200":
		withdraw(new BigDecimal(200));
		break;
	 case "$1,000":
		withdraw(new BigDecimal(1000));
		break;
	}
 }

 public void getBalance() {
	BufferedReader file;
	try {
	 file = new BufferedReader(new FileReader("Log/"
		 + Integer.toString(bankAccounts[j].getAccNum()) + ".log"));
	 soundEffect.playBeep();
	 for (ActionListener i : enter.getActionListeners()) {
		enter.removeActionListener(i);
	 }
	 javax.swing.JPanel panel = new javax.swing.JPanel();
	 panel.setLayout(new GridBagLayout());
	 panel.add(new JLabel(" Your back account have $" + format.format(bankAccounts[j].getBalance())
		 + " and your interest rate is " + bankAccounts[j].getInterestRate()),
		 new GridBagConstraints(0, 0, 5, 1, 0.0, 0.0, 17, 0, insets, 0, 0));
	 String[] topBar = {"Date", "Time", "Transaction", "Amount", "Balance"};
	 int k = 0;
	 for (String i : topBar) {
		panel.add(new JLabel(i), new GridBagConstraints(k++, 1, 1, 1, 0.0, 0.0, 17, 0, insets, 0, 0));
	 }
	 for (String line = ""; (line = file.readLine()) != null;) {
		String[] line2 = line.split(";");
		for (int i = 0; i < line2.length; i++) {
		 panel.add(new JLabel(line2[i]), new GridBagConstraints(i, k, 1, 1, 0.0, 0.0, 17, 0, insets, 0, 0));
		}
		k++;
	 }
	 javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(panel, 22, 31);
	 scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
		@Override
		protected void configureScrollBarColors() {
		 this.thumbColor = new Color(50, 50, 50);
		 this.trackColor = new Color(0, 0, 0);
		}
	 });
	 scroll.setMinimumSize(new Dimension(500, 150));
	 showMessage(scroll);
	 enter.setText("back");
	 for (ActionListener i : enter.getActionListeners()) {
		enter.removeActionListener(i);
	 }
	 enter.addActionListener((e) -> {
		soundEffect.playBeep();
		enter.setText("Enter");
		menu();
	 });

	} catch (IOException e) {
	 System.out.println(e);
	} catch (Exception e) {
	 System.out.println(e);
	}
 }

 private void withdraw() {
	for (ActionListener i : enter.getActionListeners()) {
	 enter.removeActionListener(i);
	}
	setEnablePad(true);
	soundEffect.playBeep();
	byte[] note = {0};
	final BigDecimal balance = bankAccounts[j].getBalance();
	JTextField text = new JTextField();
	JButton back = new JButton("back");
	JRadioButton[] radioBtn = new JRadioButton[3];
	javax.swing.ButtonGroup b = new javax.swing.ButtonGroup();
	javax.swing.JPanel redio = new javax.swing.JPanel();
	String[] retioText = {"Auto", "$10", "$50"};
	for (byte i = 0; i < 3; i++) {
		radioBtn[i] = new JRadioButton(retioText[i]);
		redio.add(radioBtn[i]);
		b.add(radioBtn[i]);
		Byte k = i;
		radioBtn[i].addItemListener(e -> {
			if (e.getStateChange() == 1) {
				note[0] = k;
			}
		});
	}
	radioBtn[0].setSelected(true);
	javax.swing.border.TitledBorder border = javax.swing.BorderFactory.createTitledBorder("note denominations");
	border.setTitleColor(Color.white);
	redio.setBorder(border);
	inputText = text;
	this.removeAll();
	text.addKeyListener(keyListener);
	this.add(new JLabel("please enter the amount to withdrawal"), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, insets, 0, 0));
	this.add(new JLabel("(minimum $10)"), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 0, insets, 0, 0));
	this.add(text, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 10, 0, insets, 100, 0));
	this.add(redio, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, 10, 0, insets, 100, 0));
	this.add(back, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, 17, 0, insets, 50, 0));
	this.add(enter, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, 13, 0, insets, 50, 0));
	back.addActionListener( eve -> {
	 soundEffect.playBeep();
	 menu();
	});
	text.requestFocus();
	this.revalidate();
	this.repaint();
	enter.addActionListener((eve) -> {
	 soundEffect.playBeep();
	 setEnablePad(false);
	 setEnableCancel(false);
	 try {
		final BigDecimal withdrawAmount =  new BigDecimal(text.getText());
		if (withdrawAmount.compareTo(balance) == -1) {
		 if (withdrawAmount.doubleValue() % 10 == 0) {
			int noteAmount[] = {(int) withdrawAmount.intValue(), 0, 0, 0};
			BigDecimal newBalance = balance.subtract(withdrawAmount);
			switch (note[0]) {
			 case 0:
				noteAmount[1] = (int) Math.floor(noteAmount[0] / 100);
				noteAmount[0] = noteAmount[0] % 100;
			 case 2:
				noteAmount[2] = (int) Math.floor(noteAmount[0] / 50);
				noteAmount[0] = noteAmount[0] % 50;
			 case 1:
				noteAmount[3] = (int) Math.floor(noteAmount[0] / 10);
			}
			bankAccounts[j].setBalance(newBalance);
			saveTransaction();
			soundEffect.setFile("counting.wav");
			soundEffect.play();
			loading();
			new Timer(3000, (event) -> {
			 returnCard();
			 for (ActionListener i : enter.getActionListeners()) {
				enter.removeActionListener(i);
			 }
			 enter.addActionListener((e) -> {
				soundEffect.setFile("cashOut.wav");
				soundEffect.play();
				new Thread() {
				 @Override
				 public void run() {
					try {
					 for (byte i = 0; i < 4; i++) {
						this.sleep(250);
						soundEffect.playBeep();
					 }
					} catch (InterruptedException e) {
					}
				 }
				}.start();
				showMessage(new JLabel( /*language=html*/ "<html> New balance: $" + format.format(newBalance) + "<br>$100 X "
					+ noteAmount[1] + "<br>$50 X " + noteAmount[2] + "<br>$10 X " + noteAmount[3]
					+ "<br>Place remanber to take your cash<br>↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓"));
				enter.addActionListener((e2) -> {
				 soundEffect.setFile("close.wav");
				 soundEffect.play();
				 new Timer(500, (eve2) -> {
					addLog("Withdraw;$" + format.format(withdrawAmount)
						+ ";" + format.format(newBalance));
					exitATM(true);
				 });
				});

			 });

			});

		 } else {
			showMessage(new JLabel(/*language=html*/ "<html><font color='red'>Your withdrawal amount must be above $10 as the smaller note is $10</font></html>"));
			enter.addActionListener((e) -> {
			 soundEffect.playBeep();
			 for (ActionListener i : enter.getActionListeners()) {
				enter.removeActionListener(i);
			 }
			 setEnableCancel(true);
			 withdraw();

			});

		 }
		} else {
		 showMessage(new JLabel(/*language=html*/ "<html><font color='red'>Your withdrawal amount have exceeded the balance"));
		 enter.addActionListener((e) -> {
			soundEffect.playBeep();
			for (ActionListener i : enter.getActionListeners()) {
			 enter.removeActionListener(i);
			}
			setEnableCancel(true);
			withdraw();

		 });
		}
	 } catch (NumberFormatException e) {
		showMessage(new JLabel(/*language=html*/  "<html><font color='red'>Invalid input, please re-enter.</font></html>"));
		enter.addActionListener((e2) -> {
		 soundEffect.playBeep();
		 for (ActionListener i : enter.getActionListeners()) {
			enter.removeActionListener(i);
		 }
		 setEnableCancel(true);
		 withdraw();

		});
	 } catch (Exception e) {
	 } finally {
		text.setText(null);
	 }
	});

 }

 /* for quick Withdraw*/
 private void withdraw(final BigDecimal withdrawAmount) {
	for (ActionListener i : enter.getActionListeners()) {
	 enter.removeActionListener(i);
	}
	soundEffect.playBeep();
	BigDecimal balance = bankAccounts[j].getBalance();
	double withdrawAmountDouble = withdrawAmount.doubleValue();
	if (withdrawAmountDouble < balance.doubleValue() && withdrawAmountDouble > 0) {
	 final BigDecimal newBalance = balance.subtract(withdrawAmount);
	 bankAccounts[j].setBalance(newBalance);
	 saveTransaction();
	 soundEffect.setFile("counting.wav");
	 soundEffect.play();
	 setEnableCancel(false);
	 loading();
	 new Timer(3000, (eve) -> {
		returnCard();
		for (ActionListener i : enter.getActionListeners()) {
		 enter.removeActionListener(i);
		}
		enter.addActionListener((e) -> {
		 soundEffect.setFile("cashOut.wav");
		 soundEffect.play();
		 new Thread() {
			@Override
			public void run() {
			 try {
				for (byte i = 0; i < 4; i++) {
				 this.sleep(250);
				 soundEffect.playBeep();
				}
			 } catch (Exception e) {
			 }
			}
		 }.start();
		 showMessage(new JLabel(/*language=html*/ "<html> New balance: $"
			 + format.format(newBalance)
			 + "<br>Place remanber to take your cash<br>↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓"));
		 addLog("Withdraw;$" + format.format(withdrawAmount) + ";$" + format.format(newBalance));
		 enter.addActionListener((e2) -> {
			soundEffect.setFile("close.wav");
			soundEffect.play();
			new Timer(500, (e3) -> {
			 exitATM(true);
			});

		 });

		});

	 });
	} else {
	 showMessage(new JLabel(/*language=html*/ "<html><font color='red'>Your withdrawal amount have exceeded the balance</font></html>"));
	 enter.addActionListener((e) -> {
		setEnableCancel(true);
		menu();
	 });
	}
 }

 private void depositCash() {
	for (ActionListener i : enter.getActionListeners()) {
	 enter.removeActionListener(i);
	}
	setEnablePad(true);
	soundEffect.setFile("cashOut.wav");
	soundEffect.play();
	soundEffect.playBeep();
	final BigDecimal balance = bankAccounts[j].getBalance();
	JButton back = new JButton("Back");
	JTextField text = new JTextField(15);
	inputText = text;
	this.removeAll();
	text.addKeyListener(keyListener);
	this.add(new JLabel("please enter the amount to deposit"),
		new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, insets, 0, 0));
	this.add(text, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 0, insets, 0, 0));
	this.add(back, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 17, 0, insets, 50, 0));
	this.add(enter, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 13, 0, insets, 50, 0));
	this.revalidate();
	this.repaint();
	text.requestFocus();
	back.addActionListener((eve) -> {
	 soundEffect.playBeep();
	 soundEffect.setFile("close.wav");
	 soundEffect.play();
	 menu();
	});
	enter.addActionListener((eve) -> {
	 setEnablePad(false);
	 setEnableCancel(false);
	 BigDecimal[] depositAmount = new BigDecimal[1];
	 try {

		/*ask for amount to deposit*/
		depositAmount[0] = new BigDecimal(text.getText());
		if (depositAmount[0].compareTo(BigDecimal.ZERO) == 1) {
		 soundEffect.playBeep();
		 soundEffect.setFile("close.wav");
		 soundEffect.play();
		 loading();
		 Thread.sleep(500);
		 soundEffect.setFile("counting.wav");
		 soundEffect.play();
		 new Timer(3000, (e3) -> {
			JButton no = new JButton("No");
			this.removeAll();
			this.add(new JLabel("Are you sure you want to deposit $"
				+ format.format(depositAmount[0])),
				new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
					10, 0, insets, 0, 0));
			enter.setText("Yes");
			this.add(no, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				17, 0, insets, 50, 0));
			this.add(enter, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				13, 0, insets, 50, 0));
			setEnableCancel(true);
			this.revalidate();
			this.repaint();
			for (ActionListener i : enter.getActionListeners()) {
			 enter.removeActionListener(i);
			}
			enter.setText("Yes");
			enter.addActionListener((e4) -> {
			 enter.setText("Enter");
			 soundEffect.playBeep();
			 BigDecimal newBalance = balance.add(depositAmount[0]);
			 bankAccounts[j].setBalance(newBalance);
			 saveTransaction();
			 showMessage(new JLabel(/*language=html*/ "<html> You have successfully deposit $" + format.format(depositAmount[0])
				 + " to your bank account <br> new balance: $" + format.format(newBalance)));
			 addLog("Deposit;$" + format.format(depositAmount[0]) + ";$" + format.format(newBalance));
			 enter.addActionListener((e5) -> {
				soundEffect.playBeep();
				exitATM(false);
			 });

			});
			no.addActionListener((e5) -> {
			 enter.setText("Enter");
			 for (ActionListener i : enter.getActionListeners()) {
				enter.removeActionListener(i);
				depositCash();
			 }
			 depositCash();
			});

		 });

		} else {
		 showMessage(new JLabel(/*language=html*/ "<html><font color='red'>depositAmount cannot be negative</font></html>"));
		 enter.addActionListener((e3) -> {
			soundEffect.playBeep();
			depositCash();
		 });

		}
	 } catch (NumberFormatException e) {
		soundEffect.playBeep();
		soundEffect.setFile("close.wav");
		soundEffect.play();
		showMessage(new JLabel(/*language=html*/ "<html><font color='red'>Invalid input, please re-enter.</font></html>"));
		enter.addActionListener((e2) -> {
		 soundEffect.playBeep();
		 depositCash();
		});

	 } catch (Exception e) {
		System.err.print(e);
	 }

	});
 }

 private void fundTransfer() {
	for (ActionListener i : enter.getActionListeners()) {
	 enter.removeActionListener(i);
	}
	setEnablePad(true);
	this.removeAll();
	soundEffect.playBeep();
	JButton next = new JButton("Next");
	JButton back = new JButton("Back");
	JTextField text1 = new JTextField(10);
	JTextField text2 = new JTextField(10);
	/*Listen if the user preess enter go to the next textfield  */
	text1.addKeyListener(keyListener);
	text1.addFocusListener(new NextField(text2));
	text2.addFocusListener(new LastField(next));
	text2.addKeyListener(keyListener);
	this.add(new JLabel(" Enter the account number you world like to transfer to:"),
		new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, 10, 0, insets, 0, 0));
	this.add(text1, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, 10, 0, insets, 100, 0));
	this.add(new JLabel(" Enter the amount you world like to send:"),
		new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, 10, 0, insets, 0, 0));
	this.add(text2, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, 10, 0, insets, 100, 0));
	this.add(back, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, 17, 0, insets, 50, 0));
	this.add(next, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, 13, 0, insets, 50, 0));
	this.revalidate();
	this.repaint();
	BigDecimal balance = bankAccounts[j].getBalance();
	enter.addActionListener((e2) -> next.doClick());
	back.addActionListener((e2) -> {
	 soundEffect.playBeep();
	 setEnablePad(false);
	 menu();
	});
	next.addActionListener((e2) -> {
	 setEnablePad(false);
	 try {
		int transferAcc = Integer.parseInt(text1.getText());
		if (transferAcc != bankAccounts[j].getAccNum()) {
		 final BigDecimal transferAmount = new BigDecimal(text2.getText());
		 if (transferAmount.compareTo(balance) == -1 && transferAmount.compareTo(BigDecimal.ZERO) == 1) {
			for (int[] i = {0}; bankAccounts.length > i[0]; i[0]++) {
			 if (bankAccounts[i[0]].getAccNum() == transferAcc) {
				for (ActionListener k : enter.getActionListeners()) {
				 enter.removeActionListener(k);
				}
				JButton no = new JButton("No");
				this.removeAll();
				this.add(new JLabel("Are you sure you want to transfer $"
					+ format.format(transferAmount) + " to " + transferAcc),
					new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
						10, 0, insets, 0, 0));
				enter.setText("Yes");
				this.add(no, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					17, 0, insets, 50, 0));
				this.add(enter, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					13, 0, insets, 50, 0));
				this.revalidate();
				this.repaint();
				no.addActionListener(e3 -> {
				 soundEffect.playBeep();
				 enter.setText("Enter");
				 fundTransfer();
				});
				enter.addActionListener(e3 -> {
				 soundEffect.playBeep();
				 enter.setText("Enter");
				 for (ActionListener k : enter.getActionListeners()) {
					enter.removeActionListener(k);
				 }
				 BigDecimal newBalance = balance.subtract(transferAmount);
				 bankAccounts[j].setBalance(newBalance);
				 BigDecimal newBalance2 = bankAccounts[i[0]].getBalance().add(transferAmount);
				 bankAccounts[i[0]].setBalance(newBalance2);
				 saveTransaction();
				 addLog("Transfer to " + transferAcc + ";$" + format.format(transferAmount)
					 + ";$" + format.format((newBalance)));
				 try (FileWriter fw = new FileWriter(  new StringBuilder("Log/").append(transferAcc).append(new char[] {'.','l','o','g'},0,4).toString() , true);) {
					 fw.write(java.time.LocalDateTime.now().toString().split("\\.")[0].replace('T', ';'));
					 fw.write(new char[] {';','R', 'e', 'c', 'e', 'i', 'v', 'e', ' ', 'f', 'r', 'o', 'm', ' '},0,14);
					 fw.write(Integer.toString(bankAccounts[j].getAccNum()));
					 char[] $$ =  {';','$'};
					 fw.write($$,0,2);
					 fw.write(format.format(transferAmount));
					 fw.write($$, 0, 2);
					 fw.write(format.format(newBalance2));
					 fw.write('\n');
					 /*fw.write(java.time.LocalDateTime.now().toString().split("\\.")[0].replace('T', ';')
						+ ";" + "Receive from " + bankAccount[j].getAccNum() + ";$" + format.format(transferAmount)
						+ ";$" + format.format(newBalance2) + "\n");*/
					fw.close();
				 } catch (Exception e) {
				 }
				 showMessage(new JLabel("$" + format.format(transferAmount)
					 + "  have successfully transfer to " + transferAcc));
				 enter.addActionListener(eve2 -> {
					soundEffect.playBeep();
					menu();
				 });
				});
				return;
			 }
			}
			showMessage(new JLabel(/*language=html*/ "<html><font color='red'>Bank account not found</font></html>"));
			enter.addActionListener(e -> {
			 soundEffect.playBeep();
			 for (ActionListener i : enter.getActionListeners()) {
				enter.removeActionListener(i);
			 }
			 fundTransfer();

			});
		 } else {
			showMessage(new JLabel(/*language=html*/ "<html><font color='red'>Your transfer amount have exceeded the balance"));
			enter.addActionListener((e) -> {
			 soundEffect.playBeep();
			 for (ActionListener i : enter.getActionListeners()) {
				enter.removeActionListener(i);
			 }
			 fundTransfer();

			});
		 }
		} else {
		 showMessage(new JLabel(/*language=html*/ "<html><font color='red'>You cannot transfer to yourself"));
		 enter.addActionListener((e) -> {
			soundEffect.playBeep();
			for (ActionListener i : enter.getActionListeners()) {
			 enter.removeActionListener(i);
			}
			fundTransfer();
		 });
		}

	 } catch (NumberFormatException e) {
		showMessage(new JLabel(/*language=html*/ "<html><font color='red'>Invalid input, please re-enter</font></html>"));
		enter.addActionListener((e4) -> {
		 soundEffect.playBeep();
		 for (ActionListener i : enter.getActionListeners()) {
			enter.removeActionListener(i);
		 }
		 fundTransfer();

		});
	 } catch (Exception e) {
	 }
	}
	);

	soundEffect.playBeep();
 }

 private void changePassword() {
	for (ActionListener i : enter.getActionListeners()) {
	 enter.removeActionListener(i);
	}
	setEnablePad(true);
	soundEffect.playBeep();
	JPasswordField currPass = new JPasswordField(10);
	JPasswordField pass = new JPasswordField(10);
	JPasswordField pass1 = new JPasswordField(10);
	JButton ok = new JButton("Ok");
	JButton back = new JButton("Back");
	this.removeAll();
	/*Listen if the user preess enter go to the next Password field  */
	currPass.addKeyListener(keyListener);
	pass.addKeyListener(keyListener);
	pass1.addKeyListener(keyListener);
	inputText = currPass;
	currPass.addFocusListener(new NextField(pass));
	pass.addFocusListener(new NextField(pass1));
	pass1.addFocusListener(new LastField(ok));
	javax.swing.JComponent[] com = {new JLabel("Enter your current password"),
	 currPass, new JLabel("Enter your new password:"), pass, new JLabel("Verity your password:"), pass1};
	for (int i = 0; i < 6; i++) {
	 this.add(com[i], new GridBagConstraints(0, i, 2, 1, 0.0, 0.0, 10, 0, insets, i % 2 == 0 ? 0 : 100, 0));
	}
	this.add(back, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, 17, 0, insets, 50, 0));
	this.add(ok, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, 13, 0, insets, 50, 0));
	this.revalidate();
	this.repaint();
	currPass.requestFocus();
	back.addActionListener(e2 -> {
	 soundEffect.playBeep();
	 menu();
	});
	ok.addActionListener((e2) -> {
	 setEnabled(false);
	 soundEffect.playBeep();

	 if (BCrypt.checkpw(new String(currPass.getPassword()),
		 bankAccounts[j].getPassword())) {
		char[] pin = pass.getPassword();
		pass.setText("");
		try {
		 Integer.parseInt(new String(pin));
		 if (java.util.Arrays.equals(pin, pass1.getPassword())) {
			bankAccounts[j].setPassword(BCrypt.hashpw(new String(pin), BCrypt.gensalt()));
			saveTransaction();
			showMessage(new JLabel("Your password has successfully been changed"));
			enter.addActionListener((e) -> {
			 soundEffect.playBeep();
			 addLog("Password Changed; ; ");
			 menu();
			});

		 } else {
			showMessage(new JLabel("<html><font color='red'>New password "
				+ "and verity password not match</font></html>"));
			enter.addActionListener((e3) -> {
			 soundEffect.playBeep();
			 changePassword();
			});

		 }
		} catch (NumberFormatException e) {
		 System.out.println(e);
		 showMessage(new JLabel("<html><font color='red'>invalid key,please re-enter</font></html>"));
		 enter.addActionListener((e3) -> {
			soundEffect.playBeep();
			changePassword();
		 });

		} catch (Exception e) {
		} finally {
		 pass1.setText("");
		}
	 } else {
		showMessage(new JLabel("<html><font color='red'>Wrong current password, please re-enter</font></html>"));
		enter.addActionListener(e -> exitATM(false));
	 }

	});

 }

 public void addLog(String log) {
	try (FileWriter fw = new FileWriter("Log/"
		+ bankAccounts[j].getAccNum() + ".log", true);) {
	fw.write(java.time.LocalDateTime.now().toString().split("\\.")[0].replace('T', ';'));
	fw.write(';');
	fw.write(log);
	fw.write('\n');
	 fw.close();
	} catch (Exception e) {
	}
 }

 /* returnCard and exitATM are separate method because in 
    cash Withdraw  the ATM card will come out before the note*/
 public void exitATM(boolean cardReturned) {
	enter.setText("Enter");
	for (ActionListener i : enter.getActionListeners()) {
	 enter.removeActionListener(i);
	}
	if (cardReturned) {

	 soundEffect.setFile("thankYou.wav");
	 soundEffect.play();
	 showMessage(new JLabel("Thank you for banking with the best."));
	 soundEffect.playBeep();
	 j = -1;
	 enter.addActionListener(e -> {
		soundEffect.playBeep();
		findAccount();
	 });
	} else {
	 returnCard();
	 enter.addActionListener(e -> {
		soundEffect.playBeep();
		exitATM(true);
	 });
	}

 }

 public void returnCard() {
	for (ActionListener i : enter.getActionListeners()) {
	 enter.removeActionListener(i);
	}
	setEnableCancel(false);
	showMessage(new JLabel("Please take your ATM card"));
	new Thread() {
	 @Override
	 public void run() {
		try {
		 for (byte i = 0; i < 4; i++) {
			this.sleep(250);
			soundEffect.playBeep();
		 }
		} catch (InterruptedException e) {
		}
	 }
	}.start();

 }

 private void saveTransaction() {
	try {
		/*
	 FileWriter fw = new FileWriter("./account.txt");
	 fw.write(Integer.toString(bankAccounts.length));
	 for (BankAccount i : bankAccounts) {
		fw.write(System.lineSeparator());
		fw.write(i.getChars());
	 }
	 fw.close();
		 */
	 java.io.ObjectOutputStream os = new java.io.ObjectOutputStream(new java.io.FileOutputStream("./banaccount.dat"));
	 os.writeObject(bankAccounts);
	 os.close();

	} catch (IOException e) {
	}
 }

 private class NextField implements FocusListener, ActionListener {

	private JTextField nextText;

	private NextField(JTextField nextText) {
	 this.nextText = nextText;
	}

	@Override
	public void focusGained(FocusEvent e) {
	 inputText = (JTextField) e.getComponent();
	 for (ActionListener i : enter.getActionListeners()) {
		enter.removeActionListener(i);
	 }
	 enter.addActionListener(this);
	}

	@Override
	public void focusLost(FocusEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	 nextText.requestFocus();
	}
 }

 private class LastField implements FocusListener, ActionListener {

	private JButton next;

	private LastField(JButton next) {
	 this.next = next;
	}

	@Override
	public void focusGained(FocusEvent e) {
	 inputText = (JTextField) e.getComponent();
	 for (ActionListener i : enter.getActionListeners()) {
		enter.removeActionListener(i);
	 }
	 enter.addActionListener(this);
	}

	@Override
	public void focusLost(FocusEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	 next.doClick();
	}
 }

 private class Timer extends javax.swing.Timer {

	public Timer(int delay, ActionListener listener) {
	 super(1, listener);
	 this.setInitialDelay(delay);
	 this.setRepeats(false);
	 this.start();
	}

 }

 private class SoundEffect {

	private Clip clip;
	private Clip beep;

	private SoundEffect() {
	 try {
		beep = AudioSystem.getClip();
		beep.open(AudioSystem.getAudioInputStream(
			this.getClass().getResource("audio/beep.wav")));
	 } catch (Exception e) {
	 }
	}

	public void playBeep() {
	 beep.setFramePosition(0);
	 beep.start();
	}

	public void setFile(String soundFileName) {
	 try {
		clip = AudioSystem.getClip();
		clip.open(AudioSystem.getAudioInputStream(this.getClass().getResource("audio/" + soundFileName)));
	 } catch (Exception e) {
	 }
	}

	public void play() {
	 clip.setFramePosition(0);
	 clip.start();
	}

 }
}
