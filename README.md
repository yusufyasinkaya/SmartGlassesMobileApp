# 3D Sound Synthesizer Mobile App Assistant for Blind People
The function of this app is to synthesize texts that are coming from object detection into 3D sound objects. This is a demo project that changes stereo channels related to the object center positions inside camera frame. 

The main purpose of this demo app is to make blind people able to understand the positions of the objects in front of them without needing to touch or getting close to those objects.

## Formula
Human brain is able to understand the direction that is coming to the ears. One of the actual reasons human brain is able to do it is that both ears are actually hearing the sounds coming from around. When one ear hears less than the other, our brains can manage to realize how much left or how much right the sound is coming from. But it would not be possible to achieve this kind of functionality this good with only one ear.

We have decided to use a formula in order to be able to realize the object positions in horizontal axis when they are synthesized into sounds by the app.   

Here is the formula that is being used in the project in order to achieve directional sound:

>$camMaxX$ is in between: $[0, camResolutionX]$

>$objectCenterX$ is between $[0, camMaxX]$

>$volumeMax=(objectCenterX)/(camMaxX)$

Finally "volumeMax" variable is being used be calculate channel volume multipliers using this strategy:

>$leftChannelMultiplier = 1 - volumeMax$

>$rightChannelMultiplier = volumeMax$

## User Tests:
