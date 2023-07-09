package com.example.courseworkthird.service;

import com.example.courseworkthird.dto.SockDTO;
import com.example.courseworkthird.exception.InsufficientSockQuantityException;
import com.example.courseworkthird.exception.InvalidSockRequestException;
import com.example.courseworkthird.model.Color;
import com.example.courseworkthird.model.Size;
import com.example.courseworkthird.model.Sock;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SockService {
    private final ObjectMapper objectMapper;
    private final AuditService auditService;
    private final Map<Sock, Integer> socks = new HashMap<>();

    public SockService(ObjectMapper objectMapper, AuditService auditService) {
        this.objectMapper = objectMapper;
        this.auditService = auditService;
    }

    public void addSock(SockDTO sockDTO) {
        validateRequest(sockDTO);
        Sock sock = mapToSock(sockDTO);
        if (socks.containsKey(sock)) {
            socks.put(sock, socks.get(sock) + sockDTO.getQuantity());
        } else {
            socks.put(sock, sockDTO.getQuantity());
        }
        auditService.recordAddOperation(sock, sockDTO.getQuantity());
    }

    public void issueSock (SockDTO sockDTO){
        decreaseSockQuantity (sockDTO, false);
    }

    public void removeDefectiveSocks (SockDTO sockDTO){
        decreaseSockQuantity(sockDTO, false);
    }

    public FileSystemResource exportData() throws IOException {
        Path filePath = Files.createTempFile("export-", ".json");
        List<SockDTO> sockList = new ArrayList<>();
        for (Map.Entry<Sock, Integer> entry : this.socks.entrySet()){
            sockList.add(mapToDTO(entry.getKey(), entry.getValue()));
        }
        Files.write(filePath,
                objectMapper.writeValueAsBytes(sockList));
        return new FileSystemResource(filePath);
    }

    public void importData(InputStream inputStream) throws IOException {
        List<SockDTO> importList = objectMapper.readValue(inputStream, new TypeReference<>() {
        });
        this.socks.clear();
        for (SockDTO dto : importList) {
            addSock(dto);
        }
    }

    private void decreaseSockQuantity(SockDTO sockDTO, boolean isIssue){
        validateRequest(sockDTO);
        Sock sock = mapToSock(sockDTO);
        int sockQuantity = socks.getOrDefault(sock, 0);
        if (sockQuantity >= sockDTO.getQuantity()){
            socks.put(sock, sockQuantity - sockDTO.getQuantity());
        } else {
            throw new InsufficientSockQuantityException("Нет таких носков");
        }
        if (isIssue){
            auditService.recordIssueOperation(sock, sockDTO.getQuantity());
        } else {
            auditService.recordRemoveDefectedOperation(sock, sockDTO.getQuantity());
        }
    }

    public int getSockQuantity(Color color, Size size, Integer cottonMin, Integer cottonMax) {
        int total = 0;
        for (Map.Entry<Sock, Integer> entry : socks.entrySet()){
            if (color != null && !entry.getKey().getColor().equals(color)){
                continue;
            }
            if (size != null && !entry.getKey().getSize().equals(size)){
                continue;
            }
            if (cottonMin != null && entry.getKey().getCottonPerсentage() < cottonMin){
                continue;
            }
            if (cottonMax != null && entry.getKey().getCottonPerсentage() > cottonMax){
                continue;
            }
            total += entry.getValue();
        }
        return total;
    }

    public void validateRequest(SockDTO sockDTO) {
        if (sockDTO.getColor() == null || sockDTO.getSize() == null) {
            throw new InvalidSockRequestException("Все поля должны быть заполнены");
        }
        if (sockDTO.getCottonPercentage() < 0 || sockDTO.getCottonPercentage() > 100) {
            throw new InvalidSockRequestException("Процент содержания хлопка указать в промежутке от 0 до 100");
        }
        if (sockDTO.getQuantity() <= 0) {
            throw new InvalidSockRequestException("Указать количество больше 0");
        }
    }

    private Sock mapToSock (SockDTO sockDTO) {
         return new Sock(sockDTO.getColor(), sockDTO.getSize(), sockDTO.getCottonPercentage());
    }

    private SockDTO mapToDTO(Sock sock, int quantity){
        SockDTO sockDTO = new SockDTO();
        sockDTO.setColor(sock.getColor());
        sockDTO.setSize(sock.getSize());
        sockDTO.setCottonPercentage(sock.getCottonPerсentage());
        sockDTO.setQuantity(quantity);
        return sockDTO;
    }
}