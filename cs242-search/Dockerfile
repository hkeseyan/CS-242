FROM python:3.6

MAINTAINER Jorge Mercado <jmerc010@ucr.edu>

COPY . /app
RUN rm -rf /app/venv

WORKDIR /app
RUN pip3 install -r /app/requirements.txt

RUN python3 -m nltk.downloader punkt
RUN python3 -m nltk.downloader stopwords

ENV APP_SETTINGS=config.py
ENV FLASK_DEBUG=0
ENV FLASK_APP=run.py

ENTRYPOINT ["flask"]
CMD ["run", "--host", "0.0.0.0"]
