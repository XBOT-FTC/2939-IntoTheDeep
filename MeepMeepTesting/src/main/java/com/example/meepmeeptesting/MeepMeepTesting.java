
package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
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

        //starting pose 6, -62 for specimen
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-37, -62, Math.toRadians(270)))
                .setReversed(true)
                .waitSeconds(2)
                .splineToLinearHeading(new Pose2d(-48, -50, Math.toRadians(-135)), Math.toRadians(270))
                .waitSeconds(2)
                .turnTo(Math.toRadians(270))
                .lineToY(-43)
                .waitSeconds(2)
                .splineToLinearHeading(new Pose2d(-48, -50, Math.toRadians(-135)), Math.toRadians(270))

                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
