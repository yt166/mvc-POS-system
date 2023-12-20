package model;

import dto.Itemdto;

import java.sql.SQLException;
import java.util.List;

public interface ItemModel {
    boolean addItem(Itemdto dto);
    boolean updateItem(Itemdto dto);

    boolean deleteItem(String itemCode);

    Itemdto getItem(String itemCode) throws SQLException, ClassNotFoundException;
    List<Itemdto> allItem() throws SQLException, ClassNotFoundException;
}
