package br.fai.lds.client.service.impl;

import br.fai.lds.client.service.ReportService;
import br.fai.lds.client.service.UserService;
import br.fai.lds.model.entities.UserModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private UserService<UserModel> userService;

    private enum TYPE {
        PDF,
        HTML,
        XML
    }


    @Override
    public String generateAndGetPdfFilePath() {

        String home = System.getProperty("user.home");

        // c:/users/lab/Documents/fai/reports/
        String basePath = home
                + File.separator
                + "Documents"
                + File.separator
                + "fai"
                + File.separator
                + "reports"
                + File.separator;

        List<UserModel> users = userService.find();

        if (users == null || users.isEmpty()) {
            return "";
        }

        File file;
        try {
            file = ResourceUtils
                    .getFile("classpath:reports/users-report-fai.jrxml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }

        JasperReport jasperReport;
        try {
            jasperReport = JasperCompileManager
                    .compileReport(file.getAbsolutePath());
        } catch (JRException e) {
            e.printStackTrace();
            return "";
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(users);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("MODIFIED_TITLE", "FAI - 2022");

        JasperPrint jasperPrint;
        try {
            jasperPrint = JasperFillManager
                    .fillReport(jasperReport, parameters, dataSource);
        } catch (JRException e) {
            e.printStackTrace();
            return "";
        }

        String filePath = generateFilePath(basePath);

        try {
            JasperExportManager
                    .exportReportToPdfFile(jasperPrint, filePath);
            return filePath;
        } catch (JRException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String generateFilePath(String basePath) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy-HH_mm_ss");
        Date date = new Date(System.currentTimeMillis());
        String filename = formatter.format(date) + "-report." + TYPE.PDF;

        return basePath + filename;
    }
}
