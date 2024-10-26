package org.firstinspires.ftc.teamcode.lib.arm;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.ButtonToggle;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class Arm {
    ArmRotation rotation;
    ArmClaw grabber;
    ArmSlide slide;
    ArmWrist wrist;
    ButtonToggle dpadUp = new ButtonToggle();
    ButtonToggle dpadDown = new ButtonToggle();
    ButtonToggle dpadLeft = new ButtonToggle();
    ButtonToggle dpadRight = new ButtonToggle();
    ButtonToggle leftBumper = new ButtonToggle();
    public final int EXTENSION_THRESHOLD = Constants.getScoringExtensionThreshold();

    public Arm(HardwareMap hardwareMap) {
        rotation = new ArmRotation(hardwareMap);
        grabber = new ArmClaw(hardwareMap);
        slide = new ArmSlide(hardwareMap);
        wrist = new ArmWrist(hardwareMap);
    }

    // method that runs everything
    public void controls(Gamepad gamepad, Telemetry telemetry) {
        chooseToggle(gamepad);
        if (dpadUp.isToggled()) {
            slide.slide(telemetry, ArmSlide.SlidePositions.HIGH_BASKET);
            rotateToScore(gamepad, ArmSlide.SlidePositions.HIGH_BASKET);
        }
        else if (dpadDown.isToggled()) {
            slide.slide(telemetry, ArmSlide.SlidePositions.LOW_BASKET);
            rotateToScore(gamepad, ArmSlide.SlidePositions.HIGH_BASKET);
        }
        else if (dpadLeft.isToggled()) { // FIXME: Comment out if specimen scoring not being used
            slide.slide(telemetry, ArmSlide.SlidePositions.SPECIMEN);
            rotateToScore(gamepad, ArmSlide.SlidePositions.SPECIMEN);
        }
        else if (dpadRight.isToggled()) {
            slide.slide(telemetry, ArmSlide.SlidePositions.TRANSFER);
            rotation.transferPosition();
            wrist.transfer();
        }
        else {
            slide.slide(telemetry, ArmSlide.SlidePositions.HOMED);
            rotation.transferPosition();
            wrist.transfer();
        }

        if (leftBumper.isToggled()) {
            grabber.close();
        }
        else {
            grabber.open();
        }
    }

    public void chooseToggle(Gamepad gamepad) {
        if (gamepad.dpad_up) {
            dpadUp.press();
            toggleOthersOff(ArmSlide.SlidePositions.HIGH_BASKET);
        }
        else if (!gamepad.dpad_up) {
            dpadUp.letGo();
        }
        else if (gamepad.dpad_down) {
            dpadDown.press();
            toggleOthersOff(ArmSlide.SlidePositions.LOW_BASKET);
        }
        else if (!gamepad.dpad_down) {
            dpadDown.letGo();
        }
        else if (gamepad.dpad_left) {
            dpadLeft.press();
            toggleOthersOff(ArmSlide.SlidePositions.SPECIMEN);
        }
        else if (!gamepad.dpad_left) {
            dpadLeft.letGo();
        }
        else if (gamepad.dpad_right) {
            dpadRight.press();
            toggleOthersOff(ArmSlide.SlidePositions.TRANSFER);
        }
        else if (!gamepad.dpad_right) {
            dpadRight.letGo();
        }

        if (gamepad.left_bumper) {
            leftBumper.press();
        }
        else {
            leftBumper.letGo();
        }
    }

    public void toggleOthersOff(ArmSlide.SlidePositions position) {
        if (position != ArmSlide.SlidePositions.HIGH_BASKET) {
            dpadUp.setFalseToggle();
        }
        if (position != ArmSlide.SlidePositions.LOW_BASKET) {
            dpadDown.setFalseToggle();
        }
        if (position != ArmSlide.SlidePositions.SPECIMEN) {
            dpadLeft.setFalseToggle();
        }
        if (position != ArmSlide.SlidePositions.TRANSFER) {
            dpadRight.setFalseToggle();
        }
    }

    public void rotateToScore(Gamepad gamepad, ArmSlide.SlidePositions position) {
        if (position == ArmSlide.SlidePositions.HIGH_BASKET) {
            if (gamepad.left_trigger > 0.2 && slide.getCurrentPosition() > Constants.getHighBasketSlideExtension() - EXTENSION_THRESHOLD) {
                rotation.basketPosition();
                wrist.score();
            }
            else {
                rotation.transferPosition();
                wrist.transfer();
            }
        }
        else if (position == ArmSlide.SlidePositions.SPECIMEN) {
            if (gamepad.left_trigger > 0.2 && slide.getCurrentPosition() > Constants.getSpecimenSlideExtension() - EXTENSION_THRESHOLD) {
                rotation.specimenPosition();
                wrist.score(); // TODO: Figure out if wrist position for baskets is same as specimen
            }
            else {
                rotation.transferPosition();
                wrist.transfer();
            }
        }
    }
}
