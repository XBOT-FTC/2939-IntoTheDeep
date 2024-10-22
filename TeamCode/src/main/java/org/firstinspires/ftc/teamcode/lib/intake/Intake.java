package org.firstinspires.ftc.teamcode.lib.intake;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
    IntakeSlide slide = null;
    IntakePivot pivot = null;
    IntakeWheels wheels = null;

    public Intake(HardwareMap hardwareMap) {
        slide = new IntakeSlide(hardwareMap);
        pivot = new IntakePivot(hardwareMap);
        wheels = new IntakeWheels(hardwareMap);
    }

    public void intake(Gamepad gamepad, Telemetry telemetry) {
        // deploys slide to ready position, then deploys intake pivot if slide is out far enough
        if (gamepad.y) {
            slide.slide(telemetry, IntakeSlide.SlidePositions.READY);
            if (gamepad.right_trigger > 0.2 && slide.getCurrentPosition() > slide.getSlidePositionTicks(IntakeSlide.SlidePositions.READY) - 60) { // TODO: Tune threshold for both
                slide.slide(telemetry, IntakeSlide.SlidePositions.INTAKE);
                pivot.deploy();
                wheels.intake();
            }
        }
    }
}
