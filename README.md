FemDat

<!-- ## Neo4J

### Crear nuevo usuario Neo4J

User/Pass por defecto de Neo4J: neo4j

`curl -v -u neo4j:neo4j -X POST localhost:7474/user/neo4j/password -H "Content-type:application/json" -d "{\"password\":\"secret\"}"`


### Correr neo4j server
`neo4j start` -->

# Iniciar servicios de BD

## MongoDB
`services start mongo`

## ElasticSearch
# mantenerlo corriendo en puerto por defecto (9200)
`services start elasticsearch`

# obtener indices
`GET "localhost:9200/_"`

`GET "localhost:9200/femdat"`

### test
`curl http://localhost:9200/`

# Posibles rutas para obtener data

`GET /tweets/last-week/summary`

Obtiene la cantidad de tweets que hablan del feminismo en la última semana (últimos 7 días), con el formato

{
	"data": [
		["01-01-2000", 13],
		["02-01-2000", 227],
		["03-01-2000", 520],
		["04-01-2000", 376],
		["05-01-2000", 798],
		["06-01-2000", 1000]
		["07-01-2000", 1000]
	]
}

`GET /tweets/last-week/apreciation`

{
	"data": [
		[67.6, 32.4],
	]
}

`GET /tweets/top-figures/`
{
	"data": [
		{"username": "Mingo", followers: 1133, accountURL: "https://twitter.com/MingoManriquez" },
		{"username": "Verónica", followers: 2292, accountURL: "https://twitter.com/veroisapunk"},
		{"username": "Donald J. Trump", followers: 56200000, accountURL: "https://twitter.com/realDonaldTrump"},
		{"username": "Donal.D.Trump", followers: 341, accountURL: "https://twitter.com/TrumpPinky"},
		{"username": "Sebastian Piñera", followers: 2150000, accountURL: "https://twitter.com/sebastianpinera"},
	]
}
