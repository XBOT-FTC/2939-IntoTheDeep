package org.firstinspires.ftc.teamcode.test;


import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
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
import org.firstinspires.ftc.teamcode.lib.intake.IntakePivot;
import org.firstinspires.ftc.teamcode.rr.MecanumDrive;


@Config
@Autonomous(name= "RoadRunnerTest", group="Autonomous")
public class RoadRunnerTest extends LinearOpMode {
    ArmSlide slide = new ArmSlide(hardwareMap);
    ArmRotation rotation = new ArmRotation(hardwareMap);
    ArmClaw grabber =  new ArmClaw(hardwareMap);
    ArmWrist wrist = new ArmWrist(hardwareMap);
    IntakePivot pivot = new IntakePivot(hardwareMap);
    ArmSlide.SlidePositions slidePosition = ArmSlide.SlidePositions.HOMED;

//    public Action slideToHighBasket(Telemetry telemetry) {
//        return new Action() {
//            private boolean initialized = false;
//
//            @Override
//            public boolean run(@NonNull TelemetryPacket packet) {
//                if (!initialized) {
//                    slide.slide(telemetry, ArmSlide.SlidePositions.HIGH_BASKET);
//                    initialized = true;
//                }
//
//                double pos = slide.getCurrentPosition();
//                packet.put("slidePosition", pos);
//                return pos < Constants.highBasketSlideExtension;
//            }
//        };
//    }

//    public Action armSlideUpdate(ArmSlide.SlidePositions slidePositions, Telemetry telemetry) {
//        return new Action() {
//            private boolean initialized = false;
//            @Override
//            public boolean run(@NonNull TelemetryPacket packet) {
//                if (!initialized) {
//                    slide.slide(telemetry, slidePositions);
//                    initialized = true;
//                }
//
//                double pos = slide.getCurrentPosition();
//                packet.put("slidePosition", pos);
//                return false;
//            }
//        };
//    }

//    public Action initSystems() {
//        return new SequentialAction(
//                new InstantAction(() -> {
//                    pivot.home();
//                }),
//                new InstantAction(() -> {
//                    slidePosition = ArmSlide.SlidePositions.LIFT_HIGH_SPECIMEN;
//                }),
//                new InstantAction(() -> {
//                    wrist.scoreHighSpecimen();
//                }),
//                new InstantAction(() -> {
//                    rotation.specimenHighPosition();
//                })
//        );
//    }

    @Override
    public void runOpMode() {
        Pose2d startingPose = new Pose2d(6, -62, Math.toRadians(270));

        MecanumDrive drive = new MecanumDrive(hardwareMap, startingPose);

        Action trajectoryAction = drive.actionBuilder(drive.pose)
                .setReversed(true)
//                .afterTime(0, new SequentialAction(
//                        new InstantAction(() -> {
//                            slidePosition = ArmSlide.SlidePositions.HIGH_SPECIMEN;
//                        })))
                .waitSeconds(2)
                .lineToY(-33)
                .build();


//        while (!isStopRequested() && !opModeIsActive()) {
//            telemetry.addLine("Some telemetry here.");
//            telemetry.update();
//        }

        Actions.runBlocking(
                new SequentialAction(
//                        initSystems(),
//                        new ParallelAction(
//                                trajectoryAction,
//                                armSlideUpdate(slidePosition, telemetry)
//                        )
                        trajectoryAction
                )

        );

    }
}