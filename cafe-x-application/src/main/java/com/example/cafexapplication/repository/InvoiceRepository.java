package com.example.cafexapplication.repository;

import com.example.cafexapplication.model.Invoice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, Long> {

}
