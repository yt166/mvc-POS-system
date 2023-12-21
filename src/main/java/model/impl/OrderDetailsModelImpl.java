package model.impl;

import db.DBConnection;
import dto.OrderDetailsdto;
import model.OrderDetailsModel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailsModelImpl implements OrderDetailsModel {
    @Override
    public boolean saveOrderDetails(List<OrderDetailsdto> list) throws SQLException, ClassNotFoundException {
        boolean saved = true;
        for(OrderDetailsdto dto : list){
            String sql ="INSERT INTO orderdetail VALUE (?,?,?,?) ";
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, dto.getOrderID());
            preparedStatement.setString(2, dto.getItemCode());
            preparedStatement.setInt(3,dto.getQty());
            preparedStatement.setDouble(4,dto.getUnitPrize());

            if(!(preparedStatement.executeUpdate()>0)){
                saved= false;
            }
        }
        return saved;
    }
}
