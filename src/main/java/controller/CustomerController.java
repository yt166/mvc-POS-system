package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import db.DBConnection;
import dto.Customerdto;
import dto.tm.CustomerTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.CustomerModel;
import model.impl.CustomerModelImpl;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

public class CustomerController {
    public AnchorPane customerPane;
    public JFXButton btnBack;
    public Label lblSetId;
    public JFXTextField txtCusName;
    public JFXTextField txtCusAddress;
    public JFXTextField txtCusSalary;
    public JFXTextField txtUpdateID;
    public JFXTextField txtUpdateName;
    public JFXTextField txtUpdateAddress;
    public JFXTextField btnUpdateSalary;
    public JFXButton btnSave;
    public JFXButton btnUpdate;
    public TreeTableColumn colName;
    public TreeTableColumn colAddress;
    public TreeTableColumn colSalary;
    public TreeTableColumn colOption;
    public JFXButton btnReload;
    public Button btnReport;

    public JFXTextField txtUpdateSalary;
    public TreeTableColumn colId;
    public JFXTreeTableView<CustomerTm> tblCustomer;

    private CustomerModel customerModel = new CustomerModelImpl();

    public void initialize(){
        generateID();
        colId.setCellValueFactory(new TreeItemPropertyValueFactory<>("cusid"));
        colName.setCellValueFactory(new TreeItemPropertyValueFactory<>("cusName"));
        colAddress.setCellValueFactory(new TreeItemPropertyValueFactory<>("cusAddress"));
        colSalary.setCellValueFactory(new TreeItemPropertyValueFactory<>("cusSalary"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));
        loadCustomerTable();
    }

    private void setData(CustomerTm newValue) {
        if(newValue!=null){
            lblSetId.setText(newValue.getCusid());
            txtCusName.setText(newValue.getCusName());
            txtCusAddress.setText(newValue.getCusAddress());
            txtCusSalary.setText(String.valueOf(newValue.getCusSalary()));
        }
    }

    private void loadCustomerTable() {
        ObservableList<CustomerTm> list = FXCollections.observableArrayList();
        try {
            List<Customerdto> dtolist = customerModel.allCustomers();

            for (Customerdto dto: dtolist) {
                Button btn = new Button("Delete");

                CustomerTm ctm = new CustomerTm(
                        dto.getCusID(),
                        dto.getCusName(),
                        dto.getCusAddress(),
                        dto.getCusSalary(),
                        btn
                );

                btn.setOnAction(actionEvent -> {
                    deleteCustomer(ctm.getCusid());
                });

                list.add(ctm);
            }

            TreeItem<CustomerTm> treeItem = new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren);
            tblCustomer.setRoot(treeItem);
            tblCustomer.setShowRoot(false);

        } catch (SQLException |ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteCustomer(String cusid) {
        try {
            boolean deleted = customerModel.deleteCustomer(cusid);

            if(deleted){
                new Alert(Alert.AlertType.INFORMATION,"Customer Deleted Successfully").show();
            }else{
                new Alert(Alert.AlertType.INFORMATION,"Something Wents Wrong").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } 
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) customerPane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Home.fxml"))));
        stage.show();
    }
    public void btnReportOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load("src/main/resources/reports/Blank_A4_2.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        try {
            boolean isSaved = customerModel.saveCustomer(new Customerdto(lblSetId.getText(), txtCusName.getText(), txtCusAddress.getText(), Double.parseDouble(txtCusSalary.getText())));

            if(isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Customer Saved Successfully").show();
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        ;
    }

    private void clearFields() {
        txtCusName.clear();
        txtCusAddress.clear();
        txtCusSalary.clear();
        txtUpdateID.clear();
        txtUpdateName.clear();
        txtUpdateAddress.clear();
        txtUpdateSalary.clear();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        try {
            boolean updated = customerModel.updateCustomer(new Customerdto(txtUpdateID.getText(),
                    txtUpdateName.getText(),
                    txtUpdateAddress.getText(),
                    Double.parseDouble(txtUpdateSalary.getText())
            ));

            if(updated){
                new Alert(Alert.AlertType.INFORMATION,"Customer Updated").show();
                loadCustomerTable();
                clearFields();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnReloadOnAction(ActionEvent actionEvent) {
        loadCustomerTable();
        tblCustomer.refresh();
        clearFields();
    }

    public void generateID() {
        try {
            Customerdto cdto = customerModel.lastCustomer();
            if (cdto != null) {
                String id = cdto.getCusID();
                int num = Integer.parseInt(id.split("[C]")[1]);
                num++;
                lblSetId.setText(String.format("C%03d", num));
            } else {
                lblSetId.setText("C001");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
