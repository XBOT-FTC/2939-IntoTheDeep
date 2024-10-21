package org.firstinspires.ftc.teamcode.lib.drive;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.PoseSubsystem;
import org.firstinspires.ftc.teamcode.lib.PIDManager;

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
    double precisionModeLimit = 0.5;
    PIDManager quickRotatePID = new PIDManager(0,0,0);
    PIDManager driveToPositionPID = new PIDManager(0,0,0);
    PoseSubsystem pose;

    public JohnMecanumDrive(HardwareMap hardwareMap, DcMotorSimple.Direction direction, PoseSubsystem pose) {
        leftFrontDrive  = hardwareMap.get(DcMotorEx.class, ElectricalContract.LeftFrontDriveMotor());
        leftBackDrive = hardwareMap.get(DcMotorEx.class, ElectricalContract.LeftBackDriveMotor());
        rightFrontDrive  = hardwareMap.get(DcMotorEx.class, ElectricalContract.RightFrontDriveMotor());
        rightBackDrive = hardwareMap.get(DcMotorEx.class, ElectricalContract.RightBackDriveMotor());

        leftFrontDrive.setDirection(direction);
        leftBackDrive.setDirection(direction);
        rightFrontDrive.setDirection(direction.inverted());
        rightBackDrive.setDirection(direction.inverted());

        this.pose = pose;
    }

    public void drive(Gamepad gamepad, IMU imu, Telemetry telemetry) {
        double y = -gamepad.left_stick_y;
        double x = gamepad.left_stick_x;

        // get rotation intent from triggers
        double rotateIntent = -gamepad.left_trigger + gamepad.right_trigger;
        // rightStick quick rotate
        double quickRotateY = -gamepad.right_stick_y;
        double quickRotateX = gamepad.right_stick_x;

        Pose2d currentPose = pose.getPose(telemetry);

        // recalibrate drive
        if (gamepad.options) {
            imu.resetYaw();
        }

        // retrieve heading from pose
        //TODO: check whether pose heading is radians or degrees
        double botHeading = currentPose.heading.toDouble();
        double botHeadingDegrees = Math.toDegrees(botHeading);

        // use pid to calculate power to drive to the Net Zone
        double translationXError = driveToPositionPID.pidControl(currentPose.position.x, PoseSubsystem.netZonePose.position.x);
        double translationYError = driveToPositionPID.pidControl(currentPose.position.y, PoseSubsystem.netZonePose.position.y);
        // use pid to calculate power to drive to the Net Zone
        double rotateError = quickRotatePID.pidControl(currentPose.heading.toDouble(), PoseSubsystem.netZonePose.heading.toDouble());

        // Rotate the movement direction counter to the bot's rotation
        // add power from driveToNetZone
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading) + translationXError;
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading) + translationYError;

        rotX *= 1.1;  // Counteract imperfect strafing

        // get headingPower from drive logic for quick rotate
        // add power from driveToNetZone
        double headingPower = DriveLogic.getQuickRotatePower(quickRotateY, quickRotateX, botHeadingDegrees, quickRotatePID, telemetry) + rotateError;

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