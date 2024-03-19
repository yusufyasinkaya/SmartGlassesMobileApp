# A 3D Sound Synthesizer Mobile App Assistant for the Visually Impaired
The purpose of this mobile application is to synthesize textual information obtained from object detection into 3D sound representations. Specifically, the app modifies stereo channels based on the positions of detected objects within the camera frame.

The base project is derived from YOLO TF Lite project and the model is being used is YOLO Tiny.

## Objective
The primary goal of this demo application is to enhance the spatial awareness of blind individuals by enabling them to perceive the positions of objects in their environment without relying on physical touch or close proximity.

## The Brain’s Ability to Discern Directional Sound
The human brain possesses the remarkable ability to determine the direction from which sounds originate. This capability is partly attributed to the fact that both ears receive auditory input from the surrounding environment. When one ear receives less sound than the other, our brains can infer whether a sound source is to the left or right. However, achieving accurate directional perception with only one ear is challenging.

## The Formula for Directional Sound Synthesis
To achieve directional sound representation, we employ a formula that relates object positions in the horizontal axis to synthesized audio cues within the app:

1. Camera Maximum X-coordinate (_camMaxX_): Represents the range of possible horizontal positions within the camera frame, spanning from 0 to the camera resolution width (camResolutionX).

2. Object Center X-coordinate (_objectCenterX_): Denotes the horizontal position of the object being synthesized into sound. It also falls within the range of [0, camMaxX].

3. Volume Scaling (_volumeX_): Calculated as the ratio of objectCenterX to camMaxX. This value determines the relative volume of the synthesized sound for each channel.
   >$volumeX=(objectCenterX)/(camMaxX)$


Finally, we use the following channel volume multipliers:

>**Left Channel Multiplier:** $1−volumeX$

>**Right Channel Multiplier:** $volumeX$

## User Tests:
To assess the efficacy of our approach, we conducted user testing sessions. Participants, while blindfolded, interacted with the app to evaluate its capability in conveying object positions through 3D sound cues. These tests provided valuable insights into the app’s performance and its potential impact on enhancing spatial awareness for visually impaired users. The results contribute to our understanding of how well the app achieves its intended purpose.

Video of one of the tests: https://youtu.be/1OMR2CmaAEw

**This project is supported within the scope of TÜBİTAK 2209-A program.**
