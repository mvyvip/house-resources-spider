package com.hs.reptilian.service.impl;

import com.hs.reptilian.mapper.ReptilianMapper;
import com.hs.reptilian.model.ReptilianList;
import com.hs.reptilian.service.ReptilianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReptilianServiceImpl implements ReptilianService {

    @Autowired
    private ReptilianMapper reptilianMapper;

    @Override
    public List<ReptilianList> findAll() {
        return reptilianMapper.findAll();
    }

}
