package com.luizjacomn.billreminder.service;

import com.luizjacomn.billreminder.model.entity.Bill;
import com.luizjacomn.billreminder.model.filter.BillFilter;
import com.luizjacomn.billreminder.repository.BillRepository;
import com.luizjacomn.billreminder.repository.spec.BillSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;

    @Transactional
    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }

    @Transactional(readOnly = true)
    public List<Bill> list(BillFilter filter) {
        return billRepository.findAll(BillSpecs.byFilter(Objects.requireNonNullElse(filter, BillFilter.empty())));
    }
}
