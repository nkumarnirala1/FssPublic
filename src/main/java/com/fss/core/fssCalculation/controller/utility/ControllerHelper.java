package com.fss.core.fssCalculation.controller.utility;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
public class ControllerHelper {

    public void addActiveFormsToModel(Model model, List<String> activeForm) {

        if (activeForm != null && !activeForm.isEmpty()) {
            for (String form : activeForm) {
                model.addAttribute(form, true);
            }
        }

    }

}
