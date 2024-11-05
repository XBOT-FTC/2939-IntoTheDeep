package org.firstinspires.ftc.teamcode.test;


import androidx.annotation.NonNull;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.Constants;
import org.firstinspires.ftc.teamcode.lib.arm.ArmClaw;
import org.firstinspires.ftc.teamcode.lib.arm.ArmRotation;
import org.firstinspires.ftc.teamcode.lib.arm.ArmSlide;
import org.firstinspires.ftc.teamcode.lib.arm.ArmWrist;
import org.firstinspires.ftc.teamcode.rr.MecanumDrive;


@Autonomous(name= "RoadRunnerTest", group="Autonomous")
public class RoadRunnerTest extends LinearOpMode {
    ArmSlide slide = new ArmSlide(hardwareMap);
    ArmRotation rotation = new ArmRotation(hardwareMap);
    ArmClaw grabber =  new ArmClaw(hardwareMap);
    ArmWrist wrist = new ArmWrist(hardwareMap);

    public Action slideToHighBasket(Telemetry telemetry) {
        return new Action() {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    slide.slide(telemetry, ArmSlide.SlidePositions.HIGH_BASKET);
                    initialized = true;
                }

                double pos = slide.getCurrentPosition();
                packet.put("slidePosition", pos);
                return pos < Constants.highBasketSlideExtension;
            }
        };
    }

    @Override
    public void runOpMode() {
        Pose2d startingPose = new Pose2d(0,0, 0);

        MecanumDrive drive = new MecanumDrive(hardwareMap, startingPose);

        Action trajectoryAction = drive.actionBuilder(drive.pose)
                .lineToY(37)
                .setTangent(Math.toRadians(0))
                .lineToX(18)
                .waitSeconds(3)
                .setTangent(Math.toRadians(0))
                .lineToXSplineHeading(46, Math.toRadians(180))
                .waitSeconds(3)
                .build();


        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addLine("Some telemetry here.");
            telemetry.update();
        }

        Actions.runBlocking(
                new SequentialAction(
                        trajectoryAction,
                        slideToHighBasket(telemetry),
                        new SleepAction(2),
                        new InstantAction(() -> {
                            rotation.basketPosition();
                        }),
                        new SleepAction(2),
                        new InstantAction(() -> {
                            wrist.score();
                        }),
                        new SleepAction(2),
                        new InstantAction(() -> {
                            grabber.open();
                        })
                        //other Actions here.
                )
        );

    }
}
