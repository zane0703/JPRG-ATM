package ATM;

import java.math.BigDecimal;

/**
 * @author Ang Yun, Zane Student ID:P1949955 class: DIT/FT/1B/01
 */
public abstract class BankAccount implements java.io.Serializable {

 private final int accountNumber;
 private String password;
 private BigDecimal balance;
 private boolean notLock;
 private String userName;

 public BankAccount(final String userName, final int accountNumber, final String password, final BigDecimal balance, final boolean notLock) {
	this.userName = userName;
	this.accountNumber = accountNumber;
	this.password = password;
	this.balance = balance;
	this.notLock = notLock;

 }

 protected String getName() {
	return userName;
 }

 public int getAccNum() {
	return accountNumber;
 }

 protected boolean isNotLock() {
	return notLock;
 }

 protected String getPassword() {
	return password;
 }

 protected BigDecimal getBalance() {
	return balance;
 }

 protected void setBalance(BigDecimal balance) {
	this.balance = balance;
 }

 protected void setPassword(String password) {
	this.password = password;
 }

 protected void lock() {
	this.notLock = false;
 }

 protected String getInterestRate() {
	return "";
 }

 protected abstract char[] getChars();

}
