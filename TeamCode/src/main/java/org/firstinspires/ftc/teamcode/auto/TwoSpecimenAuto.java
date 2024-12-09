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
import org.firstinspires.ftc.teamcode.lib.intake.IntakeClaw;
import org.firstinspires.ftc.teamcode.lib.intake.IntakeClawSwivel;
import org.firstinspires.ftc.teamcode.lib.intake.IntakePivot;
import org.firstinspires.ftc.teamcode.lib.intake.IntakeSlide;
import org.firstinspires.ftc.teamcode.rr.MecanumDrive;


@Autonomous(name= "TwoSpecimenAuto", group="Autonomous")
public class TwoSpecimenAuto extends LinearOpMode {
    ArmSlide armSlide;
    ArmRotation rotation;
    ArmClaw armClaw;
    ArmWrist wrist;
    IntakePivot pivot;
    ArmSlide.SlidePositions armSlidePosition;
    IntakeClaw intakeClaw;
    IntakeClawSwivel intakeClawSwivel;
    IntakeSlide intakeSlide;
    IntakeSlide.SlidePositions intakeSlidePosition;
    @Override
    public void runOpMode() {
        armSlide = new ArmSlide(hardwareMap);
        rotation = new ArmRotation(hardwareMap);
        armClaw =  new ArmClaw(hardwareMap);
        wrist = new ArmWrist(hardwareMap);
        pivot = new IntakePivot(hardwareMap);
        intakeClaw = new IntakeClaw(hardwareMap);
        intakeClawSwivel = new IntakeClawSwivel(hardwareMap);
        armSlidePosition = ArmSlide.SlidePositions.ZERO;
        intakeSlide = new IntakeSlide(hardwareMap);
        intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;

        Pose2d startingPose = new Pose2d(9, -62, Math.toRadians(0));

        MecanumDrive drive = new MecanumDrive(hardwareMap, startingPose);

        Action trajectoryAction = drive.actionBuilder(drive.pose)


                // drive to chamber
                .setTangent(Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(6, -42, Math.toRadians(270)), Math.toRadians(90))
                .afterTime(0, new InstantAction(() -> {
                    intakeSlidePosition = IntakeSlide.SlidePositions.READY;
                }))

                .waitSeconds(0.5)

                // drive all the way up to the chamber
                .lineToY(-27, new TranslationalVelConstraint(10))

                // scoring sequence
                .afterTime(0, new InstantAction(() -> {
                    armClaw.open();
                }))
                .lineToY(-40, new TranslationalVelConstraint(15))

                // return to transfer
                .afterTime(0, new SequentialAction(
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.TRANSFER;
                        }),
                        new InstantAction(() -> {
                            rotation.transferPosition();
                        }),
                        new InstantAction(() -> {
                            wrist.transfer();
                        }))
                )
                .afterTime(0.75, new SequentialAction(
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;
                        })
                ))
                .waitSeconds(0.5)

                // drive to sample push position
                .splineToLinearHeading(new Pose2d(36, -35, Math.toRadians(180)), Math.toRadians(90),
                        new TranslationalVelConstraint(60))
                .splineToConstantHeading(new Vector2d(47, -10), Math.toRadians(0),
                        new TranslationalVelConstraint(60))

                // push sample
                .strafeTo(new Vector2d(47, -48), new TranslationalVelConstraint(60))

                // TODO: intake sequence, stagger the actions
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.SPECIMEN_INTAKE;
                        })
                ))


                // drive to intake specimen #1
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(47, -36, Math.toRadians(270)), Math.toRadians(180))
                .waitSeconds(2)
                .lineToY(-42, new TranslationalVelConstraint(8))

                // transfer specimen sequence
                .afterTime(0, new InstantAction(() -> {
                    intakeClaw.close();
                }))
                .afterTime(0.5, new InstantAction(() -> {
                    pivot.home();
                }))
                .afterTime(0.7, new SequentialAction(
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;
                        })
                ))
                .afterTime(1.25, new SequentialAction(
                        new InstantAction(() -> {
                            intakeClaw.open();
                        }),
                        new InstantAction(() -> {
                            armClaw.close();
                        }))
                )
                .waitSeconds(1.25)



                // drive and align to  chamber
                .setTangent(Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(5, -42), Math.toRadians(90))

                .afterTime(0, new SequentialAction(
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.READY;
                        }),
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.LOW_SPECIMEN;
                        }))
                )
                .afterTime(0.25, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.specimenHighPosition();
                        }),
                        new InstantAction(() -> {
                            wrist.scoreHighSpecimen();
                        }),
                        new InstantAction(() -> {
                            pivot.deploy();
                        })
                ))
                .afterTime(1.5, new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.ZERO;
                }))
                .waitSeconds(2)

                // drive all the way up to the chamber
                .lineToY(-27, new TranslationalVelConstraint(10))

                // scoring sequence
                .afterTime(0, new InstantAction(() -> {
                    armClaw.open();
                }))
                .waitSeconds(0.01)

                .lineToY(-40, new TranslationalVelConstraint(15))

                // return to transfer
                .afterTime(0, new SequentialAction(
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            rotation.autoEndPosition();
                        }),
                        new InstantAction(() -> {
                            wrist.scoreLowSpecimen();
                        }))
                )
                .afterTime(0.75, new SequentialAction(
                        new InstantAction(() -> {
                            armSlidePosition =  ArmSlide.SlidePositions.NEGATIVE;
                        }),
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.NEGATIVE;
                        })
                ))
                .waitSeconds(0.5)

                .splineToLinearHeading(new Pose2d(40, -55, Math.toRadians(90)), Math.toRadians(0))

//                // zero mechanisms to end auto
//                .afterTime(2.25, new SequentialAction(
//                        new InstantAction(() -> {
//                            armSlidePosition = ArmSlide.SlidePositions.LOW_SPECIMEN;
//                        }),
//                        new InstantAction(() -> {
//                            rotation.autoEndPosition();
//                        }),
//                        new InstantAction(() -> {
//                            wrist.scoreLowSpecimen();   // 0.3 is better but lets see
//                        })
//
//                ))
//                .afterTime(3, new SequentialAction(
//                        new InstantAction(() -> {
//                            armSlidePosition = ArmSlide.SlidePositions.NEGATIVE;
//                        }),
//                        new InstantAction(() -> {
//                            intakeSlidePosition = IntakeSlide.SlidePositions.NEGATIVE;
//                        })
//                ))
//                .waitSeconds(2.25)
                .waitSeconds(0.5)

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
                    armClaw.close();
                }),
//                new InstantAction(() -> {
//                    armSlidePosition = ArmSlide.SlidePositions.HIGH_SPECIMEN;
//                }),
                new InstantAction(() -> {
                    pivot.deploy();
                }),
                new InstantAction(() -> {
                    rotation.specimenHighPosition();
                }),
                new InstantAction(() -> {
                    intakeClawSwivel.transfer();
                }),
                new InstantAction(() -> {
                    wrist.scoreHighSpecimen();
                })
        );
    }
}