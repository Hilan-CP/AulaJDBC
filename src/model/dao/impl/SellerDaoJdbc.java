package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
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
		PreparedStatement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.prepareStatement("insert into seller "
													+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
													+ "values (?, ?, ?, ?, ?)",
													statement.RETURN_GENERATED_KEYS);
			statement.setString(1, obj.getName());
			statement.setString(2, obj.getEmail());
			statement.setDate(3, new Date(obj.getBirthDate().getTime()));
			statement.setDouble(4, obj.getBaseSalary());
			statement.setInt(5, obj.getDepartment().getId());
			
			int rows = statement.executeUpdate();
			
			if(rows > 0) {
				result = statement.getGeneratedKeys();
				if(result.next()) {
					obj.setId(result.getInt(1));
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			Database.closeStatement(statement);
			Database.closeResultSet(result);
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("update seller "
													+ "set Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
													+ "where Id = ?");
			statement.setString(1, obj.getName());
			statement.setString(2, obj.getEmail());
			statement.setDate(3, new Date(obj.getBirthDate().getTime()));
			statement.setDouble(4, obj.getBaseSalary());
			statement.setInt(5, obj.getDepartment().getId());
			statement.setInt(6, obj.getId());
			
			statement.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			Database.closeStatement(statement);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement("delete from seller where Id = ?");
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			Database.closeStatement(statement);
		}

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
