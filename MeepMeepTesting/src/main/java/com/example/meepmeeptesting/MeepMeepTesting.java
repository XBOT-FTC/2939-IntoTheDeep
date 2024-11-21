
package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.SequentialAction;
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

        //starting pose 6, -62 for specimen, -37, -72
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(9, -62, Math.toRadians(0)))


                .waitSeconds(1)
                // drive to chamber
                .setTangent(90)
                .splineToSplineHeading(new Pose2d(6, -33, Math.toRadians(90)), Math.toRadians(90))
                .waitSeconds(1)

                // lower arm to score preloaded specimen

                .waitSeconds(2)

                // drive to sample push position
                .setReversed(true)
                .splineToLinearHeading(new Pose2d(35, -35, Math.toRadians(270)), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(46, -10), Math.toRadians(0))

                // push sample
                .setReversed(false)
                .lineToY(-57)

                // drive to sample push position
                .setReversed(true)
                .splineToConstantHeading(new Vector2d(56, -10), Math.toRadians(0))

                // push sample
                .setReversed(false)
                .lineToY(-57)

                // drive to intake specimen #1
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(44, -56), Math.toRadians(270))

                //open grabber


                // intake specimen


                // raise specimen off field wall


                // lower arm back down


                // drive and align to chamber
                .setTangent(Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(6, -37, Math.toRadians(90)), Math.toRadians(90))
                .waitSeconds(0.5)
                .lineToY(-33)

                // lower arm to score specimen


                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
