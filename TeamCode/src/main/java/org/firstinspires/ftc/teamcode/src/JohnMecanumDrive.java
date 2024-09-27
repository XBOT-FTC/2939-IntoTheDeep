package org.firstinspires.ftc.teamcode.src;

import com.outoftheboxrobotics.photoncore.hardware.motor.PhotonAdvancedDcMotor;
import com.outoftheboxrobotics.photoncore.hardware.motor.PhotonDcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class JohnMecanumDrive {
    PhotonDcMotor leftFrontMotor = null;
    PhotonDcMotor leftBackMotor = null;
    PhotonDcMotor rightFrontMotor = null;
    PhotonDcMotor rightBackMotor = null;
    PhotonAdvancedDcMotor leftFrontDrive = null;
    PhotonAdvancedDcMotor leftBackDrive = null;
    PhotonAdvancedDcMotor rightFrontDrive = null;
    PhotonAdvancedDcMotor rightBackDrive = null;

    private double powerLimit = 1;
    int count = (int) (powerLimit * 10);
    int powerLimitLoop = 0;
    double powerLimitIncrement = 0.1;
    int precisionLoop = 0;
    boolean precisionMode = false;
    double precisionModeLimit = 0;

    public JohnMecanumDrive(HardwareMap hardwareMap) {
        leftFrontMotor = (PhotonDcMotor) hardwareMap.dcMotor.get(ElectricalContract.leftFrontDriveMotor());
        leftBackMotor = (PhotonDcMotor) hardwareMap.dcMotor.get(ElectricalContract.leftBackDriveMotor());
        rightFrontMotor = (PhotonDcMotor) hardwareMap.dcMotor.get(ElectricalContract.rightFrontDriveMotor());
        rightBackMotor = (PhotonDcMotor) hardwareMap.dcMotor.get(ElectricalContract.rightBackDriveMotor());

        leftFrontDrive = new PhotonAdvancedDcMotor(leftFrontMotor);
        leftBackDrive = new PhotonAdvancedDcMotor(leftBackMotor);
        rightFrontDrive = new PhotonAdvancedDcMotor(rightFrontMotor);
        rightBackDrive = new PhotonAdvancedDcMotor(rightBackMotor);

        leftFrontDrive.setCacheTolerance(0.01);
        leftBackDrive.setCacheTolerance(0.01);
        rightFrontDrive.setCacheTolerance(0.01);
        rightBackDrive.setCacheTolerance(0.01);
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
        //TODO: reverse power for left or right motors
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rotateIntent), 1);
        double leftFrontPower = -((rotY + rotX + rotateIntent + headingPower) / denominator);
        double leftBackPower = -((rotY - rotX + rotateIntent + headingPower) / denominator);
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