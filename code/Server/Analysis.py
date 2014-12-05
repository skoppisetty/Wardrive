#!/usr/bin/env python
"""
A daemon which checks the recent entries into the database and does the analysis on 
the basestation statsctics and update its result to the database.

"""

import redis, time, psycopg2
import requests, json
from sys import argv

r = redis.StrictRedis(host='127.0.0.1', port=6379, db=5)
dbconn = psycopg2.connect(database='wardrive',host='localhost',port='5432',user='wardrive',password='Md3YqGEJ396Q')



class Stats:
  def __init__(self):
	self.MCC = ""
	self.MNC = ""
	self.NETWORK_NAME = ""
	self.NETWORK_COUNTRY = ""
	self.NETWORK_TYPE =  ""
	self.CELLID =  ""
	self.PSC =  ""
	self.LAC =  ""
	self.timestamp =  "" 
  
def set_stats(stats,row):
	print row
	stats.MCC = row[6]
	stats.MNC = row[7]
	stats.NETWORK_NAME = row[8]
	stats.NETWORK_COUNTRY = row[9]
	stats.NETWORK_TYPE = row[10]
	stats.CELLID = row[11]
	stats.PSC = row[12]
	stats.LAC = row[13]
	stats.timestamp = row[16]
	return stats


def check_MCC_MNC(stats,stats_id):
	cur = dbconn.cursor()
	result = {'error': True, 'message' : []}
	cur.execute("""SELECT * FROM logger_mcc_mnc WHERE "NETWORK_MCC" = %s AND "NETWORK_MNC" = %s """, (stats.MCC, stats.MNC))
	# colnames = [desc[0] for desc in cur.description]
	for each in cur:
		print each
		if stats.NETWORK_COUNTRY.upper() != each[3].upper():
			result['error'] = False
			result['message'].append("Country mismatch: Collected " + stats.NETWORK_COUNTRY + " Repository: " + each[3])
		elif stats.NETWORK_NAME.upper() != each[6].upper():
			result['error'] = False
			result['message'].append("Network name mismatch: Collected -" + stats.NETWORK_NAME + " Repository: " + each[6])
	cur.execute("""UPDATE logger_statistics set "MCCMNC_STATUS" = %s , "MCCMNC_LOG" = %s where ID=%s""",(result["error"],json.dumps(result),stats_id))
	if cur.rowcount == 1:
		print "saved MCCMNC_LOG stats to db"
		return True
	else:
		print "Unable to save mccmnc"
	cur.close()
	print str(result)
	print "Done checking"
	return False

    # OPENCELLID_STATUS = models.BooleanField(default=False)
    # OPENCELLID_LOG = models.TextField()
    # MCCMNC_STATUS = models.BooleanField(default=False)
    # MCCMNC_LOG = models.TextField()
    # CHECK_STATUS = models.BooleanField(default=False)

def gen_api_url(stats):
	api_key = '1bf30105-4514-466b-b854-c7ba5cdcf85b'
	base_url = 'http://opencellid.org/cell/get?key='
	url = base_url + api_key + "&mcc=" + stats.MCC + "&mnc=" + stats.MNC + "&lac=" + stats.LAC +  "&cellid=" + stats.CELLID
	return url + "&format=json"

def check_opencellid(stats,stats_id):
	curs = dbconn.cursor()
	# url = 'http://opencellid.org/cell/get?key=1bf30105-4514-466b-b854-c7ba5cdcf85b&mcc=2602&mnc=2&lac=10250&cellid=26511&format=json'
	# url = 'http://opencellid.org/cell/get?key=1bf30105-4514-466b-b854-c7ba5cdcf85b&mcc=260&mnc=2&lac=10250&cellid=265&format=json'
	url = gen_api_url(stats)
	r = requests.get(url)
	status = False
	if r.status_code == 200:
		result = json.loads(r.text)
		if "error" in result:
			print "error : ", result
			status = False
		else:
			print "Success: ", result
			status = True
		curs.execute("""UPDATE logger_statistics set "OPENCELLID_STATUS" = %s , "OPENCELLID_LOG" = %s where ID=%s""",(status,json.dumps(result),stats_id))
		if curs.rowcount == 1:
			print "saved OPENCELLID_LOG stats to db"
			return True
		else:
			print "Unable to save opencellid"
	else:
	    # r.raise_for_status()
	    print "failed to reach opencellid"
	return False


def save_state(mccmnc,opencellid, stats_id):
	curs = dbconn.cursor()
	if mccmnc == True and opencellid == True:
		status = True
		curs.execute("""UPDATE logger_statistics set "CHECK_STATUS" = %s where ID=%s""",(status,stats_id))
		if curs.rowcount == 1:
			print "saved state to db"
			return True
		else:
			print "Unable to save state"


def daemon():
	while True:
		# try:
			stats_id = r.lpop("check_stats")
			if stats_id != None:
				print "Checking for ", stats_id
				cursor = dbconn.cursor()
				cursor.execute("""
					SELECT * FROM logger_statistics
					WHERE id = """ + stats_id + ";")
				for row in cursor:
					if row != None:
						stats = Stats()
						stats =  set_stats(stats,row)
						save_state(check_MCC_MNC(stats,stats_id),check_opencellid(stats,stats_id),stats_id)
						print stats.MCC, stats.MNC, stats.NETWORK_NAME, stats.NETWORK_COUNTRY, stats.NETWORK_TYPE, stats.CELLID, stats.PSC, stats.LAC, stats.timestamp
				cursor.close()
				dbconn.commit()
			time.sleep(3)

def sync():
	cursor = dbconn.cursor()
	cursor.execute("""
					SELECT * FROM logger_statistics;""")
	for row in cursor:
		if row != None:
			stats = Stats()
			stats =  set_stats(stats,row)
			stats_id = row[0]
			save_state(check_MCC_MNC(stats,stats_id),check_opencellid(stats,stats_id),stats_id)
			print stats.MCC, stats.MNC, stats.NETWORK_NAME, stats.NETWORK_COUNTRY, stats.NETWORK_TYPE, stats.CELLID, stats.PSC, stats.LAC, stats.timestamp
	cursor.close()
	dbconn.commit()
	time.sleep(3)
	print "Sync Done"

if __name__ == "__main__":
	if len(argv) != 2:
		print 'usage: %s <daemon/sync>' % argv[0]
		exit(1)
	if argv[1] == "daemon":
		daemon()
	else:
		sync()
