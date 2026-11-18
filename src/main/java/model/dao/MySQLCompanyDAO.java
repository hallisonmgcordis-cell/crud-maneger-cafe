package model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Company;
import model.ModelException;
import model.User;

public class MySQLCompanyDAO implements CompanyDAO {

	@Override
	public boolean save(Company company) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO companies VALUES (DEFAULT, ?, ?, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		
		db.setString(1, company.getName());
		db.setString(2, company.getRole());
		db.setDate(3, company.getStart() == null ? new Date() : company.getStart());
			
		if (company.getEnd() == null)
			db.setNullDate(4);
		else db.setDate(4, company.getEnd());

		db.setInt(5, company.getUser().getId());
		
		return db.executeUpdate() > 0;	
	}

	@Override
	public boolean update(Company company) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlUpdate = "UPDATE companies "
				+ " SET name = ?, "
				+ " role = ?, "
				+ " start = ?, "
				+ " end = ?, "
				+ " user_id = ? "
				+ " WHERE id = ?; "; 
		
		db.prepareStatement(sqlUpdate);
		
		db.setString(1, company.getName());
		db.setString(2, company.getRole());
		
		db.setDate(3, company.getStart() == null ? new Date() : company.getStart());
		
		if (company.getEnd() == null)
			db.setNullDate(4);
		else db.setDate(4, company.getEnd());
		
		db.setInt(5, company.getUser().getId());
		db.setInt(6, company.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Company company) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlDelete = " DELETE FROM companies "
		         + " WHERE id = ?;";

		db.prepareStatement(sqlDelete);		
		db.setInt(1, company.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public List<Company> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		
		List<Company> companies = new ArrayList<Company>();
			
		// Declara uma instrução SQL
		String sqlQuery = " SELECT c.id as 'company_id', c.*, u.* \n"
				+ " FROM companies c \n"
				+ " INNER JOIN users u \n"
				+ " ON c.user_id = u.id;";
		
		db.createStatement();
	
		db.executeQuery(sqlQuery);

		while (db.next()) {
			User user = new User(db.getInt("user_id"));
			user.setName(db.getString("nome"));
			user.setGender(db.getString("sexo"));
			user.setEmail(db.getString("email"));
			
			Company company = new Company(db.getInt("company_id"));
			company.setName(db.getString("name"));
			company.setRole(db.getString("role"));
			company.setStart(db.getDate("start"));
			company.setEnd(db.getDate("end"));
			company.setUser(user);
			
			companies.add(company);
		}
		
		return companies;
	}

	@Override
	public List<Company> listPage(int page, int pageSize) throws ModelException {
		DBHandler db = new DBHandler();
		
		List<Company> companies = new ArrayList<Company>();
		
		db.prepareStatement("SELECT c.id AS 'company_id', c.*, u.* "
				+ " FROM companies c "
				+ " INNER JOIN users u ON c.user_id = u.id "
				+ " ORDER BY c.name, c.id LIMIT ? OFFSET ?");
		db.setInt(1, pageSize);
		db.setInt(2, (page - 1) * pageSize);
		db.executeQuery();
		
		while (db.next()) {
			User user = new User(db.getInt("user_id"));
			user.setName(db.getString("nome"));
			user.setGender(db.getString("sexo"));
			user.setEmail(db.getString("email"));
			
			Company company = new Company(db.getInt("company_id"));
			company.setName(db.getString("name"));
			company.setRole(db.getString("role"));
			company.setStart(db.getDate("start"));
			company.setEnd(db.getDate("end"));
			company.setUser(user);
			
			companies.add(company);
		}
		
		return companies;
	}

	@Override
	public int count() throws ModelException {
		DBHandler db = new DBHandler();
		
		db.prepareStatement("SELECT COUNT(*) AS total FROM companies");
		db.executeQuery();
		
		int total = 0;
		if (db.next()) {
			total = db.getInt("total");
			db.close();
		}
		
		return total;
	}

	@Override
	public Company findById(int id) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sql = "SELECT * FROM companies WHERE id = ?;";
		
		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();
		
		Company c = null;
		while (db.next()) {
			c = new Company(id);
			c.setName(db.getString("name"));
			c.setRole(db.getString("role"));
			c.setStart(db.getDate("start"));
			c.setEnd(db.getDate("end"));
			
			UserDAO userDAO = DAOFactory.createDAO(UserDAO.class); 
			User user = userDAO.findById(db.getInt("user_id"));
			c.setUser(user);
			
			break;
		}
		db.close();
		
		return c;
	}
}
