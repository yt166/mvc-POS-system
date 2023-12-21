package controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import db.DBConnection;
import dto.Itemdto;
import dto.tm.ItemTm;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import model.ItemModel;
import model.impl.ItemModelImpl;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Predicate;

public class ItemController {
    public JFXComboBox cmbItemCode;
    public JFXTextField txtUpdateDes;
    public JFXTextField txtUpdateUnitPrize;
    public JFXTextField txtUpdateQty;
    public JFXButton btnUpdate;
    public JFXButton btnAdd;
    public Label lblSetCode;
    public JFXTextField txtDes;
    public JFXTextField txtUnitPrize;
    public JFXTextField txtQtyOnHand;
    public JFXTreeTableView<ItemTm> tblItem;
    public TreeTableColumn colCode;
    public TreeTableColumn colDes;
    public TreeTableColumn colUnitPrize;
    public TreeTableColumn colQty;
    public TreeTableColumn colOption;
    public Label lblAddItem;

    private ItemModel itemModel = new ItemModelImpl();
    public void initialize(){
        colCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        colDes.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        colUnitPrize.setCellValueFactory(new TreeItemPropertyValueFactory<>("unitPrize"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qtyOnHand"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btnDelete"));
        loadItemTable();
        generateID();
    }
    private void loadItemTable() {
        ObservableList<ItemTm> tmList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM item";

        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet result = stm.executeQuery(sql);

            while (result.next()){
                JFXButton btn = new JFXButton("Delete");

                ItemTm tm = new ItemTm(
                        result.getString(1),
                        result.getString(2),
                        result.getDouble(3),
                        result.getInt(4),
                        btn
                );

                btn.setOnAction(actionEvent -> {
                    deleteItem(tm.getCode());
                });

                tmList.add(tm);
            }

            TreeItem<ItemTm> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
            tblItem.setRoot(treeItem);
            tblItem.setShowRoot(false);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteItem(String code) {
        try {
            boolean deleted = itemModel.deleteItem(code);
            if(deleted){
                new Alert(Alert.AlertType.INFORMATION, "Item Deleted Cussefully").show();
            }else{
                new Alert(Alert.AlertType.INFORMATION,"Something Wents Wrong").show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadItemTable();
    }


    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) tblItem.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Home.fxml"))));
        stage.show();
    }

    public void btnReportOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load("src/main/resources/reports/itemReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        try {
            boolean updated = itemModel.updateItem(new Itemdto(lblSetCode.getText(),txtDes.getText(),Double.parseDouble(txtUnitPrize.getText()),Integer.parseInt(txtQtyOnHand.getText())));

            if(updated){
                new Alert(Alert.AlertType.INFORMATION,"Customer Added Succesfully").show();
            }else {
                new Alert(Alert.AlertType.INFORMATION,"Not Added").show();
                clearFields();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadItemTable();
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        try {
            boolean added = itemModel.addItem(new Itemdto(lblSetCode.getText(),txtDes.getText(),Double.parseDouble(txtUnitPrize.getText()),Integer.parseInt(txtQtyOnHand.getText())));

            if(added){
                new Alert(Alert.AlertType.INFORMATION,"Customer Added Succesfully").show();
            }else {
                new Alert(Alert.AlertType.INFORMATION,"Not Added").show();
                clearFields();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        loadItemTable();
    }

    private void clearFields() {
        txtDes.clear();
        txtUnitPrize.clear();
        txtQtyOnHand.clear();
        txtUpdateDes.clear();
        txtUpdateUnitPrize.clear();
        txtUpdateQty.clear();
    }

    private void generateID() {
        try {
            Itemdto itemdto = itemModel.lastItem();
            if(itemdto != null){
                String id = itemdto.getItemCode();
                int num = Integer.parseInt(id.split("[P]")[1]);
                num++;
                lblSetCode.setText(String.format("P%03d",num));
            }else {
                lblSetCode.setText("P0001");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
