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
import org.firstinspires.ftc.teamcode.lib.intake.IntakeWheels;
import org.firstinspires.ftc.teamcode.rr.MecanumDrive;


@Autonomous(name= "SpecimenToBasketAuto", group="Autonomous")
public class SpecimenToBasketAuto extends LinearOpMode {
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

        Pose2d startingPose = new Pose2d(-9, -62, Math.toRadians(0));

        MecanumDrive drive = new MecanumDrive(hardwareMap, startingPose);

        Action trajectoryAction = drive.actionBuilder(drive.pose)

                .setTangent(Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(-9, -37, Math.toRadians(270)), Math.toRadians(90))
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

                // put arm down to prepare for sample scoring
                .afterTime(0, new SequentialAction(
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            rotation.transferPosition();
                        }),
                        new InstantAction(() -> {
                            wrist.transfer();
                        }))
                )

                // align robot to 1st sample
                .setTangent(Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(-48, -52), Math.toRadians(180))

                // running intake sequence
                .afterTime(0, new SequentialAction(
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.AUTO_INTAKE;
                        }),
                        new InstantAction(() -> {
                            pivot.deploy();
                        }),
                        new InstantAction(() -> {
                            intakeWheels.intake();
                        })))
                .waitSeconds(0.01)

                // drive to intake the sample
                .lineToY(-41, new TranslationalVelConstraint(25))

                // stop intake sequence
                .afterTime(0.25, new SequentialAction(
                        new InstantAction(() -> {
                            pivot.home();
                        }),
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            intakeWheels.stop();
                        })))
                .waitSeconds(0.25)

                // transfer sequence
                .afterTime(0.5, new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.TRANSFER;
                }))
                .afterTime(1.1, new InstantAction(() -> {
                    grabber.close();
                }))
                .afterTime(1.35, new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.HOMED;
                }))
                .waitSeconds(0.35)

                // drive to observation zone
                .splineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(45)), Math.toRadians(180))

                // scoring basket sequence
                .afterTime(0,
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HIGH_BASKET;
                        }))
                .afterTime(0.75, new InstantAction(() -> {
                    wrist.score();
                }))

                .afterTime(0.75, new InstantAction(() -> {
                            rotation.basketPosition();
                        })
                )
                .afterTime(1.75, new SequentialAction(
                        new InstantAction(() -> {
                            grabber.open();
                        })))
                .afterTime(2.25, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.transferPosition();
                        }),
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            wrist.transfer();
                        })))
                .waitSeconds(2.25)




                // align robot to 2nd sample
                .splineToLinearHeading(new Pose2d(-58, -52, Math.toRadians(90)), Math.toRadians(0))

                // running intake sequence
                .afterTime(0.2, new SequentialAction(
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.AUTO_INTAKE;
                        }),
                        new InstantAction(() -> {
                            pivot.deploy();
                        }),
                        new InstantAction(() -> {
                            intakeWheels.intake();
                        })))
                .waitSeconds(0.2)

                // drive to intake the sample
                .lineToY(-41, new TranslationalVelConstraint(25))

                // stop intake sequence
                .afterTime(0.25, new SequentialAction(
                        new InstantAction(() -> {
                            pivot.home();
                        }),
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            intakeWheels.stop();
                        })))
                .waitSeconds(0.25)

                // transfer sequence
                .afterTime(0.5, new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.TRANSFER;
                }))
                .afterTime(1.1, new InstantAction(() -> {
                    grabber.close();
                }))
                .afterTime(1.35, new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.HOMED;
                }))
                .waitSeconds(0.35)

                // drive to observation zone
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(45)), Math.toRadians(270))

                // scoring basket sequence
                .afterTime(0,
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HIGH_BASKET;
                        }))
                .afterTime(0.75, new InstantAction(() -> {
                    wrist.score();
                }))

                .afterTime(0.75, new InstantAction(() -> {
                            rotation.basketPosition();
                        })
                )
                .afterTime(1.75, new SequentialAction(
                        new InstantAction(() -> {
                            grabber.open();
                        })))
                .afterTime(2.25, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.transferPosition();
                        }),
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            wrist.transfer();
                        })))
                .waitSeconds(2.25)







                // align to 3rd sample
                .splineToSplineHeading(new Pose2d(-49, -26, Math.toRadians(180)), Math.toRadians(90))

                // running intake sequence
                .afterTime(0, new SequentialAction(
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.AUTO_INTAKE;
                        }),
                        new InstantAction(() -> {
                            pivot.deploy();
                        }),
                        new InstantAction(() -> {
                            intakeWheels.intake();
                        })))
                .waitSeconds(0.4)

                // drive to intake the sample
                .lineToX(-54, new TranslationalVelConstraint(3))

                // stop intake sequence
                .afterTime(1.5, new SequentialAction(
                        new InstantAction(() -> {
                            pivot.home();
                        }),
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            intakeWheels.stop();
                        })))
                .waitSeconds(1.5)

                // transfer sequence
                .afterTime(0.5, new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.TRANSFER;
                }))
                .afterTime(1.1, new InstantAction(() -> {
                    grabber.close();
                }))
                .afterTime(1.35, new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.HOMED;
                }))
                .waitSeconds(0.35)

                // drive to observation zone
                .setTangent(Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(45)), Math.toRadians(270))

                // scoring basket sequence
                .afterTime(0,
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HIGH_BASKET;
                        }))
                .afterTime(0.75, new InstantAction(() -> {
                    wrist.score();
                }))

                .afterTime(0.75, new InstantAction(() -> {
                            rotation.basketPosition();
                        })
                )
                .afterTime(1.75, new SequentialAction(
                        new InstantAction(() -> {
                            grabber.open();
                        })))
                // zero mechanisms to end auto
                .afterTime(2.25, new SequentialAction(
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.ZERO;
                        }),
                        new InstantAction(() -> {
                            rotation.specimenIntakePosition();
                        }),
                        new InstantAction(() -> {
                            wrist.intakeSpecimen();
                        })

                ))
                .waitSeconds(2.25)

                .turnTo(Math.toRadians(90))
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