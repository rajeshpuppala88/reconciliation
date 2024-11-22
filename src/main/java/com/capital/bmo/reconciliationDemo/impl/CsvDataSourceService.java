package com.capital.bmo.reconciliationDemo.impl;

import com.capital.bmo.reconciliationDemo.model.ReconciliationRecord;
import com.capital.bmo.reconciliationDemo.service.DataSourceService;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvDataSourceService implements DataSourceService {

    @Override
    public List<ReconciliationRecord> readData(MultipartFile file) {
        List<ReconciliationRecord> records = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             InputStreamReader isr = new InputStreamReader(inputStream);
             CSVReader reader = new CSVReader(isr)) {
            List<String[]> rows = reader.readAll();
            String[] headers = rows.get(0); // Header row

            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                ReconciliationRecord record = new ReconciliationRecord();
                Map<String, Object> fields = new HashMap<>();
                for (int j = 0; j < headers.length; j++) {
                    fields.put(headers[j], row[j]);
                }
                record.setFields(fields);
                records.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }
}
