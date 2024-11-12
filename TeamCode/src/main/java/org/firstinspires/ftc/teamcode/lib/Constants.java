package org.firstinspires.ftc.teamcode.lib;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
@Config
public class Constants {

    // LIST: ARM
    // SUBLIST: GRABBER
    public static Servo.Direction grabberDirection = Servo.Direction.REVERSE;
    public static double openPosition = 0.25;
    public static double closedPosition = 0.5;

    // SUBLIST: ROTATION
    public static Servo.Direction leftArmRotationDirection = Servo.Direction.REVERSE;
    public static Servo.Direction rightArmRotationDirection = Servo.Direction.FORWARD;
    public static double basketArmPosition = 0.74;
    public static double specimenIntakeArmPosition = 0.22;
    public static double specimenHighArmPosition = 0.35;
    public static double specimenLowArmPosition = 0.16;
    public static double transferArmPosition = 0.16;

    // SUBLIST: SLIDES
    public static int maxArmSlideExtension = 3000;
    public static DcMotorSimple.Direction leftArmSlideDirection = DcMotorSimple.Direction.REVERSE;
    public static int highBasketSlideExtension = 2900;
    public static int lowBasketSlideExtension = 750;
    public static int intakeSpecimenSlideExtension = 325;
    public static int highSpecimenSlideExtension = 1000;
    public static int lowSpecimenSlideExtension = 230;
    public static int hangSlideExtension = 536; // TODO: No hanging yet
    public static int homedSlideExtension = 536;
    public static int transferSlideExtension =  210;
    public static int scoringExtensionThreshold = 40;
    public static int liftExtension = 350;

    // SUBLIST: WRIST
    public static Servo.Direction wristDirection = Servo.Direction.REVERSE;
    public static double wristTransferPosition = 0.31;
    public static double wristIntakeSpecimenPosition = 0.54;
    public static double wristHighSpecimenPosition = 0.45;
    public static double wristLowSpecimenPosition =  0.63;
    public static double wristScorePosition = 0.7;

    // LIST: INTAKE
    // SUBLIST: WHEELS
    public static DcMotorSimple.Direction leftWheelDirection = DcMotorSimple.Direction.REVERSE;
    public static DcMotorSimple.Direction rightWheelDirection = DcMotorSimple.Direction.FORWARD;
    public static double intakePower = 1;
    public static double ejectPower = -1;

    // SUBLIST: PIVOT
    public static Servo.Direction leftPivotDirection = Servo.Direction.FORWARD;
    public static Servo.Direction rightPivotDirection = Servo.Direction.REVERSE;
    public static double homedPivotPosition = 0.66;
    public static double deployedPivotPosition = 0.86;

    // SUBLIST: SLIDES
    public static int maxIntakeSlideExtension = 1400;
    public static double intakeSlidePower = 0.65;
    public static DcMotorSimple.Direction leftIntakeMotorDirection = DcMotorSimple.Direction.FORWARD;
    public static int readySlideExtension = 400;
    public static int intakeSlideExtension = 1300;
    public static int homedKillPowerThreshold = 30;
    public static int readyExtensionThreshold = 300;

    // LIST: DRIVE
    public static double precisionModeLimit = 0.5;
    public static DcMotorSimple.Direction leftDriveMotorDirection = DcMotorSimple.Direction.REVERSE;
}
