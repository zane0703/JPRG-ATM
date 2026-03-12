package ATM;

import java.io.CharArrayWriter;
import java.math.BigDecimal;

/**
 * @author Ang Yun, Zane Student ID:P1949955 class: DIT/FT/1B/01
 */
public class FixedDeposit extends BankAccount{
    public FixedDeposit(final String userName, final int accountNumber, final String password, final BigDecimal balance, final boolean notLock){
	super(userName, accountNumber, password, balance, notLock);
    }
    @Override
    protected String getInterestRate(){
	return "1.65% per year";
    }
    @Override
    public String toString(){
    	return new StringBuilder(getName())
    			.append(';')
    			.append(getAccNum())
    			.append(';')
    			.append(getPassword())
    			.append(';')
				.append(getBalance())
				.append(';')
				.append(isNotLock())
    			.append( new char[] {';', 'F', 'i', 'x', 'e', 'd', ' ', 'D', 'e', 'p', 'o', 's', 'i', 't', ' ', 'a', 'c', 'c', 'o', 'u', 'n', 't'},0,22)
    			.toString();
	//return getName()+";"+getAccNum()+";"+getPassword()+";"+getBalance()+";"+isNotLock()+
	//	";Fixed Deposit account";
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
				.append( new char[] {';', 'F', 'i', 'x', 'e', 'd', ' ', 'D', 'e', 'p', 'o', 's', 'i', 't', ' ', 'a', 'c', 'c', 'o', 'u', 'n', 't'},0,22);
		int length =  stringBuilder.length();
		char[] chars = new char[length];
		stringBuilder.getChars(0,length,chars,0);
		return chars;
	}
}
