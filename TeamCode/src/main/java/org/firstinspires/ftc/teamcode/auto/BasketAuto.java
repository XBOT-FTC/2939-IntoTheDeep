package org.firstinspires.ftc.teamcode.auto;


import androidx.annotation.NonNull;

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
import org.firstinspires.ftc.teamcode.lib.intake.IntakeSlide;
import org.firstinspires.ftc.teamcode.lib.intake.IntakeWheels;
import org.firstinspires.ftc.teamcode.rr.MecanumDrive;


@Autonomous(name= "BasketAuto", group="Autonomous")
public class BasketAuto extends LinearOpMode {
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

        Pose2d startingPose = new Pose2d(-38, -62, Math.toRadians(0));

        MecanumDrive drive = new MecanumDrive(hardwareMap, startingPose);

        Action trajectoryAction = drive.actionBuilder(drive.pose)

                // drive to observation zone
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(-51, -51, Math.toRadians(45)), Math.toRadians(180))

                // scoring basket sequence
                .afterTime(0,
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HIGH_BASKET;
                        }))
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.basketPosition();
                        }),
                        new InstantAction(() -> {
                            wrist.score();
                        }))
                )
                .afterTime(2, new SequentialAction(
                        new InstantAction(() -> {
                            grabber.open();
                        })))
                .afterTime(3, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.transferPosition();
                        }),
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            wrist.transfer();
                        })))
                .waitSeconds(4)

                // align robot to sample
                .splineToLinearHeading(new Pose2d(-48, -52, Math.toRadians(90)), Math.toRadians(0))
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
                .lineToY(-41)

                // stop intake sequence
                .afterTime(0.5, new SequentialAction(
                        new InstantAction(() -> {
                            pivot.home();
                        }),
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            intakeWheels.stop();
                        })))
                .waitSeconds(0.75)

                // transfer sequence
                .afterTime(1, new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.TRANSFER;
                }))
                .afterTime(1.5, new InstantAction(() -> {
                    grabber.close();
                }))
                .afterTime(2.75, new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.HOMED;
                }))

                .waitSeconds(2.75)

                // drive to observation zone
                .splineToLinearHeading(new Pose2d(-51, -51, Math.toRadians(45)), Math.toRadians(180))

                // scoring basket sequence
                .afterTime(0,
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HIGH_BASKET;
                        }))
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.basketPosition();
                        }),
                        new InstantAction(() -> {
                            wrist.score();
                        }))
                )
                .afterTime(2, new SequentialAction(
                        new InstantAction(() -> {
                            grabber.open();
                        })))
                .afterTime(3, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.transferPosition();
                        }),
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            wrist.transfer();
                        })))
                .waitSeconds(4)

                // align robot to sample
                .splineToLinearHeading(new Pose2d(-58, -52, Math.toRadians(90)), Math.toRadians(0))

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
                .lineToY(-41)


                // stop intake sequence
                .afterTime(0.5, new SequentialAction(
                        new InstantAction(() -> {
                            pivot.home();
                        }),
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            intakeWheels.stop();
                        })))
                .waitSeconds(0.75)

                // transfer sequence
                .afterTime(1, new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.TRANSFER;
                }))
                .afterTime(1.5, new InstantAction(() -> {
                    grabber.close();
                }))
                .afterTime(2.75, new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.HOMED;
                }))

                .waitSeconds(2.75)

                // drive to observation zone
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(-51, -51, Math.toRadians(45)), Math.toRadians(270))




                // scoring basket sequence
                .afterTime(0,
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HIGH_BASKET;
                        }))
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.basketPosition();
                        }),
                        new InstantAction(() -> {
                            wrist.score();
                        }))
                )
                .afterTime(2, new SequentialAction(
                        new InstantAction(() -> {
                            grabber.open();
                        })))
                .afterTime(3, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.transferPosition();
                        }),
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HOMED;
                        })))
                .waitSeconds(4)

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
                    armSlidePosition = ArmSlide.SlidePositions.HOMED;
                }),
                new InstantAction(() -> {
                    rotation.transferPosition();
                }),
                new InstantAction(() -> {
                    wrist.transfer();
                }),
                new InstantAction(() -> {
                    pivot.home();
                })
        );
    }
}