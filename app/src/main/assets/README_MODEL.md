# Gesture Recognition Model

This directory contains the MediaPipe gesture recognition model file (`gesture_recognizer.task`) used for Filipino Sign Language recognition.

## Model Information

- **Model Type**: MediaPipe Hand Landmarker
- **File**: `gesture_recognizer.task`
- **Purpose**: Detect hand landmarks and recognize Filipino Sign Language gestures
- **Supported Gestures**: A, B, C, L, V (basic alphabet signs)

## Implementation

The gesture recognition is implemented using:

1. **HandLandmarkerHelper.kt**: Core MediaPipe integration for hand landmark detection
2. **RecognizeSign.kt**: UI component for camera preview and gesture recognition
3. **Camera Integration**: Uses CameraX for real-time camera feed
4. **Gesture Processing**: Analyzes hand landmark positions to identify signs

## Usage

1. Navigate to the Recognize Sign screen from the home screen
2. Position your hand clearly in front of the camera
3. Make sure your hand is well-lit
4. Hold the sign steady for better recognition
5. The app will display the detected sign and confidence level

## Technical Details

- **Detection Confidence**: Minimum 50% confidence threshold
- **Processing Frequency**: 1 second intervals
- **Supported Hands**: Up to 2 hands simultaneously
- **Camera**: Front-facing camera preferred

## Troubleshooting

- Ensure good lighting conditions
- Keep hand steady during detection
- Make sure hand is fully visible in frame
- Check camera permissions are granted

## Future Enhancements

- Support for more Filipino Sign Language gestures
- Improved confidence scoring
- Real-time gesture recognition
- Hand landmark visualization overlay


