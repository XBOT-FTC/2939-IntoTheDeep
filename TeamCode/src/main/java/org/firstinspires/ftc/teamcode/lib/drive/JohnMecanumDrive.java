package org.firstinspires.ftc.teamcode.lib.drive;

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

public class JohnMecanumDrive {
    private final DcMotorEx leftFrontDrive;
    private final DcMotorEx leftBackDrive;
    private final DcMotorEx rightFrontDrive;
    private final DcMotorEx rightBackDrive;
    private final double precisionModeLimit = Constants.precisionModeLimit;
    ButtonToggle precisionModeToggle =  new ButtonToggle();
    PIDManager quickRotatePID = new PIDManager(0.018,0,0);

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

        // get the translation intent from joystick
        double y = -(gamepad.left_stick_y * gamepad.left_stick_y);
        double x = gamepad.left_stick_x * gamepad.left_stick_x;

        // get rotation intent from triggers
        double rotateIntent = -(gamepad.left_trigger * gamepad.left_trigger) + (gamepad.right_trigger * gamepad.right_trigger);

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

        precisionModeToggle.update(gamepad.a);

        // Normalize wheel powers to be less than 1.0
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rotateIntent), 1);
        double leftFrontPower = ((rotY + rotX + rotateIntent + headingPower) / denominator);
        double leftBackPower = ((rotY - rotX + rotateIntent + headingPower) / denominator);
        double rightFrontPower = ((rotY - rotX - rotateIntent - headingPower) / denominator);
        double rightBackPower = ((rotY + rotX - rotateIntent - headingPower) / denominator);

        if (precisionModeToggle.isToggled()) {
            leftFrontDrive.setPower(leftFrontPower *= precisionModeLimit);
            leftBackDrive.setPower(leftBackPower *= precisionModeLimit);
            rightFrontDrive.setPower(rightFrontPower *= precisionModeLimit);
            rightBackDrive.setPower(rightBackPower *= precisionModeLimit);
        } else {
            leftFrontDrive.setPower(leftFrontPower *= 0.7);
            leftBackDrive.setPower(leftBackPower *= 0.7);
            rightFrontDrive.setPower(rightFrontPower *= 0.7);
            rightBackDrive.setPower(rightBackPower *= 0.7);
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