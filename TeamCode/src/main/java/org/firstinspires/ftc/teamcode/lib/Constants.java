package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public class Constants {

    // LIST: ARM
    // SUBLIST: GRABBER
    public static Servo.Direction getGrabberDirection() {
        return Servo.Direction.REVERSE; // TODO: Test direction
    }
    public static double getOpenPosition() {
        return 0.25; // TODO: Tune
    }
    public static double getClosedPosition() {
        return 0.5; // TODO: Tune
    }

    // SUBLIST: ROTATION
    public static Servo.Direction getLeftArmRotationDirection() {
        return Servo.Direction.REVERSE;
    }
    public static Servo.Direction getRightArmRotationDirection() {
        return Servo.Direction.FORWARD;
    }
    public static double getBasketArmPosition() {
        return 0.06; // TODO: Tune
    }
    public static double getSpecimenIntakeArmPosition() {
        return 0.58; // TODO: Tune
    }
    public static double getSpecimenHighArmPosition() {
        return 0.45;
    }
    public static double getSpecimenLowArmPosition() {
        return 0.64;
    }
    public static double getTransferArmPosition() {
        return 0.64; // TODO: Tune
    }

    // SUBLIST: SLIDES
    public static int getMaxArmSlideExtension() {
        return 3000; // TODO: Find extension
    }
    public static DcMotorSimple.Direction getLeftArmSlideDirection() {
        return DcMotorSimple.Direction.REVERSE; // TODO: Find direction
    }
    public static int getHighBasketSlideExtension() {
        // value should be set to ~2700, lower number right now because it's hella loud in my house
        return 2900; // TODO: Tune
    }
    public static int getLowBasketSlideExtension() {
        return 750; // TODO: Tune
    }
    public static int getIntakeSpecimenSlideExtension() {
        return 500;
    }
    public static int getHighSpecimenSlideExtension() {
        return 1000; // TODO: Tune
    }
    public static int getLowSpecimenSlideExtension() {
        return 230; // TODO: Tune
    }
    public static int getHangSlideExtension() {
        return 536; // TODO: Tune
    }
    public static int getHomedSlideExtension() {
        return 536; // TODO: Tune
    }
    public static int getTransferSlideExtension() {
        return 200; // TODO: Tune
    }
    public static int getScoringExtensionThreshold() {
        return 40; // TODO: Find extension
    }
    public static int getLiftExtension() {
        return 500;
    }

    // SUBLIST: WRIST
    public static Servo.Direction getWristDirection() {
        return Servo.Direction.REVERSE; // TODO: Test direction
    }
    public static double getWristTransferPosition() {
        return 0.3; // TODO: Tune
    }
    public static double getWristIntakeSpecimenPosition() {
        return 0.5;
    }
    public static double getWristHighSpecimenPosition() {
        return 0.45;
    }
    public static double getWristLowSpecimenPosition() {
        return 0.63; // TODO: Tune
    }
    public static double getWristScorePosition() {
        return 0.7; // TODO: Tune
    }

    // LIST: INTAKE
    // SUBLIST: WHEELS
    public static DcMotorSimple.Direction getLeftWheelDirection() {
        return DcMotorSimple.Direction.REVERSE; // TODO: Test direction
    }
    public static DcMotorSimple.Direction getRightWheelDirection() {
        return DcMotorSimple.Direction.FORWARD; // TODO: Test direction
    }
    public static double getIntakePower() {
        return 1; // TODO: Tune
    }
    public static double getEjectPower() {
        return -1; // TODO: Tune
    }

    // SUBLIST: PIVOT
    public static Servo.Direction getLeftPivotDirection() {
        return Servo.Direction.FORWARD; // TODO: Test direction
    }
    public static Servo.Direction getRightPivotDirection() {
        return Servo.Direction.REVERSE; // TODO: Test direction
    }
    public static double getHomedPivotPosition() {
        return 0.3; // TODO: This should be the homed position if we zero, but double check FIXME: this might be different for left and right servos
    }
    public static double getDeployedPivotPosition() {
        return 0.57; // TODO: Tune FIXME: this might be different for left and right servos
    }

    // SUBLIST: SLIDES
    public static int getMaxIntakeSlideExtension() {
        return 1350; // TODO: Find extension
    }
    public static double getIntakeSlidePower() {
        return 0.65; // TODO: Tune
    }
    public static DcMotorSimple.Direction getLeftIntakeMotorDirection() {
        return DcMotorSimple.Direction.FORWARD; // TODO: Find direction
    }
    public static int getReadySlideExtension() {
        return 700; // TODO: Find extension
    }
    public static int getIntakeSlideExtension() {
        return 1000; // TODO: Find Extension
    }
    public static int getHomedKillPowerThreshold() {
        return 30; // TODO: Find extension
    }
    public static int getReadyExtensionThreshold() {
        return 300; // TODO: Find extension
    }

    // LIST: DRIVE
    public static double getPrecisionModeLimit() {
        return 0.5; // TODO: find these values for MecanumDrive
    }
    public static DcMotorSimple.Direction getLeftDriveMotorDirection() {
        return DcMotorSimple.Direction.REVERSE; // TODO: Find direction
    }
}
