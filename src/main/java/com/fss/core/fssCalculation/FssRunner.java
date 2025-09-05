package com.fss.core.fssCalculation;


import com.fss.core.fssCalculation.modal.ExcelElement;
import com.fss.core.fssCalculation.persistance.UserRepository;
import com.fss.core.fssCalculation.securityconfig.User;
import com.fss.core.fssCalculation.service.utility.ExcelSheetGenerator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

//@Component
public class FssRunner {
    
    @Autowired
    private UserRepository userRepository;

     @Autowired
    ExcelSheetGenerator excelSheetGenerator;

     @Autowired
    PasswordEncoder passwordEncoder;
    @PostConstruct
    public void run() throws IOException {

        User user = new User();

        user.setId(1L);
        user.setUsername("nkumarnirala1");
        user.setFullname("Nitish Kumar");
        user.setRole("admin");
        user.setEmail("nkumarnirala1@gmail.com");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword("admin");

        User save = userRepository.save(user);

        if(save!=null)
        {
            System.out.println("user added successfully");
        }


    }
}
