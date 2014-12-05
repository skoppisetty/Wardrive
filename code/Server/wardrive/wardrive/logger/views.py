from django.shortcuts import render
from django.http import HttpResponse
from models import Statistics
from django.core import serializers
import json,sys,logging
from django.views.decorators.csrf import csrf_exempt

logger = logging.getLogger(__name__)

import redis
r = redis.StrictRedis(host='127.0.0.1', port=6379, db=5)

def index(request):
	print "sample"
	return HttpResponse("Project Home page use /save_data to save json logs")

@csrf_exempt
def save_data(request):
	result = { "error" : "", "status" : ""}
	print request.POST
	for key, value in request.POST.iteritems():
		print "1", key, value
	if request.method == 'POST':
		if request.POST.get('stats', ''):
			logger.error(request.POST['stats'])
			try:
				stats = json.loads(request.POST['stats'])
				stats = Statistics(IMEI = stats['IMEI'],IMSI = stats['IMSI'],PHONE_MODEL = stats['PHONE_MODEL'],SIM_SN = stats['SIM_SN'],GSM_TYPE = stats['GSM_TYPE'],NETWORK_MCC = stats['NETWORK_MCC'],NETWORK_MNC = stats['NETWORK_MNC'],NETWORK_NAME = stats['NETWORK_NAME'],NETWORK_COUNTRY = stats['NETWORK_COUNTRY'],NETWORK_TYPE = stats['NETWORK_TYPE'],CELL_ID = stats['CELL_ID'],CELL_PSC = stats['CELL_PSC'],CELL_LAC = stats['CELL_LAC'],RSSI = stats['RSSI'],GPS = stats['GPS'],COLLECTED_TIME = stats['COLLECTED_TIME']) 
				stats.save()
				# print stats.id
				result['status'] = 'True'
				result['error'] = ""
				try:
					r.rpush("check_stats",stats.id)
				except:
					result['error'] = "unable to add it to check status queque"	
				return HttpResponse(json.dumps(result), content_type="application/json")
			except:
				result['status'] = 'False'
				print sys.exc_info()
				result['error'] = "Unexpected error:" + str(sys.exc_info()[0])
				return HttpResponse(json.dumps(result), content_type="application/json")
		else:
			result['status'] = 'False'
			result['error'] = "Couldnt find the stats variable"
			return HttpResponse(json.dumps(result), content_type="application/json")
	else:
		result['status'] = 'False'
		result['error'] = "Using unknown request type"
		return HttpResponse(json.dumps(result), content_type="application/json")

def read_data(request):
	from django.db.models import Q
	# stats = Statistics.objects.filter(MCCMNC_STATUS= False) | Statistics.objects.filter(OPENCELLID_STATUS= False)
	stats = Statistics.objects.filter(CHECK_STATUS = True).filter(Q(MCCMNC_STATUS= False) | Q(OPENCELLID_STATUS = False))
	# stats = Statistics.objects.filter('CHECK_STATUS' = True, MCCMNC_STATUS = False,  OPENCELLID_STATUS = False).order_by('-SAVED_TIME')
	output = serializers.serialize('json', stats)
	return HttpResponse(output, content_type="application/json")
