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
    ButtonToggle dpadLeft = new ButtonToggle();
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

        dpadLeft.update(gamepad.dpad_left);
        if (gamepad.y) { // deploys slide to ready position, then gives option to intake or eject once the slide is in threshold of target extension
            slide.slide(telemetry, IntakeSlide.SlidePositions.INTAKE);
            if (slide.getCurrentPosition() > Constants.intakeSlideExtension - EXTENSION_THRESHOLD) {
                pivot.deploy();

                double magnitude = Math.sqrt((Math.pow(gamepad.left_stick_x, 2) + Math.pow(-gamepad.left_stick_y, 2)));
                double robotHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
                double targetJoystickAngle = -Math.toDegrees(Math.atan2(gamepad.left_stick_x, -gamepad.left_stick_y)) + robotHeading;

                targetJoystickAngle = (360 - targetJoystickAngle) % 180;
                double adjustedSwivelPos = targetJoystickAngle / 180;

                if (magnitude > 0.3) {
                    swivel.setPos(adjustedSwivelPos);
                }
                else if (gamepad.right_bumper) {
                    swivel.change();
                }
                else {
                    swivel.transfer();
                }

                telemetry.addData("x value: ", gamepad.left_stick_x);
                telemetry.addData("y value: ", -gamepad.left_stick_y);
                telemetry.addData("swivel pos: ", swivel.getPosition());
            }
        }
        else if (dpadLeft.isToggled()) {
           pivot.deploy();
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
