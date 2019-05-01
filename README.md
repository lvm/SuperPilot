# Pilot.sc

An `Event` type for [Pilot Synthetiser](https://github.com/hundredrabbits/Pilot/).
Can generate note messages and effect messages. Can be used together or separated.

## Install

Clone this repository in the `Extension` directory.
The path can be found by evaluating `Platform.userExtensionDir` or `Platform.systemExtensionDir`.

## Regular messages

```
Pbind(
  \type, \pilot,
  \dur, 1,
  \note, Pseq([0,3,7],inf),
  \chan, 2,
  \octave, Pseq([3,4,5].stutter(3),inf),
  \amp, 0.5
)
```

This should generate the following messages

```
"23C8f", "23d8f", "23G8f",
"24C8f", "24d8f", "24G8f",
"25C8f", "25d8f", "25G8f",
etc...
```

### Effects messages

```
Pbind(
  \type, \pilot,
  \dur, 1,
  \revWet, 0.5,
  \revDepth, 1.0,
  \bitWet, 0.25,
  \bitDepth, 0.65,
)
```

This should generate the following messages.

```
"REV8f", "BIT4A", etc...
```


The names of the fx are the same used in Pilot.
Note that it's possible to use multiple filters at once, and that can be used alone or while sending notes.


In order to send effects alone, you'll need to define a `\note` parameter as `false`. Otherwise for each effect value, a single note will be sent to Channel 0 ("05Cff").
```
Pbind(
  \type, \pilot,
  \dur, 1,
  \note, false,
  \revWet, 0.5,
  \revDepth, 1.0,
  \bitWet, 0.25,
  \bitDepth, 0.65,
)
```


## LICENSE

See [LICENSE](LICENSE)