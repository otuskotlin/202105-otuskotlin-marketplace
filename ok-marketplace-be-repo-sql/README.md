## How to connect to PostgreSQL

```shell
docker run \
  --name some-postgres \
  -e POSTGRES_PASSWORD=marketplace-pass \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_DB=marketplacedevdb \
  -p 5432:5432 \
  -d postgres
```
