package com.studyslot.space.service;

import com.studyslot.space.entity.Space;
import com.studyslot.space.repository.SpaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SpaceService {

    public final SpaceRepository spaceRepository;

    public SpaceService(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    public List<Space> findAll(){
        return spaceRepository.findAll();
    }

}
