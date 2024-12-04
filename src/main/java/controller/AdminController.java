package controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.UserMapper;
import model.Role;
import model.validator.Notification;
import reports.generator.ReportData;
import service.orders.OrdersService;
import service.rightsRoles.RightsRolesService;
import service.user.AuthenticationService;
import view.AdminView;
import view.model.UserDTO;
import view.model.builder.UserDTOBuilder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Map;

import static database.Constants.Roles.CUSTOMER;

public class AdminController {

    private final AdminView adminView;
    private final AuthenticationService authenticationService;
    private final RightsRolesService rightsRolesService;
    private final OrdersService ordersService;


    public AdminController(AdminView adminView, AuthenticationService authenticationService, RightsRolesService rightsRolesService, OrdersService ordersService){
        this.adminView = adminView;
        this.authenticationService = authenticationService;
        this.rightsRolesService = rightsRolesService;
        this.ordersService = ordersService;
        this.adminView.addSaveButtonListener(new SaveButtonListener());
        this.adminView.addUpdateButtonListener(new UpdateButtonListener());
        this.adminView.addGeneratePDFButtonListener(new GeneratePDFButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = adminView.getUsername();
            String password = adminView.getPassword();
            String role = adminView.getRole();
            Integer id_role;
            if (role.equals("Administrator")){
                id_role = 1;
            }else if(role.equals("Employee")){
                id_role = 2;
            }else{
                id_role = 3;
            }


            Notification<String> registerNotification = authenticationService.register(username, password,id_role);

            if (registerNotification.hasErrors()) {
                adminView.setActionTargetText((registerNotification.getFormattedErrors()));
            } else {
                adminView.setActionTargetText("Register Successful!");
                UserDTO userDTO = new UserDTOBuilder().setUsername(username).setPassword(registerNotification.getResult()).setRoles(Collections.singletonList(rightsRolesService.findRoleById(Long.valueOf(id_role)))).build();
                adminView.addUserToObservableList(userDTO);
            }
        }
    }


    private class UpdateButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {


            String username = adminView.getUsername();
            String password = adminView.getPassword();
            String role = adminView.getRole();
            Integer id_role;
            if (role.equals("Administrator")){
                id_role = 1;
            }else if(role.equals("Employee")){
                id_role = 2;
            }else{
                id_role = 3;
            }


            Notification<Boolean> updateNotification = authenticationService.updateRole(username, password,id_role);

            if (updateNotification.hasErrors()) {
                adminView.setActionTargetText((updateNotification.getFormattedErrors()));
            } else {
                adminView.setActionTargetText("Updated Successful!");
                UserDTO userDTO = new UserDTOBuilder().setUsername(username).setPassword(password).build();
                adminView.addUserToObservableList(userDTO);
            }
        }
    }

    private class GeneratePDFButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

        Map<String, ReportData> reportData = ordersService.getSalesReport();
        String filePath = "sales_report.pdf";
        generatePdfReport(reportData, filePath);
        adminView.setActionTargetText("PDF generated at: " + filePath);
        }
    }

    public void generatePdfReport(Map<String, ReportData> reportData, String filePath){
        Document document = new Document();
         try{
             PdfWriter.getInstance(document, new FileOutputStream(filePath));
             document.open();
             document.add(new Paragraph("SalesReport"));
             document.add(new Paragraph(" "));

             PdfPTable table = new PdfPTable(3);
             table.addCell("Employee");
             table.addCell("Books Sold");
             table.addCell("Total Sales");

             for(Map.Entry<String, ReportData> entry : reportData.entrySet()){
                 table.addCell(entry.getKey());
                 table.addCell(String.valueOf(entry.getValue().getBooksSold()));
                 table.addCell(String.valueOf(entry.getValue().getTotalPrice()));
             }
             document.add(table);

         } catch (Exception e) {
            e.printStackTrace();
         }
         document.close();


    }

}
