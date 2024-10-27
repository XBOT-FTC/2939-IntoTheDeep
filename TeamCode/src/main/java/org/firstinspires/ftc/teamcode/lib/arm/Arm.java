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
        chooseToggle(gamepad); // choose which dpad is toggled, only one can be toggled at a time
        if (dpadUp.isToggled()) {
            slide.slide(telemetry, ArmSlide.SlidePositions.HIGH_BASKET);
            rotateToScore(gamepad, ArmSlide.SlidePositions.HIGH_BASKET);
        }
        else if (dpadDown.isToggled()) {
            slide.slide(telemetry, ArmSlide.SlidePositions.LOW_BASKET);
            rotateToScore(gamepad, ArmSlide.SlidePositions.LOW_BASKET); // rotation for arm and wrist are the same for both baskets
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
        else { // if nothing is toggled, bring everything to default position
            slide.slide(telemetry, ArmSlide.SlidePositions.HOMED);
            rotation.transferPosition();
            wrist.transfer();
        }

        if (leftBumper.isToggled()) { // individually control grabber
            grabber.close();
        }
        else {
            grabber.open();
        }

        telemetry.addData("Up Toggle", dpadUp.isToggled());
        telemetry.addData("Down Toggle", dpadDown.isToggled());
        telemetry.addData("Left Toggle", dpadLeft.isToggled());
        telemetry.addData("Right Toggle", dpadRight.isToggled());
    }

    // toggle buttons based on gamepad inputs
    public void chooseToggle(Gamepad gamepad) {
        if (gamepad.dpad_up) {
            dpadUp.press();
            toggleOthersOff(ArmSlide.SlidePositions.HIGH_BASKET);
        }
        else {
            dpadUp.letGo();
        }
        if (gamepad.dpad_down) {
            dpadDown.press();
            toggleOthersOff(ArmSlide.SlidePositions.LOW_BASKET);
        }
        else {
            dpadDown.letGo();
        }
        if (gamepad.dpad_left) {
            dpadLeft.press();
            toggleOthersOff(ArmSlide.SlidePositions.SPECIMEN);
        }
        else {
            dpadLeft.letGo();
        }
        if (gamepad.dpad_right) {
            dpadRight.press();
            toggleOthersOff(ArmSlide.SlidePositions.TRANSFER);
        }
        else {
            dpadRight.letGo();
        }

        if (gamepad.left_bumper) {
            leftBumper.press();
        }
        else {
            leftBumper.letGo();
        }
    }

    // clear toggle for all buttons except the button corresponding to the position parameter
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
        else if (position == ArmSlide.SlidePositions.LOW_BASKET) {
            if (gamepad.left_trigger > 0.2 && slide.getCurrentPosition() > Constants.getLowBasketSlideExtension() - EXTENSION_THRESHOLD) {
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
