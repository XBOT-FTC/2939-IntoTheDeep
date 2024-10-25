package org.firstinspires.ftc.teamcode.lib.arm;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.ButtonToggle;
import org.firstinspires.ftc.teamcode.lib.Constants;
import org.firstinspires.ftc.teamcode.lib.PIDManager;

@Config
public class ScoringSlide {
    private final DcMotor linearSlideLeft;
    private final DcMotor linearSlideRight;
    public final int MAX_POSITION = 3500; // TODO: Get max
    public final int MIN_POSITION = 0;
    public final double IN_PER_TICK = 0; // TODO: tune
    public SlidePositions extension;
    public int targetPosition = 0;
    private final PIDManager armPID = new PIDManager(0.0093,0,0, 0.0001); // TODO: tune
    public final double positionTolerance = 20; // TODO: tune
    public final double velocityTolerance = 0.09; // TODO: tune
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

    // TODO: Test which motors are inverted
    public ScoringSlide(HardwareMap hardwareMap) {
        // motor for left linear slide setup
        linearSlideLeft = hardwareMap.get(DcMotor.class, ElectricalContract.leftSlideMotor());
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // using external pid
        linearSlideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideLeft.setDirection(Constants.getLeftScoringSlideDirection());

        // motor for right linear slide setup
        linearSlideRight = hardwareMap.get(DcMotor.class, ElectricalContract.rightSlideMotor());
        linearSlideRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // using external pid
        linearSlideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideRight.setDirection(Constants.getLeftScoringSlideDirection().inverted());
    }

    public void slide(Gamepad gamepad, Telemetry telemetry) {
        // get inputs
        // TODO: Figure out button mapping
        highBasketToggle.update(gamepad.dpad_up);
        lowBasketToggle.update(gamepad.dpad_down);
        specimenToggle.update(gamepad.dpad_left);
        hangToggle.update(gamepad.dpad_right); // TODO: Remove if we don't have hanging

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
            power = armPID.pidfControl(linearSlideLeft.getCurrentPosition(), targetPosition, positionTolerance, velocityTolerance);
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

    // TODO: Get tick values
    public int getSlidePositionTicks(SlidePositions slidePosition) {
        int ticks = 0;
        switch(slidePosition) {
            case HIGH_BASKET:
                ticks = Constants.getHighBasketSlidePosition();
                break;
            case LOW_BASKET:
                ticks = Constants.getLowBasketSlidePosition();
                break;
            case SPECIMEN:
                ticks = Constants.getSpecimenSlidePosition();
                break;
            case HANG:
                ticks = Constants.getHangSlidePosition();
                break;
            case HOMED:
                break;
        }
        return ticks;
    }

}
