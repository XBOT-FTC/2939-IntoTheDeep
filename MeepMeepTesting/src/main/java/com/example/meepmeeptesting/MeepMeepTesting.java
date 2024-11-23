
package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        //starting pose 6, -62 for specimen, -37, -62
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(6, -62, Math.toRadians(0)))


                // drive to chamber
                .setTangent(90)
                .splineToSplineHeading(new Pose2d(6, -37, Math.toRadians(270)), Math.toRadians(90))
                .waitSeconds(0.5)

                .lineToY(-34)
                .waitSeconds(1)

                // drive to sample push position
                .splineToLinearHeading(new Pose2d(37, -35, Math.toRadians(270)), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(50, -10), Math.toRadians(0))

                // push sample
                .setReversed(false)
                .lineToY(-57)

                // drive to sample push position
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(61, -10), Math.toRadians(0))

                // push sample
                .setReversed(false)
                .lineToY(-57)

                // drive to intake specimen #1
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(44, -56), Math.toRadians(270))

                .waitSeconds(0.5)

                .lineToY(-62)

                // drive and align to  chamber
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(2, -37), Math.toRadians(180))
                .waitSeconds(0.5)

                .lineToY(-34)

                .waitSeconds(1)

                // drive to human player
                .setTangent(Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(44, -56), Math.toRadians(0))

                .waitSeconds(0.5)

                .lineToY(-62)

                // drive and align to chamber
                .setTangent(90)
                .splineToConstantHeading(new Vector2d(-2, -37), Math.toRadians(180))
                .waitSeconds(0.5)

                .lineToY(-34)

                // drive to human player
                .setTangent(Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(44, -56), Math.toRadians(0))

                .waitSeconds(0.5)

                .lineToY(-62)

                // drive and align to chamber
                .setTangent(90)
                .splineToConstantHeading(new Vector2d(-6, -37), Math.toRadians(180))
                .waitSeconds(0.5)

                .lineToY(-34)




















//                // drive to observation zone
//                .setTangent(Math.toRadians(90))
//                .splineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(45)), Math.toRadians(180))
//
//                .waitSeconds(2.25)
//
//                // align robot to 1st sample
//                .splineToLinearHeading(new Pose2d(-48, -52, Math.toRadians(90)), Math.toRadians(0))
//
//                .waitSeconds(0.01)
//
//                // drive to intake the sample
//                .lineToY(-41)
//
//                .waitSeconds(0.25)
//
//                .waitSeconds(0.35)
//
//                // drive to observation zone
//                .splineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(45)), Math.toRadians(180))
//
//                .waitSeconds(2.25)
//
//
//
//                // align robot to 2nd sample
//                .splineToLinearHeading(new Pose2d(-58, -52, Math.toRadians(90)), Math.toRadians(0))
//
//
//                .waitSeconds(0.2)
//
//                // drive to intake the sample
//                .lineToY(-41)
//
//                .waitSeconds(0.25)
//
//                .waitSeconds(0.35)
//
//                // drive to observation zone
//                .setTangent(Math.toRadians(270))
//                .splineToLinearHeading(new Pose2d(-52, -52, Math.toRadians(45)), Math.toRadians(270))
//
//
//                .waitSeconds(2.25)
//
//
//
//                // align to 3rd sample
//                .splineToSplineHeading(new Pose2d(-49, -26, Math.toRadians(180)), Math.toRadians(90))
//
//                .waitSeconds(0.4)
//
//                // drive to intake the sample
//                .lineToX(-54, new TranslationalVelConstraint(3))
//
//                .waitSeconds(1.5)
//
//                .waitSeconds(0.35)
//
//                // drive to observation zone
//                .setTangent(Math.toRadians(0))
//                .splineToLinearHeading(new Pose2d(-51, -51, Math.toRadians(45)), Math.toRadians(270))








                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
