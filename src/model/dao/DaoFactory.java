package model.dao;

import database.Database;
import model.dao.impl.SellerDaoJdbc;

public class DaoFactory {
	public static SellerDao createSellerDao() {
		return new SellerDaoJdbc(Database.getConnection());
	}
}
