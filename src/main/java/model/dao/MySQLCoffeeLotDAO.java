package model.dao;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.CoffeeLot;
import model.ModelException;
import model.User;

public class MySQLCoffeeLotDAO implements CoffeeLotDAO {

	private static final String SELECT_WITH_OWNER = 
			" SELECT l.*, u.nome, u.sexo, u.email "
			+ " FROM coffee_lots l "
			+ " INNER JOIN users u ON l.user_id = u.id ";

	@Override
	public boolean save(CoffeeLot lot) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO coffee_lots "
				+ " (name, variety, area, planting_date, irrigated, user_id) "
				+ " VALUES (?, ?, ?, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		db.setString(1, lot.getName());
		db.setString(2, lot.getVariety());
		db.setDouble(3, lot.getArea());
		db.setDate(4, lot.getPlantingDate() == null ? new Date() : lot.getPlantingDate());
		db.setBoolean(5, lot.isIrrigated());
		db.setInt(6, lot.getUser().getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean update(CoffeeLot lot) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlUpdate = "UPDATE coffee_lots "
				+ " SET name = ?, "
				+ " variety = ?, "
				+ " area = ?, "
				+ " planting_date = ?, "
				+ " irrigated = ?, "
				+ " user_id = ? "
				+ " WHERE id = ?;";
		
		db.prepareStatement(sqlUpdate);
		db.setString(1, lot.getName());
		db.setString(2, lot.getVariety());
		db.setDouble(3, lot.getArea());
		db.setDate(4, lot.getPlantingDate() == null ? new Date() : lot.getPlantingDate());
		db.setBoolean(5, lot.isIrrigated());
		db.setInt(6, lot.getUser().getId());
		db.setInt(7, lot.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(CoffeeLot lot) throws ModelException {
		DBHandler db = new DBHandler();
		
		db.prepareStatement("DELETE FROM coffee_lots WHERE id = ?;");
		db.setInt(1, lot.getId());
		
		try {
			return db.executeUpdate() > 0;
		} catch (ModelException e) {
			// Lote com colheitas registradas não pode ser removido
			if (e.getCause() instanceof SQLIntegrityConstraintViolationException)
				return false;
			
			throw e;
		}
	}

	@Override
	public List<CoffeeLot> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		
		List<CoffeeLot> lots = new ArrayList<CoffeeLot>();
		
		db.createStatement();
		db.executeQuery(SELECT_WITH_OWNER + " ORDER BY l.name");
		
		while (db.next()) {
			lots.add(createLot(db));
		}
		
		return lots;
	}

	@Override
	public List<CoffeeLot> listPage(int page, int pageSize) throws ModelException {
		DBHandler db = new DBHandler();
		
		List<CoffeeLot> lots = new ArrayList<CoffeeLot>();
		
		db.prepareStatement(SELECT_WITH_OWNER + " ORDER BY l.name, l.id LIMIT ? OFFSET ?");
		db.setInt(1, pageSize);
		db.setInt(2, (page - 1) * pageSize);
		db.executeQuery();
		
		while (db.next()) {
			lots.add(createLot(db));
		}
		
		return lots;
	}

	@Override
	public int count() throws ModelException {
		DBHandler db = new DBHandler();
		
		db.prepareStatement("SELECT COUNT(*) AS total FROM coffee_lots");
		db.executeQuery();
		
		int total = 0;
		if (db.next()) {
			total = db.getInt("total");
			db.close();
		}
		
		return total;
	}

	@Override
	public CoffeeLot findById(int id) throws ModelException {
		DBHandler db = new DBHandler();
		
		db.prepareStatement(SELECT_WITH_OWNER + " WHERE l.id = ?");
		db.setInt(1, id);
		db.executeQuery();
		
		CoffeeLot lot = null;
		if (db.next()) {
			lot = createLot(db);
			db.close();
		}
		
		return lot;
	}
	
	private CoffeeLot createLot(DBHandler db) throws ModelException {
		User owner = new User(db.getInt("user_id"));
		owner.setName(db.getString("nome"));
		owner.setGender(db.getString("sexo"));
		owner.setEmail(db.getString("email"));
		
		CoffeeLot lot = new CoffeeLot(db.getInt("id"));
		lot.setName(db.getString("name"));
		lot.setVariety(db.getString("variety"));
		lot.setArea(db.getDouble("area"));
		lot.setPlantingDate(db.getDate("planting_date"));
		lot.setIrrigated(db.getBoolean("irrigated"));
		lot.setUser(owner);
		
		return lot;
	}
}
