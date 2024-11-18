package org.firstinspires.ftc.teamcode.test;


import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.arm.ArmClaw;
import org.firstinspires.ftc.teamcode.lib.arm.ArmRotation;
import org.firstinspires.ftc.teamcode.lib.arm.ArmSlide;
import org.firstinspires.ftc.teamcode.lib.arm.ArmWrist;
import org.firstinspires.ftc.teamcode.lib.intake.IntakePivot;
import org.firstinspires.ftc.teamcode.lib.intake.IntakeWheels;
import org.firstinspires.ftc.teamcode.rr.MecanumDrive;


@Config
@Autonomous(name= "RoadRunnerTest", group="Autonomous")
public class RoadRunnerTest extends LinearOpMode {
    ArmSlide slide;
    ArmRotation rotation;
    ArmClaw grabber;
    ArmWrist wrist;
    IntakePivot pivot;
    IntakeWheels intake;
    ArmSlide.SlidePositions slidePosition;

    @Override
    public void runOpMode() {
        slide = new ArmSlide(hardwareMap);
        rotation = new ArmRotation(hardwareMap);
        grabber =  new ArmClaw(hardwareMap);
        wrist = new ArmWrist(hardwareMap);
        pivot = new IntakePivot(hardwareMap);
        slidePosition = ArmSlide.SlidePositions.HOMED;

        Pose2d startingPose = new Pose2d(-37, -62, Math.toRadians(270));

        MecanumDrive drive = new MecanumDrive(hardwareMap, startingPose);

        Action trajectoryAction = drive.actionBuilder(drive.pose)
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(-48, -50, Math.toRadians(-135)), Math.toRadians(270))
                .waitSeconds(2)
                .afterTime(2, new SequentialAction(
                        new InstantAction(() -> {
                            slidePosition = ArmSlide.SlidePositions.HIGH_BASKET;
                        }),
                        new InstantAction(() -> {
                            rotation.basketPosition();
                        })))
                .afterTime(3, new SequentialAction(
                        new InstantAction(() -> {
                            grabber.open();
                        })))
//                .turnTo(Math.toRadians(270))
//                .waitSeconds(1)
//                .lineToY(-43)
//                .waitSeconds(2)
//                .splineToLinearHeading(new Pose2d(-48, -50, Math.toRadians(-135)), Math.toRadians(270))
                .build();


        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addLine("Some telemetry here.");
            telemetry.update();
        }

        Actions.runBlocking(
                new SequentialAction(
                        initSystems(),
                        new ParallelAction(
                                trajectoryAction,
                                armSlideUpdate(slidePosition, telemetry)
                        )
                )

        );

    }

    public Action armSlideUpdate(ArmSlide.SlidePositions slidePositions, Telemetry telemetry) {
        return new Action() {
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    slide.slide(telemetry, slidePositions);
                    initialized = true;
                }

                double pos = slide.getCurrentPosition();
                packet.put("slidePosition", pos);
                return false;
            }
        };
    }

    public Action initSystems() {
        return new SequentialAction(
                new InstantAction(() -> {
                    slidePosition = ArmSlide.SlidePositions.TRANSFER;
                }),
                new InstantAction(() -> {
                    grabber.close();
                }),
                new InstantAction(() -> {
                    wrist.score();
                }),
                new InstantAction(() -> {
                    pivot.home();
                })
        );
    }
}