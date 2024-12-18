package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.lib.arm.Arm;
import org.firstinspires.ftc.teamcode.lib.drive.JohnMecanumDrive;
import org.firstinspires.ftc.teamcode.lib.intake.Intake;

@TeleOp(name = "沈小婷", group= "Linear OpMode")
public class Teleop extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {

        IMU imu = hardwareMap.get(IMU.class, ElectricalContract.imu());
        JohnMecanumDrive drive = new JohnMecanumDrive(hardwareMap);
        Intake intake = new Intake(hardwareMap);
        Arm arm = new Arm(hardwareMap);

        //TODO:
        // IMU adjustments for RevHub orientation
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT));
        imu.initialize(parameters);

        waitForStart();
        if (isStopRequested()) return;

        // reset yaw before tele-op
        imu.resetYaw();

        while (opModeIsActive()) {
            drive.drive(gamepad1, imu, telemetry);
            intake.controls(gamepad2, telemetry, imu);
            arm.controls(gamepad2, telemetry);

            telemetry.update();
        }
    }
}
