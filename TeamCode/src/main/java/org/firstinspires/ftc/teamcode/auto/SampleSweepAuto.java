package org.firstinspires.ftc.teamcode.auto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.lib.drive.JohnMecanumDrive;

@Autonomous(name="SampleSweepAuto", group="Auto")
public class SampleSweepAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        JohnMecanumDrive drive = new JohnMecanumDrive(hardwareMap);
        waitForStart();

        drive.strafe(0.5, "right");
        sleep(3000);

        drive.forwardBackwards(0.7, "forward");
        sleep(3000);
        drive.strafe(0.5, "right");
        sleep(2000);
        drive.forwardBackwards(0.7, "backwards");
        sleep(3000);
        drive.forwardBackwards(0.7, "forwards");
        sleep(3000);

        drive.strafe(0.5, "right");
        sleep(2000);
        drive.forwardBackwards(0.7, "backwards");
        sleep(3000);
        drive.forwardBackwards(0.7, "forwards");
        sleep(3000);

        drive.strafe(0.5, "right");
        sleep(2000);
        drive.forwardBackwards(0.7, "backwards");
        sleep(3000);
        drive.forwardBackwards(0.7, "forwards");
        sleep(3000);
    }
}