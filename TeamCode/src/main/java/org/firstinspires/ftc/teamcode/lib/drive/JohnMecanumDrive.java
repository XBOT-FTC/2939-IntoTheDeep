package org.firstinspires.ftc.teamcode.lib.drive;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.ButtonToggle;
import org.firstinspires.ftc.teamcode.lib.Constants;
import org.firstinspires.ftc.teamcode.lib.PIDManager;

@Config
public class JohnMecanumDrive {
    private final DcMotorEx leftFrontDrive;
    private final DcMotorEx leftBackDrive;
    private final DcMotorEx rightFrontDrive;
    private final DcMotorEx rightBackDrive;
    private final double precisionModeLimit = Constants.precisionModeLimit;
    ButtonToggle precisionModeToggle =  new ButtonToggle();

    public static double p = 0.008, i = 0, d = 0;

    public JohnMecanumDrive(HardwareMap hardwareMap) {
        leftFrontDrive  = hardwareMap.get(DcMotorEx.class, ElectricalContract.leftFrontDriveMotor());
        leftBackDrive = hardwareMap.get(DcMotorEx.class, ElectricalContract.leftBackDriveMotor());
        rightFrontDrive  = hardwareMap.get(DcMotorEx.class, ElectricalContract.rightFrontDriveMotor());
        rightBackDrive = hardwareMap.get(DcMotorEx.class, ElectricalContract.rightBackDriveMotor());

        leftFrontDrive.setDirection(Constants.leftDriveMotorDirection);
        leftBackDrive.setDirection(Constants.leftDriveMotorDirection);
        rightFrontDrive.setDirection(Constants.leftDriveMotorDirection.inverted());
        rightBackDrive.setDirection(Constants.leftDriveMotorDirection.inverted());
    }

    public void drive(Gamepad gamepad, IMU imu, Telemetry telemetry) {
        PIDManager quickRotatePID = new PIDManager(p,i,d);


        // get the translation intent from joystick
        double y = DriveLogic.exponentAndRetainSign(-gamepad.left_stick_y, 3);
        double x = DriveLogic.exponentAndRetainSign(gamepad.left_stick_x, 3);

        // get rotation intent from triggers
        double rotateIntent = DriveLogic.exponentAndRetainSign(-gamepad.left_trigger, 3) +
                DriveLogic.exponentAndRetainSign(gamepad.right_trigger, 3);

        // rightStick quick rotate
        double quickRotateY = -gamepad.right_stick_y;
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
        double headingPower = DriveLogic.getQuickRotatePower(quickRotateY, quickRotateX, botHeadingDegrees, quickRotatePID, telemetry);
        if (gamepad.y) {
            headingPower += DriveLogic.getQuickRotateToHeadingPower(botHeadingDegrees, -45, quickRotatePID, telemetry);
        }

//        precisionModeToggle.update(gamepad.a);

        // Normalize wheel powers to be less than 1.0
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rotateIntent), 1);
        double leftFrontPower = ((rotY + rotX + rotateIntent + headingPower) / denominator);
        double leftBackPower = ((rotY - rotX + rotateIntent + headingPower) / denominator);
        double rightFrontPower = ((rotY - rotX - rotateIntent - headingPower) / denominator);
        double rightBackPower = ((rotY + rotX - rotateIntent - headingPower) / denominator);

        if (gamepad.a) {
            leftFrontDrive.setPower(leftFrontPower *= precisionModeLimit);
            leftBackDrive.setPower(leftBackPower *= precisionModeLimit);
            rightFrontDrive.setPower(rightFrontPower *= precisionModeLimit);
            rightBackDrive.setPower(rightBackPower *= precisionModeLimit);
        } else {
            leftFrontDrive.setPower(leftFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightFrontDrive.setPower(rightFrontPower);
            rightBackDrive.setPower(rightBackPower);
        }

        telemetry.addData("Front Motor Powers:", "Left Front (%.2f), Right Front (%.2f)", leftFrontPower, rightFrontPower);
        telemetry.addData("Back Motor Powers:", "Left Back (%.2f), Right Back (%.2f)", leftBackPower, rightBackPower);
        telemetry.addData("current heading:", botHeadingDegrees);
    }

    // auto movement methods
    public void strafe (double speed, String direction) {
        if (direction.equals("left")) {
            drivesSetPowerX(speed);
        }
        else {
            drivesSetPowerX(-speed);
        }
    }
    public void forwardBackwards (double speed, String direction) {
        if (direction.equals("forward")) {
            drivesSetPowerY(speed);
        }
        else {
            drivesSetPowerY(speed);
        }
    }
    public void drivesSetPowerY(double speed) {
        leftFrontDrive.setPower(speed);
        leftBackDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        rightBackDrive.setPower(speed);
    }
    public void drivesSetPowerX(double speed) {
        leftFrontDrive.setPower(-speed);
        leftBackDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        rightBackDrive.setPower(-speed);
    }

}