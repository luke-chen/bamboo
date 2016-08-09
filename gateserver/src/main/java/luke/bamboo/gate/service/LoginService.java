package luke.bamboo.gate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import luke.bamboo.data.domain.Account;
import luke.bamboo.data.service.AccountDataService;

@Service
public class LoginService {
    @Autowired
    AccountDataService accountData;
    
    public Account loginAndRegister(String username, String password) {
    	Account account = accountData.loginAccount(username, password);
    	if(account == null) {
    		 accountData.newAccount(username, password);
    		 account = accountData.loginAccount(username, password);
    	}
    	return account;
    }
}
