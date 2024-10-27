package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.ButtonToggle;
import org.firstinspires.ftc.teamcode.lib.Constants;
import org.firstinspires.ftc.teamcode.lib.arm.Arm;
import org.firstinspires.ftc.teamcode.lib.arm.ArmClaw;
import org.firstinspires.ftc.teamcode.lib.arm.ArmRotation;
import org.firstinspires.ftc.teamcode.lib.arm.ArmWrist;
import org.firstinspires.ftc.teamcode.lib.intake.IntakePivot;
import org.firstinspires.ftc.teamcode.lib.intake.IntakeWheels;

@TeleOp(name = "TelemetryTest", group= "Linear OpMode")
public class TelemetryTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        // LIST: SLIDES
        // intake slides
        DcMotor intakeSlideLeft = hardwareMap.get(DcMotor.class, ElectricalContract.leftIntakeSlideMotor());
        DcMotor intakeSlideRight = hardwareMap.get(DcMotor.class, ElectricalContract.rightIntakeSlideMotor());
        intakeSlideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeSlideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeSlideLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeSlideRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeSlideLeft.setDirection(Constants.getLeftIntakeMotorDirection());
        intakeSlideRight.setDirection(Constants.getLeftIntakeMotorDirection().inverted());

        // arm slides
        DcMotor armSlideLeft = hardwareMap.get(DcMotor.class, ElectricalContract.leftArmSlideMotor());
        DcMotor armSlideRight = hardwareMap.get(DcMotor.class, ElectricalContract.rightArmSlideMotor());
        armSlideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armSlideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armSlideLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armSlideRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armSlideLeft.setDirection(Constants.getLeftArmSlideDirection());
        armSlideRight.setDirection(Constants.getLeftArmSlideDirection().inverted());

//        // LIST: INTAKE SUBCOMPONENTS
//        // intake pivot
        IntakePivot intakePivot = new IntakePivot(hardwareMap);
//
//        // intake wheels
        IntakeWheels intakeWheels = new IntakeWheels(hardwareMap);
//
//        // LIST: ARM SUBCOMPONENTS
//        // arm claw
//        ArmClaw armClaw = new ArmClaw(hardwareMap);

//        // arm rotation
//        ArmRotation armRotation = new ArmRotation(hardwareMap);
////
////        // arm wrist
//        ArmWrist armWrist = new ArmWrist(hardwareMap);
////
//        ButtonToggle tester = new ButtonToggle();
//        ButtonToggle testerTwo = new ButtonToggle();
        ButtonToggle testerThree = new ButtonToggle();



        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            // intake slide ticks
            telemetry.addData("Left Intake Slide Current Position", intakeSlideLeft.getCurrentPosition());
            telemetry.addData("Right Intake Slide Current Position", intakeSlideRight.getCurrentPosition());

            // arm slide ticks
            telemetry.addData("Left Arm Slide Current Position", armSlideLeft.getCurrentPosition());
            telemetry.addData("Right Arm Slide Current Position", armSlideRight.getCurrentPosition());

            // test non-continuous servos with tester button (put one of the servo objects into the if else statement to test)
            // TODO: To tune, change the servo positions inside Constants class
//            tester.update(gamepad2.y);
//            testerTwo.update(gamepad2.a);
            testerThree.update(gamepad2.b);
//            if (tester.isToggled()) {
//                armRotation.basketPosition();
//            }
//            else {
//                armRotation.transferPosition();
//            }
//            if (testerTwo.isToggled()) {
//                armWrist.score();
//            }
//            else {
//                armWrist.transfer();
//            }
//            telemetry.addData("Tester Toggle", tester.isToggled());
//
            // test continuous servos (intake wheels) by holding a button
            if (gamepad2.right_trigger > 0.2) {
                intakeWheels.intake();
            }
            else if (gamepad2.left_trigger > 0.2) {
                intakeWheels.eject();
            }
            else {
                intakeWheels.stop();
            }

//            if (testerThree.isToggled()) {
//                intakePivot.deploy();
//            }
//            else {
//                intakePivot.home();
//            }

            telemetry.update();
        }
    }
}
