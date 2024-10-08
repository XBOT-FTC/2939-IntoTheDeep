package org.firstinspires.ftc.teamcode.src.lib.drive;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.src.ElectricalContract;

public class JohnMecanumDrive {
    private DcMotorEx leftFrontDrive = null;
    private DcMotorEx leftBackDrive = null;
    private DcMotorEx rightFrontDrive = null;
    private DcMotorEx rightBackDrive = null;
    private double powerLimit = 1;
    int count = (int) (powerLimit * 10);
    int powerLimitLoop = 0;
    double powerLimitIncrement = 0.1;
    int precisionLoop = 0;
    boolean precisionMode = false;
    double precisionModeLimit = 0;

    public JohnMecanumDrive(HardwareMap hardwareMap, DcMotorSimple.Direction direction) {
        leftFrontDrive  = hardwareMap.get(DcMotorEx.class, ElectricalContract.LeftFrontDriveMotor());
        leftBackDrive = hardwareMap.get(DcMotorEx.class, ElectricalContract.LeftBackDriveMotor());
        rightFrontDrive  = hardwareMap.get(DcMotorEx.class, ElectricalContract.RightFrontDriveMotor());
        rightBackDrive = hardwareMap.get(DcMotorEx.class, ElectricalContract.RightBackDriveMotor());

        leftFrontDrive.setDirection(direction);
        leftBackDrive.setDirection(direction);
        rightFrontDrive.setDirection(direction.inverted());
        rightBackDrive.setDirection(direction.inverted());

        encoderSetUp();
    }

    public void drive(Gamepad gamepad, IMU imu, Telemetry telemetry) {
        double y = -gamepad.left_stick_y; // TODO: Reverse y value if needed (change sign)
        double x = gamepad.left_stick_x;

        // get rotation intent from triggers
        double rotateIntent = -gamepad.left_trigger + gamepad.right_trigger;
        // rightStick quick rotate
        double quickRotateY = -gamepad.right_stick_y; // TODO: Reverse y value if needed (change sign)
        double quickRotateX = gamepad.right_stick_x;

        // recalibrate drive
        if (gamepad.options) {
            imu.resetYaw();
        }

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double botHeadingDegrees = Math.toDegrees(botHeading);

        // Rotate the movement direction counter to the bot's rotation
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX *= 1.1;  // Counteract imperfect strafing

        // get headingPower from drive logic for quick rotate
        double headingPower = DriveLogic.getQuickRotatePower(quickRotateY, quickRotateX, botHeadingDegrees, telemetry);

        adjustPowerLimit(gamepad);
        precisionModeSwitch(gamepad);

        // Normalize wheel powers to be less than 1.0
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rotateIntent), 1);
        double leftFrontPower = ((rotY + rotX + rotateIntent + headingPower) / denominator);
        double leftBackPower = ((rotY - rotX + rotateIntent + headingPower) / denominator);
        double rightFrontPower = ((rotY - rotX - rotateIntent - headingPower) / denominator);
        double rightBackPower = ((rotY + rotX - rotateIntent - headingPower) / denominator);

        if (precisionMode) {
            leftFrontDrive.setPower(leftFrontPower *= precisionModeLimit);
            leftBackDrive.setPower(leftBackPower *= precisionModeLimit);
            rightFrontDrive.setPower(rightFrontPower *= precisionModeLimit);
            rightBackDrive.setPower(rightBackPower *= precisionModeLimit);
        } else {
            leftFrontDrive.setPower(leftFrontPower *= powerLimit);
            leftBackDrive.setPower(leftBackPower *= powerLimit);
            rightFrontDrive.setPower(rightFrontPower *= powerLimit);
            rightBackDrive.setPower(rightBackPower *= powerLimit);
        }

        telemetry.addData("Front Motor Powers:", "Left Front (%.2f), Right Front (%.2f)", leftFrontPower, rightFrontPower);
        telemetry.addData("Back Motor Powers:", "Left Back (%.2f), Right Back (%.2f)", leftBackPower, rightBackPower);

        telemetry.addData("current heading:", botHeadingDegrees);

//        telemetry.addData("Left Front Ticks:", "(%.2f)", leftFrontDrive.getCurrentPosition());
//        telemetry.addData("Left Back Ticks:", "(%.2f)", leftBackDrive.getCurrentPosition());
//        telemetry.addData("Right Front Ticks:", "(%.2f)", rightFrontDrive.getCurrentPosition());
//        telemetry.addData("Right Back Ticks:", "(%.2f)", rightBackDrive.getCurrentPosition());
//        telemetry.addData("Left Front Expected Distance Inches: ", "(%.2f)",
//                DriveLogic.getExpectedRobotDistanceInches(leftFrontDrive.getCurrentPosition()));
//        telemetry.addData("Right Front Expected Distance Inches: ", "(%.2f)",
//                DriveLogic.getExpectedRobotDistanceInches(rightFrontDrive.getCurrentPosition()));
    }

    public void setPowerLimit(double max) {
        powerLimit = max;
        count = (int) (powerLimit * 10);
    }
    public void adjustPowerLimit(Gamepad gamepad) {
        if (gamepad.dpad_up) {
            if (count++ <= 10 && powerLimitLoop == 0) {
                count++;
                powerLimitLoop++;
            }
        }
        else if (gamepad.dpad_down) {
            if (count-- >= 0 && powerLimitLoop == 0) {
                count--;
                powerLimitLoop++;
            }
        }
        else {
            powerLimitLoop = 0;
        }
        powerLimit = powerLimitIncrement * count;
    }

    public void encoderSetUp() {
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //TODO: see difference in RUN_USING_ENCODER and RUN_WITHOUT_ENCODER for driving
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void precisionModeSwitch(Gamepad gamepad) {
        if (gamepad.a) {
            if (precisionLoop == 0) {
                precisionMode = !precisionMode;
                precisionLoop++;
            }
        }
        else {
            precisionLoop = 0;
        }
    }
}