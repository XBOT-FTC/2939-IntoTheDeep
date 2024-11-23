package org.firstinspires.ftc.teamcode.auto;


import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.arm.ArmClaw;
import org.firstinspires.ftc.teamcode.lib.arm.ArmRotation;
import org.firstinspires.ftc.teamcode.lib.arm.ArmSlide;
import org.firstinspires.ftc.teamcode.lib.arm.ArmWrist;
import org.firstinspires.ftc.teamcode.lib.intake.IntakePivot;
import org.firstinspires.ftc.teamcode.lib.intake.IntakeSlide;
import org.firstinspires.ftc.teamcode.lib.intake.IntakeWheels;
import org.firstinspires.ftc.teamcode.rr.MecanumDrive;


@Autonomous(name= "SpecimenAuto", group="Autonomous")
public class SpecimenAuto extends LinearOpMode {
    ArmSlide armSlide;
    ArmRotation rotation;
    ArmClaw grabber;
    ArmWrist wrist;
    IntakePivot pivot;
    IntakeWheels intakeWheels;
    ArmSlide.SlidePositions armSlidePosition;
    IntakeSlide intakeSlide;
    IntakeSlide.SlidePositions intakeSlidePosition;

    @Override
    public void runOpMode() {
        armSlide = new ArmSlide(hardwareMap);
        rotation = new ArmRotation(hardwareMap);
        grabber =  new ArmClaw(hardwareMap);
        wrist = new ArmWrist(hardwareMap);
        pivot = new IntakePivot(hardwareMap);
        armSlidePosition = ArmSlide.SlidePositions.HOMED;
        intakeWheels = new IntakeWheels(hardwareMap);
        intakeSlide = new IntakeSlide(hardwareMap);
        intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;

        Pose2d startingPose = new Pose2d(9, -62, Math.toRadians(0));

        MecanumDrive drive = new MecanumDrive(hardwareMap, startingPose);

        Action trajectoryAction = drive.actionBuilder(drive.pose)

                // drive to chamber
                .setTangent(90)
                .splineToSplineHeading(new Pose2d(6, -40, Math.toRadians(90)), Math.toRadians(90))
//                .afterTime(0, new SequentialAction(
//                        new InstantAction(() -> {
//                            armSlidePosition = ArmSlide.SlidePositions.LIFT_HIGH_SPECIMEN;
//                        }),
//                        new InstantAction(() -> {
//                            rotation.specimenHighPosition();
//                        }),
//                        new InstantAction(() -> {
//                            wrist.scoreHighSpecimen();
//                        }))
//                )
                .waitSeconds(0.5)
                .lineToY(-34)

                // lower arm to score preloaded specimen
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.LOW_BASKET;
                        }),
                        new InstantAction(() -> {
                            rotation.specimenIntakePosition();
                        }),
                        new InstantAction(() -> {
                            wrist.intakeSpecimen();
                        }))
                )
                .afterTime(1.25, new InstantAction(() -> {
                            grabber.open();
                        })
                )
                .waitSeconds(2)

                // drive to sample push position
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(37, -35, Math.toRadians(270)), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(50, -10), Math.toRadians(0))

                // lower pivot to push sample
                .afterTime(0, new InstantAction(() -> {
                    pivot.deploy();
                }))

                // push sample
                .setReversed(false)
                .lineToY(-57)

                // drive to sample push position
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(61, -10), Math.toRadians(0))

                // push sample
                .setReversed(false)
                .lineToY(-57)

                // raise pivot to start scoring specimens
                .afterTime(0, new InstantAction(() -> {
                    pivot.home();
                }))

                // drive to intake specimen #1
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(44, -56), Math.toRadians(270))

                //open grabber
                .afterTime(0, new SequentialAction(
                        new InstantAction(() -> {
                            grabber.open();
                        }),
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.INTAKE_SPECIMEN;
                        })
                        )

                )
                .waitSeconds(0.5)

                .lineToY(-62)
                .waitSeconds(1)

                // intake specimen
                .afterTime(0, new InstantAction(() -> {
                            grabber.close();
                        })
                )

                // raise specimen off field wall
                .afterTime(1, new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.LIFT_INTAKE_SPECIMEN;
                        })
                )

                // lower arm back down
                .afterTime(2, new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.INTAKE_SPECIMEN;
                        })
                )

                // drive and align to  chamber
                .setTangent(Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(6, -40, Math.toRadians(90)), Math.toRadians(90))
                .waitSeconds(1)

                // raise arm to score specimen
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.LIFT_HIGH_SPECIMEN;
                        }),
                        new InstantAction(() -> {
                            rotation.specimenHighPosition();
                        }),
                        new InstantAction(() -> {
                            wrist.scoreHighSpecimen();
                        }))
                )

                .lineToY(-34)

                // lower arm to score specimen
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.LOW_BASKET;
                        }),
                        new InstantAction(() -> {
                            rotation.specimenIntakePosition();
                        }),
                        new InstantAction(() -> {
                            wrist.intakeSpecimen();
                        }))
                )
                .afterTime(1.25, new InstantAction(() -> {
                            grabber.open();
                        })
                )

                // zero mechanisms to end auto
                .afterTime(0, new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.ZERO;
                }))
                .waitSeconds(2)

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
                                armSlideUpdate(telemetry),
                                intakeSlideUpdate(telemetry)
                        )
                )

        );

    }


    public Action armSlideUpdate(Telemetry telemetry) {
        return new Action() {
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    initialized = true;
                }
                armSlide.slide(telemetry, armSlidePosition);

                double pos = armSlide.getCurrentPosition();
                packet.put("slidePosition", pos);
                return true;
            }
        };
    }

    public Action intakeSlideUpdate(Telemetry telemetry) {
        return new Action() {
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    initialized = true;
                }
                intakeSlide.slide(telemetry, intakeSlidePosition);
                return true;
            }
        };
    }


    public Action initSystems() {
        return new SequentialAction(
                new InstantAction(() -> {
                    grabber.close();
                }),
                new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.LIFT_HIGH_SPECIMEN;
                }),
                new InstantAction(() -> {
                    rotation.specimenHighPosition();
                }),
                new InstantAction(() -> {
                    wrist.scoreHighSpecimen();
                }),
                new InstantAction(() -> {
                    pivot.home();
                })
        );
    }
}