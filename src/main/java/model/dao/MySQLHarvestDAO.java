package model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.CoffeeLot;
import model.Harvest;
import model.ModelException;
import model.User;

public class MySQLHarvestDAO implements HarvestDAO {

	private static final String SELECT_WITH_RELATIONS = 
			" SELECT h.*, l.name AS lot_name, u.nome AS user_name "
			+ " FROM harvests h "
			+ " INNER JOIN coffee_lots l ON h.coffee_lot_id = l.id "
			+ " INNER JOIN users u ON h.user_id = u.id ";

	@Override
	public boolean save(Harvest harvest) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO harvests "
				+ " (coffee_lot_id, harvest_date, bags, quality, notes, user_id) "
				+ " VALUES (?, ?, ?, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		db.setInt(1, harvest.getCoffeeLot().getId());
		db.setDate(2, harvest.getHarvestDate() == null ? new Date() : harvest.getHarvestDate());
		db.setInt(3, harvest.getBags());
		db.setString(4, harvest.getQuality());
		db.setString(5, harvest.getNotes());
		db.setInt(6, harvest.getUser().getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean update(Harvest harvest) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlUpdate = "UPDATE harvests "
				+ " SET coffee_lot_id = ?, "
				+ " harvest_date = ?, "
				+ " bags = ?, "
				+ " quality = ?, "
				+ " notes = ?, "
				+ " user_id = ? "
				+ " WHERE id = ?;";
		
		db.prepareStatement(sqlUpdate);
		db.setInt(1, harvest.getCoffeeLot().getId());
		db.setDate(2, harvest.getHarvestDate() == null ? new Date() : harvest.getHarvestDate());
		db.setInt(3, harvest.getBags());
		db.setString(4, harvest.getQuality());
		db.setString(5, harvest.getNotes());
		db.setInt(6, harvest.getUser().getId());
		db.setInt(7, harvest.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Harvest harvest) throws ModelException {
		DBHandler db = new DBHandler();
		
		db.prepareStatement("DELETE FROM harvests WHERE id = ?;");
		db.setInt(1, harvest.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public List<Harvest> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		
		List<Harvest> harvests = new ArrayList<Harvest>();
		
		db.createStatement();
		db.executeQuery(SELECT_WITH_RELATIONS + " ORDER BY h.harvest_date DESC, h.id DESC");
		
		while (db.next()) {
			harvests.add(createHarvest(db));
		}
		
		return harvests;
	}

	@Override
	public List<Harvest> listPage(int page, int pageSize) throws ModelException {
		DBHandler db = new DBHandler();
		
		List<Harvest> harvests = new ArrayList<Harvest>();
		
		db.prepareStatement(SELECT_WITH_RELATIONS 
				+ " ORDER BY h.harvest_date DESC, h.id DESC LIMIT ? OFFSET ?");
		db.setInt(1, pageSize);
		db.setInt(2, (page - 1) * pageSize);
		db.executeQuery();
		
		while (db.next()) {
			harvests.add(createHarvest(db));
		}
		
		return harvests;
	}

	@Override
	public int count() throws ModelException {
		DBHandler db = new DBHandler();
		
		db.prepareStatement("SELECT COUNT(*) AS total FROM harvests");
		db.executeQuery();
		
		int total = 0;
		if (db.next()) {
			total = db.getInt("total");
			db.close();
		}
		
		return total;
	}

	@Override
	public Harvest findById(int id) throws ModelException {
		DBHandler db = new DBHandler();
		
		db.prepareStatement(SELECT_WITH_RELATIONS + " WHERE h.id = ?");
		db.setInt(1, id);
		db.executeQuery();
		
		Harvest harvest = null;
		if (db.next()) {
			harvest = createHarvest(db);
			db.close();
		}
		
		return harvest;
	}
	
	private Harvest createHarvest(DBHandler db) throws ModelException {
		CoffeeLot lot = new CoffeeLot(db.getInt("coffee_lot_id"));
		lot.setName(db.getString("lot_name"));
		
		User user = new User(db.getInt("user_id"));
		user.setName(db.getString("user_name"));
		
		Harvest harvest = new Harvest(db.getInt("id"));
		harvest.setCoffeeLot(lot);
		harvest.setHarvestDate(db.getDate("harvest_date"));
		harvest.setBags(db.getInt("bags"));
		harvest.setQuality(db.getString("quality"));
		harvest.setNotes(db.getString("notes"));
		harvest.setUser(user);
		
		return harvest;
	}
}
