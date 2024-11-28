//package org.firstinspires.ftc.teamcode.test;
//
//
//import androidx.annotation.NonNull;
//
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import com.acmerobotics.roadrunner.Action;
//import com.acmerobotics.roadrunner.InstantAction;
//import com.acmerobotics.roadrunner.ParallelAction;
//import com.acmerobotics.roadrunner.Pose2d;
//import com.acmerobotics.roadrunner.SequentialAction;
//import com.acmerobotics.roadrunner.Vector2d;
//import com.acmerobotics.roadrunner.ftc.Actions;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.teamcode.lib.arm.ArmClaw;
//import org.firstinspires.ftc.teamcode.lib.arm.ArmRotation;
//import org.firstinspires.ftc.teamcode.lib.arm.ArmSlide;
//import org.firstinspires.ftc.teamcode.lib.arm.ArmWrist;
//import org.firstinspires.ftc.teamcode.lib.intake.IntakePivot;
//import org.firstinspires.ftc.teamcode.lib.intake.IntakeSlide;
//import org.firstinspires.ftc.teamcode.rr.MecanumDrive;
//
//
//@Config
//@Autonomous(name= "RoadRunnerTest", group="Autonomous")
//public class RoadRunnerTest extends LinearOpMode {
//    ArmSlide slide;
//    ArmRotation rotation;
//    ArmClaw grabber;
//    ArmWrist wrist;
//    IntakePivot pivot;
//    IntakeWheels intakeWheels;
//    ArmSlide.SlidePositions slidePosition;
//    IntakeSlide intakeSlide;
//    IntakeSlide.SlidePositions intakeSlidePosition;
//
//    @Override
//    public void runOpMode() {
//        slide = new ArmSlide(hardwareMap);
//        rotation = new ArmRotation(hardwareMap);
//        grabber =  new ArmClaw(hardwareMap);
//        wrist = new ArmWrist(hardwareMap);
//        pivot = new IntakePivot(hardwareMap);
//        slidePosition = ArmSlide.SlidePositions.HOMED;
//        intakeWheels = new IntakeWheels(hardwareMap);
//        intakeSlide = new IntakeSlide(hardwareMap);
//        intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;
//
//        Pose2d startingPose = new Pose2d(-37, -62, Math.toRadians(90));
//
//        MecanumDrive drive = new MecanumDrive(hardwareMap, startingPose);
//
//        Action trajectoryAction = drive.actionBuilder(drive.pose)
//                .setReversed(true)
//                .lineToY(-56)
//                .splineToLinearHeading(new Pose2d(-50, -52, Math.toRadians(45)), Math.toRadians(90))
//                .afterTime(0,
//                        new InstantAction(() -> {
//                            slidePosition = ArmSlide.SlidePositions.HIGH_BASKET;
//                        }))
//                .afterTime(1, new SequentialAction(
//                        new InstantAction(() -> {
//                            rotation.basketPosition();
//                        }),
//                        new InstantAction(() -> {
//                            wrist.score();
//                        }))
//                )
//                .afterTime(2.5, new SequentialAction(
//                        new InstantAction(() -> {
//                            grabber.open();
//                        })))
//                .afterTime(3.5, new SequentialAction(
//                        new InstantAction(() -> {
//                            rotation.transferPosition();
//                        }),
//                        new InstantAction(() -> {
//                            slidePosition = ArmSlide.SlidePositions.TRANSFER;
//                        })))
//                .waitSeconds(1)
//                .turnTo(Math.toRadians(90))
//                .strafeTo(new Vector2d(-47, -52))
//                .waitSeconds(1)
//                .afterTime(1, new SequentialAction(
//                        new InstantAction(() -> {
//                            intakeSlidePosition = IntakeSlide.SlidePositions.INTAKE;
//                        }),
//                        new InstantAction(() -> {
//                            pivot.deploy();
//                        }),
//                        new InstantAction(() -> {
//                            intakeWheels.intake();
//                        })))
//                .waitSeconds(1)
//                .lineToY(-41)
//                .afterTime(2, new SequentialAction(
//                        new InstantAction(() -> {
//                            intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;
//                        }),
//                        new InstantAction(() -> {
//                            intakeWheels.stop();
//                        })))
//                .build();
//
//
//        while (!isStopRequested() && !opModeIsActive()) {
//            telemetry.addLine("Some telemetry here.");
//            telemetry.update();
//        }
//
//        Actions.runBlocking(
//                new SequentialAction(
//                        initSystems(),
//                        new ParallelAction(
//                                trajectoryAction,
//                                armSlideUpdate(telemetry),
//                                intakeSlideUpdate(telemetry)
//                        )
//                )
//
//        );
//
//    }
//
//    public Action armSlideUpdate(Telemetry telemetry) {
//        return new Action() {
//            private boolean initialized = false;
//            @Override
//            public boolean run(@NonNull TelemetryPacket packet) {
//                if (!initialized) {
//                    initialized = true;
//                }
//                slide.slide(telemetry, slidePosition);
//
//                double pos = slide.getCurrentPosition();
//                packet.put("slidePosition", pos);
//                return true;
//            }
//        };
//    }
//
//    public Action intakeSlideUpdate(Telemetry telemetry) {
//        return new Action() {
//            private boolean initialized = false;
//            @Override
//            public boolean run(@NonNull TelemetryPacket packet) {
//                if (!initialized) {
//                    initialized = true;
//                }
//                intakeSlide.slide(telemetry, intakeSlidePosition);
//                return true;
//            }
//        };
//    }
//
//    public Action initSystems() {
//        return new SequentialAction(
//                new InstantAction(() -> {
//                    grabber.close();
//                }),
//                new InstantAction(() -> {
//                    slidePosition = ArmSlide.SlidePositions.TRANSFER;
//                }),
//                new InstantAction(() -> {
//                    rotation.transferPosition();
//                }),
//                new InstantAction(() -> {
//                    wrist.transfer();
//                }),
//                new InstantAction(() -> {
//                    pivot.home();
//                })
//        );
//    }
//}