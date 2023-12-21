package model;

import dto.Itemdto;

import java.sql.SQLException;
import java.util.List;

public interface ItemModel {
    boolean addItem(Itemdto dto) throws SQLException, ClassNotFoundException;
    boolean updateItem(Itemdto dto) throws SQLException, ClassNotFoundException;

    boolean deleteItem(String itemCode) throws SQLException, ClassNotFoundException;

    Itemdto getItem(String itemCode) throws SQLException, ClassNotFoundException;
    List<Itemdto> allItem() throws SQLException, ClassNotFoundException;
    Itemdto lastItem() throws SQLException, ClassNotFoundException;
}
