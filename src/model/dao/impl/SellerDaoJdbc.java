package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.Database;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJdbc implements SellerDao {
	private Connection connection;
	
	public SellerDaoJdbc(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement statement = null;
		ResultSet result = null;
		try{
			statement = connection.prepareStatement("select seller.*, department.Name as DepartmentName "
												+ "from seller inner join department "
												+ "on seller.DepartmentId = department.Id "
												+ "where seller.Id = ?");
			
			statement.setInt(1, id);
			result = statement.executeQuery();
			
			if(result.next()) {
				Department dep = createDepartment(result);
				Seller sel = createSeller(result, dep);
				return sel;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			Database.closeStatement(statement);
			Database.closeResultSet(result);
		}
		return null;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.prepareStatement("select seller.*, department.Name as DepartmentName "
												+ "from seller inner join department "
												+ "on seller.DepartmentId = department.Id ");
			result = statement.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while(result.next()) {
				Department dep = map.get(result.getInt("DepartmentId"));
				if(dep == null) {
					dep = createDepartment(result);
				}
				
				Seller sel = createSeller(result, dep);
				list.add(sel);
			}
			return list;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			Database.closeStatement(statement);
			Database.closeResultSet(result);
		}
		return null;
	}
	
	@Override
	public List<Seller> findByDepartment(Department obj) {
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.prepareStatement("select seller.*, department.Name as DepartmentName "
												+ "from seller inner join department "
												+ "on seller.DepartmentId = department.Id "
												+ "where DepartmentId = ?");
			statement.setInt(1, obj.getId());
			result = statement.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while(result.next()) {
				Department dep = map.get(result.getInt("DepartmentId"));
				if(dep == null) {
					dep = createDepartment(result);
				}
				
				Seller sel = createSeller(result, dep);
				list.add(sel);
			}
			return list;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			Database.closeStatement(statement);
			Database.closeResultSet(result);
		}
		return null;
	}
	
	private Department createDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepartmentName"));
		return dep;
	}
	
	private Seller createSeller(ResultSet rs, Department dep) throws SQLException {
		Seller sel = new Seller();
		sel.setId(rs.getInt("Id"));
		sel.setName(rs.getString("Name"));
		sel.setEmail(rs.getString("Email"));
		sel.setBaseSalary(rs.getDouble("BaseSalary"));
		sel.setBirthDate(rs.getDate("BirthDate"));
		sel.setDepartment(dep);
		return sel;
	}
}
