package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.lib.drive.JohnMecanumDrive;
import org.firstinspires.ftc.teamcode.vision.AprilTagLocalizer;
import org.firstinspires.ftc.teamcode.vision.AprilTagMultiPortal;
import org.firstinspires.ftc.vision.VisionPortal;

@TeleOp(name = "TeleOp", group= "Linear OpMode")
public class Teleop extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {

        IMU imu = hardwareMap.get(IMU.class, ElectricalContract.imu());
        AprilTagMultiPortal aprilTagMultiPortal = new AprilTagMultiPortal(ElectricalContract.webcam1(),
                ElectricalContract.webcam2(), hardwareMap);

                JohnMecanumDrive drive = new JohnMecanumDrive(hardwareMap, DcMotorSimple.Direction.REVERSE, aprilTagMultiPortal);
//        AprilTagLocalizer aprilTagLocalizer = new AprilTagLocalizer(hardwareMap);


        // IMU adjustments for RevHub orientation
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        imu.initialize(parameters);

        while (!opModeIsActive()) {
//            if (aprilTagLocalizer.getVisionPortal().getCameraState() == VisionPortal.CameraState.STREAMING) {
//                telemetry.addData("Camera", "Ready");
//                telemetry.update();
//            }
//            else {
//                telemetry.addData("Camera", "Waiting");
//                telemetry.update();
//            }
        }

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            drive.drive(gamepad1, imu, telemetry);
//            aprilTagLocalizer.telemetryAprilTag(telemetry);
            aprilTagMultiPortal.telemetryAprilTag(telemetry);

            telemetry.update();
        }
    }
}
