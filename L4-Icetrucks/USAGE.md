# Using the API

As a reference, here are (as a complement to the redoc interface), cURL examples to test out the API.

## Endpoints

### Create ice cream
Creates a new ice cream, assigning it a new ID

[Documentation](https://icecreamtruck.lol/docs#/paths/~1icecreams/post)

```shell
curl -X POST https://icecreamtruck.lol/icecreams \
  -H 'Accept: application/json'
```


## List ice creams

Lists all ice creams by its ID.

[Documentation](https://icecreamtruck.lol/docs#/paths/~1icecreams/get)

```shell
curl -X GET https://icecreamtruck.lol/icecreams \
  -H 'Accept: application/json'
```

## Get ice cream by ID

Gets an ice cream by its ID

[Documentation](https://icecreamtruck.lol/docs#/paths/~1icecreams~1%7Bid%7D/get)

```shell

curl -X GET https://icecreamtruck.lol/icecreams/{id} \
  -H 'Accept: application/json'

```

## Update ice cream

Updates an ice cream by its id

[Documentation](https://icecreamtruck.lol/docs#/paths/~1icecreams~1%7Bid%7D/put)

```shell
curl -X PUT https://icecreamtruck.lol/icecreams/{id} \
  -H 'Accept: application/json'
```

## Delete ice cream

Deletes an ice cream by its ID

[Documentation](https://icecreamtruck.lol/docs#/paths/~1icecreams~1%7Bid%7D/delete)

```shell
curl -X DELETE https://icecreamtruck.lol/icecreams/{id} \
  -H 'Accept: application/json'
```

### Create truck
Creates a new truck, assigning it a new ID

[Documentation](https://icecreamtruck.lol/docs#/paths/~1trucks/post)

```shell
curl -X POST https://icecreamtruck.lol/trucks \
  -H 'Accept: application/json'
```

## List trucks
Lists all trucks

[Documentation](https://icecreamtruck.lol/docs#/paths/~1trucks/get)

```shell
curl -X GET https://icecreamtruck.lol/trucks \
  -H 'Accept: application/json'
```

## Get truck by ID
Gets a truck by its ID

[Documentation](https://icecreamtruck.lol/docs#/paths/~1trucks~1%7Bid%7D/get)

```shell
curl -X GET https://icecreamtruck.lol/trucks/{id} \
  -H 'Accept: application/json'

```

## Update truck

Updates / creates a truck by its ID

[Documentation](https://icecreamtruck.lol/docs#/paths/~1trucks~1%7Bid%7D/put)

```shell
curl -X PUT https://icecreamtruck.lol/trucks/{id} \
  -H 'Accept: application/json'
```

## Delete truck

Deletes a truck by its ID

[Documentation](https://icecreamtruck.lol/docs#/paths/~1trucks~1%7Bid%7D/delete)

```shell
curl -X DELETE https://icecreamtruck.lol/trucks/{id} \
  -H 'Accept: application/json'
```
