package org.firstinspires.ftc.teamcode.test;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.vision.AprilTagLocalizer;
import org.firstinspires.ftc.vision.VisionPortal;

@TeleOp
public class AprilTagTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        AprilTagLocalizer aprilTagLocalizer = new AprilTagLocalizer(hardwareMap);

        while (!opModeIsActive()) {
            if (aprilTagLocalizer.getVisionPortal().getCameraState() == VisionPortal.CameraState.STREAMING) {
                telemetry.addData("Camera", "Ready");
                telemetry.update();
            } else {
                telemetry.addData("Camera", "Waiting");
                telemetry.update();
            }
        }

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            Pose2d pose2d =  aprilTagLocalizer.update(telemetry);

            telemetry.addLine("April Tag Pose2d");
            telemetry.addLine(String.format("%6.1f %6.1f %6.1f  (inch)",
                    pose2d.position.x,
                    pose2d.position.y,
                    pose2d.heading.toDouble()));
            telemetry.update();
        }
    }
}
