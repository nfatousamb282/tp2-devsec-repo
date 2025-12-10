package persistence;

import java.util.List;

import model.Account;
import model.User;

/**
*
* @author ouziri
* @version 0.1
*/

public interface IPersistence {
	
	public Account save(Account account) throws Exception;
	public Account findAccountById (int accountId) throws Exception;
	void updateBalance(int accountId, double newBalance) throws Exception;
	public List<Account> findAccountByOwnerEmail(String ownerEmail) throws Exception;
	public User findUserByEmail(String userEmail) throws Exception;
	public User validateUserByEmailPassword(String ownerEmail, String pwd) throws Exception;
	
	
//	public Client findClientById (Long clientId); 
//	public Client registerClient(Client client);

}
