package com.fss.core.fssCalculation.controller.utility;

import com.fss.core.fssCalculation.modal.input.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;


@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DefaultInput {

    public SlidingInput prepareSlidingWindowInput() {
        SlidingInput slidingInput = new SlidingInput();

        slidingInput.setUnsupportedLength(3000.0);
        slidingInput.setGridLength(1200.0);
        slidingInput.setWindPressure(2.35);
        slidingInput.setGlassThickness(12.00);
        slidingInput.setCentralMeetingProfile(false);
        slidingInput.setTransomToTransomDistance(3000.0);


        return slidingInput;
    }

    public CasementInput prepareCasementWindowInput() {
        CasementInput casementInput = new CasementInput();

        casementInput.setUnsupportedLength(3000.0);
        casementInput.setGridLength(1200.0);
        casementInput.setWindPressure(2.35);
        casementInput.setGlassThickness(12.00);
        casementInput.setCentralMeetingProfile(false);
        casementInput.setTransomToTransomDistance(3000.0);
        casementInput.setTopPanelHeight(3000.0);
        casementInput.setBottomPanelHeight(3000.0);
        casementInput.setSettingBlockLocation(3000.0);


        return casementInput;
    }

    public void prepareMullionDefaults(Model model, HttpSession session, MullionInput mullionInput) {
        // Glass Thickness
        Double sessionGlassThickness = (session.getAttribute("glassThickness") instanceof Double)
                ? (Double) session.getAttribute("glassThickness") : null;
        double glassThickness = mullionInput.getGlassThickness() != 0.0 ? mullionInput.getGlassThickness() :
                (sessionGlassThickness != null && sessionGlassThickness != 0.0 ? sessionGlassThickness : 12.0);
        mullionInput.setGlassThickness(glassThickness);
        model.addAttribute("glassThickness", glassThickness);

        // Cross Sectional Area
        Double sessionCrossSectionalArea = (session.getAttribute("crossSectionalArea") instanceof Double)
                ? (Double) session.getAttribute("crossSectionalArea") : null;
        double crossSectionalArea = mullionInput.getCrossSectionalArea() != 0.0 ? mullionInput.getCrossSectionalArea() :
                (sessionCrossSectionalArea != null && sessionCrossSectionalArea != 0.0 ? sessionCrossSectionalArea : 6.25);
        mullionInput.setCrossSectionalArea(crossSectionalArea);
        model.addAttribute("crossSectionalArea", crossSectionalArea);

        // Transom to Transom Distance
        Double sessionTransomToTransomDistance = (session.getAttribute("transomToTransomDistance") instanceof Double)
                ? (Double) session.getAttribute("transomToTransomDistance") : null;
        double transomToTransomDistance = mullionInput.getTransomToTransomDistance() != 0.0 ? mullionInput.getTransomToTransomDistance() :
                (sessionTransomToTransomDistance != null && sessionTransomToTransomDistance != 0.0 ? sessionTransomToTransomDistance : 1200.0);
        mullionInput.setTransomToTransomDistance(transomToTransomDistance);
        model.addAttribute("transomToTransomDistance", transomToTransomDistance);

        // b
        Double sessionB = (session.getAttribute("b") instanceof Double) ? (Double) session.getAttribute("b") : null;
        double b = mullionInput.getB() != 0.0 ? mullionInput.getB() :
                (sessionB != null && sessionB != 0.0 ? sessionB : 60.0);
        mullionInput.setB(b);
        model.addAttribute("b", b);

        // a
        Double sessionA = (session.getAttribute("a") instanceof Double) ? (Double) session.getAttribute("a") : null;
        double a = mullionInput.getA() != 0.0 ? mullionInput.getA() :
                (sessionA != null && sessionA != 0.0 ? sessionA : 100.0);
        mullionInput.setA(a);
        model.addAttribute("a", a);

        // t2
        Double sessionT2 = (session.getAttribute("t2") instanceof Double) ? (Double) session.getAttribute("t2") : null;
        double t2 = mullionInput.getT2() != 0.0 ? mullionInput.getT2() :
                (sessionT2 != null && sessionT2 != 0.0 ? sessionT2 : 3.0);
        mullionInput.setT2(t2);
        model.addAttribute("t2", t2);

        // t1
        Double sessionT1 = (session.getAttribute("t1") instanceof Double) ? (Double) session.getAttribute("t1") : null;
        double t1 = mullionInput.getT1() != 0.0 ? mullionInput.getT1() :
                (sessionT1 != null && sessionT1 != 0.0 ? sessionT1 : 2.0);
        mullionInput.setT1(t1);
        model.addAttribute("t1", t1);

        // iyy
        Double sessionIyy = (session.getAttribute("iyy") instanceof Double) ? (Double) session.getAttribute("iyy") : null;
        double iyy = mullionInput.getIyy() != 0.0 ? mullionInput.getIyy() :
                (sessionIyy != null && sessionIyy != 0.0 ? sessionIyy : 46.24);
        mullionInput.setIyy(iyy);
        model.addAttribute("iyy", iyy);

        // boundingboxy
        Double sessionBoundingboxy = (session.getAttribute("boundingboxy") instanceof Double)
                ? (Double) session.getAttribute("boundingboxy") : null;
        double boundingboxy = mullionInput.getBoundingboxy() != 0.0 ? mullionInput.getBoundingboxy() :
                (sessionBoundingboxy != null && sessionBoundingboxy != 0.0 ? sessionBoundingboxy : 2.0);
        mullionInput.setBoundingboxy(boundingboxy);
        model.addAttribute("boundingboxy", boundingboxy);

        // boundingboxy
        Double sessionBoundingboxx = (session.getAttribute("boundingboxx") instanceof Double)
                ? (Double) session.getAttribute("boundingboxx") : null;
        double boundingboxx = mullionInput.getBoundingboxx() != 0.0 ? mullionInput.getBoundingboxx() :
                (sessionBoundingboxx != null && sessionBoundingboxx != 0.0 ? sessionBoundingboxx : 3.0);
        mullionInput.setBoundingboxx(boundingboxx);
        model.addAttribute("boundingboxx", boundingboxx);


        // userIxx
        Double sessionUserIxx = (session.getAttribute("userIxx") instanceof Double)
                ? (Double) session.getAttribute("userIxx") : null;
        double userIxx = mullionInput.getUserIxx() != 0.0 ? mullionInput.getUserIxx() :
                (sessionUserIxx != null && sessionUserIxx != 0.0 ? sessionUserIxx : 200.0);
        mullionInput.setUserIxx(userIxx);
        model.addAttribute("userIxx", userIxx);

        // Save to session so they persist
        session.setAttribute("glassThickness", glassThickness);
        session.setAttribute("crossSectionalArea", crossSectionalArea);
        session.setAttribute("transomToTransomDistance", transomToTransomDistance);
        session.setAttribute("b", b);
        session.setAttribute("a", a);
        session.setAttribute("t2", t2);
        session.setAttribute("t1", t1);
        session.setAttribute("iyy", iyy);
        session.setAttribute("boundingboxy", boundingboxy);
        session.setAttribute("userIxx", userIxx);
    }

    public void prepareTransomDefaults(Model model, HttpSession session, TransomInput transomInput) {
        // Glass Thickness
        Double sessionGlassThickness = (session.getAttribute("glassThickness") instanceof Double)
                ? (Double) session.getAttribute("glassThickness") : null;
        model.addAttribute("glassThickness",
                transomInput.getGlassThickness() != 0.0 ? transomInput.getGlassThickness() :
                        (sessionGlassThickness != null && sessionGlassThickness != 0.0 ? sessionGlassThickness : 12.0));

        // Cross Sectional Area (specific to Transom)
        Double sessionCrossSectionalArea = (session.getAttribute("transomCrossSectionalArea") instanceof Double)
                ? (Double) session.getAttribute("transomCrossSectionalArea") : null;
        model.addAttribute("transomCrossSectionalArea",
                transomInput.getTransomCrossSectionalArea() != 0.0 ? transomInput.getTransomCrossSectionalArea() :
                        (sessionCrossSectionalArea != null && sessionCrossSectionalArea != 0.0 ? sessionCrossSectionalArea : 9.6));

        // Unsupported Length
        Double sessionUnsupportedLength = (session.getAttribute("transomUnsupportedLength") instanceof Double)
                ? (Double) session.getAttribute("transomUnsupportedLength") : null;
        model.addAttribute("transomUnsupportedLength",
                transomInput.getTransomUnsupportedLength() != 0.0 ? transomInput.getTransomUnsupportedLength() :
                        (sessionUnsupportedLength != null && sessionUnsupportedLength != 0.0 ? sessionUnsupportedLength : 1200.0));

        // Section Width B
        Double sessionB = (session.getAttribute("sectionWidthB") instanceof Double) ? (Double) session.getAttribute("sectionWidthB") : null;
        model.addAttribute("sectionWidthB",
                transomInput.getSectionWidthB() != 0.0 ? transomInput.getSectionWidthB() :
                        (sessionB != null && sessionB != 0.0 ? sessionB : 10.0));

        // Depth of Section (a)
        Double sessionDepth = (session.getAttribute("depthOfSectionTransom") instanceof Double) ? (Double) session.getAttribute("depthOfSectionTransom") : null;
        model.addAttribute("depthOfSectionTransom",
                transomInput.getDepthOfSectionTransom() != 0.0 ? transomInput.getDepthOfSectionTransom() :
                        (sessionDepth != null && sessionDepth != 0.0 ? sessionDepth : 101.0));

        // t2
        Double sessionT2 = (session.getAttribute("t2Transom") instanceof Double) ? (Double) session.getAttribute("t2Transom") : null;
        model.addAttribute("t2Transom",
                transomInput.getT2Transom() != 0.0 ? transomInput.getT2Transom() :
                        (sessionT2 != null && sessionT2 != 0.0 ? sessionT2 : 2.6));

        // t1
        Double sessionT1 = (session.getAttribute("thicknessTaTransom_t1") instanceof Double) ? (Double) session.getAttribute("thicknessTaTransom_t1") : null;
        model.addAttribute("thicknessTaTransom_t1",
                transomInput.getThicknessTaTransom_t1() != 0.0 ? transomInput.getThicknessTaTransom_t1() :
                        (sessionT1 != null && sessionT1 != 0.0 ? sessionT1 : 2.6));

        // Iyy
        Double sessionIyy = (session.getAttribute("transomIyy") instanceof Double) ? (Double) session.getAttribute("transomIyy") : null;
        model.addAttribute("transomIyy",
                transomInput.getTransomIyy() != 0.0 ? transomInput.getTransomIyy() :
                        (sessionIyy != null && sessionIyy != 0.0 ? sessionIyy : 150.9));

        // Ixx
        Double sessionIxx = (session.getAttribute("transomIxx") instanceof Double) ? (Double) session.getAttribute("transomIxx") : null;
        model.addAttribute("transomIxx",
                transomInput.getTransomIxx() != 0.0 ? transomInput.getTransomIxx() :
                        (sessionIxx != null && sessionIxx != 0.0 ? sessionIxx : 46.24));

        // Bounding Box Y
        Double sessionBoundingBoxY = (session.getAttribute("transomBoundingBoxY") instanceof Double)
                ? (Double) session.getAttribute("transomBoundingBoxY") : null;
        model.addAttribute("transomBoundingBoxY",
                transomInput.getTransomBoundingBoxY() != 0.0 ? transomInput.getTransomBoundingBoxY() :
                        (sessionBoundingBoxY != null && sessionBoundingBoxY != 0.0 ? sessionBoundingBoxY : 2.85));

        // Save to session
        session.setAttribute("glassThickness", model.getAttribute("glassThickness"));
        session.setAttribute("transomCrossSectionalArea", model.getAttribute("transomCrossSectionalArea"));
        session.setAttribute("transomUnsupportedLength", model.getAttribute("transomUnsupportedLength"));
        session.setAttribute("sectionWidthB", model.getAttribute("sectionWidthB"));
        session.setAttribute("depthOfSectionTransom", model.getAttribute("depthOfSectionTransom"));
        session.setAttribute("t2Transom", model.getAttribute("t2Transom"));
        session.setAttribute("thicknessTaTransom_t1", model.getAttribute("thicknessTaTransom_t1"));
        session.setAttribute("transomIyy", model.getAttribute("transomIyy"));
        session.setAttribute("transomIxx", model.getAttribute("transomIxx"));
        session.setAttribute("transomBoundingBoxY", model.getAttribute("transomBoundingBoxY"));
    }

    public GlazingInput prepareDefaultInput() {
        GlazingInput input = new GlazingInput();
        input.setUnsupportedLength(3000.0);
        input.setGridLength(1200.0);
        input.setWindPressure(2.35);
        input.setStackBracket(0.0);

        return input;
    }

    public OuterProfileInput prepareOuterProfileInput() {
        OuterProfileInput outerProfileInput = new OuterProfileInput();

        outerProfileInput.setEccentricity(50.0);
        outerProfileInput.setLegThickness(5.0);

        return outerProfileInput;
    }

    public CentralProfileInput CentralProfileDefaultInput(Model model, HttpSession session) {
        CentralProfileInput centralProfileDefaultInput = new CentralProfileInput();

        MullionInput mullionInput = new MullionInput();
        prepareMullionDefaults(model, session, mullionInput);
        centralProfileDefaultInput.setShutterA(mullionInput);
        centralProfileDefaultInput.setShutterB(mullionInput);

        return centralProfileDefaultInput;
    }


}
