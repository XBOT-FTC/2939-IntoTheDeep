package org.firstinspires.ftc.teamcode.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.Constants;
import org.firstinspires.ftc.teamcode.lib.PIDManager;

@Config
@TeleOp
public class ScoringSlidePIDTuner extends LinearOpMode {
    public PIDManager pidManager;
    public static double p = 0, i = 0, d= 0;
    public static double f = 0;
    public static int target = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        // motor for left linear slide setup
        DcMotor linearSlideLeft = hardwareMap.get(DcMotor.class, ElectricalContract.leftArmSlideMotor());
        linearSlideLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearSlideLeft.setDirection(Constants.leftArmSlideDirection);

        // motor for right linear slide setup
        DcMotor linearSlideRight = hardwareMap.get(DcMotor.class, ElectricalContract.rightArmSlideMotor());
        linearSlideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        linearSlideRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearSlideRight.setDirection(Constants.leftArmSlideDirection.inverted());

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            pidManager = new PIDManager(p, i, d, f);
            int currentPosition = linearSlideLeft.getCurrentPosition();

            double power  = pidManager.pidfControl(currentPosition, target, 20, 0.01);

            linearSlideLeft.setPower(power);
            linearSlideRight.setPower(power);

            telemetry.addData("CurrentPosition", currentPosition);
            telemetry.addData("target", target);
            telemetry.addData("power", power);

            telemetry.update();
        }
    }
}