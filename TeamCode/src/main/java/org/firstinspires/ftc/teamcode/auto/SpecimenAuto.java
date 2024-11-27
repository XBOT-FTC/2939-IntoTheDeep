package org.firstinspires.ftc.teamcode.auto;


import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
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
                .setTangent(Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(6, -37, Math.toRadians(270)), Math.toRadians(90))
                .waitSeconds(0.5)

                //raise arm
                .afterTime(0, new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.LOW_SPECIMEN;
                }))
                .waitSeconds(0.5)

                // drive all the way up to the chamber
                .lineToY(-32, new TranslationalVelConstraint(5))

                // scoring sequence
                .afterTime(0, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.specimenLowPosition();
                        }),
                        new InstantAction(() -> {
                            wrist.scoreLowSpecimen();
                        }))
                )
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.specimenIntakePosition();
                        }),
                        new InstantAction(() -> {
                            wrist.intakeSpecimen();
                        }))
                )
                .afterTime(1.1, new InstantAction(() -> {
                    grabber.open();
                }))
                .waitSeconds(1.2)

                // drive to sample push position
                .splineToLinearHeading(new Pose2d(36, -35, Math.toRadians(180)), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(47, -10), Math.toRadians(0))

                // push sample
                .strafeTo(new Vector2d(47, -57))

                // drive to sample push position
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(58, -10), Math.toRadians(0))

                // push sample
                .strafeTo(new Vector2d(58, -57))





                // drive to intake specimen #1
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(44, -52, Math.toRadians(270)), Math.toRadians(180))
                .afterTime(0, new InstantAction(() -> {
                            grabber.open();
                        })
                )
                .waitSeconds(0.5)
                .lineToY(-62)
                .waitSeconds(0.01)


                .afterTime(0, new InstantAction(() -> {
                            grabber.close();
                        })
                )
                .afterTime(0.5, new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.LIFT_INTAKE_SPECIMEN;
                        })
                )
                // prepare to score specimen
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.LOW_SPECIMEN;
                        }),
                        new InstantAction(() -> {
                            rotation.basketPosition();
                        }),
                        new InstantAction(() -> {
                            wrist.score();
                        }))
                )
                .waitSeconds(1)

                // drive and align to  chamber
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(4, -37), Math.toRadians(180))

                .waitSeconds(0.5)

                // drive all the way up to the chamber
                .lineToY(-32, new TranslationalVelConstraint(5))

                // scoring sequence
                .afterTime(0, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.specimenLowPosition();
                        }),
                        new InstantAction(() -> {
                            wrist.scoreLowSpecimen();
                        }))
                )
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.specimenIntakePosition();
                        }),
                        new InstantAction(() -> {
                            wrist.intakeSpecimen();
                        }))
                )
                .afterTime(1.1, new InstantAction(() -> {
                    grabber.open();
                }))
                .waitSeconds(2)

                .lineToY(-40)

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
                    armSlidePosition = ArmSlide.SlidePositions.INTAKE_SPECIMEN;
                }),
                new InstantAction(() -> {
                    rotation.basketPosition();
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