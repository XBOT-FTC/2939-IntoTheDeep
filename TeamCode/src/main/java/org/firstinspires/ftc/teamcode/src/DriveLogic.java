package org.firstinspires.ftc.teamcode.src;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class DriveLogic {
    public static double kP = 0, kI = 0, kD = 0;
    public static final double TICKS_PER_REV = 537.7;
    public static final double WHEEL_DIAMETER = 97; // MM
    public static final double RPM = 312;
    public static final double k = 1; // scaling variable: measured distance / expected distance

    public static double pidControl(double actual, double target){

        PIDController pidController = new PIDController(kP, kI, kD);
        pidController.setPID(kP, kI, kD);

        return pidController.calculate(actual, target);
    }

    public static double getLinearVelocity(double RPS) {
        // linear velocity of a wheel is the radius * angular velocity
        double radius = WHEEL_DIAMETER / 2;
        // return velocity (mm/s)
        return RPS * radius;
    }

    public static double getExpectedRobotDistanceInches(int encoderTicks) {
        double distancePerTick = (Math.PI * WHEEL_DIAMETER) / TICKS_PER_REV;
        return (encoderTicks * distancePerTick * k) / 25.4;
    }

    public static double getQuickRotatePower(double rightJoystickY, double rightJoystickX, double currentHeading, Telemetry telemetry) {
        double goalHeading;

        //TODO: refine quadrants
        if (rightJoystickY > 0.7 && Math.abs(rightJoystickX) < 0.1) {
            goalHeading = 0;
        } else if (rightJoystickX < -0.7 && Math.abs(rightJoystickY) < 0.1) {
            goalHeading = 90;
        } else if (rightJoystickY < -0.7 && Math.abs(rightJoystickX) < 0.1) {
            goalHeading = 180;
        } else if (rightJoystickX > 0.7 && Math.abs(rightJoystickY) < 0.1) {
            goalHeading = -90;
        } else {
            goalHeading = currentHeading;
        }

        // Calculate the error between the desired heading and the current heading
        double error = goalHeading - currentHeading;

        // Ensure the error is within the range [-180, 180]
        if (error > 180) {
            error -= 360;
        } else if (error < -180) {
            error += 360;
        }
        double absError = Math.abs(error);

        // doing this so the state is always less than the target
        //TODO: if this doesn't work add a big number (180, 360, 1000) to both variables so the error that the pid calculates is always positive
        double state = Math.abs(goalHeading) - absError;
        double target = Math.abs(goalHeading);

        double headingPower = pidControl(state, target);

        // handling which direction to rotate
        if (error > 0) {
            headingPower *= -1;
        }

        telemetry.addData("currentHeading", currentHeading);
        telemetry.addData("goalHeading", goalHeading);
        telemetry.addData("state", state);
        telemetry.addData("target", target);
        telemetry.addData("error", error);
        telemetry.addData("absError", absError);
        telemetry.addData("headingPower", headingPower);
        if (error > 0 ) {
            telemetry.addLine("Supposed to be turning LEFT");
        }else {
            telemetry.addLine("Supposed to be turning RIGHT");
        }

        return headingPower;
    }
}