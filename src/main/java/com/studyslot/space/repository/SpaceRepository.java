package com.studyslot.space.repository;

import com.studyslot.space.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceRepository extends JpaRepository<Space,Long> {

    // space list 검색
    List<Space> findByNameContainingOrAddressContainingOrRoadAddressContaining(
            String name, String address, String roadAddress
    );
}

