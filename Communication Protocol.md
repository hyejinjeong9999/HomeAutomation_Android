# Communication Protocol

## Android => Server

### Socket Connection

* Socket Connection
  * /ID:ANDROID IN
* Socket disconnection
  * /ID:ANDROID OUT

### Data

* Window
  * /ANDROID>/WINDOW ON
  * /ANDROID>/WINDOW OFF
* Airpurifier
  * /ANDROID>/AIRPURIFIER ON
  * /ANDROID>/AIRPURIFIER OFF
* Airconditioner
  * /ANDROID>/AIRCONDITIONER ON
  * /ANDROID>/AIRCONDITIONER OFF
* REFRESH
  * /ANDROID>/REFRESH ON
* Log
  * /ANDROID>/LOG

## Server => Android

* JSON Object

## Server => Latte

* ON
* OFF

## Latte => Server

### Sensor Data

#### Air Purifier (AP)
* /AIRPURIFIER
* /airpurifierStatus : (1 : on, 0 : off)
* /dust25 :
* /dust10 :
* /gasStatus : (ppm)
* ex) /AIRPURIFIER /airpurifierStatus:1 /dust25:4 /dust10:10 /gasStatus:554
#### Air Conditioner(AC)
* /AIRCONDITIONER
* /airconditionerStatus: (1 : on, 0 : off)
* /temp :
* /airconditionerMode: (COLD:1, DRY:2)
* /airconditionerTemp: 
* /airconditionerSpeed: (speed : 1, 2, 3)
* ex) /AIRCONDITIONER /airconditionerStatus:1 /temp:25 /airconditionerMode:1 /airconditionerTemp: 25 /airconditionerSpeed:3
#### Windows (WD)
* /WINDOW
* /windowStatus: (1 : on, 0 : off)
ex) /WINDOW /windowStatus:1

## Data Share

### Sensor Data Send Cycle

* Latte Panda => Server
  * 10s

### Server => Android

* Every time sensor data receive

## Server => Matlab
* /FACE
## Matlab => Server
### 얼굴 찾음
* ex) 0.9854 0.045 0.345 0.345
* %로 출력
### 얼굴 못 찾음
* /null




