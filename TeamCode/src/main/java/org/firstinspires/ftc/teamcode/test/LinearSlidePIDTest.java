package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.lib.ScoringSlide;

@TeleOp
public class LinearSlidePIDTest extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {

        ScoringSlide linearSlide = new ScoringSlide(hardwareMap, DcMotorSimple.Direction.FORWARD);

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            linearSlide.slide(gamepad2, telemetry);

            telemetry.update();
        }
    }
}
