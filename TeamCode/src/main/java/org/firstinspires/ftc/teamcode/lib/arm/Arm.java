package org.firstinspires.ftc.teamcode.lib.arm;

import android.widget.Button;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.ButtonToggle;
import org.firstinspires.ftc.teamcode.lib.Constants;

public class Arm {
    ArmRotation rotation;
    ArmClaw grabber;
    ArmSlide slide;
    ButtonToggle dpadUp = new ButtonToggle();
    ButtonToggle dpadDown = new ButtonToggle();
    ButtonToggle dpadLeft = new ButtonToggle();
    ButtonToggle dpadRight = new ButtonToggle();

    public Arm(HardwareMap hardwareMap) {
        rotation = new ArmRotation(hardwareMap);
        grabber = new ArmClaw(hardwareMap);
        slide = new ArmSlide(hardwareMap);
    }

    // method that runs everything
    public void controls(Gamepad gamepad, Telemetry telemetry) {
        chooseToggle(gamepad);
        if (dpadUp.isToggled()) {

        }
    }

    public void chooseToggle(Gamepad gamepad) {
        if (gamepad.dpad_up) {
            toggleButtons(ArmSlide.SlidePositions.HIGH_BASKET);
        }
        else if (gamepad.dpad_down) {
            toggleButtons(ArmSlide.SlidePositions.LOW_BASKET);
        }
        else if (gamepad.dpad_left) {
            toggleButtons(ArmSlide.SlidePositions.SPECIMEN);
        }
        else if (gamepad.dpad_right) {
            toggleButtons(ArmSlide.SlidePositions.HANG);
        }
        else {
            toggleButtons(ArmSlide.SlidePositions.HOMED);
        }
    }


    public void toggleButtons(ArmSlide.SlidePositions position) {
        switch (position) {
            case HIGH_BASKET:
                dpadUp.update(true);
                toggleOthersOff(ArmSlide.SlidePositions.HIGH_BASKET);
                break;
            case LOW_BASKET:
                dpadDown.update(true);
                toggleOthersOff(ArmSlide.SlidePositions.LOW_BASKET);
                break;
            case SPECIMEN:
                dpadLeft.update(true);
                toggleOthersOff(ArmSlide.SlidePositions.SPECIMEN);
                break;
            case HANG:
                dpadRight.update(true);
                toggleOthersOff(ArmSlide.SlidePositions.HANG);
                break;
            case HOMED:
                toggleOthersOff(ArmSlide.SlidePositions.HOMED);
                break;        }
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
        if (position != ArmSlide.SlidePositions.HANG) {
            dpadRight.setFalseToggle();
        }
    }
}
