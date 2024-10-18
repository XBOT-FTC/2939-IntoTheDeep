package org.firstinspires.ftc.teamcode.lib;

import android.transition.Slide;

import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.PIDManager;

public class LinearSlide {
    private DcMotor linearSlideLeft = null;
    private DcMotor linearSlideRight = null;
    public int MAX_POSITION = 4000;
    public int MIN_POSITION = 0;
    public SlidePositions extension;
    public int targetPosition = 0;
    PIDManager armPID = new PIDManager(0,0,0, 0);
    double positionTolerance = 100;
    private final ButtonToggle highBasketToggle = new ButtonToggle();
    private final ButtonToggle lowBasketToggle = new ButtonToggle();
    private final ButtonToggle specimenToggle = new ButtonToggle();
    private final ButtonToggle hangToggle = new ButtonToggle();
    enum SlidePositions {
        HIGH_BASKET,
        LOW_BASKET,
        SPECIMEN,
        HANG,
        HOMED
    }

    public LinearSlide(HardwareMap hardwareMap, DcMotorSimple.Direction direction) {
        // motor for left linear slide, sets up encoders
        linearSlideLeft = hardwareMap.get(DcMotor.class, ElectricalContract.leftSlideMotor());
        linearSlideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // using external pid
        linearSlideLeft.setTargetPosition(0);
        linearSlideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideLeft.setDirection(direction);

        // motor for right linear slide, sets up encoders
        linearSlideRight = hardwareMap.get(DcMotor.class, ElectricalContract.rightSlideMotor());
        linearSlideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        linearSlideRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // using external pid
        linearSlideRight.setTargetPosition(0);
        linearSlideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideRight.setDirection(direction.inverted());
    }

    public void slide(Gamepad gamepad, Telemetry telemetry) {
        // get inputs
        highBasketToggle.update(gamepad.a);
        lowBasketToggle.update(gamepad.b);
        specimenToggle.update(gamepad.x);
        hangToggle.update(gamepad.y);

        // if a button is toggled, set extension to corresponding SlidePosition
        if (highBasketToggle.isToggled()) {
            extension = SlidePositions.HIGH_BASKET;
        } else if (lowBasketToggle.isToggled()) {
            extension = SlidePositions.LOW_BASKET;
        } else if (specimenToggle.isToggled()) {
            extension = SlidePositions.SPECIMEN;
        } else if (hangToggle.isToggled()) {
            extension = SlidePositions.HANG;
        } else {
            extension = SlidePositions.HOMED;
        }

        // set targetPosition as ticks converted from SlidePositions
        targetPosition = getSlidePositionTicks(extension);

        // safety check
        if (targetPosition > MAX_POSITION) {
            targetPosition = MAX_POSITION;
        } else if (targetPosition < MIN_POSITION) {
            targetPosition = MIN_POSITION;
        }

        // PID control for adjusting motor power
        double power = armPID.pidControl(linearSlideLeft.getCurrentPosition(), targetPosition, positionTolerance);

        linearSlideLeft.setPower(power);
        linearSlideRight.setPower(power);

        telemetry.addData("Target Position", targetPosition);
        telemetry.addData("Left Slide Current Position", linearSlideLeft.getCurrentPosition());
        telemetry.addData("Right Slide Current Position", linearSlideRight.getCurrentPosition());
        telemetry.addData("Motor Power", power);
        telemetry.update();
    }

    public int getSlidePositionTicks(SlidePositions slidePosition) {
        int ticks = 0;
        switch(slidePosition) {
            case HIGH_BASKET:
                ticks = 3000;
                break;
            case LOW_BASKET:
                ticks = 1500;
                break;
            case SPECIMEN:
                ticks = 1000;
                break;
            case HANG:
                ticks = 500;
                break;
            default:
                case HOMED:
                    break;
        }
        return ticks;
    }

    public static class ButtonToggle {
        private boolean toggled = false;
        private boolean previousState = false;

        public void update(boolean currentState) {
            if (currentState && !previousState) {
                toggled = !toggled;
            }
            previousState = currentState;
        }

        public boolean isToggled() {
            return toggled;
        }
    }

}
