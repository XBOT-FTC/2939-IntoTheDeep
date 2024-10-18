package org.firstinspires.ftc.teamcode.lib;

import com.arcrobotics.ftclib.controller.PIDController;

public class PIDManager {
    private final double kP;
    private final double kI;
    private final double kD;
    private final double kF;
    public PIDManager(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = 0;
    }

    public PIDManager(double kP, double kI, double kD, double kF) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
    }

    public double pidControl(double actual, double target){
        PIDController pidController = new PIDController(kP, kI, kD);
        pidController.setPID(kP, kI, kD);

        return pidController.calculate(actual, target);
    }

    public double pidControl(double actual, double target, double tolerance){
        PIDController pidController = new PIDController(kP, kI, kD);
        pidController.setPID(kP, kI, kD);
        pidController.setTolerance(tolerance);

        return pidController.calculate(actual, target);
    }

}