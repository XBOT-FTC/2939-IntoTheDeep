package org.firstinspires.ftc.teamcode.auto;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.lib.drive.JohnMecanumDrive;

@Autonomous(name="ObservationZonePark", group="Auto")
public class ObservationZoneParkAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        JohnMecanumDrive drive = new JohnMecanumDrive(hardwareMap);
        waitForStart();

        drive.strafe(0.5, "right");
        sleep(3000);
    }
}