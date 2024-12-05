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


@Autonomous(name= "IntakeTwoSpecimenAuto", group="Autonomous")
public class IntakeTwoSpecimenAuto extends LinearOpMode {
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
        armSlidePosition = ArmSlide.SlidePositions.HOMED;
        intakeSlide = new IntakeSlide(hardwareMap);
        intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;

        Pose2d startingPose = new Pose2d(9, -62, Math.toRadians(0));

        MecanumDrive drive = new MecanumDrive(hardwareMap, startingPose);

        Action trajectoryAction = drive.actionBuilder(drive.pose)


                // drive to chamber
                .setTangent(Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(6, -37, Math.toRadians(270)), Math.toRadians(90))
                .waitSeconds(0.5)

                // raise arm
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
                    armClaw.open();
                }))
                .waitSeconds(1.2)

                // TODO: tune, prepare arm to transfer specimen from intake
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

                // drive to sample push position
                .splineToLinearHeading(new Pose2d(36, -35, Math.toRadians(180)), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(47, -10), Math.toRadians(0))

                // push sample
                .strafeTo(new Vector2d(47, -54))

                // drive to sample push position
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(58, -10), Math.toRadians(0))

                // push sample
                .strafeTo(new Vector2d(58, -54))


                // TODO: intake sequence, stagger the actions
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.INTAKE;
                        }),
                        new InstantAction(() -> {
                            pivot.deploy();
                        }))
                )


                // drive to intake specimen #1
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(47, -48, Math.toRadians(270)), Math.toRadians(180))
                .waitSeconds(0.01)
                .lineToY(-51)

                // transfer specimen sequence
                .afterTime(0.3, new InstantAction(() -> {
                    intakeClaw.close();
                }))
                .afterTime(0.5, new SequentialAction(
                        new InstantAction(() -> {
                            intakeSlidePosition = IntakeSlide.SlidePositions.HOMED;
                        }),
                        new InstantAction(() -> {
                            pivot.home();
                        }))
                )
                .afterTime(1, new SequentialAction(
                        new InstantAction(() -> {
                            intakeClaw.open();
                        }),
                        new InstantAction(() -> {
                            armClaw.close();
                        }))
                )
                .waitSeconds(1)



                // drive and align to  chamber
                .setTangent(Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(4, -37), Math.toRadians(90))
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


                // drive to intake specimen #2
                .setTangent(Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(47, -48), Math.toRadians(0))
                .waitSeconds(0.01)
                .lineToY(-51)
                .waitSeconds(0.5)

                // drive and align to  chamber
                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(4, -37, Math.toRadians(270)), Math.toRadians(90))
                .waitSeconds(0.5)

                // drive all the way up to the chamber
                .lineToY(-32, new TranslationalVelConstraint(5))




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
                    armSlidePosition = ArmSlide.SlidePositions.INTAKE_SPECIMEN;
                }),
                new InstantAction(() -> {
                    rotation.basketPosition();
                }),
                new InstantAction(() -> {
                    intakeClawSwivel.transfer();
                }),
                new InstantAction(() -> {
                    armClaw.open();
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