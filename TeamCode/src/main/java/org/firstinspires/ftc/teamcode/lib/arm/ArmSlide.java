package org.firstinspires.ftc.teamcode.lib.arm;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.Constants;
import org.firstinspires.ftc.teamcode.lib.PIDManager;

@Config
public class ArmSlide {
    private final DcMotor linearSlideLeft;
    private final DcMotor linearSlideRight;
    public final int MAX_POSITION = Constants.maxArmSlideExtension;
    public final int MIN_POSITION = 0;
    public final double IN_PER_TICK = 0; // TODO: tune
    public int targetPosition = 0;
    private final PIDManager armPID = new PIDManager(0.0035,0,0, 0.0001); // TODO: tune
    public final double positionTolerance = 20; // TODO: tune
    public final double velocityTolerance = 0.09; // TODO: tune
    public final int liftExtension = Constants.liftExtension;
    enum SlidePositions {
        HIGH_BASKET,
        LOW_BASKET,
        LIFT_INTAKE_SPECIMEN,
        LIFT_HIGH_SPECIMEN,
        LIFT_LOW_SPECIMEN,
        INTAKE_SPECIMEN,
        HIGH_SPECIMEN,
        LOW_SPECIMEN,
        HANG,
        HOMED,
        TRANSFER
    }

    public ArmSlide(HardwareMap hardwareMap) {
        // motor for left linear slide setup
        linearSlideLeft = hardwareMap.get(DcMotor.class, ElectricalContract.leftArmSlideMotor());
        linearSlideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearSlideLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // using external pid
        linearSlideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideLeft.setDirection(Constants.leftArmSlideDirection);

        // motor for right linear slide setup
        linearSlideRight = hardwareMap.get(DcMotor.class, ElectricalContract.rightArmSlideMotor());
        linearSlideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearSlideRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); // using external pid
        linearSlideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideRight.setDirection(Constants.leftArmSlideDirection.inverted());
    }

    public void slide(Telemetry telemetry, SlidePositions position) {

        // retrieve targetPosition based on position parameter
        switch (position) {
            case HIGH_BASKET:
                targetPosition = Constants.highBasketSlideExtension;
                break;
            case LOW_BASKET:
                targetPosition = Constants.lowBasketSlideExtension;
                break;
            case LIFT_INTAKE_SPECIMEN:
                targetPosition =  Constants.homedSlideExtension + liftExtension;
                break;
            case LIFT_HIGH_SPECIMEN:
                targetPosition =  Constants.highSpecimenSlideExtension + liftExtension;
                break;
            case LIFT_LOW_SPECIMEN:
                targetPosition =  Constants.lowSpecimenSlideExtension + liftExtension;
                break;
            case INTAKE_SPECIMEN:
                targetPosition = Constants.intakeSpecimenSlideExtension;
                break;
            case HIGH_SPECIMEN:
                targetPosition = Constants.highSpecimenSlideExtension;
                break;
            case LOW_SPECIMEN:
                targetPosition = Constants.lowSpecimenSlideExtension;
                break;
            case HANG:
                targetPosition = Constants.hangSlideExtension; // TODO: We don't have hanging
                break;
            case HOMED:
                targetPosition = Constants.homedSlideExtension;
                break;
            case TRANSFER:
                targetPosition = Constants.transferSlideExtension;
                break;
        }

        double power;
        int currentPosition = linearSlideLeft.getCurrentPosition();

        // safety check
        if (currentPosition > MAX_POSITION) {
            power = -0.25; // if slides are exceeding its max position send some negative power to move it down
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

        telemetry.addData("Arm Slide Target Position", targetPosition);
        telemetry.addData("Left Arm Slide Current Position", linearSlideLeft.getCurrentPosition());
        telemetry.addData("Right Arm Slide Current Position", linearSlideRight.getCurrentPosition());
        telemetry.addData("Arm Slide Motor Power", power);
        telemetry.update();
    }

    // returns average position of both slides
    public int getCurrentPosition() {
        return (linearSlideLeft.getCurrentPosition() + linearSlideRight.getCurrentPosition()) / 2;
    }
}
