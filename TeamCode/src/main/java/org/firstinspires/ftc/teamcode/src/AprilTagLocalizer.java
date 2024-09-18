package org.firstinspires.ftc.teamcode.src;
import android.annotation.SuppressLint;
import android.util.Size;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import java.util.List;


public class AprilTagLocalizer {
    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera
    /**
     * Variables to store the position and orientation of the camera on the robot. Setting these
     * values requires a definition of the axes of the camera and robot:
     *
     * Camera axes:
     * Origin location: Center of the lens
     * Axes orientation: +x right, +y down, +z forward (from camera's perspective)
     *
     * Robot axes (this is typical, but you can define this however you want):
     * Origin location: Center of the robot at field height
     * Axes orientation: +x right, +y forward, +z upward
     *
     * Position:
     * If all values are zero (no translation), that implies the camera is at the center of the
     * robot. Suppose your camera is positioned 5 inches to the left, 7 inches forward, and 12
     * inches above the ground - you would need to set the position to (-5, 7, 12).
     *
     * Orientation:
     * If all values are zero (no rotation), that implies the camera is pointing straight up. In
     * most cases, you'll need to set the pitch to -90 degrees (rotation about the x-axis), meaning
     * the camera is horizontal. Use a yaw of 0 if the camera is pointing forwards, +90 degrees if
     * it's pointing straight left, -90 degrees for straight right, etc. You can also set the roll
     * to +/-90 degrees if it's vertical, or 180 degrees if it's upside-down.
     */
    private Position cameraPosition = new Position(DistanceUnit.INCH,
            0, 0, 0, 0);
    private YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
            0, -90, 0, 0);
    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private AprilTagProcessor aprilTag;
    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;
    /**
     * Initialize the AprilTag processor.
     */
    public AprilTagLocalizer(HardwareMap hardwareMap) {
        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()
                // The following default settings are available to un-comment and edit as needed.
                //.setDrawAxes(false)
                //.setDrawCubeProjection(false)
                //.setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .setCameraPose(cameraPosition, cameraOrientation)
                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
                // ... these parameters are fx, fy, cx, cy.
                .build();
        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second (default)
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second (default)
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        //aprilTag.setDecimation(3);
        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();
        // set the camera
        builder.setCamera(hardwareMap.get(WebcamName.class, ElectricalContract.webcam()));
        // Choose a camera resolution. Not all cameras support all resolutions.
        builder.setCameraResolution(new Size(1920, 1080));
        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        builder.enableLiveView(true);
        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);
        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        builder.setAutoStopLiveView(false);
        // Set and enable the processor.
        builder.addProcessor(aprilTag);
        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();
        // Disable or re-enable the aprilTag processor at any time.
//        visionPortal.setProcessorEnabled(aprilTag, true);
    }
    /**
     * Add telemetry about AprilTag detections.
     */
    @SuppressLint("DefaultLocale") // suppress the warning
    public void telemetryAprilTag(Telemetry telemetry) {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());
        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)",
                        detection.robotPose.getPosition().x,
                        detection.robotPose.getPosition().y,
                        detection.robotPose.getPosition().z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)",
                        detection.robotPose.getOrientation().getPitch(AngleUnit.DEGREES),
                        detection.robotPose.getOrientation().getRoll(AngleUnit.DEGREES),
                        detection.robotPose.getOrientation().getYaw(AngleUnit.DEGREES)));
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }
        // Add "key" information to telemetry
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
    }
    public VisionPortal getVisionPortal() {
        return visionPortal;
    }
}