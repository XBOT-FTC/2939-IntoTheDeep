package org.firstinspires.ftc.teamcode.lib.intake;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ElectricalContract;

@Config
public class IntakeSlide {
    private DcMotor linearSlideLeft = null;
    private DcMotor linearSlideRight = null;
    public final int MAX_POSITION = 0; // TODO: Get max
    public final int MIN_POSITION = 0;
    public SlidePositions extension;
    public int targetPosition = 0;
    public final double slidePower = 0.5; // TODO: Tune
    enum SlidePositions {
        READY,
        INTAKE,
        HOMED
    }

    // TODO: test which motors are inverted
    public IntakeSlide(HardwareMap hardwareMap) {
        // motor for left linear slide, sets up encoders
        linearSlideLeft = hardwareMap.get(DcMotor.class, ElectricalContract.leftSlideMotor());
        linearSlideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // using external pid
        linearSlideLeft.setTargetPosition(0);
        linearSlideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideLeft.setDirection(DcMotorSimple.Direction.FORWARD);

        // motor for right linear slide, sets up encoders
        linearSlideRight = hardwareMap.get(DcMotor.class, ElectricalContract.rightSlideMotor());
        linearSlideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        linearSlideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER); // using external pid
        linearSlideRight.setTargetPosition(0);
        linearSlideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void slide(Telemetry telemetry, SlidePositions position) {

        switch (position) {
            case READY:
                extension = SlidePositions.READY;
            case INTAKE:
                extension = SlidePositions.INTAKE;
            case HOMED:
                extension = SlidePositions.HOMED;
        }

        // set targetPosition to ticks converted from SlidePositions
        targetPosition = getSlidePositionTicks(extension);
        linearSlideLeft.setTargetPosition(targetPosition);
        linearSlideRight.setTargetPosition(targetPosition);
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        linearSlideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

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
            power = slidePower;
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
            case READY:
                ticks = 0;
                break;
            case INTAKE:
                ticks = 0;
                break;
            case HOMED:
                break;
        }
        return ticks;
    }

    // returns average position
    public int getCurrentPosition() {
        return (linearSlideLeft.getCurrentPosition() + linearSlideRight.getCurrentPosition()) / 2;
    }

}
