package com.fss.core.fssCalculation.controller.utility;

import com.fss.core.fssCalculation.modal.input.MullionInput;
import com.fss.core.fssCalculation.modal.output.MullionProfileOutput;
import com.fss.core.fssCalculation.service.ReportGen.Utility;
import com.fss.core.fssCalculation.service.elements.inertia.ZxxCal;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StoreSessionAttribute {

    @Autowired
    ZxxCal zxxCal;
    
    public void populateMullionSessionAttribute(MullionInput mullionInput, MullionProfileOutput mullionProfileOutput, HttpSession session) {
        session.setAttribute("axialForce", Utility.roundTo2Decimal(mullionProfileOutput.getAxialForce()));
        session.setAttribute("a/b", Utility.roundTo2Decimal(mullionInput.getA() / mullionInput.getB()));
        session.setAttribute("t2/t1", mullionInput.getT2() / mullionInput.getT1());
        session.setAttribute("hy", mullionInput.getB() - (2 * mullionInput.getT1()));
        session.setAttribute("lambdaAt", mullionProfileOutput.getLambdaAt());
        session.setAttribute("fig1Value", mullionProfileOutput.getFig1Value().getResult());
        session.setAttribute("fig2Value", mullionProfileOutput.getFig2Value());
        session.setAttribute("fig3Value", mullionProfileOutput.getFig3Value());
        session.setAttribute("axial_bending_check", Utility.roundTo2Decimal((mullionProfileOutput.getFtMullion() / mullionProfileOutput.getFig1Value().getResult()) + (mullionProfileOutput.getMByz() / mullionProfileOutput.getFig2Value())));
        session.setAttribute("shearForce_mullion", Utility.roundTo2Decimal(mullionProfileOutput.getShearForce()));
        session.setAttribute("hx", mullionProfileOutput.getHx());
        session.setAttribute("fsx_mullion", Utility.roundTo2Decimal(mullionProfileOutput.getShearForce() / (mullionProfileOutput.getHx() * mullionInput.getT1())));
        session.setAttribute("ft_mullion", mullionProfileOutput.getFtMullion());
        session.setAttribute("fa", mullionProfileOutput.getAxialForce() / mullionInput.getCrossSectionalArea());
        session.setAttribute("udldeadload", Utility.roundTo2Decimal(mullionProfileOutput.getUdlDeadLoad()));
        session.setAttribute("selfWeight", Utility.roundTo2Decimal(mullionProfileOutput.getSelfWeight()));
        session.setAttribute("mbyz", mullionProfileOutput.getMByz());
        session.setAttribute("effectiveArea", mullionProfileOutput.getEffectiveArea());
        session.setAttribute("udlWindLoad", mullionProfileOutput.getUdlWindLoad());


        double zxx = zxxCal.calculateZxx(mullionInput.getUserIxx(), mullionInput.getBoundingboxy());
        session.setAttribute("zxx_mullion", zxx);
    }
}
