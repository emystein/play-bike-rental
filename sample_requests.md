# User

## Create User
```
http post http://localhost:9000/users id=1 name=emystein
```

## Retrieve User
```
http http://localhost:9000/users/1
```

## Reserve Rent Token
```
http http://localhost:9000/users/1/rent-token
```

# Bike

## Create Bike
```
http post http://localhost:9000/bikes serialNumber=1
```

## Retrieve Bike
```
http http://localhost:9000/bikes/1
```


# Bike Station

## Create Bike Station
```
http post http://localhost:9000/bike-stations id=1 numberOfBikeAnchorages=10
```

## Park Bike
```
http post 'http://localhost:9000/bike-stations/1/anchorages/1/bike?bikeSerialNumber=1
```

## Pick-up Bike
```
http 'http://localhost:9000/bike-stations/1/anchorages/1/bike?rentToken=1'
```

