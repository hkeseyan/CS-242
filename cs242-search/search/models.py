from search import mongo_db, mongo_coll_ii, mongo_coll_snips, lucene_web
from nltk.corpus import stopwords

import re
import requests
import math
import nltk

nltk.download('punkt')
nltk.download('stopwords')
stop_words = set(stopwords.words('english'))


def get_snip_for_doc(c, p):

    sl = 20  # snippet length
    if p >= sl:
        lb = p - sl  # lower bound position
        rsl = 0
    else:
        lb = p
        rsl = sl - p

    ub = p + sl + rsl  # upper bound position
    return ' '.join(c[lb:ub])  # TODO: check if this returns correct snippet


class LuceneQuery(object):
    """Run a query against REST API that mounts the Lucene index."""

    def __init__(self):
        self.text = None

    def search(self, query):
        results = []
        results_keys = []

        url = lucene_web % query

        response = requests.get(url)
        json_data = response.json()
        for r in json_data:

            if r['docNo'] not in results_keys:

                content = r['snippet']
                pos = content.find(query)
                if pos < 0:
                    snip = content
                else:
                    snip = get_snip_for_doc(content.split(), pos)  # fetch the snippet data

                results_keys.append(r['docNo'])
                results.append({
                    'score': r['score'],
                    'docId': r['docId'],
                    'snippet': snip,
                    'docNo': r['docNo'],
                    'title': r['title'],
                    'url': r['url']})

        results.sort(key=lambda item: item['score'], reverse=True)
        return results


class MongoQuery(object):
    """Query a mongo nackend."""

    def __init__(self):
        self.ii_coll = None
        self.snips_coll = None
        self.connect()
        self.N = self.snips_coll.count()  # 4805?

    def connect(self):
        self.ii_coll = mongo_db[mongo_coll_ii]
        self.snips_coll = mongo_db[mongo_coll_snips]
        return True

    def get_content_for_doc(self, docNo):
        snip_doc = self.snips_coll.find_one({'docno': docNo}, {'_id': 0})
        return snip_doc

    def search(self, query):

        results = []
        results_keys = []

        tokens = nltk.word_tokenize(query)

        N = self.N  # documents in corpus

        k1 = 1.2
        k2 = 100
        b = 0.75
        ri = 0
        R = 0

        # average document length in collection (from snippet)
        avdl = 450  # TODO: calculate avdl

        filtered = []
        for t in tokens:
            if t not in stop_words:
                filtered.append(t)

        for t in filtered:

            for kw_doc in self.ii_coll.find({'KEYWORD': t}):
                qfi = 1  # frequency of the term in the query

                ni = len(kw_doc['DOCS'])  # idf - number of docs containing the query term

                kw_doc['DOCS'].sort(key=lambda item: item['FREQUENCY'], reverse=True)

                for d in kw_doc['DOCS'][0:199]:  # for top 200 documents in retrieved doc numbers, run BM25 (top 20)

                    doc_id = d['DOCID']

                    bm25 = 0  # reset bm25 value for each new document
                    pos = d['KEYWORDPOSITIONS'][0][0]  # first occurrence of word in the document for the snippet

                    snip_doc = self.get_content_for_doc(doc_id)

                    content = snip_doc['doccontent']
                    title = snip_doc['doctitle']
                    url = snip_doc['url']
                    doc_no = snip_doc['docno']

                    snip = get_snip_for_doc(content.split(), int(pos))  # fetch the snippet data

                    dl = len(content)  # document length - does this just count array size and not character length?
                    for t in filtered:  # find frequency of the term in document
                        x = [m.start() for m in re.finditer(t, content)]
                        fi = len(x)

                        K = k1 * ((1 - b) + b * dl / avdl)

                        bm25 = math.log1p(
                            ((ri + 0.5) / (R - ri + 0.5)) / ((ni - ri + 0.5) / (N - ni - R + ri + 0.5))) * (
                                           k1 + 1) * fi / (K + fi) * (k2 + 1) * qfi / (k2 + qfi)
                        bm25 += bm25  # sum the bm25 for each term to get bm25 for the query

                    if doc_no not in results_keys:
                        results_keys.append(doc_no)
                        results.append({
                            'score': bm25,
                            'docId': int(doc_id),
                            'snippet': snip,
                            'docNo': doc_no,
                            'title': title,
                            'url': url})

        results.sort(key=lambda item: item['score'], reverse=True)
        return results
