
package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
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

        //starting pose 6, -62
        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(6, -62, Math.toRadians(270)))
                .setReversed(true)
                .waitSeconds(2)
                .lineToY(-33)
//                .waitSeconds(2)


//                .splineToLinearHeading(new Pose2d(38, -10, Math.toRadians(180)), Math.toRadians(90))
//                .strafeToConstantHeading(new Vector2d(45, -10))
//                .setReversed(false)
//                .strafeToConstantHeading(new Vector2d(45, -55))
//                .strafeToConstantHeading(new Vector2d(45, -10))
//                .strafeToConstantHeading(new Vector2d(55, -10))
//                .strafeToConstantHeading(new Vector2d(55, -55))
//                .strafeToConstantHeading(new Vector2d(55, -10))
//                .strafeToConstantHeading(new Vector2d(61, -10))
//                .strafeToConstantHeading(new Vector2d(61, -55))
//                .setTangent(90)
//                .splineToLinearHeading(new Pose2d(29, -11, Math.toRadians(-90)), Math.toRadians(180))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
