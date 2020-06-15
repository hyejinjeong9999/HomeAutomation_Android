# Communication Protocol

## Android => Server

### Socket Connection

* Socket Connection
  * /ID:ANDROIDtest@kim.com IN
* Socket disconnection
  * /ID:ANDROIDtest@kim.com OUT

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
* MODE
  * /ANDROID>/MODE SMART
  * /ANDROID>/MODE SLEEP
  * /ANDROID>/MODE VENTILATION
  * /ANDROID>/MODE OUTING
* Log Data
  * /ANDROID>/LOG

## Server => Android

* JSON Object

## Server => Latte

* ON
* OFF

## Latte => Server

### Sensor Data

#### Air Purifier (AP)
* /ID:AIRPURIFIER IN
* /AIRPURIFIER
* /airpurifierStatus : (1 : on, 0 : off)
* /dust25 :
* /dust10 :
* /gasStatus : (ppm)
* ex) /AIRPURIFIER /airpurifierStatus:1 /dust25:4 /dust10:10 /gasStatus:554
#### Air Conditioner(AC)
* /ID:AIRCONDITIONER IN
* /AIRCONDITIONER
* /airconditionerStatus: (1 : on, 0 : off)
* /temp :
* /airconditionerMode: (COLD:1, DRY:2)
* /airconditionerTemp: 
* /airconditionerSpeed: (speed : 1, 2, 3)
* ex) /AIRCONDITIONER /airconditionerStatus:1 /temp:25 /airconditionerMode:1 /airconditionerTemp: 25 /airconditionerSpeed:3
#### Windows (WD)
* /ID:WINDOW IN
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
* /FACE 0.856     0.011     0.007     0.126
### 얼굴 못 찾음
* /FACE 0.000     0.000     0.000     0.000




