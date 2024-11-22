package com.capital.bmo.reconciliationDemo.service;



import com.capital.bmo.reconciliationDemo.model.ReconciliationRecord;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DataSourceService {
    List<ReconciliationRecord> readData(MultipartFile sourcePath);
}
