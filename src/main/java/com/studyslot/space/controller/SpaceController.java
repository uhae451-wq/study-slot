package com.studyslot.space.controller;

import com.studyslot.space.entity.Space;
import com.studyslot.space.service.SpaceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/space")
public class SpaceController {

    private final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping("/list")
    public String SpaceFindall(Model model){
        return "/space/space-list";
    }
}
