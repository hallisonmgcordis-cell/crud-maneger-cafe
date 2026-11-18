package model.dao;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import model.ModelException;
import model.User;

public class MySQLUserDAO implements UserDAO {

	@Override
	public boolean save(User user) throws ModelException {
		
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO users (nome, sexo, email, password) "
				+ " VALUES (?, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		db.setString(1, user.getName());
		db.setString(2, user.getGender());
		db.setString(3, user.getEmail());
		db.setString(4, user.getPassword());
		  
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean update(User user) throws ModelException {
		
		DBHandler db = new DBHandler();
		
		boolean changePassword = user.getPassword() != null 
				&& !user.getPassword().isEmpty();
		
		String sqlUpdate = "UPDATE users "
				         	+ "SET nome = ?, "
				         	+ "sexo = ?, "
				         	+ "email = ? "
				         	+ (changePassword ? ", password = ? " : "")
				         + "WHERE id = ?";
		
		
		db.prepareStatement(sqlUpdate);
		
		db.setString(1, user.getName());
		db.setString(2, user.getGender());
		db.setString(3, user.getEmail());
		
		if (changePassword) {
			db.setString(4, user.getPassword());
			db.setInt(5, user.getId());
		} else {
			db.setInt(4, user.getId());
		}
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(User user) throws ModelException {
		
		DBHandler db = new DBHandler();
		
		String sqlDelete = " DELETE FROM users "
		                 + " WHERE id = ?;";

		db.prepareStatement(sqlDelete);		
		db.setInt(1, user.getId());
		
		try {
			
			return db.executeUpdate() > 0;
			
		} catch (ModelException e) {
			if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
				return false;
			}
			
			throw e; 
		}
	}

	@Override
	public List<User> listAll() throws ModelException {
	
		DBHandler db = new DBHandler();
		
		List<User> users = new ArrayList<User>();
			
		// Declara um instrução SQL
		String sqlQuery = "SELECT * FROM users ORDER BY nome";
		
		db.createStatement();
	
		db.executeQuery(sqlQuery);

		while (db.next()) {
			User u = createUser(db);
			users.add(u);
		}
		
		return users;
	}

	@Override
	public List<User> listPage(int page, int pageSize) throws ModelException {
		DBHandler db = new DBHandler();
		
		List<User> users = new ArrayList<User>();
		
		db.prepareStatement("SELECT * FROM users ORDER BY nome, id LIMIT ? OFFSET ?");
		db.setInt(1, pageSize);
		db.setInt(2, (page - 1) * pageSize);
		db.executeQuery();
		
		while (db.next()) {
			users.add(createUser(db));
		}
		
		return users;
	}

	@Override
	public int count() throws ModelException {
		DBHandler db = new DBHandler();
		
		db.prepareStatement("SELECT COUNT(*) AS total FROM users");
		db.executeQuery();
		
		int total = 0;
		if (db.next()) {
			total = db.getInt("total");
			db.close();
		}
		
		return total;
	}

	@Override
	public User findByEmail(String email) throws ModelException {
		DBHandler db = new DBHandler();
		
		db.prepareStatement("SELECT * FROM users WHERE email = ?");
		db.setString(1, email);
		db.executeQuery();
		
		User u = null;
		if (db.next()) {
			u = createUser(db);
			db.close();
		}
		
		return u;
	}

	@Override
	public User findById(int id) throws ModelException {
		
		DBHandler db = new DBHandler();
				
		String sql = "SELECT * FROM users WHERE id = ?";
		
		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();
		
		User u = null;
		while (db.next()) {
			u = createUser(db);
			break;
		}
		db.close();
		
		return u;
	}
	
	private User createUser(DBHandler db) throws ModelException {
		User u = new User(db.getInt("id"));
		u.setName(db.getString("nome"));
		u.setGender(db.getString("sexo"));
		u.setEmail(db.getString("email"));
		u.setPassword(db.getString("password"));
		
		return u;
	}
}
