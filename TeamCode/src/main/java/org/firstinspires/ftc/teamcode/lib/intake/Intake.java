package org.firstinspires.ftc.teamcode.lib.intake;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    IntakeSlide slide = null;
    IntakePivot pivot = null;
    IntakeWheels wheels = null;

    public Intake(HardwareMap hardwareMap) {
        slide = new IntakeSlide(hardwareMap);
        pivot = new IntakePivot(hardwareMap);
        wheels = new IntakeWheels(hardwareMap);

    }


}
