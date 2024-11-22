package com.capital.bmo.reconciliationDemo.service;

import com.capital.bmo.reconciliationDemo.model.ReconciliationRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReconciliationService {

    public List<ReconciliationRecord> findMissingInFirst(List<ReconciliationRecord> first, List<ReconciliationRecord> second, String keyField) {
        Set<Object> secondKeys = second.stream()
                .map(record -> record.getFields().get(keyField))
                .collect(Collectors.toSet());

        return first.stream()
                .filter(record -> !secondKeys.contains(record.getFields().get(keyField)))
                .collect(Collectors.toList());
    }

    public List<ReconciliationRecord> findMissingInSecond(List<ReconciliationRecord> first, List<ReconciliationRecord> second, String keyField) {
        return findMissingInFirst(second, first, keyField);
    }

    public List<ReconciliationRecord> findMatches(List<ReconciliationRecord> first, List<ReconciliationRecord> second, String keyField) {
        Set<Object> firstKeys = first.stream()
                .map(record -> record.getFields().get(keyField))
                .collect(Collectors.toSet());

        return second.stream()
                .filter(record -> firstKeys.contains(record.getFields().get(keyField)))
                .collect(Collectors.toList());
    }
}
