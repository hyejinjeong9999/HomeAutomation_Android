# Communication Protocol

## Android => Server

### Socket Connection

- Socket Connection
  - /ID:ANDROID IN
- Socket disconnection
  - /ID:ANDROID OUT

### Data

- Window
  - /ANDROID>/WINDOW ON
  - /ANDROID>/WINDOW OFF
- Airpurifier
  - /ANDROID>/AIRPURIFIER ON
  - /ANDROID>/AIRPURIFIER OFF
- Airconditioner
  - /ANDROID>/AIRCONDITIONER ON
  - /ANDROID>/AIRCONDITIONER OFF
- Refresh
  - /ANDROID>/REFRESH

## Server => Android

- JSON Object

## Server => Latte

- ON
- OFF

## Latte => Server

### Sensor Data

#### Air Purifier (AP)

- /AP
- /APONOFF: (1 : on, 0 : off)
- /DUST2.5:
- /DUST10:
- /GASSTATUS: (ppm)
- ex) /AP /APONOFF:1 /DUST2.5:4 /DUST10:10 /GASSTATUS:554

#### Air Conditioner(AC)

- /AC
- /ACONOFF: (1 : on, 0 : off)
- /TEMPERATURE: ex) /AC /ACONOFF:1 /TEMPERATURE:25

#### Windows (WD)

- /WD
- /WDONOFF: (1 : on, 0 : off) ex) /WD /WDONOFF:1

## Data Share

### Sensor Data Send Cycle

- Latte Panda => Server
  - 10s

### Server => Android

- Every time sensor data receive

## Server => Matlab

- /FACE

## Matlab => Server

### 얼굴 찾음

- ex) 0.9854 0.045 0.345 0.345
- %로 출력

### 얼굴 못 찾음

- /null