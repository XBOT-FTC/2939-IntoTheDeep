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
import org.firstinspires.ftc.teamcode.lib.intake.IntakeClaw;
import org.firstinspires.ftc.teamcode.lib.intake.IntakeClawSwivel;
import org.firstinspires.ftc.teamcode.lib.intake.IntakePivot;
import org.firstinspires.ftc.teamcode.lib.intake.IntakeSlide;
import org.firstinspires.ftc.teamcode.rr.MecanumDrive;


@Autonomous(name= "BasketAuto", group="Autonomous")
public class BasketAuto extends LinearOpMode {
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
        armSlidePosition = ArmSlide.SlidePositions.HOMED;
        intakeClaw = new IntakeClaw(hardwareMap);
        intakeClawSwivel = new IntakeClawSwivel(hardwareMap);
        intakeSlide = new IntakeSlide(hardwareMap);
        intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;

        Pose2d startingPose = new Pose2d(-38, -62, Math.toRadians(0));

        MecanumDrive drive = new MecanumDrive(hardwareMap, startingPose);

        Action trajectoryAction = drive.actionBuilder(drive.pose)

                // drive to observation zone
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(45)), Math.toRadians(180))

                // scoring basket sequence
                .afterTime(0,
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HIGH_BASKET;
                        }))
                .afterTime(0.75, new SequentialAction(
                        new InstantAction(() -> {
                            wrist.score();
                        }),
                        new InstantAction(() -> {
                            rotation.basketPosition();
                        })))
                .afterTime(1.75, new SequentialAction(
                        new InstantAction(() -> {
                            armClaw.open();
                        })))
                .afterTime(2.25, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.transferPosition();
                        }),
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.TRANSFER;
                        }),
                        new InstantAction(() -> {
                            wrist.transfer();
                        })))
                .waitSeconds(2.25)

                // align robot to 1st sample
                .splineToLinearHeading(new Pose2d(-48, -46, Math.toRadians(90)), Math.toRadians(90))

                .afterTime(0, new SequentialAction(
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.INTAKE;
                        }),
                        new InstantAction(() -> {
                            pivot.deploy();
                        })))
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            intakeClaw.close();
                        })))

                // transfer sequence
                .afterTime(1.3, new SequentialAction(
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            pivot.home();
                        })))
                .afterTime(2,
                        new InstantAction(() -> {
                            armClaw.close();
                        }))
                .afterTime(2.5,
                        new InstantAction(() -> {
                            intakeClaw.open();
                        }))
                .waitSeconds(2)

                // drive to observation zone
                .splineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(45)), Math.toRadians(180))

                // scoring basket sequence
                .afterTime(0,
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HIGH_BASKET;
                        }))
                .afterTime(0.75, new SequentialAction(
                        new InstantAction(() -> {
                            wrist.score();
                        }),
                        new InstantAction(() -> {
                            rotation.basketPosition();
                        })))
                .afterTime(1.75, new SequentialAction(
                        new InstantAction(() -> {
                            armClaw.open();
                        })))
                .afterTime(2.25, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.transferPosition();
                        }),
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.TRANSFER;
                        }),
                        new InstantAction(() -> {
                            wrist.transfer();
                        })))
                .waitSeconds(2.25)



                // align robot to 2nd sample
                .splineToLinearHeading(new Pose2d(-58, -46, Math.toRadians(90)), Math.toRadians(90))

                .afterTime(0, new SequentialAction(
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.INTAKE;
                        }),
                        new InstantAction(() -> {
                            pivot.deploy();
                        })))
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            intakeClaw.close();
                        })))

                // transfer sequence
                .afterTime(1.3, new SequentialAction(
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            pivot.home();
                        })))
                .afterTime(2,
                        new InstantAction(() -> {
                            armClaw.close();
                        }))
                .afterTime(2.5,
                        new InstantAction(() -> {
                            intakeClaw.open();
                        }))
                .waitSeconds(2)

                // drive to observation zone
                .splineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(45)), Math.toRadians(270))

                // scoring basket sequence
                .afterTime(0,
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HIGH_BASKET;
                        }))
                .afterTime(0.75, new SequentialAction(
                        new InstantAction(() -> {
                            wrist.score();
                        }),
                        new InstantAction(() -> {
                            rotation.basketPosition();
                        })))
                .afterTime(1.75, new SequentialAction(
                        new InstantAction(() -> {
                            armClaw.open();
                        })))
                .afterTime(2.25, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.transferPosition();
                        }),
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.TRANSFER;
                        }),
                        new InstantAction(() -> {
                            wrist.transfer();
                        })))
                .waitSeconds(2.25)

                .afterTime(0, new SequentialAction(
                        new InstantAction(() -> {
                            intakeClawSwivel.zero();
                        })))





                // align to 3rd sample
                .splineToSplineHeading(new Pose2d(-49, -26, Math.toRadians(180)), Math.toRadians(90))


                .afterTime(0, new SequentialAction(
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.AUTO_INTAKE;
                        }),
                        new InstantAction(() -> {
                            pivot.deploy();
                        })))
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            intakeClaw.close();
                        })))

                // transfer sequence
                .afterTime(1.3, new SequentialAction(
                        new InstantAction(() -> {
                            intakeClawSwivel.transfer();
                        }),
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            pivot.home();
                        })))
                .afterTime(2,
                        new InstantAction(() -> {
                            armClaw.close();
                        }))
                .afterTime(2.5,
                        new InstantAction(() -> {
                            intakeClaw.open();
                        }))
                .waitSeconds(2)

                // drive to observation zone
                .setReversed(true)
                .setTangent(Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(45)), Math.toRadians(270))


                // scoring basket sequence
                .afterTime(0,
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.HIGH_BASKET;
                        }))
                .afterTime(0.75, new SequentialAction(
                        new InstantAction(() -> {
                            wrist.score();
                        }),
                        new InstantAction(() -> {
                            rotation.basketPosition();
                        })))
                .afterTime(1.75, new SequentialAction(
                        new InstantAction(() -> {
                            armClaw.open();
                        })))
                .afterTime(2.25, new SequentialAction(
                        new InstantAction(() -> {
                            rotation.transferPosition();
                        }),
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.TRANSFER;
                        }),
                        new InstantAction(() -> {
                            wrist.transfer();
                        })))
                .waitSeconds(2.25)

                // zero mechanisms to end auto
                .afterTime(2.25, new SequentialAction(
                        new InstantAction(() -> {
                            armSlidePosition = ArmSlide.SlidePositions.NEGATIVE;
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
                    armClaw.close();
                }),
                new InstantAction(() -> {
                    intakeClaw.open();
                }),
                new InstantAction(() -> {
                    armSlidePosition = ArmSlide.SlidePositions.TRANSFER;
                }),
                new InstantAction(() -> {
                    rotation.transferPosition();
                }),
                new InstantAction(() -> {
                    intakeClawSwivel.transfer();
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