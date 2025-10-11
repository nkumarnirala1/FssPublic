package com.fss.core.fssCalculation.controller.utility;


import com.fss.core.fssCalculation.modal.generic.DownloadReportElement;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class FlowContext {

    private String activeMenu;
    private List<String> activeForm;
    private  Boolean redirectToCentralProfile = false;
    private  Boolean isAB_InterlockCheckDone = false;
    private String calculationMethod;
    private HashMap<String, String> replaceableModelMap;
    private HashMap<String, Object> inputValuesMap;
    private List<DownloadReportElement> downloadFormList = new ArrayList<>();


}
