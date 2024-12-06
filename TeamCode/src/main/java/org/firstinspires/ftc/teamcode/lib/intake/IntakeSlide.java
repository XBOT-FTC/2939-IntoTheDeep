package org.firstinspires.ftc.teamcode.lib.intake;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.Constants;
import org.firstinspires.ftc.teamcode.lib.PIDManager;

@Config
public class IntakeSlide {
    private final DcMotor linearSlideLeft;
    private final DcMotor linearSlideRight;
    public int targetPosition = 0;
    public final int MAX_POSITION = Constants.maxIntakeSlideExtension;
    public final double slidePower = Constants.intakeSlidePower;
    public final double killPowerThreshold = Constants.homedKillPowerThreshold;
    public enum SlidePositions {
        READY,
        INTAKE,
        HOMED,
        SPECIMEN_INTAKE,
        AUTO_INTAKE
    }
    PIDManager slidePID = new PIDManager(0.004,0,0.35,0); // TODO: tune

    public IntakeSlide(HardwareMap hardwareMap) {
        // motor for left linear slide, sets up encoders
        linearSlideLeft = hardwareMap.get(DcMotor.class, ElectricalContract.leftIntakeSlideMotor());
        linearSlideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // using external pid
//        linearSlideLeft.setTargetPosition(0);
        linearSlideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        linearSlideLeft.setDirection(Constants.leftIntakeMotorDirection);

        // motor for right linear slide, sets up encoders
        linearSlideRight = hardwareMap.get(DcMotor.class, ElectricalContract.rightIntakeSlideMotor());
        linearSlideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // Reset the motor encoder
        linearSlideRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // using external pid
//        linearSlideRight.setTargetPosition(0);
        linearSlideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        linearSlideRight.setDirection(Constants.leftIntakeMotorDirection.inverted());
    }

    public void slide(Telemetry telemetry, SlidePositions position) {

        // retrieve targetPosition based on position parameter
        switch (position) {
            case READY:
                targetPosition = Constants.readySlideExtension;
                break;
            case INTAKE:
                targetPosition = Constants.intakeSlideExtension;
                break;
            case SPECIMEN_INTAKE:
                targetPosition = Constants.specimenIntakeSlideExtension;
                break;
            case AUTO_INTAKE:
                targetPosition = Constants.autoIntakeSlideExtension;
                break;
            case HOMED:
                targetPosition = 0;
                break;
        }

        // set target positions to targetPosition
//        linearSlideLeft.setTargetPosition(targetPosition);
//        linearSlideRight.setTargetPosition(targetPosition);
//        linearSlideLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        linearSlideRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        double power;
        int currentPosition = getCurrentPosition();

        // safety check
        if (currentPosition > MAX_POSITION) {
            power = -0.25; // if slides are exceeding its max position send some negative power to move it down
        }
        else if (position == SlidePositions.HOMED && currentPosition < targetPosition + killPowerThreshold && currentPosition > targetPosition - killPowerThreshold) {
            power = 0; // if slides are in threshold for homed position, then kill power
        }
        else {
            power = slidePID.pidfControl(getCurrentPosition(), targetPosition, 50, 0.09);
        }

        // finally set power
        linearSlideLeft.setPower(power);
        linearSlideRight.setPower(power);

        telemetry.addData("Intake Slide Target Position", targetPosition);
        telemetry.addData("Left Intake Slide Current Position", linearSlideLeft.getCurrentPosition());
        telemetry.addData("Right Intake Slide Current Position", linearSlideRight.getCurrentPosition());
        telemetry.addData("Intake SLide Motor Power", power);
        telemetry.update();
    }

    // returns average position of both slides
    public int getCurrentPosition() {
        return (linearSlideLeft.getCurrentPosition() + linearSlideRight.getCurrentPosition()) / 2;
    }

}
