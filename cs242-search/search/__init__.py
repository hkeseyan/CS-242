import urllib.parse
import pymongo

from flask import Flask

app = Flask(__name__)
app.secret_key = 'super secret string'
app.config.from_envvar('APP_SETTINGS')

mongo_coll_ii = app.config['MONGO_COL_II']
mongo_coll_snips = app.config['MONGO_COL_SNIPS']

db_host = app.config['DB_HOST']
db_port = app.config['DB_PORT']
db_uri = app.config['MONGO_DB_URI']
db_user = app.config['MONGO_DB_USER']
db_pass = app.config['MONGO_DB_PASS']

username = urllib.parse.quote_plus(db_user)
password = urllib.parse.quote_plus(db_pass)

#conn = db_uri % (username, password, db_host, db_port)
conn = db_uri % (db_host, db_port)

mongo_client = pymongo.MongoClient(conn)
mongo_db = mongo_client[app.config['MONGO_DB']]

from search import views, models
