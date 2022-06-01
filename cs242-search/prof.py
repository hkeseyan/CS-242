

from search import app
from werkzeug.contrib.profiler import ProfilerMiddleware


def main():

    app.config['PROFILE'] = True
    app.wsgi_app = ProfilerMiddleware(app.wsgi_app, restrictions=[30])
    app.run(host="0.0.0.0", port=5000, debug=True)


if __name__ == '__main__':
    main()
