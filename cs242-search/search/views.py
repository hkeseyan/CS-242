from .models import MongoQuery, LuceneQuery
from search import app
from flask import request, render_template


def bold_query(content, query):
    final_words = []
    q_words = query.split()
    words = content.split()

    for w in words:
        if w in q_words:
            final_words.append("<div style=\"display: inline-block;font-weight: bold\">" + w + "</div>")
        else:
            final_words.append(w)

    return ' '.join(final_words)


@app.route('/search', methods=['GET'])
def search():
    """Run a search."""

    e = request.args.get('e')
    if e == 'lucene':
        e = LuceneQuery()
    else:
        e = MongoQuery()

    q = request.args.get('q')
    results = e.search(q)
    for r in results:
        r['snippet'] = bold_query(content=r['snippet'], query=q)

    return render_template('index.html', results=results, query=q)


@app.route('/')
def index():
    """Render the default page."""
    return render_template('index.html')
