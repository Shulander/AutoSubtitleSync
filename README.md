AutoSubtitleSync
================

Autonomous subtitle synchronization tool

The propose of this tool is to aid the synchronization of a Subrip subtitle (.srt) autonomously, it will use another Subrip file as correct and will analyze the content to determine the proper way to synchronize the file.

The main idea about this process is to use a linear and angular correction:
* linear correction: all entities from the subtitle are shifted by a time (ex. 5s delayed by all the movie)
* angular correction: the entities starts to increase the gap by the time played from the movie (ex. starts 0s delayed ends with 30s delayed)

As you can see this is a Linear Equation (http://en.wikipedia.org/wiki/Linear_equation)

<pre>
y = ax + b

y : new time
x: old time
a: angular coefficient
b: linear coefficient
</pre>


Synchronization Steps
================

* Find 2 points (entities) to determine the linear equation A(x1, y1) and B(x2, y2)
* Calculate the angular and linear coefficients
* apply the linear equation to all entities from the unsynchronized subtitle

... to be continued
