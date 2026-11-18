package model.dao;

import java.util.List;

import model.Harvest;
import model.ModelException;

public interface HarvestDAO {
	boolean save(Harvest harvest) throws ModelException;
	boolean update(Harvest harvest) throws ModelException;
	boolean delete(Harvest harvest) throws ModelException;
	List<Harvest> listAll() throws ModelException;
	List<Harvest> listPage(int page, int pageSize) throws ModelException;
	int count() throws ModelException;
	Harvest findById(int id) throws ModelException;
}
