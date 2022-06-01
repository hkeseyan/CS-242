
.PHONY: venv run clean

DOCKER_IMAGE = jorgemariomercado/cs242-search
IMAGE_VERSION = 1.0

venv: clean
	virtualenv -p python3 venv && \
    source ./venv/bin/activate; \
    pip3 install -r requirements.txt

run:
	source ./venv/bin/activate; \
	APP_SETTINGS=config.py FLASK_DEBUG=1 FLASK_APP=run.py flask run; \

profile:
	source ./venv/bin/activate; \
	APP_SETTINGS=config.py FLASK_DEBUG=1 python3 prof.py ; \

clean:
	rm -rf venv
	rm -rf __pycache__
	find . -name *.pyc -exec rm -rf '{}' \;

docker_build:
	docker build -t $(DOCKER_IMAGE):$(IMAGE_VERSION) -t $(DOCKER_IMAGE):latest .

docker_login:
	docker login --username jorgemariomercado

docker_push: docker_login
	docker push $(DOCKER_IMAGE):$(IMAGE_VERSION)
	docker push $(DOCKER_IMAGE):latest