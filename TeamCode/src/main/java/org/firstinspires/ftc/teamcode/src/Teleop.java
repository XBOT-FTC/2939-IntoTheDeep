package org.firstinspires.ftc.teamcode.src;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;

import java.util.concurrent.TimeUnit;

@TeleOp(name = "TeleOp", group= "Linear OpMode")
public class Teleop extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {

        IMU imu = hardwareMap.get(IMU.class, ElectricalContract.imu());
        AprilTagMecanumDrive drive = new AprilTagMecanumDrive(hardwareMap, DcMotorSimple.Direction.REVERSE);
        AprilTagLocalizer aprilTagLocalizer = new AprilTagLocalizer(hardwareMap);
        setManualExposure(6, 250, aprilTagLocalizer.getVisionPortal());

        // IMU adjustments for RevHub orientation
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        imu.initialize(parameters);

        while (!opModeIsActive()) {
            if (aprilTagLocalizer.getVisionPortal().getCameraState() == VisionPortal.CameraState.STREAMING) {
                telemetry.addData("Camera", "Ready");
                telemetry.update();
            }
            else {
                telemetry.addData("Camera", "Waiting");
                telemetry.update();
            }
        }

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            drive.drive(gamepad1, imu, telemetry, aprilTagLocalizer.getAprilTag());
            aprilTagLocalizer.telemetryAprilTag(telemetry);


            telemetry.update();
        }
    }

    /*
    Manually set the camera gain and exposure.
    This can only be called AFTER calling initAprilTag(), and only works for Webcams;
    */
    private void setManualExposure(int exposureMS, int gain, VisionPortal visionPortal) {
        // Wait for the camera to be open, then use the controls

        if (visionPortal == null) {
            return;
        }

        // Make sure camera is streaming before we try to set the exposure controls
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting");
            telemetry.update();
            while (!isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
                sleep(20);
            }
            telemetry.addData("Camera", "Ready");
            telemetry.update();
        }

        // Set camera controls unless we are stopping.
        if (!isStopRequested())
        {
            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
                sleep(50);
            }
            exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
            sleep(20);
            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
            gainControl.setGain(gain);
            sleep(20);
        }
    }
}
