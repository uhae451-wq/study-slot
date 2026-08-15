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

    // space 검색
    public List<Space> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        return spaceRepository.findByNameContainingOrAddressContainingOrRoadAddressContaining(
                keyword, keyword, keyword
        );
    }

}
