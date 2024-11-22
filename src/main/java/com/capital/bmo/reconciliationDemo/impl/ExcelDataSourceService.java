package com.capital.bmo.reconciliationDemo.impl;

import com.capital.bmo.reconciliationDemo.model.ReconciliationRecord;
import com.capital.bmo.reconciliationDemo.service.DataSourceService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelDataSourceService implements DataSourceService {

    @Override
    public List<ReconciliationRecord> readData(MultipartFile file) {
        List<ReconciliationRecord> records = new ArrayList<>();
        try (InputStream fis = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0); // Assume single sheet
            List<String> headers = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // Header row
                    row.forEach(cell -> headers.add(cell.getStringCellValue()));
                } else { // Data rows
                    ReconciliationRecord record = new ReconciliationRecord();
                    Map<String, Object> fields = new HashMap<>();
                    for (int i = 0; i < headers.size(); i++) {
                        fields.put(headers.get(i), row.getCell(i).toString());
                    }
                    record.setFields(fields);
                    records.add(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }
}
