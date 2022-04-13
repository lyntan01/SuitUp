/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author meganyee
 */
@Named(value = "reportManagedBean")
@ViewScoped
public class ReportManagedBean implements Serializable {

    @Resource(name = "suitUpDataSource")
    private DataSource suitUpDataSource;

    private final LocalDate today = LocalDate.now();

    public ReportManagedBean() {
    }

//    public void generateStaffReport()
//    {        
//        
//        try
//        {
//            FacesContext.getCurrentInstance().getExternalContext().responseReset();
//            Map<String, Object> parameters = new HashMap<>();
//            parameters.put("Date", today);
//            
//            generateReport("salesreport", parameters);
//                
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Staff report generated successfully!", null));
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occured while generating staff report", null));
//        }
//    }
//    
//    public void generateOrdersReport()
//    {              
//        try
//        {
//            FacesContext.getCurrentInstance().getExternalContext().responseReset();
//            Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(today, LocalTime.MIN));
//            Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(today, LocalTime.MIN));
//            
//            Map<String, Object> parameters = new HashMap<>();
//            
//            parameters.put("StartDate", startTimestamp);
//            parameters.put("EndDate", endTimestamp);
//           
//            generateReport("ordersreport", parameters);
//
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Orders report generated successfully!", null));
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occured while generating orders report", null));
//        }
//    }
//    
//    public void generateReport(String fileName, Map<String, Object> parameters) throws Exception 
//    {
//        InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreport/" + fileName + ".jasper");
//        OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();
//        
//        JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, suitUpDataSource.getConnection());
////        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, suitUpDataSource.getConnection());
////        
////        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
////        response.reset();
////        response.setContentType("application/pdf");
////        response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + ".pdf\"");
//        
////        ServletOutputStream stream = response.getOutputStream();
////        JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
//        FacesContext.getCurrentInstance().responseComplete();
//        outputStream.flush();
//        outputStream.close();
//
//    }
//    public void generateReport(ActionEvent event)
//    {        
//        try
//        {
//            Map<String, Object> parameters = new HashMap<>();
//            
//            Timestamp startTimestamp = Timestamp.valueOf(LocalDateTime.of(today, LocalTime.MIN));
//            Timestamp endTimestamp = Timestamp.valueOf(LocalDateTime.of(today, LocalTime.MIN));
//            
//            
//            parameters.put("StartDate", startTimestamp);
//            parameters.put("EndDate", endTimestamp);
//
//            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreport/ordersreport.jasper");                
//            OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();
//
//            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, suitUpDataSource.getConnection());
//            
//            outputStream.flush();
//            outputStream.close();
//        }
//        catch(JRException ex)
//        {
//                ex.printStackTrace();
//        }
//        catch(SQLException ex)
//        {   
//                ex.printStackTrace();
//        }
//        catch(IOException ex)
//        {
//        }
//    }
    public void generateOrdersReport(ActionEvent event) {
        try {

            Map<String, Object> parameters = new HashMap<>();

            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(new Date());
            Date startDate = cal.getTime();
            cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
            Date endDate = cal.getTime();
            
            parameters.put("EndDate", endDate);
            parameters.put("StartDate", startDate);

            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreport/ordersreport.jasper");
            OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();

            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, suitUpDataSource.getConnection());

            FacesContext.getCurrentInstance().responseComplete();
        } catch (JRException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
        }
    }
    
    public void generateStaffReport(ActionEvent event) {
        try {

            Map<String, Object> parameters = new HashMap<>();

            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(new Date());
            Date date = cal.getTime();
            
            parameters.put("Date", date);

            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreport/salesreport.jasper");
            OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();

            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, suitUpDataSource.getConnection());

            FacesContext.getCurrentInstance().responseComplete();
        } catch (JRException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
        }
    }

}
