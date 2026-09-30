package com.teacher.service;

import com.teacher.entity.Material;
import com.teacher.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepo;

    public List<Material> list(String type, String purpose, String keyword) {
        return materialRepo.findAll();
    }

    public Material getById(Long id) {
        return materialRepo.findById(id).orElseThrow();
    }
}
