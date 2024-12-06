package org.firstinspires.ftc.teamcode.lib;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
@Config
public class Constants {

    // LIST: ARM
    // SUBLIST: CLAW
    public static Servo.Direction armClawDirection = Servo.Direction.REVERSE;
    public static double armClawOpenPosition = 0.5;
    public static double armClawClosedPosition = 0.84;

    // SUBLIST: WRIST
    public static Servo.Direction wristDirection = Servo.Direction.REVERSE;
    public static double wristTransferPosition = 0.05;
    public static double wristIntakeSpecimenPosition = 0.46;
    public static double wristHighSpecimenPosition = 0.25;
    public static double wristLowSpecimenPosition =  0.40;
    public static double wristScorePosition = 0.61;

    // SUBLIST: ROTATION
    public static Servo.Direction leftArmRotationDirection = Servo.Direction.REVERSE;
    public static Servo.Direction rightArmRotationDirection = Servo.Direction.FORWARD;
    public static double basketArmPosition = 0.67;
    public static double specimenIntakeArmPosition = 0.14;
    public static double specimenHighArmPosition = 0.8;
    public static double specimenLowArmPosition = 0.45;
    public static double transferArmPosition = 0.25;

    // SUBLIST: SLIDES
    public static int maxArmSlideExtension = 3000;
    public static DcMotorSimple.Direction leftArmSlideDirection = DcMotorSimple.Direction.REVERSE;
    public static int highBasketSlideExtension = 2700;
    public static int lowBasketSlideExtension = 650;
    public static int intakeSpecimenSlideExtension = 340;
    public static int highSpecimenSlideExtension = 0;
    public static int lowSpecimenSlideExtension = 400;
    public static int hangSlideExtension = 536; // TODO: No hanging yet
    public static int homedSlideExtension = 400;
    public static int transferSlideExtension =  400;
    public static int autoTransferSlideExtension = 350;
    public static int scoringExtensionThreshold = 150;
    public static int liftExtension = 200;


    // LIST: INTAKE
    // SUBLIST: CLAW
    public static Servo.Direction intakeClawDirection = Servo.Direction.FORWARD; // TODO: Find
    public static double intakeClawOpenPosition = 0.5; // TODO: Find
    public static double intakeClawClosedPosition = 0.0; // TODO: Find

    // SUBLIST: CLAW SWIVEL
    public static Servo.Direction intakeClawSwivelDirection = Servo.Direction.FORWARD; // TODO: Find
    public static double ICSTransferPosition = 0.5; // TODO: Find
    public static double ICSChangePosition = 0; // TODO: Find


    // SUBLIST: PIVOT
    public static Servo.Direction leftPivotDirection = Servo.Direction.FORWARD;
    public static Servo.Direction rightPivotDirection = Servo.Direction.REVERSE;
    public static double homedPivotPosition = 0.21;
    public static double deployedPivotPosition = 0.9;

    // SUBLIST: SLIDES
    public static int maxIntakeSlideExtension = 1450;
    public static double intakeSlidePower = 0.65;
    public static DcMotorSimple.Direction leftIntakeMotorDirection = DcMotorSimple.Direction.FORWARD;
    public static int readySlideExtension = 400;
    public static int intakeSlideExtension = 1000;
    public static int autoIntakeSlideExtension = 900;
    public static int homedKillPowerThreshold = 30;
    public static int readyExtensionThreshold = 400;

    // LIST: DRIVE
    public static double precisionModeLimit = 0.5;
    public static DcMotorSimple.Direction leftDriveMotorDirection = DcMotorSimple.Direction.REVERSE;
}
