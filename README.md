# hello-cloud-run-clj

This project uses [neil](https://github.com/babashka/neil).

## Tests

Run all tests:

```sh
clojure -M:test
```

## Development

```sh
clojure -M -m hello-cloud-run-clj.hello-cloud-run-clj
```

## Build

Compile the uberjar:

```sh
clojure -T:build uber
```

Run the uberjar:

```sh
java -jar ./target/hello-cloud-run-clj-1.2.0-standalone.jar 1 2 3
```

## Container

Build the container image:

```sh
docker build ./ --file Dockerfile \
  --build-arg APP_NAME=hello-cloud-run-clj \
  --tag hello-cloud-run-clj:latest
```

Inspect the container image with [dive](https://github.com/wagoodman/dive):

```sh
dive hello-cloud-run-clj:latest
```

Run the container:

```sh
docker run -it --rm -p 4000:3000 \
  --env ENVIRONMENT=DEVELOPMENT \
  --env PORT=3000 \
  hello-cloud-run-clj:latest
```

## Deploy to Cloud Run

```sh
gcloud builds submit ./ --config cloudbuild.yaml --async
```

## Call the Cloud Run service

```sh
curl -L ${CLOUD_RUN_SERVICE_URL}
```
