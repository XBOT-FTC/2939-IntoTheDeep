package org.firstinspires.ftc.teamcode.lib.intake;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class Intake {
    IntakePivot pivot;
    IntakeSlide slide;
    IntakeWheels wheels;

    public Intake(HardwareMap hardwareMap) {
        pivot = new IntakePivot(hardwareMap);
        slide = new IntakeSlide(hardwareMap);
        wheels = new IntakeWheels(hardwareMap);
    }

    // method that runs everything
    public void controls(Gamepad gamepad, Telemetry telemetry) {
        if (gamepad.y) { // deploys slide to ready position, then gives option to intake or eject
            slide.slide(telemetry, IntakeSlide.SlidePositions.READY);
            pivot.home();
            if (gamepad.right_trigger > 0.2 && slide.getCurrentPosition() > Constants.getReadySlideExtension() - Constants.getReadyExtensionThreshold()) {
                slide.slide(telemetry, IntakeSlide.SlidePositions.INTAKE);
                pivot.deploy();
                wheels.intake();
            }
            else if (gamepad.right_bumper && slide.getCurrentPosition() > Constants.getReadySlideExtension() - Constants.getReadyExtensionThreshold()) {
                pivot.deploy();
                wheels.eject();
            }
        }
        else { // back to home position
            slide.slide(telemetry, IntakeSlide.SlidePositions.HOMED);
            pivot.home();
            wheels.stop();
        }
    }
}
