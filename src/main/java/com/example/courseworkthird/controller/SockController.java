package com.example.courseworkthird.controller;

import com.example.courseworkthird.dto.SockDTO;
import com.example.courseworkthird.exception.InsufficientSockQuantityException;
import com.example.courseworkthird.model.Color;
import com.example.courseworkthird.model.Size;
import com.example.courseworkthird.service.SockService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/sock")
@Tag(name = "Интернет-магазин носков")
public class SockController {
    private final SockService sockService;

    public SockController(SockService sockService) {
        this.sockService = sockService;
    }

    @ExceptionHandler(InsufficientSockQuantityException.class)
    public ResponseEntity<String> handleInvalidException(InsufficientSockQuantityException invalidSockRequestExceprion) {
        return ResponseEntity.badRequest().body(invalidSockRequestExceprion.getMessage());
    }

    @PostMapping
    public void addSock(@RequestBody SockDTO sockDTO){
        sockService.addSock(sockDTO);
    }

    @PutMapping
    public void issueSocks(@RequestBody SockDTO sockDTO){
        sockService.issueSock(sockDTO);
    }

    @GetMapping
    public int getSocksCount(@RequestParam (required = false, name = "color") Color color,
                             @RequestParam (required = false, name = "size") Size size,
                             @RequestParam (required = false, name = "cottonMin") Integer cottonMin,
                             @RequestParam (required = false, name = "cottonMax") Integer cottonMax) {
        return sockService.getSockQuantity(color, size, cottonMin, cottonMax);
    }

    @DeleteMapping
    public void removeDefectiveSocks(@RequestBody SockDTO sockDTO){
        sockService.removeDefectiveSocks(sockDTO);
    }

    @GetMapping("/export")
    public FileSystemResource exportData() throws IOException {
        return sockService.exportData();
    }

    @PostMapping(name = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importData(@RequestParam("file") MultipartFile file) throws IOException {
        sockService.importData(file.getInputStream());
        return ResponseEntity.accepted().build();
    }
}