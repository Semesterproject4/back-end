IMAGE=registry.nymann.dev/bierproductie/simulator
ONBUILD=registry.nymann.dev/bierproductie/simulator:onbuild

build:
	docker build --cache-from ${ONBUILD} -t ${ONBUILD} -f docker/Dockerfile .
	docker push ${IMAGE}
