package com.fss.core.fssCalculation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/window")
public class WindowController {


    @GetMapping("/sliding")
    public String slidingCalculation()
    {
        return "";
    }
}
