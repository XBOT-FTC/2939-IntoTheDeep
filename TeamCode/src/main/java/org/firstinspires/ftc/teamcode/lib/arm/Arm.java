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
    ButtonToggle x =  new ButtonToggle();
    ButtonToggle b = new ButtonToggle();
    ButtonToggle leftBumper = new ButtonToggle();
    public final int EXTENSION_THRESHOLD = Constants.scoringExtensionThreshold;

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
            if (gamepad.left_trigger > 0.2) {
                rotation.basketPosition();
                wrist.score();
            }
            else {
                rotation.transferPosition();
                wrist.transfer();
            }
        }
        else if (dpadDown.isToggled()) {
            slide.slide(telemetry, ArmSlide.SlidePositions.LOW_BASKET);
            if (gamepad.left_trigger > 0.2) {
                rotation.basketPosition();
                wrist.score();
            }
            else {
                rotation.transferPosition();
                wrist.transfer();
            }
        }
        else if (dpadLeft.isToggled()) {
            if (gamepad.left_trigger > 0.2) {
                slide.slide(telemetry, ArmSlide.SlidePositions.LOW_SPECIMEN);
                rotation.basketPosition();
                wrist.score();
            }
            else {
                slide.slide(telemetry, ArmSlide.SlidePositions.TRANSFER);
                rotation.transferPosition();
                wrist.transfer();
            }
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
            grabber.open();
        }
        else {
            grabber.close();
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
            toggleOthersOff(ArmSlide.SlidePositions.INTAKE_SPECIMEN);
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
//        if (gamepad.b) {
//            b.press();
//            toggleOthersOff(ArmSlide.SlidePositions.HIGH_SPECIMEN);
//        }
//        else {
//            b.letGo();
//        }
//        if (gamepad.x) {
//            x.press();
//            toggleOthersOff(ArmSlide.SlidePositions.LOW_SPECIMEN);
//        }
//        else {
//            x.letGo();
//        }

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
        if (position != ArmSlide.SlidePositions.INTAKE_SPECIMEN) {
            dpadLeft.setFalseToggle();
        }
        if (position != ArmSlide.SlidePositions.TRANSFER) {
            dpadRight.setFalseToggle();
        }
//        if (position != ArmSlide.SlidePositions.HIGH_SPECIMEN) {
//            b.setFalseToggle();
//        }
//        if (position != ArmSlide.SlidePositions.LOW_SPECIMEN) {
//            x.setFalseToggle();
//        }
    }
}
