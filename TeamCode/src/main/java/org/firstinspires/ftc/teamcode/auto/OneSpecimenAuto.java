//package org.firstinspires.ftc.teamcode.auto;
//
//
//import androidx.annotation.NonNull;
//
//import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
//import com.acmerobotics.roadrunner.Action;
//import com.acmerobotics.roadrunner.InstantAction;
//import com.acmerobotics.roadrunner.ParallelAction;
//import com.acmerobotics.roadrunner.Pose2d;
//import com.acmerobotics.roadrunner.SequentialAction;
//import com.acmerobotics.roadrunner.TranslationalVelConstraint;
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
//@Autonomous(name= "ONE-SpecimenAuto", group="Autonomous")
//public class OneSpecimenAuto extends LinearOpMode {
//    ArmSlide armSlide;
//    ArmRotation rotation;
//    ArmClaw grabber;
//    ArmWrist wrist;
//    IntakePivot pivot;
//    IntakeWheels intakeWheels;
//    ArmSlide.SlidePositions armSlidePosition;
//    IntakeSlide intakeSlide;
//    IntakeSlide.SlidePositions intakeSlidePosition;
//
//    @Override
//    public void runOpMode() {
//        armSlide = new ArmSlide(hardwareMap);
//        rotation = new ArmRotation(hardwareMap);
//        grabber =  new ArmClaw(hardwareMap);
//        wrist = new ArmWrist(hardwareMap);
//        pivot = new IntakePivot(hardwareMap);
//        armSlidePosition = ArmSlide.SlidePositions.HOMED;
//        intakeWheels = new IntakeWheels(hardwareMap);
//        intakeSlide = new IntakeSlide(hardwareMap);
//        intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;
//
//        Pose2d startingPose = new Pose2d(9, -62, Math.toRadians(0));
//
//        MecanumDrive drive = new MecanumDrive(hardwareMap, startingPose);
//
//        Action trajectoryAction = drive.actionBuilder(drive.pose)
//
//                // drive to chamber
//                .setTangent(Math.toRadians(90))
//                .splineToSplineHeading(new Pose2d(6, -37, Math.toRadians(270)), Math.toRadians(90))
//                .waitSeconds(0.5)
//
//                //raise arm
//                .afterTime(0, new InstantAction(() -> {
//                    armSlidePosition = ArmSlide.SlidePositions.LOW_SPECIMEN;
//                }))
//                .waitSeconds(0.5)
//
//                // drive all the way up to the chamber
//                .lineToY(-32, new TranslationalVelConstraint(5))
//
//                // scoring sequence
//                .afterTime(0, new SequentialAction(
//                        new InstantAction(() -> {
//                            rotation.specimenLowPosition();
//                        }),
//                        new InstantAction(() -> {
//                            wrist.scoreLowSpecimen();
//                        }))
//                )
//                .afterTime(1, new SequentialAction(
//                        new InstantAction(() -> {
//                            rotation.specimenIntakePosition();
//                        }),
//                        new InstantAction(() -> {
//                            wrist.intakeSpecimen();
//                        }))
//                )
//                .afterTime(1.1, new InstantAction(() -> {
//                    grabber.open();
//                }))
//                .waitSeconds(1.2)
//
//                // park
//                .splineToLinearHeading(new Pose2d(45, -55, Math.toRadians(90)), Math.toRadians(0))
//
//                // zero mechanisms to end auto
//                .afterTime(0, new InstantAction(() -> {
//                    armSlidePosition = ArmSlide.SlidePositions.ZERO;
//                }))
//                .waitSeconds(2)
//
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
//
//    public Action armSlideUpdate(Telemetry telemetry) {
//        return new Action() {
//            private boolean initialized = false;
//            @Override
//            public boolean run(@NonNull TelemetryPacket packet) {
//                if (!initialized) {
//                    initialized = true;
//                }
//                armSlide.slide(telemetry, armSlidePosition);
//
//                double pos = armSlide.getCurrentPosition();
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
//
//    public Action initSystems() {
//        return new SequentialAction(
//                new InstantAction(() -> {
//                    grabber.close();
//                }),
//                new InstantAction(() -> {
//                    armSlidePosition = ArmSlide.SlidePositions.INTAKE_SPECIMEN;
//                }),
//                new InstantAction(() -> {
//                    rotation.basketPosition();
//                }),
//                new InstantAction(() -> {
//                    wrist.score();
//                }),
//                new InstantAction(() -> {
//                    pivot.home();
//                })
//        );
//    }
//}