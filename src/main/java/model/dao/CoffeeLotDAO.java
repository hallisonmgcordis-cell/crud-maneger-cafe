package model.dao;

import java.util.List;

import model.CoffeeLot;
import model.ModelException;

public interface CoffeeLotDAO {
	boolean save(CoffeeLot lot) throws ModelException;
	boolean update(CoffeeLot lot) throws ModelException;
	boolean delete(CoffeeLot lot) throws ModelException;
	List<CoffeeLot> listAll() throws ModelException;
	List<CoffeeLot> listPage(int page, int pageSize) throws ModelException;
	int count() throws ModelException;
	CoffeeLot findById(int id) throws ModelException;
}
