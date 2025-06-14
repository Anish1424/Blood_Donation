package com.project.bloodbridge.controller;

import com.project.bloodbridge.model.BloodStock;
import com.project.bloodbridge.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")

public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @GetMapping
    public List<BloodStock> getAllStocks() {
        return stockRepository.findAll();
    }

    @PostMapping
    public BloodStock createStock(@RequestBody BloodStock stock) {
        return stockRepository.save(stock);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodStock> getStockById(@PathVariable Long id) {
        return stockRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BloodStock> updateStock(@PathVariable Long id, @RequestBody BloodStock stockDetails) {
        return stockRepository.findById(id).map(stock -> {
            stock.setBloodGroup(stockDetails.getBloodGroup());
            stock.setUnit(stockDetails.getUnit());
            BloodStock updatedStock = stockRepository.save(stock);
            return ResponseEntity.ok(updatedStock);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStock(@PathVariable Long id) {
        return stockRepository.findById(id).map(stock -> {
            stockRepository.delete(stock);
            return ResponseEntity.ok("Stock deleted");
        }).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/blood-stock/update")
    public ResponseEntity<String> updateStock(@RequestParam String bloodGroup, @RequestParam int units) {
        Optional<BloodStock> existingStock = stockRepository.findByBloodGroup(bloodGroup.trim().toUpperCase());

        BloodStock stock;
        if (existingStock.isPresent()) {
            stock = existingStock.get();
            stock.setUnit(stock.getUnit() + units);
        } else {
            stock = new BloodStock();
            stock.setBloodGroup(bloodGroup.trim().toUpperCase());
            stock.setUnit(units);
        }

        stockRepository.save(stock);
        return ResponseEntity.ok("Stock updated successfully");
    }

}

