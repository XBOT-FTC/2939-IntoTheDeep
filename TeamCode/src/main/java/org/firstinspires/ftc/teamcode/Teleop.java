package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.drive.JohnMecanumDrive;

@TeleOp(name = "TeleOp", group= "Linear OpMode")
public class Teleop extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {

        IMU imu = hardwareMap.get(IMU.class, ElectricalContract.imu());
        PoseSubsystem pose = new PoseSubsystem(hardwareMap, imu);
        JohnMecanumDrive drive = new JohnMecanumDrive(hardwareMap, DcMotorSimple.Direction.REVERSE, pose);

        // IMU adjustments for RevHub orientation
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        imu.initialize(parameters);

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            drive.drive(gamepad1, imu, telemetry);

            telemetry.update();
        }
    }
}