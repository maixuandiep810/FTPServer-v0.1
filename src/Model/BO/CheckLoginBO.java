package Model.BO;

import Model.BEAN.Account;
import Model.BEAN.Account;
import Model.DAO.CheckLoginDAO;

public class CheckLoginBO {
	
	private CheckLoginDAO _CheckLoginDAO;
	/**
	 * @param _CheckLoginDAO
	 */
	public CheckLoginBO() {
		super();
		_CheckLoginDAO = new CheckLoginDAO();
	}
	/*
	 * 
	 */
	public boolean isValidUser(String username) {
		return _CheckLoginDAO.isValidUsername(username);
	}
	/*
	 * 
	 */
	public boolean isValidPassword(String username, String password) {
		return _CheckLoginDAO.isValidPassword(username, password);
	}
	/*
	 * 
	 */
	public Account getAccount(String username) {
		return _CheckLoginDAO.getAccount(username);
	}
}
