package org.firstinspires.ftc.teamcode.lib.intake;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.lib.ButtonToggle;
import org.firstinspires.ftc.teamcode.lib.Constants;
import org.firstinspires.ftc.teamcode.lib.arm.ArmSlide;

public class Intake {
    IntakePivot pivot;
    IntakeSlide slide;
    IntakeClaw claw;
    IntakeClawSwivel swivel;
    ButtonToggle leftBumper = new ButtonToggle();
    public final int EXTENSION_THRESHOLD = Constants.readyExtensionThreshold;

    public Intake(HardwareMap hardwareMap) {
        pivot = new IntakePivot(hardwareMap);
        slide = new IntakeSlide(hardwareMap);
        claw = new IntakeClaw(hardwareMap);
        swivel = new IntakeClawSwivel(hardwareMap);
    }

    // method that runs everything
    public void controls(Gamepad gamepad, Telemetry telemetry, IMU imu) {
        chooseToggle(gamepad);
        if (gamepad.y) { // deploys slide to ready position, then gives option to intake or eject once the slide is in threshold of target extension
            slide.slide(telemetry, IntakeSlide.SlidePositions.INTAKE);
            if (slide.getCurrentPosition() > Constants.intakeSlideExtension - EXTENSION_THRESHOLD) {
                pivot.deploy();
                if (gamepad.right_bumper) {
                    swivel.change();
                }
                else {
                    swivel.transfer();
                }

                double magnitude = Math.sqrt(Math.pow(gamepad.right_stick_x, 2) + Math.pow(gamepad.right_stick_y, 2));
                double joystickRadians = Math.atan(gamepad.right_stick_y/gamepad.right_stick_x);
                double joystickDegrees = Math.toDegrees(joystickRadians);
                double targetJoystickPos = (joystickDegrees + imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)) / 180;
                if (magnitude > 0.2) {
                    swivel.setPos(targetJoystickPos);
                }
            }
        }
        else { // back to home position
            slide.slide(telemetry, IntakeSlide.SlidePositions.HOMED);
            pivot.home();
            swivel.transfer();
        }

        if (leftBumper.isToggled()) { // individually control grabber
            claw.close();
        }
        else {
            claw.open();
        }
    }

    public void chooseToggle(Gamepad gamepad) {
        if (gamepad.left_bumper) {
            leftBumper.press();
        }
        else {
            leftBumper.letGo();
        }
    }
}
