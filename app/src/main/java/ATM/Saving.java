package ATM;

import java.math.BigDecimal;

/**
 *
 * @author Zane
 *  Student ID:P1949955
 * class: DIT/FT/1B/01
 */
public class Saving extends BankAccount {

	public Saving (final String userName, final int accountNumber, final String password, final BigDecimal balance, final boolean notLock) {
		super(userName, accountNumber, password, balance, notLock);
	}
    @Override
    protected String getInterestRate() {
	double balance = getBalance().doubleValue();
	if (balance < 650000.0) {
	    return "0.05% per year";
	} else if (balance < 1000000.0) {
	    return "0.075% per year";
	} else {
	    return "0.1% per year";
	}
    }

    @Override
    public String toString() {
    return new StringBuilder(getName())
    		.append(';')
    		.append(getAccNum())
    		.append(';')
    		.append(getPassword())
    		.append(';')
    		.append(getBalance())
    		.append(';')
    		.append(isNotLock())
    		.append(new char[] {';', 'S', 'a', 'v', 'i', 'n', 'g', 's', ' ', 'A', 'c', 'c', 'o', 'u', 'n', 't'},0,16)
    		.toString();
	//return getName() + ";" + getAccNum() + ";" + getPassword() + ";" + getBalance() + ";" + isNotLock() + ";Savings Account";
    }

	@Override
	protected char[] getChars() {
		StringBuilder stringBuilder = new StringBuilder(getName())
				.append(';')
				.append(getAccNum())
				.append(';')
				.append(getPassword())
				.append(';')
				.append(getBalance())
				.append(';')
				.append(isNotLock())
				.append(new char[] {';', 'S', 'a', 'v', 'i', 'n', 'g', 's', ' ', 'A', 'c', 'c', 'o', 'u', 'n', 't'},0,16);
		int length =  stringBuilder.length();
		char[] chars = new char[length];
		stringBuilder.getChars(0,length,chars,0);
		return chars;
	}
}
