package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.Twist2d;
import com.acmerobotics.roadrunner.Twist2dDual;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.rr.TwoDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.vision.AprilTagLocalizer;
import org.firstinspires.ftc.vision.VisionPortal;

public class PoseSubsystem {
    private double inPerTick;
    private TwoDeadWheelLocalizer odometry;
    private AprilTagLocalizer aprilTagLocalizer;
    private Pose2d currentPose;
    public static Pose2d netZonePose = new Pose2d(new Vector2d(0,0),0);

    //TODO: set currentPose to some known pose and use driveToNetZone
    public PoseSubsystem(HardwareMap hardwareMap, IMU imu) {
        odometry = new TwoDeadWheelLocalizer(hardwareMap, imu, inPerTick);
        aprilTagLocalizer = new AprilTagLocalizer(hardwareMap);
        currentPose = new Pose2d(0,0,0);
    }

    public Pose2d getPose(Telemetry telemetry) {
        Twist2dDual<Time> odometryPose = odometry.update();
        currentPose.plus(new Twist2d(odometryPose.value().line, odometryPose.value().angle));

        Pose2d aprilTagPose = aprilTagLocalizer.update(telemetry);
        // if there isn't a april tag pose use odometry
        if (aprilTagPose.position.x == 0 && aprilTagPose.position.y == 0 && aprilTagPose.heading.toDouble() == 0) {
            return currentPose;
        }
        // return the available april tag pose
        else {
            currentPose = aprilTagPose;
            return aprilTagPose;
        }
    }

    public void setPose(Pose2d newPose) {
        currentPose = newPose;
    }

    public VisionPortal getVisionPortal() {
        return aprilTagLocalizer.getVisionPortal();
    }
}
