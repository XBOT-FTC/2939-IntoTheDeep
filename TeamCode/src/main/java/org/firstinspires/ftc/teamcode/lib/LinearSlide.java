package org.firstinspires.ftc.teamcode.lib;

import android.transition.Slide;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.PIDManager;
@Config
public class LinearSlide {
    private DcMotor linearSlideLeft = null;
    private DcMotor linearSlideRight = null;
    public final int MAX_POSITION = 3500;
    public final int MIN_POSITION = 0;
    public final double IN_PER_TICK = 0; //TODO: tune
    public SlidePositions extension;
    public int targetPosition = 0;
    private final PIDManager armPID = new PIDManager(0,0,0, 0); //TODO: tune
    public final double positionTolerance = 100;
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

        // set targetPosition to ticks converted from SlidePositions
        targetPosition = getSlidePositionTicks(extension);

        double power;
        int currentPosition = linearSlideLeft.getCurrentPosition();

        // safety check
        if (currentPosition > MAX_POSITION) {
            power = -0.4; // if slides are exceeding its max position send some negative power to move it down
        }
        else if (currentPosition < MIN_POSITION) {
            power = 0; // if slides are exceeding its min position, stop giving the motor power
        }
        else {
            // PID for adjusting motor power
            power = armPID.pidControl(linearSlideLeft.getCurrentPosition(), targetPosition, positionTolerance);
        }

        // finally set power
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

    // class to handle button toggling
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
