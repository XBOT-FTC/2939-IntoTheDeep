/* Copyright (c) 2024 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.vision;

import android.annotation.SuppressLint;
import android.util.Size;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.ElectricalContract;
import org.firstinspires.ftc.teamcode.lib.drive.DriveLogic;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Config
public class AprilTagMultiPortal {
    VisionPortal portal1;
    VisionPortal portal2;

    AprilTagProcessor aprilTagProcessor1;
    AprilTagProcessor aprilTagProcessor2;

    // TODO: Measure and put in position & angle data
    private final Position cameraPosition1 = new Position(DistanceUnit.INCH,
            0, 0, 0, 0);
    private final YawPitchRollAngles cameraOrientation1 = new YawPitchRollAngles(AngleUnit.DEGREES,
            0, -90, 0, 0);
    private final Position cameraPosition2 = new Position(DistanceUnit.INCH,
            0, 0, 0, 0);
    private final YawPitchRollAngles cameraOrientation2 = new YawPitchRollAngles(AngleUnit.DEGREES,
            0, -90, 0, 0);

    String webcam1;
    String webcam2;

    public static double kP = 0.019, kI = 0, kD = 0;

    public AprilTagMultiPortal(String webcam1, String webcam2, HardwareMap hardwareMap) {
        this.webcam1 = webcam1;
        this.webcam2 = webcam2;

        int[] viewIds = VisionPortal.makeMultiPortalView(2, VisionPortal.MultiPortalLayout.VERTICAL);

        int portal1ViewId = viewIds[0];
        int portal2ViewId = viewIds[1];

        aprilTagProcessor1 = buildAprilTagProcessor(cameraPosition1, cameraOrientation1);
        aprilTagProcessor2 = buildAprilTagProcessor(cameraPosition2, cameraOrientation2);

        aprilTagProcessor2.setDecimation(3);
        portal1 = buildAprilTagVisionPortalBad(ElectricalContract.webcam1(), portal1ViewId, aprilTagProcessor1, hardwareMap);
        portal2 = buildAprilTagVisionPortal(ElectricalContract.webcam2(), portal2ViewId, aprilTagProcessor2, hardwareMap);


        while (portal2.getCameraState() != VisionPortal.CameraState.STREAMING) {
        }

        ExposureControl exposure  = portal2.getCameraControl(ExposureControl.class);
        exposure.setMode(ExposureControl.Mode.Manual);
        exposure.setExposure(15, TimeUnit.MILLISECONDS);

        GainControl gain = portal2.getCameraControl(GainControl.class);
        gain.setGain(100); //max gain 100

    }

    public AprilTagProcessor buildAprilTagProcessor(Position cameraPosition, YawPitchRollAngles cameraOrientation) {
        return new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setDrawTagOutline(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(AprilTagGameDatabase.getIntoTheDeepTagLibrary())
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .setCameraPose(cameraPosition, cameraOrientation)
                .setLensIntrinsics(918.709, 918.709, 643.226, 395.994)
                .build();
    }

    public VisionPortal buildAprilTagVisionPortal(String cameraName, int viewID, AprilTagProcessor aprilTagProcessor,
                                                  HardwareMap hardwareMap) {
        return new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, cameraName))
                .setCameraResolution(new Size(1280, 800))
                .setLiveViewContainerId(viewID)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .addProcessor(aprilTagProcessor)
                .build();
    }
    public VisionPortal buildAprilTagVisionPortalBad(String cameraName, int viewID, AprilTagProcessor aprilTagProcessor,
                                                     HardwareMap hardwareMap) {
        return new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, cameraName))
//                .setCameraResolution(new Size(1280, 800))
                .setLiveViewContainerId(viewID)
                .addProcessor(aprilTagProcessor)
                .build();
    }

    @SuppressLint("DefaultLocale") // suppress the warning
    public void telemetryAprilTag(Telemetry telemetry) {
        List<AprilTagDetection> currentDetections = aprilTagProcessor2.getDetections();
        telemetry.addData("# AprilTags Detected in " + webcam2, currentDetections.size());
        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine("Robot pose data:");
                telemetry.addLine(String.format("XYZ %6.1f %6.1f  (inch)",
                        detection.robotPose.getPosition().x,
                        detection.robotPose.getPosition().y));
                telemetry.addLine(String.format("%6.1f (deg)",
                        detection.robotPose.getOrientation().getYaw(AngleUnit.DEGREES)));

                telemetry.addLine("ftcPose data:");
                telemetry.addLine(String.format("Range, Bearing" +
                                "%6.1f %6.1f",
                        detection.ftcPose.range,
                        detection.ftcPose.bearing));

            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }
    }

    public double getATLockOnPower() {
        List<AprilTagDetection> currentDetections = aprilTagProcessor2.getDetections();
        AprilTagDetection aprilTag = null;

        double actual = 0;

        if (currentDetections.size() > 0) {
            aprilTag = currentDetections.get(0);
        }
        if (aprilTag == null) {
            actual = 0;
        }
        else {
            actual = aprilTag.ftcPose.bearing;
        }

        return DriveLogic.pidControlAT(actual, 0, kP, kI, kD);
    }

}