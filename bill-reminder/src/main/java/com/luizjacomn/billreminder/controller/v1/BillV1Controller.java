package com.luizjacomn.billreminder.controller.v1;

import com.luizjacomn.billreminder.model.dto.BillRequest;
import com.luizjacomn.billreminder.model.dto.BillResponse;
import com.luizjacomn.billreminder.model.filter.BillFilter;
import com.luizjacomn.billreminder.service.BillService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bills")
public class BillV1Controller {

    private final BillService billService;

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Valid BillRequest request) {
        var bill = billService.save(request.toEntity());

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(bill.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public List<BillResponse> list(@Parameter @Valid BillFilter filter) {
        return billService.list(filter).stream().map(BillResponse::new).toList();
    }

}
