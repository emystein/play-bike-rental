# Introduction
REST API for a public bike rental system. Implemented in Scala and Play.

# Related projects
The object model lives in a separate project: https://github.com/emystein/scala-object-oriented-bike-rental-model

# REST API
The examples requests are in [httpie](https://httpie.org/) syntax.

## User


### Create User
```
http post http://localhost:9000/users name=emystein
```

### Retrieve User
```
http http://localhost:9000/users/1
```

### Reserve Rent Token
```
http http://localhost:9000/users/1/rent-token
```

## Bike

### Create Bike
```
http post http://localhost:9000/bikes serialNumber=1
```

### Retrieve Bike
```
http http://localhost:9000/bikes/1
```

## Bike Station

### Create Bike Station
```
http post http://localhost:9000/bike-stations id=1 anchorageCount=10
```

### Park Bike
```
http post 'http://localhost:9000/bike-stations/1/anchorages/1/bike serialNumber=1
```

### Pick-up Bike
```
http post 'http://localhost:9000/bike-stations/1/anchorages/1/bike/pickup?rentToken=1'
```

