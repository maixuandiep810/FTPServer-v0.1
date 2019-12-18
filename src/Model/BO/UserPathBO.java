package Model.BO;

import Model.DAO.UserPathDAO;

public class UserPathBO {
	
	private UserPathDAO _userPathDAO;

	/**
	 * 
	 */
	public UserPathBO() {
		_userPathDAO = new UserPathDAO();
	}
	/**
	 * 
	 */
	public String getPath(String username) {
		return _userPathDAO.getPath(username);
	}
}
